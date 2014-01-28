package org.hierro.mealpal.activity;

import java.util.Date;
import java.util.HashSet;

import org.hierro.mealpal.R;
import org.hierro.mealpal.dao.DAOHelper;
import org.hierro.mealpal.dto.Entry;
import org.hierro.mealpal.observe.EntryObserver;
import org.hierro.mealpal.util.Util;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.android.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

public class FoodDialog extends DialogFragment {
	private Button selectTimeButton;
	private Button submitButton;
	private Spinner selectOptionSpinner;
	private EventHandler eventHandler;
	private EditText inputEntry;
	private Date date = new Date();
	private EntryObserver entryObserver;
	private HashSet<EntryObserver> observers = new HashSet<EntryObserver>();

	public FoodDialog(){}//Empty Constructor

	public void registerObserver(EntryObserver observer){
		entryObserver = observer;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
		View view = inflater.inflate(R.layout.add_food, container);
		getDialog().setTitle("Add Food Entry");

		eventHandler = new EventHandler();
		inputEntry = (EditText) view.findViewById(R.id.solid_entryInput);
		selectTimeButton = (Button) view.findViewById(R.id.btnfood_select_time);
		selectTimeButton.setText(Util.getHours(date.getHours())+":"+Util.getMins(date)+Util.getAmPm(date.getHours()));
		submitButton = (Button) view.findViewById(R.id.btn_submit_solid);
		selectOptionSpinner = (Spinner) view.findViewById(R.id.solid_selectionSpinner);
		selectTimeButton.setOnClickListener(eventHandler);
		selectOptionSpinner.setOnItemSelectedListener(eventHandler);
		submitButton.setOnClickListener(eventHandler);

		return view;
	}


	private class EventHandler implements View.OnClickListener,OnItemSelectedListener,OnTimeSetListener{
		private String type;
		private String description;
		private DAOHelper daoHelper = new DAOHelper(getActivity());
		private TimePickerDialog timePickerDialog;
		private Date ts = new Date();
		@Override
		public void onClick(View v) {
			int typeInt=0;
			if(v == submitButton){
				description = inputEntry.getText().toString();
				typeInt = Util.getType(type);
				Entry entry = new Entry(typeInt, ts, description);
				daoHelper.createFoodEntry(entry);
				dismiss();
				inputEntry.setText("");

				if(entryObserver != null){
					entryObserver.onEntrySubmitted(FoodDialog.this, "food", ts);
				}
			}
			else if(v == selectTimeButton){
				timePickerDialog = TimePickerDialog.newInstance(this, date.getHours(), date.getMinutes(), false);
				timePickerDialog.show(getFragmentManager(), "timePickerDialog");
			}
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			if(parent.getId() == selectOptionSpinner.getId()){
				type = (String) selectOptionSpinner.getItemAtPosition(pos);
				type = type.toLowerCase();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}

		@Override
		public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
			ts.setHours(hourOfDay);
			ts.setMinutes(minute);
			selectTimeButton.setText(Util.getHours(hourOfDay)+":"+Util.getMins(ts)+Util.getAmPm(hourOfDay));
		}

	}

}
