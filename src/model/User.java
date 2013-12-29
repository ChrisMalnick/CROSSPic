package model;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{
	private static final long serialVersionUID = 3L;
	private String userName; 
	private String userID; 
	private String userPassword; 
	
	private ArrayList<String> puzzles; 
	// figure out how you're going to save the best times and map it to each puzzle

	
	public User(String userName, String userID, String userPassword){
		this.userName = userName;
		this.userID = userID; 
		this.userPassword = userPassword; 
	}
	
	public String getUserName(){
		return this.userName; 
	}
	
	public String getUserID(){
		return this.userID;
	}
	
	public String getUserPassword(){
		return this.userPassword; 
	}
	
	public void setUserName(String userName){
		this.userName = userName; 
	}
	
	public void setUserID(String userID){
		this.userID = userID; 
	}
	
	public void setUserPassword(String userPassword){
		this.userPassword = userPassword; 
	}
	
	public ArrayList<String> getPuzzles(){
		return this.puzzles; 
	}
	
	public void addPuzzle(String puzzleName){
		this.puzzles.add(puzzleName);
	}
	
	public void removePuzzle(String puzzleName){
		this.puzzles.remove(puzzleName); 
	}
	
	public boolean hasPuzzle(String puzzleName){
		if(this.puzzles.contains(puzzleName))
			return true; 
		return false; 
	}
}
