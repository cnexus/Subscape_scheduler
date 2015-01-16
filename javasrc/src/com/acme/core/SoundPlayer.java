package com.acme.core;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 * An object capable of playing MPEG sound files.
 */

public class SoundPlayer {
	private AdvancedPlayer player;
	private java.io.File soundFile;

	/**
	 * Creates a new sound player using the given File
	 * @param file - the file specifying which sound to play
	 */
	public SoundPlayer(java.io.File file){
		soundFile = file;
		java.io.InputStream stream;
		try {
			stream = new java.io.FileInputStream(soundFile);
			player = new AdvancedPlayer(stream);
		}catch(Throwable t){
			t.printStackTrace();
		}
	}

	/**
	 * Disposes of this player
	 */
	
	public void close(){
		player.close();
	}
	
	/**
	 * Plays the sound file
	 */

	public void play(){
		try{
			player.play();
		}catch(JavaLayerException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Plays this <code>SoundPlayer</code>'s file for the given number of frames
	 * @param frames - the number of frames to play
	 * @return <code>true</code> if the file played successfully, <code>false</code> otherwise
	 */

	public boolean play(int frames) {
		boolean b = false;
		try {
			b = player.play(frames);
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}

		return b;
	}
	
	/**
	 * Plays the file from <code>start</code> to </code>end</code>
	 * @param start - the frame to start from
	 * @param end - the frame to stop at
	 * @return <code>true</code> if the file played successfully, <code>false</code> otherwise
	 */

	public boolean play(int start, int end) {
		boolean b = false;
		try {
			b = player.play(start, end);
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}

		return b;
	}

	/**
	 * Stops the currently playing sound
	 */
	
	public void stop() {
		player.stop();
	}
	
	/**
	 * Returns the <code>File</code> being used for playback
	 * @return the <code>File</code> being used
	 */

	public java.io.File getPlaybackFile(){
		return soundFile;
	}


	public static void main(String[] args) throws Throwable{
		SoundPlayer player = new SoundPlayer(new java.io.File("MacStartUp.mp3"));
		player.play();
	}
}
