package com.acme.gui.main;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import com.acme.core.Organizer;
import com.acme.core.Teacher;
import com.acme.core.Utilities;
import com.acme.exporter.MSExporter;
import com.acme.gui.helper.FileChooserPanel;
import com.acme.gui.helper.MSPanel;
import com.acme.gui.helper.TeacherSchedulePanel;
import com.acme.gui.helper.util.DatabaseFileReader;
import com.birosoft.liquid.LiquidLookAndFeel;

//import ch.randelshofer.quaqua.QuaquaLookAndFeel;

/**
 * The main class, guides the user through three steps of selecting a file, confirming data, and then viewing the schedule the given creates.
 */

public class SubScapeGUI extends JFrame implements ActionListener{
	public static double VERSION = 2.3;
	public static final javax.swing.LookAndFeel lookAndFeel = new LiquidLookAndFeel();
	public static final Dimension screenSize = Utilities.getToolkit().getScreenSize();
	private static final long serialVersionUID = 1L;
	public static final Rectangle programBounds = getProgramBounds();
	private static final int WIDTH = 870;
	private static final int HEIGHT = 600;
	// private static final String[] icons = {"browse.png", "", ""};
	private int currentSlide; // Zero-based
	private MSExporter exporter;
	private FileChooserPanel fcp;
	private TeacherSchedulePanel[] teacherPanels;
	private JComponent[] slides;
	private JButton[] nextButtons;
	private JScrollPane slide1;
	private CountDownLatch latch;

	/**
	 * Constructs an instance of <code>SubScapeGUI</code>, using the given name.
	 * @param name - the name to be displayed in the frame window
	 */
	
	protected SubScapeGUI(String name) {
		super(name);
		latch = new CountDownLatch(1);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(programBounds);
		this.setLocation(getLocation().x, getLocation().y - 20);
		this.setVisible(true);

		this.setJMenuBar(new CustomMenuBar(this));

		initFields();
		currentSlide = 0;

		// Content pane setup
		JTabbedPane pane = new JTabbedPane(JTabbedPane.TOP,
				JTabbedPane.WRAP_TAB_LAYOUT);

		pane.addTab("Open Data File", slides[0]);
		pane.addTab("Select teacher data", slide1);
		pane.addTab("Master Schedule", new JPanel(true));

		// Add the stuff for the first slide (0)
		slides[0].setLayout(new GridLayout(10, 1));
		Utilities.addSpacers(slides[0], 3);
		slides[0].add(fcp);
		Utilities.addSpacers(slides[0], 5);
		// Add the next button
		JPanel nextButtonHolder = new JPanel();
		nextButtonHolder.setLayout(new GridLayout(2, 6));
		Utilities.addSpacers(nextButtonHolder, 11);
		nextButtonHolder.add(nextButtons[0]);
		slides[0].add(nextButtonHolder);

		pane.setVisible(true);
		pane.setSelectedIndex(0);

		pane.addChangeListener(new javax.swing.event.ChangeListener(){
			public void stateChanged(javax.swing.event.ChangeEvent e){
				currentSlide = ((JTabbedPane) e.getSource()).getSelectedIndex();
				updateSlides();
			}
		});

		this.setContentPane(pane);

		updateSlides();

	}

	/**
	 * Constructs an instance of <code>SubScapeGUI</code> using the default title.
	 */
	
	public SubScapeGUI() {
		this("SubScape Scheduler v" + VERSION);
	}
	
	/**
	 * Gets the centered bounds for all instance of <code>SubScapeGUI</code> according to the screen size.
	 * @return the centered bounds for all instances of <code>SubScapeGUI</code>
	 */

	private static Rectangle getProgramBounds() {
		int x = (screenSize.width - SubScapeGUI.WIDTH) / 2;
		int y = (screenSize.height - SubScapeGUI.HEIGHT) / 2;
		// System.out.println("Screen size = " + screenSize);
		// System.out.println("WIDTH = " + WIDTH + " HEIGHT = " + HEIGHT);
		// System.out.println("x = " + x + "y = " + y);

		return new Rectangle(new java.awt.Point(x, y), new Dimension(WIDTH,
				HEIGHT));
	}
	
	/**
	 * Initializes this objects fields.
	 */

	private void initFields() {
		slides = new JComponent[3];
		nextButtons = new JButton[3];
		for (int i = 0; i < slides.length; i++) {
			slides[i] = new JPanel(true);
			nextButtons[i] = new JButton("Next");
			nextButtons[i].addActionListener(this);
			if (i == 2)
				nextButtons[i].setText("Close");
		}

		slide1 = new JScrollPane(slides[1]);

		fcp = new FileChooserPanel(this, this.getWidth() - 50, 30);
	}
	
	/**
	 * Handles all ActionEvents that occur within this thread.
	 * @param e - an <code>ActionEvent</code> object containing information about where and how this method was invoked
	 */
	
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();

		if ("Next".equals(button.getText())) {
			String msg = checkForErrors();
			if (msg == null) { // No errors found
				currentSlide++;
				System.out.println("slide = " + currentSlide);
				System.out.println("message = " + msg);
				updateSlides();
			} else { // msg != null and there was an error of some sort
				JOptionPane.showMessageDialog(this, msg, "Error on Page "
						+ currentSlide, JOptionPane.ERROR_MESSAGE);
			}
		} else if ("Back".equals(button.getText())) {
			currentSlide--;
			updateSlides();
		} else if ("Close".equals(button.getText())) {
			exit();
		}
	}

	/**
	 * Checks for errors on each of the slides, based on the current slide.
	 * @return a <code>String</code> containing information about any errors found,
	 * or <code>null</code> if no errors were found on the current slide
	 */
	
	private String checkForErrors() {
		String message = null;
		switch (currentSlide) {
		case 0:
			if (fcp.getFile() == null || fcp.getText().isEmpty())
				message = "\t\tYou have not chosen a file yet.\t\t";
			break;
		case 1:
			//Check to make sure all boxes that have a check mark are filled, and all boxes
			//that have a class name and/or room are filled

			boolean correct = true;

			String defC = TeacherSchedulePanel.defaultClass;
			String defR = TeacherSchedulePanel.defaultRoom;

			Teacher[] te = new Teacher[teacherPanels.length];
			for (int i = 0; i < teacherPanels.length; i++) {
				teacherPanels[i].finish();
				te[i] = teacherPanels[i].getTeacher();
			}

			for(int i = 0; i < teacherPanels.length && correct; i++){
				Teacher t = te[i];
				System.out.println(t.fullInfo());
				int[] grid = teacherPanels[i].getPeriods();
				String[][] classInfo = {t.getClasses(), t.getRooms()};
				if(grid[i] == 1){
					//Check the rooms and class names for this teacher
					for(int r = 0; r < 2; r++){
						for(int c = 0; c < 7; c++){
							boolean classCorrect = !defC.equals(classInfo[r][c]);
							boolean room = !defR.equals(classInfo[r][c]);
							if(!classCorrect){
								message = "You need to check the " + getSuffix(c) + " period box for the " + getSuffix(i+1) + " teacher. \n His/her lastname is " + t.getLastName() + ".";
								correct = false;
							}else if(!room){
								message = "You need to check the " + getSuffix(c) + " period box for the " + getSuffix(i+1) + " teacher. \n His/her lastname is " + t.getLastName() + ".";
								correct = false;
							}
						}
					}
				}else if (grid[i] == 0){ //the period box is not filled, so check if the class/room boxes are
					for(int r = 0; r < 2; r++){
						for(int c = 0; c < 7; c++){
							boolean classCorrect = !defC.equals(classInfo[r][c]);
							boolean room = !defR.equals(classInfo[r][c]);
							if(room && ! classCorrect){
								message = "You need to fill in a class for the " + getSuffix(r+1) + " teacher. \n His/her lastname is " + t.getLastName();
								correct = false;
							}else if(!room && classCorrect){
								message = "You need to fill in a class for the " + getSuffix(r+1) + " teacher. \n His/her lastname is " + t.getLastName();
								correct = false;
							}
						}
					}
				}
			}

			break;
		case 2:
			message = null;
			break;
		}

		return message;
	}
	
	/**
	 * A convenience method that returns the given number, plus the appropriate suffix.
	 * Example: calling <code>getSuffix(5)</code> returns the <code>String "5th"</code>.
	 * @param num - the number for which to obtain the appropriate suffix.
	 * @return
	 */

	private String getSuffix(int num){
		String temp = (num + "");
		StringBuffer s = new StringBuffer(temp);
		int lastDigit = Integer.parseInt(temp.substring(temp.length()-1));
		switch(lastDigit){
		case 1:
			s.append("st");
			break;
		case 2:
			s.append("nd");
			break;
		case 3:
			s.append("rd");
			break;
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 0:
			s.append("th");
			break;
		}

		return s.toString();
	}

	/**
	 * Updates the slides based on information obtained from the previous slide
	 */

	private void updateSlides() {
		System.gc();
		JTabbedPane pane = (JTabbedPane) this.getContentPane();
		for (int i = 0; i < 3; i++) {
			if(i <= currentSlide)
				pane.setEnabledAt(i, true);
			else
				pane.setEnabledAt(i, false);
		}

		pane.setSelectedIndex(currentSlide);
		CustomMenuBar c = (CustomMenuBar) this.getJMenuBar();
		c.updateBars();
		
		slides[currentSlide] = new JPanel(true);

		if(currentSlide == 0){
			String filePath = getMostRecentFile();

			if(filePath == null){

				int choice = JOptionPane.showConfirmDialog(getParent(), "\tOpen File Manager to create your teacher files?\t", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if(choice == JOptionPane.YES_OPTION){
					FileManagerGUI gui = new FileManagerGUI();
					gui.setVisible(true);
					gui.setState(JFrame.NORMAL);
					this.setState(JFrame.ICONIFIED);
					//Utilities.launchExternal(new File("FileManager.exe").getAbsolutePath());
				}

			}else{
				fcp.setFile(new File(filePath));
			}

		}else if (currentSlide == 1) {
			// Zero-based, so the current slide is really
			// the 2nd one
			// Initialize the teacher panels
			
			//TODO Switch to the TeacherReadWriter
			
			slide1 = new JScrollPane(slides[1]);
			
			File teacherFile = fcp.getFile();
			String[] names = new DatabaseFileReader(
					teacherFile.getAbsolutePath()).readContents();
			String[] fName = new String[names.length];
			String[] lName = new String[names.length];

			for (int i = 0; i < names.length; i++) {
				// Assumes the format: FIRST LAST
				java.util.StringTokenizer st = new java.util.StringTokenizer(
						names[i], " ");
				fName[i] = st.nextToken();
				lName[i] = st.nextToken();
			}

			teacherPanels = new TeacherSchedulePanel[names.length];

			slides[1].setLayout(new GridLayout(names.length + 1, 1));
			for (int i = 0; i < names.length; i++) {
				teacherPanels[i] = new TeacherSchedulePanel(fName[i], lName[i]);
				slides[1].add(teacherPanels[i]);
			}

			// Add the next button in the bottom right
			JPanel nextPanel = new JPanel();
			nextPanel.setLayout(new GridLayout(1, 0));
			Utilities.addSpacers(nextPanel, 7);
			nextPanel.add(nextButtons[1]);
			slides[1].add(nextPanel);

			// JScrollPane tempSlide = new JScrollPane(slides[1]);
			// JTabbedPane contentPane = (JTabbedPane) this.getContentPane();
			// contentPane.setComponentAt(1, tempSlide);
		} else if (currentSlide == 2) {
			//TODO Add the price of the subs at the bottom right
			//129 * numSubs

			Teacher[] t = new Teacher[teacherPanels.length];
			for (int i = 0; i < teacherPanels.length; i++) {
				teacherPanels[i].finish();
				t[i] = teacherPanels[i].getTeacher();
			}

			// for(Teacher te: t){
			// System.out.println(te.getFullName());
			// Utilities.printGrid(te.getPeriods());
			// }

			Organizer o = new Organizer(t);
			o.sort();
			exporter = new MSExporter(o);
			MSPanel msp = new MSPanel(o);
			msp.createGrid();

			slides[2].setLayout(new GridLayout(0, 1));
			slides[2].add(msp);

			Utilities.addSpacers(slides[1], 3);

			try {
				javax.swing.UIManager.setLookAndFeel(lookAndFeel);
			} catch (Throwable e) {
				e.printStackTrace();
			}

			// Add the next button
			JPanel nextPanel = new JPanel();
			nextPanel.setLayout(new GridLayout(5, 8));
			Utilities.addSpacers(nextPanel, 4); // Add 4 blank rows
			Utilities.addSpacers(nextPanel, 7);
			nextButtons[2].removeActionListener(this);
			nextButtons[2].addActionListener(this);
			nextPanel.add(nextButtons[2]);
			slides[2].add(nextPanel);

			JScrollPane thisPane = new JScrollPane(slides[2]);
			JTabbedPane contentPane = (JTabbedPane) this.getContentPane();

			JPanel jp = (JPanel) contentPane.getComponent(2);
			jp.setLayout(new GridLayout(2, 1));
			jp.add(thisPane);

			JPanel bottomPane = new JPanel(new GridLayout(1, 2));
			String[] subs = new String[o.getSubsUsed()];
			for(int i = 0; i < subs.length; i++)
				subs[i] = "" + (i+1);

			javax.swing.JComboBox subList = new javax.swing.JComboBox(subs);
			subList.setEditable(false);
			subList.addActionListener(this);
			subList.setSelectedIndex(0);


			JPanel splitBottomRight = new JPanel(new GridLayout(5,1));
			JTextArea subsPrice = new JTextArea(0, 10);
			subsPrice.setText("Total Price: " + (o.getSubsUsed() * 129));

			splitBottomRight.add(subsPrice);
			Utilities.addSpacers(splitBottomRight, 3);
			splitBottomRight.add(nextButtons[2]);

			bottomPane.add(subList);
			bottomPane.add(splitBottomRight);
		}

		slides[currentSlide].setVisible(true);
	}
	
	/**
	 * Returns the path to the most recently modified teacher file based on information
	 * stored within the program's information file.
	 * @return a <code>String</code> path to the most recently modified file
	 */

	private String getMostRecentFile() {
		String[] teacherFiles = Utilities.getTokenValues("teacherFile");
		Utilities.printGrid(teacherFiles);

		if(teacherFiles.length == 0)
			return null;

		String mostRecent = teacherFiles[0];
		File f = null;
		for(int i = 1; i < teacherFiles.length; i++){
			f = new File(teacherFiles[i]);
			if(f.exists() && f.lastModified() > new File(mostRecent).lastModified()){
				mostRecent = teacherFiles[i];

			}
		}

		return mostRecent;
	}
	
	/**
	 * Shows a confirmation dialog to the user and closes the program
	 * if the "yes" option is selected, at which point the JVM also shuts down.
	 */

	public void exit() {
		int choice = JOptionPane.showConfirmDialog(this,
				"<html><b>\t\tAre you sure you want to exit?\t\t\n", "Exit?",
				JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			System.exit(1);
		}
	}
	
	/**
	 * Returns a two-dimensional <code>String</code> array containing the appropriate
	 * substitute's schedule, in a manner that can be exported and displayed
	 * @param o
	 * @param subNum
	 * @return
	 */

	public String[][] getSubGrid(Organizer o, int subNum) {
		// 5 headers, 7 periods to print, plus two, one for headers and one for
		// lunch
		// lunch row index =
		String[][] grid = new String[8][5];
		// Add the headers
		grid[0] = Utilities.HEADERS;
		for (int row = 1; row <= 8; row++) {
			// Add the period numbers
			if (row < 5)
				grid[row][0] = "Pd. " + row;
			else if (row == 5)
				grid[row][0] = "LUNCH";
			else if (row > 5)
				grid[row][0] = "Pd. " + (row - 1);

			grid[row][1] = Utilities.BELL_SCHEDULE[row - 1];
		}

		// Add the specific info for that substitute
		//TODO implement this method and make it display and export from updateSlides()
		
		

		return grid;
	}

	public CountDownLatch getLatch(){
		return latch;
	}

	public boolean isExcelExported() {
		return exporter.isWritten();
	}

	public int getCurrentSlide() {
		return currentSlide;
	}

	public FileChooserPanel getFCP() {
		return fcp;
	}

	public MSExporter getExcelExporter() {
		return exporter;
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static void createAndShowGUI() {
		// Set the look and feel to LiquidL&F
		try {
			javax.swing.UIManager.setLookAndFeel(lookAndFeel);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		//TeacherReadWriter rw = new TeacherReadWriter("");

		// Set the decorated property of the LiquidLookAndFeel class to true,
		// with a mac panther theme
		LiquidLookAndFeel.setLiquidDecorations(true, "panther");

		// Create the window
		SubScapeGUI window = new SubScapeGUI();
		window.setVisible(true);
	}
}

class CustomMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	private SubScapeGUI parent;
	private JMenuItem[] items;

	public CustomMenuBar(SubScapeGUI p) {
		this.setBorderPainted(true);
		parent = p;
		// Setup items array
		items = new JMenuItem[7];

		// Create the menus
		JMenu fileTab = new JMenu(" File ");
		fileTab.setSize(100, 60);
		JMenu exportTab = new JMenu(" Export ");
		exportTab.setSize(100, 60);
		JMenu aboutTab = new JMenu(" About ");

		// Create the menu items for the fileTab
		items[0] = new JMenuItem("Open teacher file...");
		items[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				Event.CTRL_MASK));
		items[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				parent.getFCP().showDialog();
			}
		});

		items[1] = new JMenuItem("Exit");
		items[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.ALT_MASK));
		items[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.exit(); // Confirm the exit choice
			}
		});

		items[6] = new JMenuItem("New");
		items[6].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				Event.CTRL_MASK));
		items[6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SubScapeGUI newGUI = new SubScapeGUI();
				newGUI.setVisible(true);
			}
		});

		fileTab.add(items[0]);
		fileTab.add(items[6]);
		fileTab.add(items[1]);

		// Create the menu items for the exportTab
		items[2] = new JMenuItem("Export to Excel...");
		// items[2].addActionListener()

		exportTab.add(items[2]);

		// Create the menu items for the aboutTab
		items[3] = new JMenuItem("About Us");
		items[3].addActionListener(new ActionListener() {
			String message = "<html><b>Here at <i>ACME Inc.</i>, we pride ourselves in making our\n"
					+ "<b>products as user friendly as possible and that our customers\n"
					+ "<b>are completely satisfied with their products and the quality\n"
					+ "<b>of service received.";

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(parent, message, "About Us",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		items[4] = new JMenuItem("Contact Us       ");
		items[4].addActionListener(new ActionListener() {
			String message = "<html><b>Contact us at <a href=\"\">info.acme.inc@gmail.com</a>";

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(parent, message, "Contact Us",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		items[5] = new JMenuItem("Version");
		items[5].addActionListener(new ActionListener() {
			String message = "<html><b>\t\tThe current version of SubScape is "
					+ SubScapeGUI.VERSION
					+ "\t\t\nreleased with the MAC look and Feel.\t\t";

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(parent, message, "Version Info",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		aboutTab.add(items[3]);
		aboutTab.add(items[4]);
		aboutTab.add(items[5]);

		//
		//
		//

		this.add(fileTab);
		this.add(exportTab);
		this.add(aboutTab);
	}

	public void updateBars() {
		if (parent.getCurrentSlide() == 1) { // Current slide is zero-based
			items[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
					Event.CTRL_MASK | Event.SHIFT_MASK));
			items[2].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) { // Export to excel
					if (!parent.isExcelExported()) {
						try {
							parent.getExcelExporter().exportSchedule();
						} catch (Throwable e1) {
							JOptionPane.showMessageDialog(parent,
									"Export to Excel failed.", "Error: Page 2",
									JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();
						}
						// String filePath =
						// parent.getExcelExporter().getDirectoryPath();
						String message = "<html><b>Exported to Excel successfully!";
						JOptionPane.showMessageDialog(parent, message, "Info",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane
						.showMessageDialog(
								parent,
								"<html><b>This schedule has already been exported to Excel!",
								"Info", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
		}
	}
}

//class PrintListener implements ActionListener{
//	Printable parent;
//	public PrintListener(Printable parent){
//		this.parent = parent;
//	}
//	
//	@Override
//	public void actionPerformed(ActionEvent arg0) {
//		PrinterJob job = PrinterJob.getPrinterJob();
//		job.setPrintable(parent);
//		boolean ok = job.printDialog();
//		if (ok) {
//			try {
//				job.print();
//			} catch (PrinterException ex) {
//				/* The job did not successfully complete */
//			}
//		}
//	}
//	
//}
