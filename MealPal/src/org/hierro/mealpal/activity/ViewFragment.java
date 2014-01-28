package org.hierro.mealpal.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.hierro.mealpal.R;
import org.hierro.mealpal.dao.DAOHelper;
import org.hierro.mealpal.dto.Entry;
import org.hierro.mealpal.observe.EntryObserver;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;

public class ViewFragment extends DialogFragment {
	private DAOHelper daoHelper;
	private static final String LOG = "ViewFragment.class";
	private View view;
	private Button selectDayButton;
	private EventHandler eventHandler = new EventHandler();
	private Date date = new Date();
	private SimpleDateFormat dateFormatForQuery = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat dateFormatForScreen = new SimpleDateFormat("EEE, MMM dd, yyyy");
	private ArrayList<Entry> selectedDayListings;
	private ListView listView;
	private ArrayAdapter<Entry> listAdapter;
	private Spinner viewFilterSpinner;
	private boolean isFirstRun = true;
	private FoodDialog foodDialog;
	private LiquidDialog liquidDialog;
	
	public ViewFragment(){}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		foodDialog = (FoodDialog) getFragmentManager().findFragmentByTag("foodDialog");
		foodDialog.registerObserver(eventHandler);
		liquidDialog = (LiquidDialog) getFragmentManager().findFragmentByTag("liquidDialog");
		liquidDialog.registerObserver(eventHandler);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.view_today_fragment, container, false);
		viewFilterSpinner = (Spinner) view.findViewById(R.id.spinner_daily_filter);
		selectDayButton = (Button) view.findViewById(R.id.btn_custom_day);
		selectDayButton.setText(dateFormatForScreen.format(date));
		eventHandler = new EventHandler();
		selectDayButton.setOnClickListener(eventHandler);
		viewFilterSpinner.setOnItemSelectedListener(eventHandler);
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);

		daoHelper = new DAOHelper(getActivity());
		listView = (ListView) view.findViewById(R.id.listView1);
		selectedDayListings = (ArrayList<Entry>) daoHelper.fetchDayLog(dateFormatForQuery.format(date));
		listAdapter = new ArrayAdapter<Entry>(getActivity(), android.R.layout.simple_list_item_1, selectedDayListings);
		listView.setAdapter(listAdapter);
	}



	private class EventHandler implements OnClickListener, OnDateSetListener, OnItemSelectedListener, EntryObserver {
		private DatePickerDialog dp = new DatePickerDialog();
		private Date inDate = new Date();
		private String typeFromSpinner = "";
		
		@Override
		public void onClick(View v) {
			if (v == selectDayButton){
				dp.setOnDateSetListener(this);
				dp.show(getFragmentManager(), "SelectCustomDay");
			}

		}
		@Override
		public void onDateSet(DatePickerDialog dialog, int year,
				int monthOfYear, int dayOfMonth) {
			inDate = new Date(year-1900, monthOfYear, dayOfMonth);
			selectDayButton.setText(dateFormatForScreen.format(inDate));
			selectedDayListings.clear();
			selectedDayListings = (ArrayList<Entry>) daoHelper.fetchDayLog(dateFormatForQuery.format(inDate));
			updateDisplay(selectedDayListings);
			viewFilterSpinner.setSelection(0);
		}
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			ArrayList<Entry> tempList = new ArrayList<Entry>();
			
			if(!isFirstRun){
				if(parent.getId() == viewFilterSpinner.getId()){
					typeFromSpinner = (String) parent.getItemAtPosition(pos);
					if(typeFromSpinner.equalsIgnoreCase("food")){
						tempList.clear();
						tempList = (ArrayList<Entry>) daoHelper.fetchDayLog(dateFormatForQuery.format(inDate), 3, 4);
						updateDisplay(tempList);
					}else if(typeFromSpinner.equalsIgnoreCase("liquid")){
						tempList.clear();
						tempList = (ArrayList<Entry>) daoHelper.fetchDayLog(dateFormatForQuery.format(inDate), 1, 2);
						updateDisplay(tempList);
					}else{
						tempList.clear();
						tempList = (ArrayList<Entry>) daoHelper.fetchDayLog(dateFormatForQuery.format(inDate));
						updateDisplay(tempList);
					}
				}
			}else{
				isFirstRun = false;
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}

		public void updateDisplay(ArrayList<Entry> tempList){
			listAdapter.clear();
			listAdapter.addAll(tempList);
			listAdapter.notifyDataSetChanged();
		}
		@Override
		public void onEntrySubmitted(DialogFragment fragment, String type,
				Date currentDateOnScreen) {
			type = eventHandler.typeFromSpinner;
			ArrayList<Entry> list = new ArrayList<Entry>();
			if(type.equalsIgnoreCase("food")){
				list = (ArrayList<Entry>) daoHelper.fetchDayLog(dateFormatForQuery.format(currentDateOnScreen), 3, 4);
			}else if(type.equalsIgnoreCase("liquid")){
				list = (ArrayList<Entry>) daoHelper.fetchDayLog(dateFormatForQuery.format(currentDateOnScreen), 1, 2);
			}else{
				list = (ArrayList<Entry>) daoHelper.fetchDayLog(dateFormatForQuery.format(currentDateOnScreen));
			}
			updateDisplay(list);
		}
	}
}

