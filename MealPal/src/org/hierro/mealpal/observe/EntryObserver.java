package org.hierro.mealpal.observe;

import java.util.Date;

import android.app.DialogFragment;

public interface EntryObserver {

	public void onEntrySubmitted(DialogFragment fragment, String type, Date currentDateOnScreen);
	
}
