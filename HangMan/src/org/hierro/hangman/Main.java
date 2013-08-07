package org.hierro.hangman;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener{
	private String chosenWord;
	private ImageView errImage;
	private TextView displayWord;
	private ImageView goImage;
	private TextView goText;
	private Button yesBtn;
	private Button noBtn;
	private TextView usedLetters;
	private TextView revealWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		try {
			if(HangManEngine.getChosenWord() == null)
				chosenWord = HangManEngine.chooseWord(getAssets().open("words.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		usedLetters = (TextView) findViewById(R.id.usedLetters);
		displayWord = (TextView) findViewById(R.id.displayWord);
		errImage = (ImageView) findViewById(R.id.progressImage);
		displayWord.setText(HangManEngine.getDisplayWord());
		adjustErrProgress();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_newGame:
			restartActivity();
			break;
		case R.id.menu_exit:
			HangManEngine.newGame();
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private void adjustErrProgress(){
		switch (HangManEngine.getErrCounter()) {
		case 1:
			errImage.setImageResource(R.drawable.two);
			break;
		case 2:
			errImage.setImageResource(R.drawable.three);
			break;
		case 3:
			errImage.setImageResource(R.drawable.four);
			break;
		case 4:
			errImage.setImageResource(R.drawable.five);
			break;
		case 5:
			errImage.setImageResource(R.drawable.six);
			break;
		case 6:
			errImage.setImageResource(R.drawable.seven);
			displayWord.setText(chosenWord);
			break;
		}
	}

	private void checkGameStatus(){
		String gameOver = HangManEngine.checkGameOver();
		if(!gameOver.isEmpty()){
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			View dialogView = inflater.inflate(R.layout.newgame, null);

			Dialog gameOverDialog = new Dialog(Main.this,android.R.style.Theme_Holo_Dialog);
			gameOverDialog.setContentView(dialogView);
			gameOverDialog.setCancelable(false);
			gameOverDialog.setCanceledOnTouchOutside(false);

			revealWord = (TextView) dialogView.findViewById(R.id.revealWordTextView);
			goText = (TextView) dialogView.findViewById(R.id.gameOverTextView);
			goText.setText("Play Again ? ");
			goImage = (ImageView) dialogView.findViewById(R.id.gameOverImageViewer);
			noBtn = (Button) dialogView.findViewById(R.id.noButton);
			noBtn.setOnClickListener(this);
			yesBtn = (Button) dialogView.findViewById(R.id.yesButton);
			yesBtn.setOnClickListener(this);

			if(gameOver.equalsIgnoreCase("win")){
				gameOverDialog.setTitle("Congratulations !");;
				goImage.setImageResource(R.drawable.winning);
			}
			if(gameOver.equalsIgnoreCase("lose")){
				revealWord.setText("The Word is: " + chosenWord);
				gameOverDialog.setTitle("Womp Womp Womp Womp !");
				goImage.setImageResource(R.drawable.losing);
			}
			gameOverDialog.show();
		}
	}

	private void restartActivity()
	{
		HangManEngine.newGame();
		Intent i = getApplicationContext().getPackageManager()
				.getLaunchIntentForPackage(getApplicationContext().getPackageName() );

		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
		startActivity(i);
	}

	@Override
	public void onClick(View v) {
		Button alphabetButtons;
		String input;
		switch (v.getId()) {
		case R.id.yesButton:
			restartActivity();
			break;
		case R.id.noButton:
			HangManEngine.newGame();
			finish();
			break;
		default:
			alphabetButtons = (Button) v;
			input = alphabetButtons.getText().toString().toLowerCase();
			if(!HangManEngine.alreadyUsed(input.charAt(0))){
				displayWord.setText(HangManEngine.checkInput(input.charAt(0)));
				adjustErrProgress();
				usedLetters.setText("Used Letters: " + HangManEngine.getUsedLetters().toString().toUpperCase());
				checkGameStatus();
			}else{
				Toast.makeText(Main.this, "Letter Already Used !", Toast.LENGTH_SHORT).show();
			}

			//			alphabetButtons.setEnabled(false);  //TODO fix so buttons stay disabled after rotation 
			break;
		}

	}
}

