package entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Puzzle {
	
	private int size;
	
	private String id;
	private String name;
	
	private boolean[][] puzzle;
	
	public Puzzle(int i, String s) {
		
		size = i;
		puzzle = new boolean[i][i];
		load(s);
		
	}
	
	public String getID() {
		
		return id;
		
	}
	
	public String getName() {
		
		return name;
		
	}
	
	public boolean[][] getBoard() {
		
		return puzzle;
		
	}
	
	private void load(String s) {
		
		try {
			
			File load = new File(System.getProperty("user.dir") + "/puzzles/cp_" + s + ".dat");
			
			if(!load.exists())
				return;
			
			FileReader fileReader = new FileReader(load.getAbsoluteFile());
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			String line;
			int row = 0;
			
			while((line = bufferedReader.readLine()) != null) {
				
				if(row == 1)
					id = line;
				
				else if(row == 2)
					name = line;
				
				else if(row >= 9) {
					
					for(int i = 0; i < size; i ++) {
						
						if(line.charAt(i) == '1')
							puzzle[row - 9][i] = true;
						
						else if(line.charAt(i) == '0')
							puzzle[row - 9][i] = false;
						
						else {
							
							System.err.println("Incorrect value found in file.");
							System.exit(1);
							
						}
						
					}
					
				}
				
				row ++;
				
			}
			
			bufferedReader.close();
			fileReader.close();
			
		}
		
		catch(IOException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
}
