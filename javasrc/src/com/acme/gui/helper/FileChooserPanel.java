package com.acme.gui.helper;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * A class that allows the program to receive data on what files the user has selected.
 * @author Carlos Gonzales
 * @see JFileChooser
 */

public class FileChooserPanel extends JPanel implements ActionListener{
	public static final Dimension DEFAULT = new Dimension(600, 23);
	private static final long serialVersionUID = 1L;
	private static final FileNameExtensionFilter FILTER = new FileNameExtensionFilter("SCP Files Only", "scp");

	private JFrame parent;
	private JButton browseButton;
	private JTextField tf;
	private String path;
	private File file;
	private JFileChooser chooser;

	/**
	 * Constructs a new FileChooserPanel with the given parent JFrame, width, and height.
	 * @param parent - the parent JFrame
	 * @param width - the width to give this object
	 * @param height - the height to give this object
	 */

	public FileChooserPanel(JFrame parent, int width, int height){
		super(true);
		this.setPreferredSize(new Dimension(width, height));
		if(parent == null){
			JFrame frame = new JFrame("Default");
			frame.add(this);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			this.parent = frame;
		}else{
			this.parent = parent;
		}

		//Instantiate components
		path = "";
		file = null;

		chooser = new JFileChooser();
		browseButton = new JButton("<html>Browse<html>");
		tf = new JTextField("Choose a file.");

		//Configure the components
		tf.setPreferredSize(new Dimension((width * 3)/5, height));
		browseButton.addActionListener(this);
		browseButton.setPreferredSize(new Dimension(width / 5, height));
		chooser.setFileFilter(FILTER);
		chooser.setCurrentDirectory(new File("."));
		chooser.setPreferredSize(new Dimension(600, 360));

		//Set options for this panel
		this.setPreferredSize(new Dimension(width, 50));
		//this.setSize(width, height);
		this.add(tf, 0);
		this.add(browseButton, 1);
		this.setVisible(true);

	}

	/**
	 * Constructs a new FileChooserPanel with the given parent JFrame using the default dimensions.
	 * @param parent - the parent JFrame
	 */

	public FileChooserPanel(JFrame parent){
		this(parent, DEFAULT.width, DEFAULT.height);
	}

	/**
	 * Sets the path that will be displayed in this panel.
	 * @param path - the new path to be displayed
	 */

	public void setDisplayText(String path){
		tf.setText(path);
	}

	/**
	 * Sets this and all of its subcomponents visible or not visible.
	 * @param b - the boolean specifying whether this component will be visible or not
	 */

	public void setVisible(boolean b){
		super.setVisible(b);
		tf.setVisible(b);
		browseButton.setVisible(b);
	}

	/**
	 * Called when the browse button is pressed.
	 * @param e - the ActionEvent object containing information about what invoked this method
	 */

	public void actionPerformed(ActionEvent e) {
		//Called when the "browse" button is pressed
		int returnVal = chooser.showOpenDialog(parent);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			path = chooser.getSelectedFile().getPath();
			file = new File(path);
			tf.setText(path);
			System.out.println("You chose to open this file: " +
					chooser.getSelectedFile().getName());
		}
	}

	/**
	 * Sets the currently held <code>File</code> and updates the path shown.
	 * @param f - the new <code>File</code> to use
	 */

	public void setFile(File f){
		file = f;
		path = f.getAbsolutePath();
		tf.setText(path);
	}

	/**
	 * Gets the file path for the current <code>File</code>.
	 * @return
	 */

	public String getFilePath(){
		return path;
	}

	/**
	 * Gets the currently held <code>File</code>.
	 * @return the currently held <code>File</code>
	 */

	public File getFile(){
		return file;
	}

	/**
	 * Gets the text that is being displayed
	 * @return the text currently displayed
	 */

	public String getText(){
		return tf.getText();
	}

	/**
	 * Shows the "Open" dialog.
	 */

	public void showDialog(){
		actionPerformed(null);
	}

	/**
	 * Sets the <code>FileFilter</code> for this FileChooserPanel
	 * @param filter - the new <code>FileFilter</code> to use
	 */

	public void setFileFilter(FileFilter filter){
		this.chooser.setFileFilter((FileFilter) filter);
	}

	//	public static void main(String[] args) throws Throwable{
	//		UIManager.setLookAndFeel(new LiquidLookAndFeel());
	//		LiquidLookAndFeel.setLiquidDecorations(true, "panther");
	//		JFrame jf = new JFrame();
	//		jf.getContentPane().setBackground(Color.cyan);
	//		jf.add(new FileChooserPanel(jf));
	//		//jf.getContentPane().add(new FileChooserPanel(jf, FileChooserPanel.DEFAULT.width, FileChooserPanel.DEFAULT.height));
	//
	//		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//		jf.setSize(600, 300);
	//		jf.setVisible(true);
	//		//jf.pack();
	//		//
	//		//		JFrame frame = new JFrame();
	//		//		frame.setBackground(Color.WHITE);
	//		//		frame.setBounds(200, 100, 400, 400);
	//		//		Container pane = frame.getContentPane();
	//		//		pane.add(new FileChooserPanel(frame, frame.getWidth(), 20));
	//		//		frame.setVisible(true);
	//		//
	//		//		JPanel jp = new JPanel();
	//		//		jp.setBounds(0, 0, 100, 50);
	//		//		jp.setVisible(true);
	//		//		frame.pack();
	//		//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//		//		frame.setSize(frame.getWidth(), 300);
	//	}
}
