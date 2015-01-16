package com.acme.gui.main;

import java.awt.Event;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import com.acme.core.Teacher;
import com.acme.core.TeacherReadWriter;
import com.acme.core.Utilities;
import com.acme.gui.helper.TeacherEditFrame;

public class FileManagerGUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private ArrayList<Teacher> teachers;
	private File openFile;
	private JTable infoTable;
	private CountDownLatch latch;

	/**
	 * Constructs a FileManagerGUI with no file open
	 */

	public FileManagerGUI(){
		this(null);
	}

	/**
	 * Constructs a new FileManagerGUI with the given file's contents.
	 * @param file - an SCP file
	 */

	public FileManagerGUI(String file){
		super("File Manager " + ((int)(SubScapeGUI.VERSION-1) + 0.5));
		//setLayout(new java.awt.GridBagLayout());

		setLayout(new java.awt.GridLayout(2, 1));

		MenuBar bar = new MenuBar(this);
		this.setJMenuBar(bar);
		java.awt.Rectangle b = SubScapeGUI.programBounds;
		int height = 2 * b.height/3;
		int width = 2 * b.width/3;

		int xPos = b.x + b.width/2 - width/2;
		int yPos = b.y + b.height/2 - height/2;

		java.awt.Rectangle newBounds = new Rectangle(xPos, yPos, width, height);
		setBounds(newBounds);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFileAssoc();

		latch = new CountDownLatch(1);
		teachers = new ArrayList<Teacher>();

		if(file != null)
			openFile = new File(file);

		showOpenFile();


		//TODO Fix this
		JPanel panel = new JPanel(new java.awt.GridLayout(1, 2));
		String[] subs = new String[teachers.size()];
		
		for(int i = 0; i < teachers.size(); i++){
			subs[i] = teachers.get(i).getFullName();
		}
		
		javax.swing.JComboBox subsList = new javax.swing.JComboBox(subs);
		panel.add(subsList);
		panel.add(new JButton("Add"));

		this.add(panel);

		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Throwable th){
			th.printStackTrace();
		}
		
		this.add(infoTable);
		
		try{
			UIManager.setLookAndFeel(SubScapeGUI.lookAndFeel);
		}catch(Throwable th){
			th.printStackTrace();
		}
	}

	private void setFileAssoc() {
		String token = Utilities.getTokenValue("fileAssoc");
		System.out.println("token = " + token);
		if(token == null || token.endsWith("false")){
			try {
				BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(Utilities.getInfo()));
				String path = new File(".").getAbsolutePath() + "\\File Manager.exe";
				writer.append("ftype SCPfile="+path);
				Utilities.runBatchFile("extensionConfig.bat");
				Utilities.addToken("fileAssoc", "true");

			} catch (IOException e) {
				e.printStackTrace();
				Utilities.addToken("fileAssoc", "false");
			}
		}
	}

	private void showOpenFile() {
		if(openFile == null){
			String[] classes = {"Bio", "Health", "Writing", "", "Literature", "", "Reading"};
			String[] rooms = {"42", "15", "33", "", "199", "", "12"};
			Teacher te = new Teacher("Gonzales", "Carlos", classes, rooms);
			addTeacher(te);
			Teacher[] t = {te};
			infoTable = new JTable(new TableModel(t));
			return;
		}

		TeacherReadWriter rw = new TeacherReadWriter(openFile);
		Teacher[] teachers = rw.getTeachers();

		loadTeacherList(teachers);
		loadTableModel(teachers);
	}

	private void loadTableModel(Teacher[] teachers) {
		TableModel current = new TableModel(teachers);
		infoTable = new JTable(current);
	}

	private void loadTeacherList(Teacher[] teachers) {

	}

	/**
	 * Called when an object with an attached <code>ActionListener</code> is activated.
	 * @param e - an <code>ActionEvent</code> object containing information about the event
	 */

	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem) e.getSource();
		String text = source.getText();
		//TODO implement this so it displays the teacher in the JTable

		final Teacher selectedTeacher = getSelectedTeacher();

		if("Edit".equals(text)){
			if(selectedTeacher == null)
				return;

			TeacherEditFrame frame = new TeacherEditFrame(this, selectedTeacher);
			frame.setVisible(true);

			try {
				latch.await();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			resetLatch();
		}else if("Add".equals(text) || "Save".equals(text)){
			if(selectedTeacher == null){
				System.out.println("NULLNULLLNULLLNULL");
				return;
			}

			final FileManagerGUI parent = this;
			Callable<Teacher> frameMini = new Callable<Teacher>() {
				private TeacherEditFrame frame;
				public Teacher call() throws Exception {
					frame = new TeacherEditFrame(parent, selectedTeacher);
					frame.setVisible(true);
					
					return getTeacher();
				}
				
				public Teacher getTeacher(){
					return frame.getTeacher();
				}
			};

			long timeout = 0;

			ExecutorService executor = Executors.newSingleThreadExecutor();
			Future<Teacher> job = null;
			try{
				//================= HERE ==================
				job = executor.submit(frameMini);
				executor.awaitTermination(timeout, TimeUnit.SECONDS);      
				if(!job.isDone())
					System.out.println("Done");
				//=========================================
			}catch (Exception exc) {
				exc.printStackTrace();      
			}finally{
				if(!executor.isShutdown() )
					executor.shutdownNow();
			}
			
			Teacher newTeacher = null;
			try {
				newTeacher = job.get();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				e1.printStackTrace();
			}
			
			System.out.println("teacher = " + newTeacher);
		}


	}
	
	//TODO Implement updateComponents() after a teacher is added
	//updateComponents();

	private Teacher getSelectedTeacher(){

		String tName = (String) infoTable.getValueAt(infoTable.getSelectedRow(), 0);

		System.out.println("Selected teacher = " + tName);

		Teacher selectedTeacher = null;
		for(int i = 0; i < teachers.size(); i++){
			System.out.println("teacher " + i + " last = " + teachers.get(i).getLastName());
			if(teachers.get(i).getLastName().toUpperCase().equals(tName.toUpperCase()))
				selectedTeacher = teachers.get(i);
		}

		return selectedTeacher;
	}

	private void resetLatch() {
		latch = new CountDownLatch(1);
	}

	/**
	 * Returns the <code>Teacher</code>'s that are currently being edited.
	 * @return an <code>ArrayList</code> of <code>Teacher</code>'s
	 */

	public Teacher[] getTeacherArray(){
		Teacher[] t = new Teacher[teachers.size()];
		for(int i = 0; i < t.length; i++){
			t[i] = teachers.get(i);
		}

		return t;
	}

	/**
	 * Adds the given <code>Teacher</code> to the currently open file
	 * @param t - a valid <code>Teacher</code> object
	 */

	public void addTeacher(Teacher t){
		System.out.println("teacher = " + t);
		teachers.add(t);
		Collections.sort(teachers);
	}

	public CountDownLatch getLatch(){
		return latch;
	}

	//	public static void main(String[] args){
	//		if(args.length == 0){
	//
	//			try{
	//				UIManager.setLookAndFeel(SubScapeGUI.lookAndFeel);
	//			}catch(Throwable t){
	//				t.printStackTrace();
	//			}
	//
	//			LiquidLookAndFeel.setLiquidDecorations(true, "panther");
	//
	//			FileManagerGUI gui = new FileManagerGUI();
	//			gui.setVisible(true);
	//		} else{
	//			FileManagerGUI gui = new FileManagerGUI(args[0]);
	//			gui.setVisible(true);
	//		}
	//	}

	private void save() {
		TeacherReadWriter rw = new TeacherReadWriter(openFile);
		rw.setTeachers(teachers);
	}

	public JTable getTable(){
		return infoTable;
	}

	/**
	 * A <code>JMenuBar</code> specific to a <code>FileManagerGUI</code>'s needs.
	 **/

	private class MenuBar extends JMenuBar{
		private static final long serialVersionUID = 1L;
		private JMenuItem[] items;
		private FileManagerGUI parent;
		private JFileChooser chooser;

		public MenuBar(FileManagerGUI gui){
			chooser = new JFileChooser();
			items = new JMenuItem[10];
			parent = gui;
			initMenu();
		}

		private void initMenu() {
			//Initialize the menus
			JMenu fileTab = new JMenu("File");
			fileTab.setMnemonic(KeyEvent.VK_F);

			JMenu loadTab = new JMenu("Load");
			loadTab.setMnemonic(KeyEvent.VK_L);

			JMenu createTab = new JMenu("Create");
			createTab.setMnemonic(KeyEvent.VK_C);

			/** Initialize the menu items for each tab **/
			items[0] = new JMenuItem("Open");
			items[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
			items[0].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("SCP Files Only", "scp"));
					int option = chooser.showOpenDialog(parent);
					if(option == JFileChooser.APPROVE_OPTION){
						File selected = chooser.getSelectedFile();
						parent.openFile = selected;
						parent.showOpenFile();
					}
				}
			});

			items[1] = new JMenuItem("Save as...");
			items[1].addActionListener(new ActionListener(){
				private File file;

				public void actionPerformed(ActionEvent e) {
					chooser.setAcceptAllFileFilterUsed(false);
					chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("SCP Files", "scp"));
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					chooser.setDialogType(JFileChooser.SAVE_DIALOG);
					int option = chooser.showSaveDialog(parent);
					if(option == JFileChooser.APPROVE_OPTION){
						file = chooser.getSelectedFile();
						System.out.println(file);
						String lastNum = Utilities.getTokenValue("teacherFileCount");
						int lastN = 0;
						if(lastNum == null){
							Utilities.addToken("teacherFileCount", "1");
						}else{
							lastN = Integer.parseInt(lastNum) + 1;
						}

						file = new File(file.getAbsolutePath() + "\\TeacherFile"+lastN);

						TeacherReadWriter rw = new TeacherReadWriter(file);
						rw.setTeachers(parent.getTeacherArray());

						Utilities.setToken("teacherFileCount", "" + lastN);
					}
				}
			});

			items[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));

			items[2] = new JMenuItem("Save");
			items[2].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					//TODO Reimplement this method after testing the TeacherEditFrame

					parent.actionPerformed(new ActionEvent(items[2], 0, "Add"));

					//parent.save();
				}
			});

			fileTab.add(items[0]);
			fileTab.add(items[2]);
			fileTab.add(items[1]);

			this.add(fileTab);
			this.add(createTab);
			this.add(loadTab);
		}
	}

	private class TableModel extends javax.swing.table.AbstractTableModel{
		private static final long serialVersionUID = 1L;
		public static final boolean DEBUG = true;
		private static final String INVALID = "---";

		private String[] columnNames;
		private Object[][] data;

		public TableModel(Teacher[] t){
			//Copy the arrays into these fields
			columnNames = new String[8];
			columnNames[0] = "Teacher Name";

			for(int i = 0; i < 7; i++)
				columnNames[i] = "Period " + (i+1);

			data = new Object[t.length][8];

			for(int r = 0; r < data.length; r++){
				for(int c = 1; c < data[r].length; c++){
					boolean taught = t[r].getPeriods()[c-1].booleanValue();
					if(!taught){
						data[r][c] = INVALID;
					}else{
						data[r][c] = "Yes";
					}
				}
			}

			for(int r = 0; r < data.length; r++){
				data[r][0] = t[r].getLastName();
			}
		}

		public void insertRow(int rows){
			Object[][] newData = new Object[data.length + rows][data[0].length];

			for(int i = 0; i < data.length; i++)
				for(int j = 0; j < data[i].length; j++)
					newData[i][j] = data[i][j];

			data = newData;
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		/*
		 * JTable uses this method to determine the default renderer/
		 * editor for each cell.  If we didn't implement this method,
		 * then the last column would contain text ("true"/"false"),
		 * rather than a check box.
		 */

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		/*
		 * Don't need to implement this method unless your table's
		 * editable.
		 */
		//		public boolean isCellEditable(int row, int col) {
		//			//Note that the data/cell address is constant,
		//			//no matter where the cell appears onscreen.
		//			if (col > 0 && row > 0){
		//				if(!(getValueAt(row, col) instanceof String))
		//					return true;
		//			}
		//
		//			return false;
		//		}

		/*
		 * Don't need to implement this method unless your table's
		 * data can change.
		 */

		public void setValueAt(Object value, int row, int col) {
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

		public void setTeacherAt(Teacher t, int row){
			if(row <= 0)
				return;

			for(int i = 1; i < data[row].length; i++){
				boolean teaches = t.getPeriods()[i-1].booleanValue();
				if(teaches){
					setValueAt("TEACHING", row, i);
				}else{
					setValueAt(INVALID, row, i);
				}
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
	}

}
