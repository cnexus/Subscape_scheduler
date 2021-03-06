package com.acme.exporter;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.acme.core.Organizer;
import com.acme.core.Teacher;
import com.acme.core.Utilities;

import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

/**
 * A specialized version of {@link Exporter} that exports a grid generated by an object of the {@link Organizer} class.
 */

public class MSExporter extends Exporter {
	/**
	 * Predefined colors compatible with Excel
	 */
	
	private static Colour[] GRID_COLORS = {
		Colour.AQUA,
		Colour.BLUE,
		//Colour.CORAL,
		Colour.BLUE_GREY,
		Colour.GOLD,
		Colour.ICE_BLUE,
		Colour.TURQUOISE,
		Colour.YELLOW,
		Colour.ROSE,
		Colour.LIME,
		Colour.LIGHT_BLUE,
		Colour.OCEAN_BLUE,
		Colour.ORANGE,
		Colour.BROWN,
		Colour.RED,
		Colour.PLUM
		};
	
	protected static Date DATE  = Calendar.getInstance().getTime();
	@SuppressWarnings("deprecation")
	protected static String filename = "Schedule_" + (DATE.getYear() + 1900) + "-" + (DATE.getMonth() + 1) + "-" + DATE.getDate() + ".xls";
	private int totalSubs;
	private String[] teachers;
	private Colour[] colors;
	private int[][] grid;
	private boolean written;

	public MSExporter(int[][] grid, String[] t, int subs) {
		super(filename);
		totalSubs = subs;
		teachers = t;
		written = false;
		this.grid = grid;
		randomizeColors();
	}
	
	public MSExporter(Organizer o){
		this(o.getGrid(), getTeacherNames(o.getTeachers()), o.getSubsUsed());
	}
	
	private void randomizeColors() {
		Random gen = new Random(System.currentTimeMillis());
		colors = new Colour[totalSubs];
		for(int i = 0; i < colors.length; i++){
			Colour c = GRID_COLORS[(int)(Math.random() * GRID_COLORS.length)];
			while(!isUniqueColor(c, i)){
				c = GRID_COLORS[gen.nextInt(GRID_COLORS.length)];
			}
			colors[i] = c;
		}
		
//		for(Colour c : colors){
//			System.out.println(c.getDescription());
//		}
	}

	private boolean isUniqueColor(Colour c, int maxIndex) {
		for(int i = 0; i < maxIndex; i++)
			if(colors[i].equals(c))
				return false;

		return true;
	}

	public void setTeachers(String[] t){
		System.arraycopy(t, 0, teachers, 0, teachers.length);
	}

	@SuppressWarnings("deprecation")
	public void exportSchedule() throws Throwable{
		if(written)
			return;
		
		excelSheet.setColumnView(0, 30); //Set the width of the first column to 80px
		
		for(int k = 1; k < grid.length; k++){ //Set the width of the other columns
			excelSheet.setColumnView(k, 20);
		}
		WritableCellFormat c = new WritableCellFormat();
		c.setAlignment(Alignment.CENTRE);
		//System.out.println("Alignment = " + c.getAlignment().getDescription());
		//First write the teacher names all the way down
		//System.out.println("length = " + teachers.length);
		for(int i = 0; i < teachers.length; i++){
			System.out.println("teacher " + i + " = " + teachers[i]);
			addLabel(0, i+1, teachers[i], FORMATS[0]); //Preinitialized bold cell format
		}
		
		//Write the teacher names
		for(int i = 0; i < 7; i++){
			int j = i + 1;
			addLabel(j, 0, "P"+j, FORMATS[0]);
		}

		//printGrid(grid);
		for(int row = 0; row < grid.length; row++){
			WritableFont f = new WritableFont(WritableFont.TIMES, 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
			for(int col = 0; col < grid[0].length; col++){
				//System.out.println("row = " + row + " \t col = " + col);
				int subNum = grid[row][col];
				if(subNum != Organizer.NOT_VALID){
					int colorRGB[] = new int[3];
					colorRGB[0] = colors[subNum-1].getDefaultRed();
					colorRGB[1] = colors[subNum-1].getDefaultGreen();
					colorRGB[2] = colors[subNum-1].getDefaultBlue();
					int contrast = Utilities.getContrastColor(colorRGB);
					
					if(contrast == 0)
						f = new WritableFont(WritableFont.TIMES, 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
					else if(contrast == 1)
						f = new WritableFont(WritableFont.TIMES, 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);

					c = new WritableCellFormat(f);
					c.setBackground(colors[subNum-1]);
					c.setAlignment(Alignment.CENTRE);
					addNumber(col+1, row+1, subNum, c); //Offset col by 1 because of the teacher names column
				}else{
					c = new WritableCellFormat();
					c.setBackground(Colour.GREY_25_PERCENT);
					c.setAlignment(Alignment.CENTRE);
					addLabel(col+1, row+1, "-", c);
				}
			}
		}
		written = true;
		workbook.write();
		workbook.close();
	}

	public static String[] getTeacherNames(Teacher[] t){
		String[] s = new String[t.length];
		for(int i = 0; i < t.length; i++)
			s[i] = t[i].getFullName();

		return s;
	}

	public void printGrid(int[][] grid2) {
		for(int[] i: grid2){
			System.out.println(java.util.Arrays.toString(i));
		}
	}
	
	public int[][] getGrid(){
		return grid;
	}

	public void printGrid(){
		printGrid(this.grid);
	}

	public boolean isWritten(){
		return written;
	}

}
