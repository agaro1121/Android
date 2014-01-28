package org.hierro.mealpal.activity;

import org.hierro.mealpal.R;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends FragmentActivity {

	private Button addFoodButton;
	private Button addLiquidButton;
	private OnClickListener buttonHandler;
	private FoodDialog foodDialog = new FoodDialog();
	private LiquidDialog liquidDialog = new LiquidDialog();
	private FragmentManager fm;
	private FragmentTransaction ft;
	private boolean isFoodFirstRun = false;
	private boolean isLiquidFirstRun = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		addFoodButton = (Button) findViewById(R.id.btn_addSolid);
		addLiquidButton = (Button) findViewById(R.id.btn_addLiquid);
		buttonHandler = new ButtonHandler();
		addFoodButton.setOnClickListener(buttonHandler);
		addLiquidButton.setOnClickListener(buttonHandler);

		foodDialog = new FoodDialog();
		liquidDialog = new LiquidDialog();
		ViewFragment v = new ViewFragment();
		fm = getFragmentManager();
		ft = fm.beginTransaction();
		ft.add(foodDialog, "foodDialog");
		ft.add(R.id.layout_view_content_main, v, "ViewFragment");
		ft.remove(foodDialog);
		ft.add(liquidDialog, "liquidDialog");
		ft.remove(liquidDialog);
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class ButtonHandler implements OnClickListener{

		@Override
		public void onClick(View v) {

			if(v == addFoodButton){
				if(isFoodFirstRun){
					ft.add(foodDialog,"foodDialog");
					isFoodFirstRun = false;
				}
				foodDialog.show(getFragmentManager(), "foodDialog");
			}
			else if(v == addLiquidButton){
				if(isLiquidFirstRun){
					ft.add(liquidDialog, "liquidDialog");
					isLiquidFirstRun = false;
				}
				liquidDialog.show(getFragmentManager(), "liquidDialog");
			}
		}
	}

}
