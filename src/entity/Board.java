package entity;

import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Graphics2D;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Timer;

import main.Frame;
import main.Panel;

public class Board {
	
	private int size;
	private boolean isPuzzle;
	
	private boolean[][] fill;
	
	private int[][] rightGuide;
	private int[][] bottomGuide;
	
	private int[][] mark;
	
	public Cursor cursor;
	public Puzzle puzzle;
	public HUD hud;
	
	public Board(int i, boolean b) {
		
		super();
		
		size = i;
		isPuzzle = b;
		
		fill = new boolean[i][i];
		
		rightGuide = new int[i][i / 2];
		bottomGuide = new int[i / 2][i];
		
		cursor = new Cursor(i);
		
		if(isPuzzle) {
			
			mark = new int[i][i];
			
			puzzle = new Puzzle(size, size + "_" + Panel.num);
			hud = new HUD(puzzle.getID(), size);
			
			Timer timer = new Timer();
			timer.schedule(hud, 0, 1000);
			
			generateGuides();
			
		}
		
	}
	
	public boolean solved() {
		
		return (java.util.Arrays.deepEquals(fill, puzzle.getBoard()));
		
	}
	
	public boolean threeStrikes() {
		
		return (hud.getStrikes() == 3);
		
	}
	
	public void fill(int col, int row) {
		
		if(isPuzzle) {
			
			if(fill[row][col] || mark[row][col] != 0)
				return;
			
			else if(!puzzle.getBoard()[row][col]) {
				
				hud.addStrike();
				mark[row][col] = 2;
				
			}
			
			else
				fill[row][col] = true;
			
		}
		
		if(!isPuzzle) {
			
			fill[row][col] = true;
			updateGuides();
			
		}
		
	}
	
	public void mark(int col, int row) {
		
		if(!fill[row][col] && mark[row][col] == 0)
			mark[row][col] = 1;
		
		else if(mark[row][col] == 1)
			mark[row][col] = 0;
		
	}
	
	public void erase(int col, int row) {
		
		fill[row][col] = false;
		updateGuides();
		
	}
	
	public void clear() {
		
		for(int i = 0; i < fill.length; i ++)
			for(int j = 0; j < fill[i].length; j ++)
				fill[i][j] = false;
		
		resetGuides();
		
	}
	
	public void invert() {
		
		for(int i = 0; i < fill.length; i ++)
			for(int j = 0; j < fill[i].length; j ++) {
				
				if(fill[i][j])
					fill[i][j] = false;
				
				else
					fill[i][j] = true;
				
			}
		
		updateGuides();
		
	}
	
	public void mirror() {
		
		boolean[][] mirror = new boolean[size][size];
		
		for(int i = 0; i < size; i ++)
			for(int j = 0; j < size; j ++)
				mirror[i][j] = fill[i][(size - 1) - j];
		
		fill = mirror;
		
		updateGuides();
		
	}
	
	public void flip() {
		
		boolean[][] flip = new boolean[size][size];
		
		for(int i = 0; i < size; i ++)
			for(int j = 0; j < size; j ++)
				flip[i][j] = fill[(size - 1) - i][j];
		
		fill = flip;
		
		updateGuides();
		
	}
	
	public void load() {
		
		String name = (String)JOptionPane.showInputDialog(Frame.frame, "Enter Name:", "Load", JOptionPane.PLAIN_MESSAGE, null, null, null);
		
		if(name == null || name.length() == 0)
			return;
		
		try {
			
			File load = new File(System.getProperty("user.dir") + "/puzzles/user/cp_" + size + "_" + name + ".dat");
			
			if(!load.exists())
				return;
			
			FileReader fileReader = new FileReader(load.getAbsoluteFile());
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			String line;
			int row = 0;
			
			while((line = bufferedReader.readLine()) != null) {
				
				/*To help circumvent cheating*/
				if(row == 1) {
					
					if(line != null && !line.equals("")) {
						
						bufferedReader.close();
						fileReader.close();
						return;
						
					}
					
				}
				
				if(row >= 9) {
					
					for(int i = 0; i < size; i ++) {
						
						if(line.charAt(i) == '1')
							fill[row - 9][i] = true;
						
						else if(line.charAt(i) == '0')
							fill[row - 9][i] = false;
						
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
			
			updateGuides();
			
		}
		
		catch(IOException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	public void save() {
		
		String name = (String)JOptionPane.showInputDialog(Frame.frame, "Enter Name:", "Save", JOptionPane.PLAIN_MESSAGE, null, null, null);
		
		if(name == null || name.equals(""))
			return;
		
		try {
			
			File save = new File(System.getProperty("user.dir") + "/puzzles/user/cp_" + size + "_" + name + ".dat");
			
			if(!save.exists())
				save.createNewFile();
			
			else if(!(JOptionPane.showConfirmDialog(null, "Overwrite?", "File Already Exists", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
				return;
			
			FileWriter fileWriter = new FileWriter(save.getAbsoluteFile());
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			
			String line = "";
			
			bufferedWriter.write(size	+ System.getProperty("line.separator")
										+ System.getProperty("line.separator")
								+ name	+ System.getProperty("line.separator")
								);
			
			if(Panel.userName != null && !Panel.userName.equals(""))
				bufferedWriter.write(Panel.userName);
			
			bufferedWriter.write(System.getProperty("line.separator")
								+ System.getProperty("line.separator")
								+ System.getProperty("line.separator")
								+ System.getProperty("line.separator")
								+ System.getProperty("line.separator")
								+ System.getProperty("line.separator")
								);
			
			for(int i = 0; i < size; i ++) {
				
				for(int j = 0; j < size; j ++) {
					
					if(fill[i][j])
						line += "1";
					
					else
						line += "0";
					
				}
				
				bufferedWriter.write(line);
				
				if(i < size - 1) {
					
					bufferedWriter.write(System.getProperty("line.separator"));
					line = "";
					
				}
				
			}
			
			bufferedWriter.close();
			fileWriter.close();
			
		}
		
		catch(Exception e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	private void generateGuides() {
		
		int count = 0;
		int b = 0;
		
		/*Right Guide*/
		for(int i = 0; i < size; i ++) {
			
			for(int j = 0; j < size; j ++) {
				
				if(puzzle.getBoard()[i][j]) {
					
					while(puzzle.getBoard()[i][j]) {
						
						count ++;
						j ++;
						
						if(j >= size)
							break;
						
					}
					
					rightGuide[i][b] = count;
					
					count = 0;
					b ++;
					
				}
				
			}
			
			b = 0;
			
		}
		
		/*Bottom Guide*/
		for(int i = 0; i < size; i ++) {
			
			for(int j = 0; j < size; j ++) {
				
				if(puzzle.getBoard()[j][i]) {
					
					while(puzzle.getBoard()[j][i]) {
						
						count ++;
						j ++;
						
						if(j >= size)
							break;
						
					}
					
					bottomGuide[b][i] = count;
					
					count = 0;
					b ++;
					
				}
				
			}
			
			b = 0;
			
		}
		
	}
	
	private void updateGuides() {
		
		int count = 0;
		int b = 0;
		
		resetGuides();
		
		/*Right Guide*/
		for(int i = 0; i < size; i ++) {
			
			for(int j = 0; j < size; j ++) {
				
				if(fill[i][j]) {
					
					while(fill[i][j]) {
						
						count ++;
						j ++;
						
						if(j >= size)
							break;
						
					}
					
					rightGuide[i][b] = count;
					
					count = 0;
					b ++;
					
				}
				
			}
			
			b = 0;
			
		}
		
		/*Bottom Guide*/
		for(int i = 0; i < size; i ++) {
			
			for(int j = 0; j < size; j ++) {
				
				if(fill[j][i]) {
					
					while(fill[j][i]) {
						
						count ++;
						j ++;
						
						if(j >= size)
							break;
						
					}
					
					bottomGuide[b][i] = count;
					
					count = 0;
					b ++;
					
				}
				
			}
			
			b = 0;
			
		}
		
	}
	
	private void resetGuides() {
		
		for(int i = 0; i < rightGuide[0].length; i ++)
			for(int j = 0; j < rightGuide.length; j ++)
				rightGuide[j][i] = 0;
		
		for(int i = 0; i < bottomGuide.length; i ++)
			for(int j = 0; j < bottomGuide[0].length; j ++)
				bottomGuide[i][j] = 0;
		
	}
	
	public void draw(Graphics2D G2D) {
		
		if(Panel.highlight)
			cursor.draw(G2D);
		
		/*Draw Lines*/
		for(int i = 0; i < size + 1; i ++) {
			
			G2D.setColor(Color.BLACK);
			
			if(i != 0 && i != size && i % 5 == 0)
				G2D.setColor(new Color(Panel.green, Panel.blue, Panel.red));
			
			/*Vertical*/
			G2D.drawLine(i * 16, 0, i * 16, (size + size / 2) * 16);
			
			/*Horizontal*/
			G2D.drawLine(0, i * 16, (size + size / 2) * 16, i * 16);
			
		}
		
		/*Vertical*/
		G2D.drawLine((size + size / 2) * 16, 0, (size + size / 2) * 16, (size + size / 2) * 16);
		
		/*Horizontal*/
		G2D.drawLine(0, (size + size / 2) * 16, (size + size / 2) * 16, (size + size / 2) * 16);
		
		/*Draw Fill & Mark*/
		for(int i = 0; i < size; i ++) {
			
			for(int j = 0; j < size; j ++) {
				
				if(fill[i][j]) {
					
					/*Game Board*/
					G2D.fillRect(j * 16 + 2, i * 16 + 2, 13, 13);
					
					/*Canvas*/
					G2D.fillRect(size * 16 + j * 8, size * 16 + i * 8, 8, 8);
					
				}
				
				if(isPuzzle) {
					
					if(mark[i][j] == 1 || mark[i][j] == 2) {
						
						if(mark[i][j] == 2) {
							
							G2D.drawLine(j * 16 + 2, i * 16 + 2, (j + 1) * 16 - 2, (i + 1) * 16 - 2);
							G2D.drawLine(j * 16 + 2, (i + 1) * 16 - 2, (j + 1) * 16 - 2, i * 16 + 2);
							
						}
						
						G2D.drawLine(j * 16 + 6, i * 16 + 6, (j + 1) * 16 - 6, (i + 1) * 16 - 6);
						G2D.drawLine(j * 16 + 6, (i + 1) * 16 - 6, (j + 1) * 16 - 6, i * 16 + 6);
						
					}
					
				}
					
			}
			
		}
		
		if(!Panel.highlight)
			cursor.draw(G2D);
		
		/*Draw Right Guide*/
		for(int i = 0; i < rightGuide.length; i ++) {
			
			G2D.setColor(Color.BLACK);
			
			if(isPuzzle && rowEquals(i))
				G2D.setColor(Color.GRAY);
			
			for(int j = 0; j < rightGuide[0].length; j ++)
				if(rightGuide[i][j] != 0)
					G2D.drawString(Integer.toString(rightGuide[i][j]), size * 16 + j * 16 + 4, i * 16 + 13);
			
		}
		
		/*Draw Bottom Guide*/
		for(int i = 0; i < bottomGuide[0].length; i ++) {
			
			G2D.setColor(Color.BLACK);
			
			if(isPuzzle && columnEquals(i))
				G2D.setColor(Color.GRAY);
			
			for(int j = 0; j < bottomGuide.length; j ++)
				if(bottomGuide[j][i] != 0)
					G2D.drawString(Integer.toString(bottomGuide[j][i]), i * 16 + 2, size * 16 + j * 16 + 13);
			
		}
		
		/*Draw HUD*/
		if(isPuzzle) {
			
			G2D.setFont(Panel.menu);
			G2D.setColor(Color.BLACK);
			hud.draw(G2D);
			
		}
		
	}
	
	private boolean rowEquals(int a) {
		
		for(int i = 0; i < size; i ++) {
			
			if(fill[a][i] != puzzle.getBoard()[a][i])
				return false;
			
		}
		
		return true;
		
	}
	
	private boolean columnEquals(int a) {
		
		for(int i = 0; i < size; i ++) {
			
			if(fill[i][a] != puzzle.getBoard()[i][a])
				return false;
			
		}
		
		return true;
		
	}
	
}
