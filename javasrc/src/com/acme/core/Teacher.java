package com.acme.core;

/**
 * An abstract representation of an immutable Teacher.
 * @author Edward Liang, Carlos Gonzales
 */

public class Teacher implements Comparable<Teacher>{
	/** This teacher's last name. */
	private String lastName;

	/** This teacher's first name. */
	private String firstName;

	/** An array that holds the course names and room numbers for this Teacher object. */
	private String[][] classInfo;

	/** An array that holds the periods that the teacher is teaching. */
	private Boolean[] periodsTaught=new Boolean[7];

	//	public Teacher(String last, String first, Boolean[] periods ){
	//		lastName = last;
	//		firstName = first;
	//		for(int i = 0; i<7; i++){
	//			periodsTaught[i] = periods[i];
	//		}
	//	}
	//	
	//	public Teacher(String last, String first, int[] periods ){
	//		lastName = last;
	//		firstName = first;
	//		for(int i = 0; i<7; i++){
	//			if(periods[i] == 1){
	//				periodsTaught[i] = true;
	//			}else if(periods[i] == 0){
	//				periodsTaught[i] = false;
	//			}
	//		}
	//	}
	//	
	//	public Teacher(Boolean[] periods){
	//		lastName = "";
	//		firstName = "";
	//		for(int i = 0; i<7; i++){
	//			periodsTaught[i] = periods[i];
	//		}
	//	}
	//	
	//	public Teacher(int[] periods){
	//		for(int i = 0; i<7; i++){
	//			if(periods[i] == 1){
	//				periodsTaught[i] = true;
	//			}
	//			else if(periods[i] == 0){
	//				periodsTaught[i] = false;
	//			}
	//		}
	//	}

	//	/**
	//	 * Constructs a Teacher object using default values.
	//	 */
	//	
	//	public Teacher(){
	//		lastName = "";
	//		firstName = "";
	//		classInfo = new String[2][7];
	//		
	//		for(int i = 0; i < 2; i++)
	//			for(int j = 0; j < 7; j++)
	//				classInfo[i][j] = "---";
	//		
	//		for(int i = 0; i < periodsTaught.length; i++){
	//			periodsTaught[i] = false;
	//		}
	//		
	//	}

	/** 
	 * Creates a Teacher object from the given last name, first name, classes, and classrooms.
	 * @param last - The teacher's last name
	 * @param first - The teacher's first name
	 * @param classes - An array that holds the course names for this teacher, in order of period (period 1 course name then period 2 course name, etc)
	 * @param classrooms - An array that holds the room numbers for this teacher, in order of period (period 1 room then period 2 room, etc)
	 */

	public Teacher(String last, String first, String[] classes, String[] classrooms){
		lastName = last;
		firstName = first;
		classInfo = new String[2][7];
		if(classes != null && classrooms != null){
			for(int i = 0;i<7;i++){
				classInfo[0][i] = classes[i];
			}

			for(int i = 0;i<7;i++){
				classInfo[1][i] = classrooms[i];
			}

			for(int i = 0; i<7; i++){
				if(classes[i] == null||classes[i] == ""||classes[i] == " "){
					periodsTaught[i] = false;
				}
				else{
					periodsTaught[i] = true;
				}
			}
		}
	}

	/** Gets the course names for this teacher.
	 * @return	A {@link String} array containing the course names for this teacher, with null indicating an empty period.
	 */

	public String[] getClasses(){
		return classInfo[0];
	}

	/** Gets the classrooms for this teacher.
	 * @return	A {@link String} array containing the classrooms assigned to this teacher, with null indicating an empty period.
	 */

	public String[] getRooms(){
		return classInfo[1];
	}

	/**
	 * Gets the full name of this teacher.
	 * @return A <code>String</code> containing the teacher's full name in the form Last, First.
	 * <br>A reversed implementation of calling <code>toString()</code> on this teacher.
	 */

	public String getFullName(){
		return this.lastName + ", " + this.firstName;
	}

	/**
	 * Gets the periods that this teacher is active/teaching.
	 * @return A <code>Boolean</code> array of length seven where <code>true</code> indicates that period is being taught.
	 */

	public Boolean[] getPeriods(){
		return periodsTaught;
	}

	/**
	 * Gets this teacher's last name.
	 * @return A <code>String</code> containing this teacher's last name.
	 */

	public String getLastName(){
		return this.lastName;
	}

	/**
	 * Gets this teacher's first name.
	 * @return A <code>String</code> containing this teacher's first name.
	 */

	public String getFirstName(){
		return firstName;
	}

	/**
	 * Checks if this teacher teaches during the given period.
	 * @param period - The period to be checked
	 * @return {@link false} if <code>period</code> = 0 or this teacher does not teach during <code>period</code>, {@link true} if this teacher teaches during <code>period</code>.
	 */

	public boolean isTeaching(int period){
		if(period == 0)
			return (Boolean) null;

		return periodsTaught[period-1].booleanValue();
	}
	
	/**
	 * Returns a multi-line String containing this teacher's name, classes, rooms, and period that he/she is teaching.
	 * @return a multi-line String containing all of this teacher's information
	 */
	
	public String fullInfo(){
		StringBuffer sb = new StringBuffer(80);
		
		sb.append(this.toString() + "\n");
		
		//Adds the class names
		for(int i = 0; i < classInfo[0].length - 1; i++)
			sb.append(classInfo[0][i] + ", ");
		sb.append(classInfo[0][classInfo[0].length -1] + "\n");
		
		
		//Add the rooms
		for(int i = 0; i < classInfo[1].length - 1; i++)
			sb.append(classInfo[1][i] + ", ");
		sb.append(classInfo[1][classInfo[1].length -1] + "\n");
		
		return sb.toString();
	}

	/** A <code>String</code> representation of this teacher.
	 * @return	The <code>String</code> representation of this teacher in the form First Last.
	 * <br>A reversed implementation of calling <code>getFullName()</code> on this teacher.
	 */

	public String toString(){
		return firstName + " " + lastName;
	}

	@Override
	public int compareTo(Teacher other) {
		return this.getLastName().compareTo(other.getLastName());
	}
}
