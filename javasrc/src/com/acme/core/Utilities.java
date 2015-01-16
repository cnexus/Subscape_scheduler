package com.acme.core;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * A utility class created to have multipurpose methods available to other classes.
 * @author Carlos Gonzales
 */

public class Utilities {
	/**
	 * An array containing the headers used by the {@link MSExporter} class and its subclasses.
	 */

	public static String HEADERS[] = {"Pd#", "Time", "Teacher", "Class Name", "Rm #"};

	/**
	 * An array containing the Poolesville High School bell schedule
	 */

	public static String[] BELL_SCHEDULE = {"7:25 - 8:11", "8:16 - 9:08", "9:13 - 9:59", "10:04 - 10:50", "10:50 - 11:36", "11:41 - 12:27", "12:32 - 1:18", "1:23 - 2:10"};
	private static Desktop desktop = Desktop.getDesktop();
	private static Toolkit defToolkit = Toolkit.getDefaultToolkit();
	private static File info = new File("config.inf");

	private Utilities(){
	}
	
	/**
	 * Creates a file from the given <code>String</code> path and passes it to <code>launchExternal(File file)</code>.
	 * @param path - the path to the file to be launched
	 * @see {@link launchExternal(File file)}
	 */
	

	public static void launchExternal(String path){
		launchExternal(new File(path));
	}

	/**
	 * Launches the associated application and opens a file.
	 * @param file - the file to be launched
	 * @throws IOException if the specified file has no associated application or the associated application fails to be launched
	 * @see Desktop
	 */

	public static void launchExternal(File file){
		try {
			desktop.open(file);
		} catch (IOException e) {
			System.out.println("Failed to open " + file.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Prints a file with the native desktop printing facility, using the associated application's print command.
	 * @param file - the file to be printed
	 * @throws IOException if the specified file has no associated application that can be used to print it
	 */

	public static void print(File file){
		try {
			desktop.print(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prints all elements in the two-dimensional <code>grid2d</code> array, in a tabular format.
	 * @param grid2d - the two-dimensional array to print
	 */

	public static void printGrid(Object[][] grid2d){
		for(Object[] o: grid2d){
			System.out.println(java.util.Arrays.toString(o));
		}
	}

	/**
	 * Prints all elements in the one-dimensional <code>grid</code> array, in a tabular format.
	 * @param grid - the one-dimensional array to print
	 */

	public static void printGrid(Object[] grid){
		System.out.println(java.util.Arrays.toString(grid));
	}

	/**
	 * Gets the default toolkit for the current operating system.
	 * @return the default toolkit
	 * @see Toolkit
	 */

	public static Toolkit getToolkit(){
		return defToolkit;
	}

	/**
	 * Gets a random color with RGB values between 0 and 255.
	 * @return a random color with RGB values between 0 and 255
	 */

	public static java.awt.Color getRandomColor(){
		return getRandomColor(0, 255);
	}

	/**
	 * Gets a random color with RGB values between 0 and 255.
	 * @param low - the lower bound for the color, cannot be greater than 255 or less than 0
	 * @param high - the upper bound for each color, cannot be greater than 255 or less than 0
	 * @return a random color with RGB values between 0 and 255
	 * @throws IllegalArugmentException if the arguments are not between 0 and 255, inclusive
	 */

	public static java.awt.Color getRandomColor(int low, int high){

		boolean highCheck = high > 255 || high < 0;
		boolean lowCheck = low > 255 || low < 0;

		if(highCheck || lowCheck)
			throw new IllegalArgumentException("Arguments must be between 0 and 255, inclusive");

		int r = (int) (Math.random() * Math.abs(low - high) + Math.min(low, high));
		int g = (int) (Math.random() * Math.abs(low - high) + Math.min(low, high));
		int b = (int) (Math.random() * Math.abs(low - high) + Math.min(low, high));

		return new java.awt.Color(r, g, b);
	}

	/**
	 * Writes the elements in the given <code>String</code> array to the file specified by <code>path</code>.
	 * @param lines - a String array containing the text to be written
	 * @param path - the path to the file which <code>lines</code> should be written
	 * @throws IOException if path does not point to a valid file
	 * @see {@link BufferedWriter}, {@link FileWriter}
	 */

	public static void write(String[] lines, String path){
		ReadWriter.write(lines, path);
	}

	/**
	 * Writes the given String to the file specified by <code>path</code>.
	 * @param line - a String array containing the text to be written
	 * @param path - the path to the file which <code>line</code> should be written
	 * @throws IOException if <code>path</code> does not point to a valid file
	 */

	public static void write(String line, String path){
		ReadWriter.write(line, path);
	}

	/**
	 * Reads all text data from the file specified by <code>path</code>.
	 * @param path - the path specifying the file be read
	 * @return a <code>String</code> array containing the data in the text file
	 * @throws IOException if path does not point to a valid file
	 * @see {@link BufferedReader}, {@link FileReader}
	 */

	public static String[] readAll(String path){
		return ReadWriter.read(path, -1);
	}

	/**
	 * Reads the given number of lines from the file specified by <code>path</code>.
	 * @param path - the path specifying the file to be read
	 * @param max - the number of lines that should be read from the file
	 * @return a <code>String</code> array of length <code>max</code> containing the lines read from <code>path</code>
	 * @throws IOException if <code>path</code> does not point to a valid file
	 * @see {@link BufferedReader}, {@link FileReader}
	 */

	public static String[] read(String path, int max){
		return ReadWriter.read(path, max);
	}

	/**
	 * Returns the Desktop instance of the current browser context.
	 * @return the <code>Desktop</code> for the current browser context.
	 * @see Desktop
	 */

	public static Desktop getDesktop(){
		return desktop;
	}

	/**
	 * Gets the contrast {@link Color} for <code>color</code> using perceptive luminance.
	 * @param color - the color for which to find the contrast
	 * @return <code>Color.WHITE</code> or <code>Color.BLACK</code>, whichever is most contrasting to <code>color</code>
	 */

	public static Color getContrastColor(Color color){
		// Counting the perceptive luminance - human eye favors green color... 
		double a = 1 - ( 0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue())/255;

		if (a < 0.5)
			return Color.WHITE; //Dark background, so use white font
		else
			return Color.BLACK; //Bright background, so use dark font

		/***************
		 * source:
		 * http://stackoverflow.com/questions/1855884/determine-font-color-based-on-background-color
		 ***************/
	}

	/**
	 * Gets the contrast for the given RGB values in <code>vals</code>.
	 * @param vals - the RGB values for which to find a contrast
	 * @return 0, which indicates a black contrast should be used, or 1, which indicates a white font should be used
	 * @throws IllegalArgumentException if <code>vals.length</code> < 3
	 */

	public static int getContrastColor(int[] vals){
		if(vals.length < 3)
			throw new IllegalArgumentException("vals must have a length of at least 3");
		int d = 0;

		// Counting the perceptive luminance - human eye favors green color... 
		double a = 1 - ( 0.299 * vals[0] + 0.587 * vals[1] + 0.114 * vals[2])/255;

		if (a < 0.5)
			d = 0; // bright colors - black font
		else
			d = 1; // dark colors - white font

		return  d;

		/***************
		 * source:
		 * http://stackoverflow.com/questions/1855884/determine-font-color-based-on-background-color
		 ***************/
	}

	/**
	 * Adds a number of spacer <code>JPanel</code>'s to the given <code>Container</code>. Implemented for use with <code>GridLayout</code>.
	 * @param container - a <code>Container</code> to which <code>JPanel</code>'s should be added
	 * @param numPanels - the number of JPanel's to add
	 * @see {@link Container}, {@link JPanel}, {@link GridLayout}
	 */

	public static void addSpacers(Container container, int numPanels) {
		for(int k = 0; k < numPanels; k++)
			container.add(new JPanel());
	}

	/**
	 * Copies all elements from <code>from</code> into <code>to</code>.
	 * 
	 * @param from - the source {@code Object[]} array
	 * @param to - the target {@code Object[]} array, has a length equal to {@code from}
	 */

	public static void copyArray(Object[] from, Object[] to){
		System.arraycopy(from, 0, to, 0, from.length);
	}

	/**
	 * Copies all elements from <code>from</code> into <code>to</code>.
	 * 
	 * @param from - the source {@code int[]} array
	 * @param to - the target {@code int[]} array , has a length equal to {@code from}
	 */

	public static void copyArray(int[] from, int[] to){
		System.arraycopy(from, 0, to, 0, from.length);
	}

	/**
	 * Checks to see if the extension given by {@code ext} is equal to the given <code>File</code>'s extension.
	 * @param file - the <code>File</code> object to check 
	 * @param ext - the extension to check for
	 * @return a <code>File</code> object guaranteed to have the same path as {@code file} with an extension equal to <code>ext</code>
	 */

	public static File checkFileExtension(File file, String ext){
		String fileName = file.getName();
		if(!fileName.endsWith(ext)){
			File newFile = new File(file.getAbsolutePath() + ext);
			System.out.println("new file = \n" + newFile);
			return newFile;
		}

		return file;
	}

	/**
	 * Returns the information file
	 * @return the information <code>File</code>
	 */

	public static File getInfo(){
		return info;
	}

	/**
	 * Gets the first token that begins with the given id.
	 * @param id - the identifier to search for in the information file
	 * @return a <code>String</code> containing the value for the first token that begins with <code>id</code>, or <code>null</code> if no such token exists
	 */

	public static String getTokenValue(String id){
		String[] tokens = Utilities.readAll(info.getAbsolutePath());

		for(String s: tokens){
			if(s != null && s.startsWith(id))
				return s.substring(s.indexOf("=")+1);
		}

		return null;
	}

	/**
	 * Gets all the tokens that begin with the given id.
	 * @param id - the identifier to search for in the information file
	 * @return a <code>String[]</code> array containing all tokens that begin with <code>id</code>
	 */

	public static String[] getTokenValues(String id){
		String[] tokens = Utilities.readAll(info.getAbsolutePath());

		ArrayList<String> goodToks = new ArrayList<String>();


		for(String s: tokens){
			if(s != null && s.startsWith(id + "="))
				goodToks.add(s.substring(s.indexOf("=")+1));
		}

		return goodToks.toArray(new String[1]);
	}

	public static void addToken(String id, String value){
		try{
			java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(info));
			java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(info));

			String line = null;
			ArrayList<String> preData = new ArrayList<String>();

			while((line = reader.readLine()) != null)
				preData.add(line);

			boolean written = false;

			writer.write("");
			writer.close();

			writer = new java.io.BufferedWriter(new java.io.FileWriter(info));

			for(int i = 0; i < preData.size(); i++){
				if(preData.get(i).startsWith(id)){
					writer.write(preData.get(i));
					writer.write(id + "=" + value);
					written = true;
				}
			}

			if(!written)
				writer.write(id + "=" + value);

			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Executes the given batch file.
	 * @param batchFile - the batch file to execute
	 * @throws IOException
	 */

	public static void runBatchFile(String batchFile) throws IOException{
		File batch = new File(batchFile);
		if(batch.exists()){
			ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", batchFile);
			pb.start();
		}else{
			throw new IllegalArgumentException("batch file does not exist");
		}
	}

	/**
	 * An internal class that handles reading and writing functions.
	 */
	
	static class ReadWriter{
		public static void write(String[] lines, String path){
			try{
				java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(path));
				for(String s: lines){
					writer.write(s);
				}

				writer.close();
			}catch(IOException t){
				System.err.println("Error writing to " + path);
				t.printStackTrace();
			}
		}

		/**
		 * Writes the given String to the file specified by <code>path</code>.
		 * @param line - a String array containing the text to be written
		 * @param path - the path to the file which <code>line</code> should be written
		 * @throws IOException if <code>path</code> does not point to a valid file
		 */

		public static void write(String line, String path){
			try{
				java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(path));
				writer.write(line);
				writer.close();
			}catch(IOException t){
				System.err.println("Error writing to " + path);
				t.printStackTrace();
			}
		}

		/**
		 * Reads all text data from the file specified by <code>path</code>.
		 * @param path - the path specifying the file be read
		 * @return a <code>String</code> array containing the data in the text file
		 * @throws IOException if path does not point to a valid file
		 * @see {@link BufferedReader}, {@link FileReader}
		 */

		public static String[] readAll(String path){
			return read(path, -1);
		}

		/**
		 * Reads the given number of lines from the file specified by <code>path</code>.
		 * @param path - The path specifying the file to be read
		 * @param max - The number of lines that should be read from the file
		 * @return A <code>String</code> array of length <code>max</code> containing the lines read from <code>path</code>
		 * @throws IOException if <code>path</code> does not point to a valid file
		 * @see {@link BufferedReader}, {@link FileReader}
		 */

		public static String[] read(String path, int max){
			java.util.ArrayList<String> lines = new java.util.ArrayList<String>();
			try{
				java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(path));
				String line = null;
				int count = 0;
				boolean check = true;
				if(max == -1)
					check = false;

				while((line = reader.readLine()) != null && (check ? (count < max) : true)){
					if(line.length() != 0){
						lines.add(line);
						count++;
					}
				}
			}catch(Throwable t){
				System.err.println("Error reading file.");
				t.printStackTrace();
			}

			return lines.toArray(new String[1]);
		}
	}

	/**
	 * Set's the first occurrence of the given token to the given value
	 * @param id - the id of the token
	 * @param val - the value that the token should be set to
	 */

	public static void setToken(String id, String val) {
		String[] tokens = Utilities.read(info.getAbsolutePath(), -1);
		int index = 0;

		for(int i = 0; i < tokens.length; i++){
			if(tokens[i].startsWith(id))
				index = i;
		}
		
		tokens[index] = id + "=" + val;
		Utilities.write(tokens, info.getAbsolutePath());
	}
}
