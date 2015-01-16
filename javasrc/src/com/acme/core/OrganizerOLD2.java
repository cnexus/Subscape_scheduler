package com.acme.core;

/**
 * Deprecated, use {@link Organizer} instead.
 */

public class OrganizerOLD2 {

	private int[][] grid;
	private Boolean[][] needSub;
	private Teacher[] teachers;

	private int currentSubNum;
	private int numOfSpots;
	private int numOfSpotsLeft;
	
	public static int NOT_VALID = 0;

	public OrganizerOLD2(Teacher[] inTeachers){

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
		
		numOfSpotsLeft = numOfSpots;

	}
	
	public void sort(){
		stack();
		dropFill();
	}

	private void stack(){
		
		int minNumber = minNum();
		int subNum = 1;
		int subPeriods = 0;
		int col = 0;
		
		while(subNum <= minNumber&&col<grid.length){
				
			
			for(int i = 0; i< 7; i++){
				
				if(needSub[col][i]&&subPeriods<=6){
					grid[col][i] = subNum;
					subPeriods++;
					numOfSpotsLeft--;
					
				}
				
			}
			
			subNum++;
			subPeriods = 0;
			col++;
			
			updateMaxSub();
			
		}
		
		
	}
	
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
	
	private void dropFill(){
		
		int numOfTeachers = teachers.length;


		int subPeriods = 0;
		int currentSub = 1;

		while(numOfSpotsLeft>0){
			System.out.println("asdf");
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
			currentSub++;
			subPeriods = 0;
			
		}
		updateMaxSub();
	}
	
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

	public int getSubsUsed(){
		return currentSubNum;
	}


	public int[][] getGrid(){
		return grid;
	}


	//Copy arr1 in to arr2
	private void copyArray(Object[] arr1, Object[] arr2){

		System.arraycopy(arr1, 0, arr2, 0, arr1.length);

	}
}