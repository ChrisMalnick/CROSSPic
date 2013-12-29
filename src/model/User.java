package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import backend.Backend;

public class User implements Serializable{
	private static final long serialVersionUID = 3L;
	private String userName; 
	private String userID; 
	private String userPassword; 
	private ArrayList<String> puzzleNames; 
	// figure out how you're going to save the best times and map it to each puzzle

	
	public User(String userName, String userID, String userPassword){
		this.userName = userName;
		this.userID = userID; 
		this.userPassword = userPassword; 
		this.puzzleNames = new ArrayList<String>(); 
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
		return this.puzzleNames; 
	}
	
	public boolean addPuzzle(Board puzzle){
		this.puzzleNames.add(puzzle.getName());
		return Backend.savePuzzle(puzzle) && Backend.saveUser(this); 
	}
	
	public void removePuzzle(String puzzleName){
		this.puzzleNames.remove(puzzleName); 
	}
	
	public Board getPuzzle(String puzzleName){
		if(this.hasPuzzle(puzzleName))
			return Backend.loadPuzzle(puzzleName); 
		else return null; 
	}
	
	public ArrayList<String> addNewPuzzles(){ 
		File dir = new File(Backend.DIR+File.separator+Backend.PUZZLEDIR); 
		if(!dir.exists())
			return null; 
		
		ArrayList<String> newPuzzles = new ArrayList<String>(); 
		for(String file : dir.list()){
			String filename = file.substring(0, file.length()-7);
			if(!this.hasPuzzle(filename)){
				newPuzzles.add(filename); 
			}
		}
		return newPuzzles; 
	}
	
	private boolean hasPuzzle(String puzzleName){
		if(this.puzzleNames.contains(puzzleName))
			return true; 
		return false; 
	}
	

}
