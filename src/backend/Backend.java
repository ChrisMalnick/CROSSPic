package backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import model.Board;
import model.User;

public class Backend {
	public static final String DIR = "data"; 
	public static final String USERDIR = "users"; 
	public static final String PUZZLEDIR = "puzzles"; 
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
			puzzleDirExists(); 
			FileOutputStream fos = new FileOutputStream(new File(Backend.generatePuzzlePath(board.getName())));
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
			puzzleDirExists(); 
			FileInputStream fis = new FileInputStream(new File(Backend.generatePuzzlePath(puzzleName))); 
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
	
	/* should probably make a call to load user from the server, but for now 
	 * its a local thing */
	public static User loadUser(String userID){
		try{
			dirExists(); 
			userDirExists();
			FileInputStream fis = new FileInputStream(new File(Backend.generateUserPath(userID))); 
			ObjectInputStream ois = new ObjectInputStream(fis); 
			User user = (User) ois.readObject(); 
			ois.close(); 
			fis.close();
			return user; 
		}
		catch(Exception e){
			return null; 
		}
	}
	
	/* should probably make a call to save user to the server, but for now 
	 * its a local thing */
	public static boolean saveUser(User user){ 
		try{
			dirExists(); 
			userDirExists(); 
			FileOutputStream fos = new FileOutputStream(new File(Backend.generateUserPath(user.getUserID()))); 
			ObjectOutputStream oos = new ObjectOutputStream(fos); 
			oos.writeObject(user); 
			oos.close(); 
			fos.close(); 
			return true; 
		}
		catch(Exception e){
			return false; 
		}
	}
	
	public static ArrayList<String> getUsers(){
		File dir = new File(Backend.DIR+File.separator+Backend.USERDIR); 
		if(!dir.exists())
			return null; 
		
		ArrayList<String> users = new ArrayList<String>(); 
		for(String s: dir.list()){
			String user = s.substring(0, s.length()-5); 
			if(Backend.loadUser(user) == null){
				continue; 
			}
			users.add(user); 
		}
		return users; 
	}
	
	public static ArrayList<String> getPuzzles(){
		File dir = new File(Backend.DIR+File.separator+Backend.PUZZLEDIR); 
		if(!dir.exists())
			return null; 
		
		ArrayList<String> puzzles = new ArrayList<String>(); 
		for(String s : dir.list()){
			String puzzle = s.substring(0, s.length()-7); 
			if(Backend.loadPuzzle(puzzle) == null){
				continue;
			}
			puzzles.add(puzzle); 
		}
		return puzzles; 
	}
	
	private static  String generatePuzzlePath(String puzzleName){
		return String.format("%s%s%s%s%s%s", Backend.DIR, File.separator, Backend.PUZZLEDIR, File.separator, puzzleName, Backend.PUZZLE);
	}
	
	private static String generateUserPath(String userID){
		return String.format("%s%s%s%s%s%s", Backend.DIR, File.separator, Backend.USERDIR, File.separator, userID, Backend.USER);
	}
	
	private static void dirExists(){
		File dir = new File(Backend.DIR+File.separator); 
		if(!dir.exists())
			dir.mkdir();
	}
	private static void userDirExists(){
		File dir = new File(Backend.DIR+File.separator+Backend.USERDIR);
		if(!dir.exists())
			dir.mkdir(); 
	}
	
	private static void puzzleDirExists(){
		File dir = new File(Backend.DIR+File.separator+Backend.PUZZLEDIR); 
		if(!dir.exists())
			dir.mkdir(); 
	}
	
}
