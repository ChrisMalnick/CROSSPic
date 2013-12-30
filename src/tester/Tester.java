package tester;

import java.util.ArrayList;

import model.Board;
import model.User;
import backend.Backend;

public class Tester {

	public static void main(String[] args){
		User user = new User("Mush", "msk154", "nope.jpg"); 
		if(!Backend.saveUser(user))
			System.out.println("failed to save user"); 
		
		Board puzzle1 = new Board("test1", 10); 
		for(int row = 0; row < 4; row++)
			puzzle1.fillSquare(0, row);
		if(!user.addPuzzle(puzzle1))
			System.out.println("failed to add and save puzzle 1"); 
		
		Board puzzle2 = new Board("test2", 10);
		for(int col = 0; col < 3; col++)
			puzzle2.fillSquare(col, 0); 
		if(!user.addPuzzle(puzzle2))
			System.out.println("failed to add and save puzzle 2"); 
		
		Board puzzle3 = new Board("test3", 10); 
		for(int row = 0; row < 10; row++)
			puzzle3.fillSquare(0, row); 
		if(!Backend.savePuzzle(puzzle3))
			System.out.println("failed to save puzzle 3"); 
		
		System.out.println("NEW PUZZLES\n------------------"); 
		ArrayList<String> newPuzzles = user.addNewPuzzles(); 
		for(String s : newPuzzles)
			System.out.println(s); 
		
		User user2 = Backend.loadUser("msk154"); 
		ArrayList<String> u2p = user2.getPuzzles(); 
		System.out.println("user2name:" + user2.getUserName()); 
		if(user2.getPuzzle(u2p.get(0)) == null)
			System.out.println("Failed to load puzzle"); 
		
		
		
	}
}
