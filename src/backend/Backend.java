package backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import model.Board;
import model.User;

public class Backend {
	public static final String DIR = "data"; 
	public static final String PUZZLE = ".puzzle"; 
	public static final String USER = ".user"; 
	
	/**
	 * Saved the given puzzle 
	 * 
	 * @param board The Puzzle you want to save
	 * @return Returns true if able to save the puzzle or returns false if unable to save the file
	 */
	public static boolean savePuzzle(Board board){
		try {
			dirExists(); 
			FileOutputStream fos = new FileOutputStream(new File(Backend.generateSavePath(board.getName())));
			ObjectOutputStream oos = new ObjectOutputStream(fos); 
			oos.writeObject(board); 
			oos.close(); 
			fos.close(); 
			return true; 
		}
		catch (Exception e){
			return false; 
		}
	}
	/**
	 * Loads a saved puzzle 
	 * @param puzzleName The name of the puzzle you want to load
	 * @return Returns the puzzle or returns null if unable to load the puzzle
	 */
	public static Board loadPuzzle(String puzzleName){
		try{
			dirExists(); 
			FileInputStream fis = new FileInputStream(new File(Backend.generateSavePath(puzzleName))); 
			ObjectInputStream ois = new ObjectInputStream(fis); 
			Board board = (Board)ois.readObject(); 
			ois.close(); 
			fis.close(); 
			return board; 
		}
		catch(Exception e){
			return null; 
		}
	}
	
	/* should probably make a call to load user from the server */
	public static User loadUser(String userID){
		
		return null; //added to make the compiler happy
	}
	
	/* should probably make a call to save user to the server */
	public static boolean saveUser(User user){ 
		
		return false; // added to make the compiler happy
	}
	
	private static  String generateSavePath(String puzzleName){
		return String.format("%s%s%s%s", Backend.DIR, File.separator, puzzleName, Backend.PUZZLE);
	}
	
	private static void dirExists(){
		File dir = new File(Backend.DIR+File.separator);
		if(!dir.exists())
			dir.mkdir(); 
	}
	
}
