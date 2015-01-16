package com.acme.core;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * 
 * A class that reads and writes data in a way that is only readable to the program.
 * This class acts as a buffer between the file system and the actual {@link FileManagerGUI}.
 * @author Edward Liang
 */

public class TeacherReadWriter{
	private File file;
	
	/** Constructs a TeacherReadWriter object from the file.
	 * @param file - the data file containing teacher information
	 */
	
	public TeacherReadWriter(File file){
		this.file = file;
	}

	/** Adds the given teacher's information into the data file.
	 * @param teacher - the <code>Teacher</code> object to append to this object's data file
	 */

	public void appendTeacher(Teacher teacher){
		try{
			FileWriter fwrite = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fwrite);

			String line = teacher.getLastName()+"~"+teacher.getLastName()+"~";

			for(int i = 0;i<7;i++)
				line+=teacher.getPeriods()[i];

			line+="~";

			for(int i = 0;i<7;i++){
				if(teacher.getClasses()[i]==null)
					line+="-";
				else
					line+=teacher.getClasses()[i];

				line+=",";
			}
			line+="~";

			for(int i = 0;i<7;i++){
				if(teacher.getClasses()[i]==null)
					line+="-";
				else
					line+=teacher.getRooms()[i];

				line+=",";

			}

			out.append(line);
			out.close();
		}catch(Throwable e){
			e.printStackTrace();
		}
	}

	/**
	 * Clears the currently held file and writes the objects specified in <code>teacher</code> to the data file.
	 * @param teachers - the new <code>Teacher</code> objects to be written to this object's data file
	 */

	public void setTeachers(Teacher[] teachers){
		try{
			FileWriter fwrite = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fwrite);
			out.write("");

			for(int i = 0; i<teachers.length;i++)
				appendTeacher(teachers[i]);

		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	/**
	 * A convenience method for <code>setTeachers(Teacher[])</code>
	 * @param teachers - an <code>ArrayList</code> of <code>Teacher</code>'s
	 */
	
	public void setTeachers(java.util.ArrayList<Teacher> teachers){
		setTeachers(teachers.toArray(new Teacher[1]));
	}

	/**
	 * Gets the teachers that are held in this object's data file.
	 * @return An <code>ArrayList</code> containing the teachers, or null if the file is empty.
	 */

	public Teacher[] getTeachers(){
		ArrayList<Teacher> teachers = new ArrayList<Teacher>();

		try{
			FileReader fread = new FileReader(file);
			BufferedReader in = new BufferedReader(fread);

			String line = "";
			while((line = in.readLine()) != null){
				StringTokenizer st = new StringTokenizer(line,"~");	
				String last = st.nextToken();
				String first = st.nextToken();
				String periodsString = st.nextToken();
				String classesString = st.nextToken();
				String roomsString = st.nextToken();
				int[] periods = new int[7];

				for(int i = 0;i<7;i++)
					periods[i] = (int)Integer.parseInt(""+periodsString.charAt(i));

				String[] classes = new String[7];

				for(int i = 0; i<7; i++){
					StringTokenizer ct = new StringTokenizer(classesString,",");
					classes[i] = ct.nextToken();			
				}

				String[] rooms = new String[7];

				for(int i = 0; i<7; i++){
					StringTokenizer ct = new StringTokenizer(roomsString,",");
					rooms[i] = ct.nextToken();			
				}

				teachers.add(new Teacher(last,first,classes,rooms));
			}

		}catch(Throwable t){
			t.printStackTrace();
		}

		return teachers.toArray(new Teacher[teachers.size()]);
	}

}
