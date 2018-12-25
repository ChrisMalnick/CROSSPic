package entity;

import java.awt.Graphics2D;

import java.util.TimerTask;

import main.Panel;

public class HUD extends TimerTask {
	
	private String id;
	
	private int minutes;
	private int seconds;
	private boolean timing;
	private boolean timeUp;
	
	private int strikes;
	private int checks;
	
	public HUD(String s, int i) {
		
		id = s;
		
		if(i == 10)
			minutes = 10;
		
		else if(i == 16)
			minutes = 20;
		
		else if(i == 20)
			minutes = 30;
		
		timing = true;
		
	}
	
	public int getMinutes() {
		
		return minutes;
		
	}
	
	public int getSeconds() {
		
		return seconds;
		
	}
	
	public boolean getTimeUp() {
		
		return timeUp;
		
	}
	
	public int getStrikes() {
		
		return strikes;
		
	}
	
	public int getChecks() {
		
		return checks;
		
	}
	
	public String minutesToString() {
		
		if(minutes < 10)
			return "0" + minutes;
		
		else
			return Integer.toString(minutes);
		
	}
	
	public String secondsToString() {
		
		if(seconds < 10)
			return "0" + seconds;
		
		else
			return Integer.toString(seconds);
		
	}
	
	public boolean timeLeft(int i) {
		
		return ((i == 10 && minutes >= 5) || (i == 16 && minutes >= 10) || (i == 20 && minutes >= 15));
		
	}
	
	public void setTiming(boolean b) {
		
		timing = b;
		
	}
	
	public void addStrike() {
		
		strikes ++;
		
	}
	
	public void addCheck() {
		
		checks ++;
		
	}
	
	public void draw(Graphics2D G2D) {
		
		//G2D.drawLine(560, 0, 560, 480);
		
		G2D.drawRect(480, 0, 160, 80);
		G2D.drawString("Puzzle", 490, 30);
		G2D.drawString(id, 538, 69);
		
		G2D.drawRect(480, 80, 160, 80);
		G2D.drawString("Time", 514, 110);
		
		G2D.drawString(minutesToString(), 506, 149);
		
		G2D.fillOval(556, 130, 8, 8);
		G2D.fillOval(556, 140, 8, 8);
		
		G2D.drawString(secondsToString(), 568, 149);
		
		G2D.drawRect(480, 160, 160, 80);
		G2D.drawString("Strikes", 484, 190);
		
		G2D.drawRect(510, 210, 20, 20);
		G2D.drawRect(550, 210, 20, 20);
		G2D.drawRect(590, 210, 20, 20);
		
		if(strikes >= 1) {
			
			G2D.drawLine(512, 212, 528, 228);
			G2D.drawLine(512, 228, 528, 212);
			
		}
		
		if(strikes >= 2) {
			
			G2D.drawLine(552, 212, 568, 228);
			G2D.drawLine(552, 228, 568, 212);
			
		}
		
		if(strikes >= 3) {
			
			G2D.drawLine(592, 212, 608, 228);
			G2D.drawLine(592, 228, 608, 212);
			
		}
		
	}
	
	public void run() {
		
		if(timing) {
			
			if(minutes == 0 && seconds == 0) {
				
				timing = false;
				timeUp = true;
				Panel.state = 7;
				return;
				
			}
			
			seconds --;
			
			if(seconds < 0) {
				
				seconds = 59;
				minutes --;
				
			}
			
		}
		
	}
	
}
