package com.acme.core;

/**
 * Unused at the moment.
 */

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import com.acme.gui.helper.TeacherEditFrame;
import com.acme.gui.main.FileManagerGUI;

public class EditFrameThread implements Callable<Boolean> {
	private volatile CountDownLatch latch;
	private FileManagerGUI parent;
	private TeacherEditFrame frame;
	private Teacher teacher;

	public EditFrameThread(FileManagerGUI p, Teacher t){
		this.latch = latch;
		parent = p;
		teacher = t;
	}

	public void run(){
		//		try {
		//			latch.await();
		//		} catch (InterruptedException e) {
		//			e.printStackTrace();
		//		}
		/**
		 * Waits until the thread is finished executing
		 * and then counts down
		 */
		
		try{
			frame = new TeacherEditFrame(parent, teacher);
			frame.setVisible(true);
			System.out.println("inside run method");
		}finally{
			latch.countDown();
			System.out.println("Thread released");
		}
	}
	
	public void setVisible(boolean b){
		frame.setVisible(b);
	}
	
	public void stopThread(){
		latch = null;
	}

	public Teacher getTeacher(){
		return frame.getTeacher();
	}

	public Boolean call() throws Exception {
		frame = new TeacherEditFrame(parent, teacher);
		frame.setVisible(true);
		System.out.println("inside call method");
		
		if(frame.isClosed()){
			return true;
		}else{
			return false;
		}
	}
}
