package com.acme.exporter;

import com.acme.core.Organizer;
import com.acme.core.Teacher;
import com.acme.core.Utilities;

/**
 * A specialized version of {@link MSExporter} that exports specific sections of the grid generated by an object of the {@link Substitute} class obtained from an instance of {@link Organizer}.
 */

public class MSSubExporter extends MSExporter {
	@SuppressWarnings("deprecation")
	protected static String filename = "SubSchedule_" + (DATE.getYear() + 1900) + "-" + (DATE.getMonth() + 1) + "-" + DATE.getDate() + ".xls";
	
	private int subNumber;
	private String[] classNames;
	private String[] classRooms;
	private String[] teachers;

	/**
	 * <b>Prerequisite: </b>  <code>0 <= subNum < o.getSubUsed() </code>
	 * <br>Constructs a new MSSubExporter using the given <code>Organizer</code> and substitute number.
	 * @param o - the <code>Organizer</code> to use
	 * @param subNum - a number specifying the substitute to use
	 */

	public MSSubExporter(Organizer o, int subNum){
		super(o);
		subNumber = subNum;
		classNames = o.getSub(subNumber).getClassNames();
		classRooms = o.getSub(subNumber).getClassRooms();
		Teacher[] t = o.getTeachers();
		teachers = new String[t.length];

		for(int i = 0; i < t.length; i++){
			teachers[i] = t[i].getFullName();
		}
	}

	/**
	 * Exports the currently held <code>Substitute</code>'s schedule.
	 */
	
	public void exportSchedule(){

		if(isWritten())
			return;
		excelSheet.setColumnView(0, 30); //Set the width of the first column to 80px
		for(int k = 1; k < getGrid().length; k++){ //Set the width of the other columns
			excelSheet.setColumnView(k, 30);
		}

		try{
			String[] HEADERS = Utilities.HEADERS;

			//Write the headers
			for(int i = 0; i < HEADERS.length; i++)
				addLabel(i, 0, HEADERS[i], FORMATS[0]);			

			//Write the period numbers
			for(int i = 0; i < 7; i++)
				addLabel(0, i+1, ""+(i+1), FORMATS[1]);


			String[] bellSchedule = Utilities.BELL_SCHEDULE;

			//Write the bell schedule
			for(int i = 0; i < 7; i++)
				addLabel(1, i+1, bellSchedule[i], FORMATS[1]); 

			//Write the teacher names, class names, and room numbers
			for(int i = 0; i < 7; i++)
				addLabel(2, i+1, teachers[i], FORMATS[1]); 

			for(int i = 0; i < 7; i++)
				addLabel(3, i+1, classNames[i], FORMATS[1]); 

			for(int i = 0; i < 7; i++)
				addLabel(4, i+1, classRooms[i], FORMATS[1]); 


		}catch(Throwable t){
			t.printStackTrace();
			System.out.println("Inside exportSchedule function, MSSubExporter");
		}

	}
}