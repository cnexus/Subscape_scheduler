package com.acme.core;

/**
 * Deprecated, use {@link Organizer} instead.
 */

public class OrganizerOLD3 {

	private int[][] grid;
	private Boolean[][] needSub;
	private Teacher[] teachers;

	private int currentSubNum;
	private int numOfSpots;
	private int numOfSpotsLeft;
	
	//private int[] rowByNumSpots;
	
	public static int NOT_VALID = 0;

	public OrganizerOLD3(Teacher[] inTeachers){

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
	
	public void sort(){
		stack();
		dropFill();
	}

	private boolean contains(int[] arr, int num){
		for(int i = 0; i<arr.length; i++){
			if(arr[i] == num){
				return true;
			}
		}
		return false;
	}
	
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