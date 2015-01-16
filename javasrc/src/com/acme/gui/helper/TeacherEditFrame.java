package com.acme.gui.helper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.AbstractTableModel;

import com.acme.core.Teacher;
import com.acme.core.Utilities;
import com.acme.gui.main.FileManagerGUI;
import com.acme.gui.main.SubScapeGUI;

/**
 * A tabular representation of a <code>Teacher</code> object for use in allowing the user to existing values.
 */

public class TeacherEditFrame extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private FileManagerGUI parent;
	private JTable table;
	private Teacher teacher;

	public TeacherEditFrame(FileManagerGUI parentFrame, Teacher t){
		super((t == null ? "Adding new teacher" : ("Editing " + t.getFullName())));
		this.parent = parentFrame;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Object[][] data = new Object[3][8];
		Object[] headers = new Object[8];
		headers[0] = "Teacher";
		
		//Center this frame over the parent frame
		int width = 450;
		int height = 80;
		

		int xPos = parent.getX() + parent.getWidth()/2 - width/2;
		int yPos = parent.getY() + parent.getHeight()/2 - height/2;

		this.setBounds(xPos, yPos, width, height);
		
		System.out.println("Bounds = " + getBounds());

		for(int i = 1; i < headers.length; i++){
			headers[i] = "Period " + i;
		}

		if(t != null){
			for(int i = 1; i < data[0].length; i++){
				data[0][i] = (t.getPeriods()[i-1].booleanValue() ? "Yes" : "No");
				data[1][i] = t.getClasses()[i-1];
				data[2][i] = t.getRooms()[i-1];
			}

			data[1][0] = "Classes";
			data[2][0] = "Rooms";
		}else{
			for(int i = 1; i < data[0].length; i++){
				data[0][i] = "No";
				data[1][i] = "";
				data[2][i] = "";
			}
		}

		table = new JTable(new TableModel(data, headers));

		this.setLayout(new java.awt.GridLayout(3, 1));
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Throwable th){
			th.printStackTrace();
		}
		
		this.add(table);
		
		try{
			UIManager.setLookAndFeel(SubScapeGUI.lookAndFeel);
		}catch(Throwable th){
			th.printStackTrace();
		}
		
		Utilities.addSpacers(this, 1);

		JPanel savePanel = new JPanel();
		savePanel.setLayout(new java.awt.GridLayout(1, 6));
		Utilities.addSpacers(savePanel, 5);
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(this);

		savePanel.add(saveButton);
	}

//	private void closeWindow(){
//		WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
//		Utilities.getToolkit().getSystemEventQueue().postEvent(wev);
//	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String sourceText = ((JButton) e.getSource()).getText();

		if("Save".equals(sourceText)){
			//Save the Teacher created and then close the window

			String first = null, last = null;
			String[][] classInfo = new String[2][7];

			for(int r = 0; r < table.getRowCount(); r++){
				for(int c = 0; c < table.getColumnCount(); c++){
					String cell = ((String)table.getValueAt(r, c));
					if(c > 0){ //First column is a description cell
						if(r == 0){
							int index = cell.indexOf(",");
							if(index == -1){
								index = cell.indexOf(" ");
								first = cell.substring(0, index);
								last = cell.substring(index+1);
							}else{
								last = cell.substring(0, index);
								first = cell.substring(index+1);
							}

							last = last.trim();
							first = first.trim();
						}else if(r == 1){
							classInfo[0][c] = cell;
						}else{ //r == 2
							classInfo[1][c] = cell;
						}
					}
				}
			}
			
			//Capitalize the name and make all rooms and class rooms capital
			for(int i = 0; i < classInfo[0].length; i++){
				classInfo[0][i] = classInfo[0][i].toUpperCase();
				classInfo[1][i] = classInfo[1][i].toUpperCase();
			}
			
			char firstLetFirst = first.charAt(0);
			char firstLetLast = last.charAt(0);
			
			String newFirst = firstLetFirst + first.substring(1);
			String newLast = firstLetLast + last.substring(1);

			teacher = new Teacher(newLast, newFirst, classInfo[0], classInfo[1]);
			this.setVisible(false);
		}
	}

//	public static void main(String[] args){
//		Teacher t = new Teacher("Gonzales", "Carlos", null, null);
//		FileManagerGUI gui = new FileManagerGUI();
//		gui.setBounds(100, 50, 300, 400);
//		gui.setVisible(true);
//
//		TeacherEditFrame frame = new TeacherEditFrame(gui, t);
//		frame.show();
//
//		try {
//			gui.getLatch().await();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
	

	public Teacher getTeacher(){
		return teacher;
	}
	
	public boolean isClosed(){
		return this.isVisible();
	}

	private class TableModel extends AbstractTableModel{
		public static final boolean DEBUG = true;
		private static final long serialVersionUID = 1L;
		private Object[][] data;
		private Object[] columns;

		public TableModel(Object[][] data, Object[] headers){
			this.data = data;
			columns = headers;
		}

		public void setValueAt(Object value, int row, int col) {
			if(row <= 0)
				return;

			if (DEBUG) {
				System.out.println("Setting value at " + row + "," + col
						+ " to " + value
						+ " (an instance of "
						+ value.getClass() + ")");
			}



			data[row][col] = value;
			fireTableCellUpdated(row, col);

			if (DEBUG) {
				System.out.println("New value of data:");
				printDebugData();
			}
		}

		public void printDebugData() {
			int numRows = getRowCount();
			int numCols = getColumnCount();

			for (int i=0; i < numRows; i++) {
				System.out.print("    row " + i + ":");
				for (int j=0; j < numCols; j++) {
					System.out.print("  " + data[i][j]);
				}
				System.out.println();
			}
			System.out.println("--------------------------");
		}

		public int getRowCount(){
			return data.length;
		}
		public int getColumnCount(){
			return columns.length;
		}
		public Object getValueAt(int row, int column){
			return data[row][column];
		}

		public boolean isCellEditable(int row, int col){
			return row > 0 && col > 0;
		}
	}
}

