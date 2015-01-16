package com.acme.core;

import com.acme.core.Utilities;

/**
 * 
 * A class representing a single substitute that holds the names of the teachers that the substitute is in for, 
 * the classes, the room numbers, and the number identifier given by an instance of {@link Organizer}.
 * 
 */

public class Substitute{
	private String[] teachersSubbed;
	private String[] classes;
	private String[] rooms;
	private int number;
	
	/** Constructs a Substitute object from the information.
	 * <br><br>
	 * <b>Prerequisites:</b>
	 * <br><code>teachersSubbed.length == 7</code>
	 * <br><code>classes.length == 7</code>
	 * <br><code>rooms.length == 7</code>
	 * @param teachersSubbed - teachers subbed for in order of period
	 * @param classes - classes subbed for in order of period
	 * @param rooms - rooms subbed for in order of period
	 * @param number - identifying number given by the {@link Organizer} class
	 */

	public Substitute(String[] teachers, String[] classes, String[] rooms, int num){
		Utilities.copyArray(classes, this.classes);
		Utilities.copyArray(teachers, teachersSubbed);
		Utilities.copyArray(rooms, this.rooms);
		number = num;
	}
	
	/** Gets the classrooms for this teacher.
	 * @return	An int identifier given by {@link Organizer}.
	 */

	public int getSubNumber(){
		return number;
	}

	
	/**
	 * @return an array of length 7 of {@link String} of the class names. 
	 * Element of array is null if sub does not teach during that period.
	 */
	
	public String[] getClassNames() {
		return classes;
	}

	/**
	 * @return an array of length 7 of {@link String} of the class room numbers. 
	 * Element of array is null if sub does not teach during that period.
	 */
	
	public String[] getClassRooms() {
		return rooms;
	}
}