package com.acme.gui.helper.util;

/**
 * Will be unused in final version, currently used in {@link SubScapeGUI}.
 */


//Responsible for reading the teacher names from a file name passed to the constructor

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class DatabaseFileReader {
	protected File file;
	protected String pathname;

	public DatabaseFileReader(String path){
		pathname = path;
		file = new File(pathname);
	}

	public String[] readContents(){
		String [] contents = null;
		//Read through once to get the number of teacher names\
		try{
			BufferedReader firstTime = new BufferedReader(new FileReader(file));
			int counter = 0;
			String line = "";
			while((line = firstTime.readLine()) != null){
				if(line.length() != 0)
					counter++;
			}

			firstTime.close();
			contents = new String[counter];

			BufferedReader secondTime = new BufferedReader(new FileReader(file));

			int i = 0;
			while((line = secondTime.readLine()) != null){
				if(line.length() != 0){
					contents[i] = line;
					i++;
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		}

		return contents;
	}

	public String toString(){
		return "DBReader for: /" + pathname;
	}
}
