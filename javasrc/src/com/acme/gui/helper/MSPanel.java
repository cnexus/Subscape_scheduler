package com.acme.gui.helper;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.acme.core.Organizer;
import com.acme.core.Teacher;
import com.acme.core.Utilities;

/**
 * A GUI representation of the integer grid returned by <code>{@link Organizer}.getGrid()</code>.
 */

public class MSPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	private static Color[] COLORS = {
		Color.BLUE,
		Color.CYAN,
		Color.GRAY,
		Color.GREEN,
		Color.ORANGE,
		Color.PINK,
		Color.RED,
		Color.YELLOW,
		new Color(255, 0, 255),
		new Color(255, 255, 153),
		new Color(51, 153, 204),
		new Color(51, 102, 204),
		new Color(102, 51, 0)
	};

	private Organizer organizer;

	/**
	 * Constructs an MSPanel using the given <code>Organizer</code>.
	 * @param o - the <code>Organizer</code> to use when creating this object
	 */

	public MSPanel(Organizer o){
		super(true);
		try {
			javax.swing.UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}

		organizer = o;
		this.setBackground(Color.WHITE);
	}
	
	/**
	 * Displays the grid inside this JPanel in accordance with the grid returned by <code>Organizer.getGrid()</code>.
	 */

	public void createGrid(){
		Teacher[] teachers = organizer.getTeachers();
		String[] teacherNames = new String[teachers.length];
		for(int i = 0; i < teachers.length; i++){
			teacherNames[i] = teachers[i].getLastName();
		}

		Color[] c = getColors(organizer.getSubsUsed());

		//Has teacherNames rows + 1 for headers. A column for each period (7),
		//plus one column for teacher names
		JLabel[][] grid = new JLabel[teacherNames.length+1][8]; 

		java.awt.Font wordFont = new java.awt.Font(Font.DIALOG, java.awt.Font.BOLD, 13);
		java.awt.Font headerFont = new java.awt.Font(Font.DIALOG, java.awt.Font.BOLD, 14);
		java.awt.Font cellFont = new java.awt.Font(Font.DIALOG, java.awt.Font.BOLD, 13);

		//First put the teacher names
		String hTag = "<html><u><b>";
		for(int i = 1; i < grid.length; i++){
			grid[i][0] = new JLabel(teacherNames[i-1]);
			grid[i][0].setForeground(Color.BLACK);
			grid[i][0].setFont(wordFont);
			grid[i][0].setAlignmentY(LEFT_ALIGNMENT);
			grid[i][0].setBackground(Color.WHITE);
		}

		//Put the header names
		for(int i = 0; i < grid[0].length; i++){
			if(i == 0)
				grid[0][i] = new JLabel(hTag + "Teachers");
			else
				grid[0][i] = new JLabel(hTag + "<html><u><b>Period " + i);

			grid[0][i].setFont(headerFont);
			grid[0][i].setAlignmentY(LEFT_ALIGNMENT);
			grid[0][i].setBackground(Color.WHITE);
		}

		int[][] intGrid = organizer.getGrid();
		//Put the colored labels
		for(int i = 1; i < grid.length; i++){
			for(int j = 1; j < grid[0].length; j++){
				grid[i][j] = new JLabel("---");
				//System.out.println("intGrid[" + (i-1) + "][" + (j-1) + "] = " + intGrid[i-1][j-1]);
				int colorIndex = intGrid[i-1][j-1] - 1;
				if(colorIndex == (Organizer.NOT_VALID - 1))
					grid[i][j].setBackground(Color.WHITE);
				else{
					grid[i][j].setBackground(c[colorIndex]);
					grid[i][j].setText((colorIndex+1) + "");
					//grid[i][j].setText(c[colorIndex].getRed() + "," + c[colorIndex].getBlue() + "," +c[colorIndex].getGreen());
				}

				Color foreground = Utilities.getContrastColor(grid[i][j].getBackground());
				grid[i][j].setForeground(foreground);
				grid[i][j].setFont(cellFont);
			}
		}

		JPanel[][] coloredGrid = new JPanel[grid.length][grid[0].length];

		for(int i = 0; i < coloredGrid.length; i++){
			for(int j = 0; j < coloredGrid[0].length; j++){
				coloredGrid[i][j] = new JPanel();
				coloredGrid[i][j].add(grid[i][j]);
				coloredGrid[i][j].setBackground(grid[i][j].getBackground());
			}
		}

		this.setLayout(new java.awt.GridLayout(grid.length, grid[0].length, 1, 0));
		addAll(coloredGrid);
	}

	private void addAll(javax.swing.JComponent[][] j){
		for(int i = 0; i < j.length; i++){
			for(int k = 0; k < j[0].length; k++){
				this.add(j[i][k]);
			}
		}
	}
	
	/**
	 * Returns the specified number of <code>Color</code> objects in an array.
	 * @param max - the number of <code>Color</code>'s to be in the returned array
	 * @return
	 */

	private static Color[] getColors(int max){
		Color[] colors = new Color[max];
		for(int i = 0; i < max; i++){
			colors[i] = COLORS[i];
		}

		return colors;
	}
}
