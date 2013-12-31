package model;

import java.io.Serializable;

public class Time implements Serializable, Runnable{
	private static final long serialVersionUID = 4L;
	private int minutes; 
	private int seconds; 
	
	public Time(int minutes, int seconds){
		if(seconds > 59 || seconds < 0 || minutes < 0){
			throw new IllegalArgumentException("Invalid Time Segment"); 
		}
		this.minutes = minutes; 
		this.seconds=  seconds; 
	}
	
	public void setMinutes(int minutes){
		this.minutes = minutes;
	}
	public int getMinutes(){
		return this.minutes;
	}
	
	public void setSeconds(int seconds){
		this.seconds = seconds; 
	}
	
	public int getSeconds(){
		return this.seconds; 
	}
	
	@Override
	public String toString(){
		return Integer.toString(minutes) + ":" + Integer.toString(seconds); 
	}
	
	/**
	 * To start the timer, run Time.start() not Time.run()
	 */
	@Override
	public void run() {
		while(minutes >= 0){
			if(seconds > 0){
				seconds--; 
			}
			else{
				minutes--; 
				seconds = 59; 
			}
			try{
				Thread.sleep(1000); 
			}
			catch(Exception e){
				//do nothing
			}
		}
	}	
}

