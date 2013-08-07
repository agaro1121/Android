package org.hierro.hangman;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Parser {

	public static List<String> readFile(InputStream in){
		List<String> words = new ArrayList<String>();
		String currentWord = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			currentWord = reader.readLine();
			while(currentWord != null){
				words.add(currentWord);
				currentWord = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return words;
	}
	
	
	
}
