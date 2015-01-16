package com.acme.core;

/**
 * Class that holds the grid of substitutes per period, and the list of teachers needing subs.
 * <br>Also sorts the substitutes along the following criteria:
 * <br> - Substitutes work at most 6 periods
 * <br> - Substitutes can not teach multiple classes at the same time
 * <br> - Least number of subs should be used
 * <br> - Substitutes should continually teach for one teacher as much as possible 
 */

public class Organizer {
	
	private int[][] grid;
	private Integer[][] objectGrid;
	private Boolean[][] needSub;
	private Teacher[] teachers;

	private int currentSubNum; //Not zero-based
	private int numOfSpots;
	private int numOfSpotsLeft;
	
	//private int[] rowByNumSpots;
	
	public static int NOT_VALID = 0;
	
	/**
	 * Creates the Organizer, and pulls information including the periods needing subs
	 * Also creates the grid in which the subs will be organized.
	 * @param inTeachers - list of teachers needing subs
	 */

	public Organizer(Teacher[] inTeachers){

		teachers = new Teacher[inTeachers.length];

		copyArray(inTeachers,teachers);

		grid = new int[teachers.length][7];
		needSub = new Boolean[teachers.length][7];

		for(int i = 0;i<grid.length;i++){

			Boolean[] schedule = new Boolean[7];

			copyArray(teachers[i].getPeriods(),schedule);
			copyArray(schedule, needSub[i]);
			
			
			for(Boolean b:schedule){
				if(b){
					numOfSpots++;
				}
			}

		}
		
//		rowByNumSpots = new int[grid.length];
//		
//		for(int i = 0; i<rowByNumSpots.length; i++){
//				
//			int numOfSpotsRow = 0;
//			for(int j = 0; j<7; j++){
//				if(needSub[i][j]){
//					numOfSpotsRow++;
//				}
//			}
//			rowByNumSpots
//			
//		}
		
		numOfSpotsLeft = numOfSpots;

	}
	
	public Substitute getSub(int subNum){
		
		String teacherList[] = new String[7];
		String classes[] = new String[7];
		String rooms[] = new String[7];
		
		for(int i = 0; i<grid.length; i++){
			for(int j = 0; j<7; j++){
				if(grid[i][j] == subNum){
					teacherList[j]=teachers[i].getFullName();
					classes[j] = teachers[i].getClasses()[j];
					rooms[j] = teachers[i].getRooms()[j];
				}
			}
		}
		
		return new Substitute(teacherList, classes, rooms, subNum);
	}
	
	/**
	 * Gets all the Substitutes
	 * @return array of all {@link Substitute} in the grid
	 */
	
	public Substitute[] getSubs(){
		if(currentSubNum == 0)
			return null;
		
		Substitute[] subs = new Substitute[currentSubNum];
		for(int i = 0; i < currentSubNum; i++){
			subs[i] = getSub(i+1);
		}
		
		return subs;
	}
	
	/**
	 * Client method that calls the two sort algorithms one after the other
	 */
	public void sort(){
		stack();
		dropFill();
		createObjectGrid();
	}

	/**
	 * Creates an array of {@link Integer} based on the array of ints, grid
	 */
	private void createObjectGrid() {
		objectGrid = new Integer[grid.length][grid[0].length];
		for(int r = 0; r < grid.length; r++){
			for(int c = 0; c < grid[0].length; c++){
				objectGrid[r][c] = grid[r][c];
			}
		}
	}

	/**
	 * Searches an array to see if it contains num.
	 * @param arr - array to be searched
	 * @param num - number to be searched for
	 * @return true if array contains num, false if it does not.
	 */
	private boolean contains(int[] arr, int num){
		for(int i = 0; i<arr.length; i++){
			if(arr[i] == num){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Fills up the teacher with the most number of classes in a row with substitutes
	 */
	
	private void stack(){
		
		int minNumber = minNum();
		int subNum = 1;
		int subPeriods = 0;
		
		int[] filledRows = new int[grid.length];
		
		for(int i = 0; i<filledRows.length;i++){
			filledRows[i] = -999;
		}
		
		int currentIteration = 0;
		
		while(subNum <= minNumber){
			
			int maxR = -1;
			int MaxNum = 0;
			
			
			for(int i = 0;i<needSub.length;i++){
				
				int currentNum = 0;
				
				
				for(int j = 0;j<7;j++){
					if(needSub[i][j]){
						currentNum++;
					}
				}
				
				
				
				if(currentNum > MaxNum&&!contains(filledRows,i)){
					MaxNum = currentNum;
					maxR = i;
				}
				
			}
			
			filledRows[currentIteration] = maxR;
			
			
			for(int i = 0; i< 7; i++){
				
				if(needSub[maxR][i]&&subPeriods<=6){
					grid[maxR][i] = subNum;
					subPeriods++;
					numOfSpotsLeft--;
					
				}
				
			}
			
			subNum++;
			subPeriods = 0;
			currentIteration++;
			
			updateMaxSub();
			
		}
		
		
		
	}
	
	/**
	 * Finds the minimum number of subs necessary
	 * @return the minimum number of subs needed to fill the grid
	 */
	
	private int minNum(){
		
		int colNum = 0;
		int maxColNum = 0;
		
		for(int i = 0;i<7;i++){
			colNum = 0;
			for(int j = 0;j<needSub.length;j++){
				if(needSub[j][i]){
					colNum++;
				}
			}
			
			if (colNum>=maxColNum){
				maxColNum = colNum;
			}
			
		}
		
		return (int) Math.max(maxColNum, Math.ceil(numOfSpots/6));
		
	}
	
	/**
	 * Fills the remaining spots after <code>stack()</code> with the remaining available substitutes.
	 */
	
	private void dropFill(){
		
		int numOfTeachers = teachers.length;


		int subPeriods = 0;
		int currentSub = 0;

		while(numOfSpotsLeft>0){
			
			subPeriods = 0;
			currentSub++;
			
			for(int i = 0; i<grid.length;i++){
				for(int j = 0; j<7; j++){
					if(grid[i][j] == currentSub){
						subPeriods++;
					}
				}
			}
			
			for(int r = 0;r<numOfTeachers;r++){
				for(int c = 0;c<7;c++){
					boolean alreadyInCol = false;
					for(int i = 0;i<numOfTeachers;i++){
						if(grid[i][c]!=0){
							if(grid[i][c]==currentSub){
								alreadyInCol = true;
							}
						}
					}

					if(!alreadyInCol && needSub[r][c] && grid[r][c]==0 && subPeriods < 6){
						grid[r][c] = currentSub;					
						subPeriods++;
						numOfSpotsLeft--;
					}
				}


			}
			
			
		}
		updateMaxSub();
	}
	
	/**
	 * Updates the currentSubNum field between the sorting algorithms
	 */
	
	private void updateMaxSub(){
		int maxNumber = currentSubNum;
		for(int i = 0;i<grid.length;i++){
			for(int j = 0; j<7; j++){
				if(grid[i][j]>maxNumber){
					maxNumber = grid[i][j];
				}
			}
		}
		currentSubNum = maxNumber;
	}

	/**
	 * Get the number of subs used
	 * @return The number of subs used in the grid
	 */
	
	public int getSubsUsed(){
		return currentSubNum;
	}

	/**
	 * get the grid of ints representing the substitutes
	 * @return 2D array of ints of Substitute numbers
	 */
	public int[][] getGrid(){
		return grid;
	}
	
	/**
	 * get the grid of Integers representing the substitutes
	 * @return 2D array of Integer objects of Substitute numbers
	 */
	
	public Integer[][] getObjectGrid(){
		return objectGrid;
	}

	/**
	 * Get the teachers in the grid
	 * @return array of {@link Teacher}
	 */
	public Teacher[] getTeachers(){
		Teacher[] t = new Teacher[teachers.length];
		copyArray(teachers, t);
		return t;
	}
	
	/**
	 * Help class to copy arrays
	 * @param arr1 array to copy from
	 * @param arr2 array to copy to
	 */
	private void copyArray(Object[] arr1, Object[] arr2){

		System.arraycopy(arr1, 0, arr2, 0, arr1.length);
	}
}