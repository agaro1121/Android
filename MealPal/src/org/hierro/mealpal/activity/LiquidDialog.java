package org.hierro.mealpal.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import org.hierro.mealpal.R;
import org.hierro.mealpal.dao.DAOHelper;
import org.hierro.mealpal.dto.Entry;
import org.hierro.mealpal.observe.EntryObserver;
import org.hierro.mealpal.util.Util;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.android.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

public class LiquidDialog extends DialogFragment {
	private Spinner optionSpinner;
	private Spinner measureSpinner;
	private Button btn_submit;
	private EventHandler eventHandler;
	private DAOHelper daoHelper;
	private EditText optionalDescription;
	private EditText quantityInput;
	private Button selectTimeButton;
	private Date date = new Date();
	private SimpleDateFormat minFormat = new SimpleDateFormat("mm");
	private EntryObserver entryObserver;
	private HashSet<EntryObserver> observers = new HashSet<EntryObserver>();

	public void registerObserver(EntryObserver observer){
		entryObserver = observer;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
		View view = inflater.inflate(R.layout.add_liquid, container);
		getDialog().setTitle("Add Liquid Entry");

		selectTimeButton = (Button) view.findViewById(R.id.btnliquid_select_time);
		selectTimeButton.setText(Util.getHours(date.getHours())+":"+minFormat.format(date)+Util.getAmPm(date.getHours()));
		quantityInput = (EditText) view.findViewById(R.id.liquid_numInput);
		optionalDescription = (EditText) view.findViewById(R.id.liquid_addDescription);
		optionalDescription.setFocusable(false);
		optionSpinner = (Spinner) view.findViewById(R.id.liquid_selectionSpinner);
		measureSpinner = (Spinner) view.findViewById(R.id.liquid_measureSpinner);
		btn_submit = (Button) view.findViewById(R.id.btn_submit_liquid);

		eventHandler = new EventHandler();
		btn_submit.setOnClickListener(eventHandler);
		optionSpinner.setOnItemSelectedListener(eventHandler);
		measureSpinner.setOnItemSelectedListener(eventHandler);
		selectTimeButton.setOnClickListener(eventHandler);

		daoHelper = new DAOHelper(getActivity());

		return view;
	}

	private class EventHandler implements OnClickListener, OnItemSelectedListener, OnTimeSetListener{
		private String type;
		private String measurement;
		private String description;
		private Entry entry;
		private Date ts = new Date();
		private TimePickerDialog timePickerDialog = new TimePickerDialog();
		@Override
		public void onClick(View v) {
			if(v == btn_submit){
				int typeInt = Util.getType(type);
				int amount = Util.getAmount(measurement);
				String amountString = quantityInput.getText().toString();
				description = optionalDescription.getText().toString();

				if(type.equalsIgnoreCase("water")){
					description = "water";
				}
				else if(type.equalsIgnoreCase("other") &&  (description == null || description.trim().equalsIgnoreCase(""))  ){
					description = "Not Specified";
				}

				if(amountString == null || amountString.length() == 0 || amountString.equalsIgnoreCase("")){
					Toast.makeText(getActivity(), "You did not enter a number !", Toast.LENGTH_SHORT).show();
				}else{
					amount = amount * Integer.parseInt(amountString);
					entry = new Entry(typeInt, ts, amount, description);
					daoHelper.createLiquidEntry(entry);
					dismiss();

					if(entryObserver != null){
						entryObserver.onEntrySubmitted(LiquidDialog.this, "liquid", ts);
					}

					optionalDescription.setText("");
					quantityInput.setText("");
					optionSpinner.setSelection(0);
					measureSpinner.setSelection(0);
				}
			}

			if(v == selectTimeButton){
				timePickerDialog = TimePickerDialog.newInstance(this, date.getHours(), date.getMinutes(), false);
				timePickerDialog.show(getFragmentManager(), "timePickerDialog");
			}
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			if(parent.getId() == optionSpinner.getId()){
				type = (String) parent.getItemAtPosition(pos);

				if(type.equalsIgnoreCase("other")){
					optionalDescription.setFocusableInTouchMode(true);
				}else if(type.equalsIgnoreCase("water")){
					optionalDescription.setFocusable(false);
				}
			}else if(parent.getId() == measureSpinner.getId()){
				measurement = (String) parent.getItemAtPosition(pos);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			type = "water";
		}

		@Override
		public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
			ts.setHours(hourOfDay);
			ts.setMinutes(minute);
			selectTimeButton.setText(Util.getHours(hourOfDay)+":"+minFormat.format(ts)+Util.getAmPm(hourOfDay));
		}
	}
}
