package org.hierro.hangman;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class HangManEngine {
	private static List usedLetters = new ArrayList();
	private static int ErrCounter;
	private static String chosenWord;
	private static StringBuilder displayWord;
	private static List<String> words;
	
//	public static void addUsedLetter(String letter){
//		usedLetters.add(letter);
//	}
	
	public static void newGame(){
		usedLetters.clear();
		ErrCounter = 0;
		chosenWord = null;
		displayWord.setLength(0);
	}

	public static String getChosenWord() {
		return chosenWord;
	}

	public static int getErrCounter() {
		return ErrCounter;
	}

	public static List getUsedLetters() {
		Collections.sort(usedLetters);
		return usedLetters;
	}

	private static String toDashes(String word){
		displayWord = new StringBuilder();
		displayWord.setLength(0);
		for (char letter : word.toCharArray()) {
			displayWord.append('-');
		}
		return displayWord.toString();
	}

	public static boolean isValidInput(char letter){
		boolean valid = true;
		if(!Character.isLetter(letter)) return !valid;
		return valid;
	}

	public static String chooseWord(InputStream in){
		if(words == null) {
			words = Parser.readFile(in);
		}
		chosenWord = words.get(new Random().nextInt(words.size()));
		toDashes(chosenWord);
		return chosenWord;
	}

	public static boolean alreadyUsed(char c){
		boolean used = false;
		if(!usedLetters.contains(c)){
			usedLetters.add(c);
		}else{
			used = true;
		}
		return used;
	}

	public static String checkInput(char input){
		boolean foundOne = false;
		for(int i=0; i<chosenWord.length();i++){
			if(input == chosenWord.charAt(i)){
				displayWord.setCharAt(i, input);
				foundOne = true;
			}
		}
		if(!foundOne){
			ErrCounter++;
		}
		return displayWord.toString();
	}

	public static String getDisplayWord() {
		return displayWord.toString();
	}

	static String checkGameOver(){
		String game = new String();
		if(ErrCounter == 6){
			game = "lose";
		}
		if(displayWord.toString().equalsIgnoreCase(chosenWord)){
			game = "win";
		}
		return game;
	}
}
