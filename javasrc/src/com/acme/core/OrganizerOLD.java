package com.acme.core;

/**
 * Deprecated, use {@link Organizer} instead.
 */

public class OrganizerOLD {

	private int[][] grid;
	private Boolean[][] needSub;
	private Teacher[] teachers;

	private int currentSubNum;
	private int numOfSpots;
	
	public static int NOT_VALID = 0;

	public OrganizerOLD(Teacher[] inTeachers){

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

	}
	
	public void sort(){
		dropFill();
	}

	private void dropFill(){
		int num = numOfSpots;

		int numOfTeachers = teachers.length;


		int subPeriods = 0;
		int currentSub = 1;

		while(num>0){

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
						num--;
					}
				}


			}
			currentSub++;
			subPeriods = 0;
			
		}
		
		currentSubNum = currentSub;
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