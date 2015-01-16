package com.acme.gui.helper;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.acme.core.Teacher;

/**
 * A GUI representation of a <code>Teacher</code> that allows the user to edit the various properties of a <code>Teacher</code>.
 */

public class TeacherSchedulePanel extends JPanel implements ItemListener {
	private static final long serialVersionUID = 1L;
	private JCheckBox[] periodBoxes = new JCheckBox[7];
	private int[] periodsTaught = new int[7];
	private JLabel teacherName;
	private String[] names = new String[2];
	private JTextField[] roomNumberBoxes = new JTextField[7];
	private JTextField[] classNames = new JTextField[7];
	private Teacher teacher;
	
	public static String defaultRoom = "Room #".trim();
	public static String defaultClass = "Class name".trim();

	/**
	 * Constructs a Teacher schedule panel using the given last name and first name.
	 * @param first - the teacher's first name
	 * @param last - the teacher's last name
	 */
	
	public TeacherSchedulePanel(String first, String last){

		for(int i = 0; i < roomNumberBoxes.length; i++){
			roomNumberBoxes[i] = new JTextField(defaultRoom, 10);
		}

		classNames = new JTextField[7];
		for(int i = 0; i < classNames.length; i++){
			classNames[i] = new JTextField(defaultClass, 10);
		}

		names[0] = last;
		names[1] = first;
		//Create the checkboxes and add THIS as an itemlistener to each one, and add an ID
		for(int i = 0; i < periodBoxes.length; i++){
			periodBoxes[i] = new JCheckBox("P" + (i+1));
			periodBoxes[i].addItemListener(this);
			periodBoxes[i].setName(i + "");
		}

		//Create the teacherName JLabel using the params
		teacherName = new JLabel(" " + names[0] + ", " + names[1]);

		//		//this.setLayout(new GridLayout())
		//		this.add(teacherName);
		//		//Add all of the checkboxes
		//		this.add(new JLabel(" "));
		//		for(int i = 0; i < periodBoxes.length; i++){
		//			this.add(periodBoxes[i]);
		//		}

		this.setLayout(new GridLayout(1, 8, 5, 0));

		this.add(teacherName);

		JPanel periods[] = new JPanel[7];
		for(int i = 0; i < periods.length; i++){
			periods[i] = new JPanel();
			JPanel checkbox = new JPanel();
			checkbox.setLayout(new GridLayout(1,1));
			checkbox.add(periodBoxes[i]);
			//checkbox.add(new JPanel());

			periods[i].setLayout(new GridLayout(3, 1)); //2 rows, 1 column
			periods[i].add(checkbox);
			periods[i].add(classNames[i]);
			periods[i].add(roomNumberBoxes[i]);
		}

		//Add the text area to get the room name
		for(int i = 0; i < periods.length; i++){
			this.add(periods[i]);
		}
	}

	/**
	 * Registers item state changes on the period boxes for this object.
	 */
	
	public void itemStateChanged(ItemEvent e) {
		JCheckBox jcb = (JCheckBox) e.getItemSelectable();
		int period = Integer.parseInt(jcb.getName());
		periodsTaught[period] = (e.getStateChange() == ItemEvent.DESELECTED) ? 0 : 1;
		//printGrid(periodsTaught);
		//System.out.println("Dimension = " + this.getSize());

	}

	@SuppressWarnings("unused")
	private void printGrid(int[] grid) {
		System.out.println(java.util.Arrays.toString(grid));
	}
	
	/**
	 * Finalizes this object by creating the internal <code>Teacher</code> object using
	 * the class names, rooms, and periods entered in the panel.
	 */

	public void finish(){
		//Read the contents from all the boxes and make the teacher object
		String[] classes = new String[7];
		String[] roomNums = new String[7];

		for(int i = 0; i < roomNumberBoxes.length; i++){
			if(periodsTaught[i] == 1){
				classes[i] = classNames[i].getText().trim();
				roomNums[i] = roomNumberBoxes[i].getText().trim();
			}else if(periodsTaught[i] == 0){
				classes[i] = defaultClass.trim();
				roomNums[i] = defaultRoom.trim();
			}
		}

		teacher = new Teacher(names[0], names[1], classes, roomNums);
	}
	
	/**
	 * Returns an integer grid representing the period boxes that are checked and unchecked
	 * @return an integer grid containing <code>0</code> for a period not being taught,
	 * and <code>1</code> for a period being taught
	 */

	public int[] getPeriods(){
		return periodsTaught;
	}

	/**
	 * Returns the internally held <code>Teacher</code> object.
	 * @return the internal <code>Teacher</code> object
	 */
	
	public Teacher getTeacher(){
		return teacher;
	}

//	public static void main(String[] args){
//		TeacherSchedulePanel tsp = new TeacherSchedulePanel("Bob", "Smith");
//		tsp.setSize(new java.awt.Dimension(400, 50));
//		javax.swing.JFrame frame = new javax.swing.JFrame("Test");
//		frame.getContentPane().add(tsp);
//		frame.setSize(420, 70);
//		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.pack();
//	}
}
