package game;

import javax.swing.*;

public class CPGE {
	public CPGUI GUI;
	boolean[][] shadeBoard;
	boolean[][] markBoard;
	boolean[][] solution;
	String[][] rightGuide;
	String[][] bottomGuide;
	int solutionSize;
	int puzzlePack;
	int puzzle;
	int puzzleSize;
	int puzzleTime;
	String puzzleName;
	
	public static void main(String[] args) {
		generateGame();
	}
	
	public static void generateGame() {
		CPGE board = new CPGE();
		board.GUI = new CPGUI(board);
		board.GUI.setVisible(true);
	}
	
	public CPGE() {
		int option2 = 2;
		while(option2 == 2) {
			String[] options1 = {"Start", "How to Play", "About"};
			int option1 = JOptionPane.showOptionDialog(null, "Welcome to CROSSPic!™ - The picture puzzle game of cross sections!", "CROSSPic!™", 0, JOptionPane.PLAIN_MESSAGE, null, options1, options1[0]);
			if(option1 == 0) {
				int option3 = 2;
				while(option3 == 2) {
					String[] options2 = {"Free Draw Mode", "Puzzle Mode", "Back"};
					option2 = JOptionPane.showOptionDialog(null, "Select a game mode:", "CROSSPic!™ - Mode Select", 0, JOptionPane.PLAIN_MESSAGE, null, options2, options2[0]);
					if(option2 == 0) {
						puzzlePack = -1;
						puzzle = -1;
						puzzleTime = 0;
						String[] options3 = {"10 x 10", "20 x 20", "Back"};
						option3 = JOptionPane.showOptionDialog(null, "Select a grid size:", "CROSSPic!™ - Free Draw Mode", 0, JOptionPane.PLAIN_MESSAGE, null, options3, options3[0]);
						if(option3 == 0) {
							puzzleSize = 10;
						}
						else if(option3 == 1) {
							puzzleSize = 20;
						}
						else if(option3 != 2)
							System.exit(0);
					}
					else if(option2 == 1) {
						String selection4 = "Back";
						while(selection4.equals("Back")) {
							Object[] selections3 = {"Puzzle Pack A", "Puzzle Pack B", "Back"};
							String selection3 = (String)JOptionPane.showInputDialog(null, "Select a Puzzle Pack:", "CROSSPic!™ - Puzzle Mode", JOptionPane.PLAIN_MESSAGE, null, selections3, selections3[0]);
							if(selection3 == null)
								selection3 = "";
							if(selection3.equals("Puzzle Pack A")) {
								option3 = -1;
								puzzlePack = 0;
								puzzleSize = 10;
								puzzleTime = 5;
								Object[] selections4 = {"Puzzle 1", "Puzzle 2", "Puzzle 3", "Puzzle 4", "Puzzle 5", "Back"};
								selection4 = (String)JOptionPane.showInputDialog(null, "Select a Puzzle:", "CROSSPic!™ - Puzzle Mode", JOptionPane.PLAIN_MESSAGE, null, selections4, selections4[0]);
								if(selection4 == null)
									selection4 = "";
								if(selection4.equals("Puzzle 1"))
									puzzle = 0;
								else if(selection4.equals("Puzzle 2"))
									puzzle = 1;
								else if(selection4.equals("Puzzle 3"))
									puzzle = 2;
								else if(selection4.equals("Puzzle 4"))
									puzzle = 3;
								else if(selection4.equals("Puzzle 5"))
									puzzle = 4;
								else if(!selection4.equals("Back"))
									System.exit(0);
							}
							else if(selection3.equals("Puzzle Pack B")) {
								option3 = -1;
								puzzlePack = 1;
								puzzleSize = 10;
								puzzleTime = 5;
								Object[] selections4 = {"Puzzle 1", "Puzzle 2", "Puzzle 3", "Puzzle 4", "Puzzle 5", "Back"};
								selection4 = (String)JOptionPane.showInputDialog(null, "Select a Puzzle:", "CROSSPic!™ - Puzzle Mode", JOptionPane.PLAIN_MESSAGE, null, selections4, selections4[0]);
								if(selection4 == null)
									selection4 = "";
								if(selection4.equals("Puzzle 1"))
									puzzle = 0;
								else if(selection4.equals("Puzzle 2"))
									puzzle = 1;
								else if(selection4.equals("Puzzle 3"))
									puzzle = 2;
								else if(selection4.equals("Puzzle 4"))
									puzzle = 3;
								else if(selection4.equals("Puzzle 5"))
									puzzle = 4;
								else if(!selection4.equals("Back"))
									System.exit(0);
							}
							else if(selection3.equals("Back"))
								selection4 = "";
							else
								System.exit(0);
						}
					}
					else if(option2 == 2)
						option3 = -1;
					else
						System.exit(0);
				}
			}
			else if(option1 == 1)
				JOptionPane.showMessageDialog(null,
						"CROSSPic!™ is a game of nonograms or picture logic puzzles in which the cells\n" +
						"of a grid are to be shaded according to the numbers on the sides of the grid\n" +
						"in order to reveal a picture. The grid in the upper left hand corner of the\n" +
						"game is the picture grid. It is the player's goal to shade in this grid\n" +
						"in order to draw a picture. The numbers in each row to the right of this\n" +
						"grid refer to the number of cells in the corresponding row of the picture\n" +
						"grid that are connected from left to right. The numbers in each column to the\n" +
						"bottom of this grid refer to the number of cells in the corresponding column\n" +
						"of the picture grid that are connected from top to bottom. At the bottom right\n" +
						"hand side of the picture grid is a canvas that fills in identically to that of\n" +
						"the picture grid only in a smaller size. To the right of the canvas is a\n" +
						"Time Limit display and directly underneath is a Strike Counter display.\n" +
						"Bellow the canvas is a Menu which includes Hint, Color, and Quit buttons.\n\n" +
						"Left clicking a cell in the picture grid attempts to shade in that cell. If the cell is\n" +
						"a part of the picture, then it will shade in and become disabled, otherwise it will be\n" +
						"marked with a gray 'X' and will also become disabled. Right clicking a cell in the\n" +
						"picture grid will mark the cell with a black 'X'. Right clicking a cell with a black 'X'\n" +
						"will remove that 'X'. Left clicking a cell with an 'X' will do nothing, so use right click\n" +
						"in order to mark the cells that you know are not in the picture. Upon left clicking a cell\n" +
						"that is not in the picture, the Strike Counter will increase, and a minute will be removed\n" +
						"from the Time Limit. If the Time Limit reaches 0 or the Strike Counter reaches 3, then the\n" +
						"game is lost. If all of the cells in the picture are shaded in the picture grid before\n" +
						"either of the lose conditions are met, then the game is won. Clicking the Hint button will\n" +
						"randomly shade in half of the size of the board's dimensions of cells(Ex: in a 10 x 10 game,\n" +
						"hint will shade in 5 cells) that are in the picture and have not been shaded in yet.\n" +
						"Upon clicking Hint, Hint will become disabled, a strike will be generated, and a minute will\n" +
						"be deducted, so if there is less than a minute left or 2 strikes, then Hint will become\n" +
						"automatically disabled. Clicking Color will cycle through a set of colors and repaint the\n" +
						"picture grid and canvas to the new color. Clicking Quit will quit the game.\n\n" +
						"In Free Draw mode, the player can draw their own pictures. Hint will be replaced with\n" +
						"Clear and upon clicking will the picture grid and canvas be reset to default.\n" +
						"Left clicking any cell will shade it, and right clicking any cell will mark it.\n\n" +
						"Here's a tip: if a row/column has a number in it that is greater than half the size of that\n" +
						"row/column, then logically the difference in row/column size and twice the difference in\n" +
						"row/column size and that number have to be in the picture at the center of that row/column\n" +
						"(Ex: a 10 x 10 game has a row with a number 7, therefore the 4 middle cells of that row\n" +
						"must be in the picture).",
						"CROSSPic!™ - How to Play", JOptionPane.INFORMATION_MESSAGE);
			else if(option1 == 2)
				JOptionPane.showMessageDialog(null,
						"v1.0b\n\n" +
						"Version Notes:\n" +
						"THIS IS A BETA, and its interface reflects this verily.\n" +
						"This game was developed on a Windows 7 64-bit platform with a 1680x1050 display;\n" +
						"therefore, compatibility may be compromised otherwise.\n" +
						"I have noticed that on a smaller resolution display, the game becomes slightly\n" +
						"distorted; however, the game remains completely functional.\n" +
						"On a smaller display, the bottom of the game may be cut off, but it should not\n" +
						"interfere with functionality. Setting the game to full screen will distort the picture\n" +
						"but will not interfere with functionality. I have noticed some random minor bugs\n" +
						"such as menu panels being rendered as blank and the hint function not revealing enough cells,\n" +
						"but these issues are relatively infrequent. I have pushed this game from alpha to beta in that\n" +
						"I have remedied all of the immediate technical issues that I have encountered. Running this\n" +
						"beta essentially makes you a tester, and your feedback on its quality is invaluable.\n" +
						"RUN AT YOUR OWN DISCRETION.\n\n" +
						"Version History:\n" +
						"v1.0a(unreleased)\n\n" +
						"Copyright © 2013 Christopher L. Malnick",
						"CROSSPic!™ - About", JOptionPane.INFORMATION_MESSAGE);
			else
				System.exit(0);
		}
		shadeBoard = new boolean[puzzleSize][puzzleSize];
		markBoard = new boolean[puzzleSize][puzzleSize];
		solution = new boolean[puzzleSize][puzzleSize];
		rightGuide = new String[puzzleSize][puzzleSize];
		bottomGuide = new String[puzzleSize][puzzleSize];
		solution = getSolution(puzzleSize, puzzlePack, puzzle);
		rightGuide = getRightGuide(puzzleSize, puzzlePack, solution);
		bottomGuide = getBottomGuide(puzzleSize, puzzlePack, solution);
	}
	
	private boolean[][] getSolution(int a, int x, int y) {
		boolean[][] answer = new boolean[a][a];
		if(x == -1) {
			for(int i = 0; i < a; i ++) {
				for(int j = 0; j < a; j ++) {
					answer[i][j] = true;
				}
			}
			return answer;
		}
		if(x == 0) {
			if(y == 0) {
				this.puzzleName = "Musical Note";
				for(int i = 7; i < 10; i ++) {
					for(int j = 0; j < 4; j ++)
						answer[i][j] = true;
				}
				for(int i = 0; i < 10; i ++)
					answer[i][4] = true;
				for(int i = 0; i < 3; i ++) {
					for(int j = 5; j < 7; j ++)
						answer[i][j] = true;
				}
				for(int i = 0; i < 4; i ++)
					answer[i][7] = true;
				answer[7][7] = true;
				for(int i = 1; i < 7; i ++)
					answer[i][8] = true;
				for(int i = 2; i < 6; i ++)
					answer[i][9] = true;
				return answer;
			}
			if(y == 1) {
				this.puzzleName = "Spade";
				for(int i = 4; i < 7; i ++)
					answer[i][0] = true;
				for(int i = 3; i < 8; i ++)
					answer[i][1] = true;
				for(int i = 2; i < 8; i ++)
					answer[i][2] = true;
				answer[9][2] = true;
				for(int i = 1; i < 7; i ++)
					answer[i][3] = true;
				answer[8][3] = true;
				answer[9][3] = true;
				for(int i = 0; i < 10; i ++)
					answer[i][4] = true;
				for(int i = 0; i < 10; i ++)
					answer[i][5] = true;
				for(int i = 1; i < 7; i ++)
					answer[i][6] = true;
				answer[8][6] = true;
				answer[9][6] = true;
				for(int i = 2; i < 8; i ++)
					answer[i][7] = true;
				answer[9][7] = true;
				for(int i = 3; i < 8; i ++)
					answer[i][8] = true;
				for(int i = 4; i < 7; i ++)
					answer[i][9] = true;
				return answer;
			}
			if(y == 2) {
				this.puzzleName = "Heart";
				for(int i = 1; i < 5; i ++)
					answer[i][0] = true;
				for(int i = 0; i < 7; i ++)
					answer[i][1] = true;
				for(int i = 0; i < 8; i ++)
					answer[i][2] = true;
				for(int i = 0; i < 9; i ++)
					answer[i][3] = true;
				for(int i = 1; i < 10; i ++)
					answer[i][4] = true;
				for(int i = 1; i < 10; i ++)
					answer[i][5] = true;
				for(int i = 0; i < 5; i ++)
					answer[i][6] = true;
				for(int i = 6; i < 9; i ++)
					answer[i][6] = true;
				for(int i = 0; i < 3; i ++)
					answer[i][7] = true;
				for(int i = 5; i < 8; i ++)
					answer[i][7] = true;
				for(int i = 0; i < 7; i ++)
					answer[i][8] = true;
				for(int i = 1; i < 5; i ++)
					answer[i][9] = true;
				return answer;
			}
			if(y == 3) {
				this.puzzleName = "Clover";
				for(int i = 4; i < 7; i ++)
					answer[i][0] = true;
				for(int i = 3; i < 8; i ++)
					answer[i][1] = true;
				for(int i = 3; i < 8; i ++)
					answer[i][2] = true;
				answer[9][2] = true;
				answer[1][3] = true;
				answer[2][3] = true;
				for(int i = 4; i < 7; i ++)
					answer[i][3] = true;
				answer[9][3] = true;
				for(int i = 0; i < 10; i ++)
					answer[i][4] = true;
				for(int i = 0; i < 10; i ++)
					answer[i][5] = true;
				answer[1][6] = true;
				answer[2][6] = true;
				for(int i = 4; i < 7; i ++)
					answer[i][6] = true;
				answer[9][6] = true;
				for(int i = 3; i < 8; i ++)
					answer[i][7] = true;
				answer[9][7] = true;
				for(int i = 3; i < 8; i ++)
					answer[i][8] = true;
				for(int i = 4; i < 7; i ++)
					answer[i][9] = true;
				return answer;
			}
			if(y == 4) {
				this.puzzleName = "Question Mark";
				for(int j = 1; j < 9; j ++)
					answer[0][j] = true;
				for(int j = 0; j < 7; j ++)
					answer[1][j] = true;
				answer[1][8] = true;
				answer[1][9] = true;
				answer[2][0] = true;
				answer[2][1] = true;
				answer[2][8] = true;
				answer[2][9] = true;
				answer[3][0] = true;
				answer[3][1] = true;
				answer[3][8] = true;
				answer[3][9] = true;
				for(int j = 5; j < 10; j ++)
					answer[4][j] = true;
				for(int j = 4; j < 9; j ++)
					answer[5][j] = true;
				answer[6][4] = true;
				answer[6][5] = true;
				answer[8][4] = true;
				answer[8][5] = true;
				answer[9][4] = true;
				answer[9][5] = true;
				return answer;
			}
			else
				return answer;
		}
		if(x == 1) {
			if(y == 0) {
				this.puzzleName = "Dollar Sign";
				answer[0][3] = true;
				answer[0][6] = true;
				for(int j = 1; j < 10; j ++)
					answer[1][j] = true;
				for(int j = 0; j < 10; j ++)
					answer[2][j] = true;
				answer[3][0] = true;
				answer[3][1] = true;
				answer[3][3] = true;
				answer[3][6] = true;
				for(int j = 0; j < 9; j ++)
					answer[4][j] = true;
				for(int j = 1; j < 10; j ++)
					answer[5][j] = true;
				answer[6][3] = true;
				answer[6][6] = true;
				answer[6][8] = true;
				answer[6][9] = true;
				for(int j = 0; j < 10; j ++)
					answer[7][j] = true;
				for(int j = 0; j < 9; j ++)
					answer[8][j] = true;
				answer[9][3] = true;
				answer[9][6] = true;
				return answer;
			}
			if(y == 1) {
				this.puzzleName = "Crescent";
				for(int i = 3; i < 7; i ++)
					answer[i][0] = true;
				for(int i = 1; i < 9; i ++)
					answer[i][1] = true;
				for(int i = 1; i < 9; i ++)
					answer[i][2] = true;
				for(int i = 0; i < 10; i ++)
					answer[i][3] = true;
				for(int i = 0; i < 3; i ++)
					answer[i][4] = true;
				for(int i = 7; i < 10; i ++)
					answer[i][4] = true;
				answer[0][5] = true;
				answer[1][5] = true;
				answer[8][5] = true;
				answer[9][5] = true;
				answer[0][6] = true;
				answer[1][6] = true;
				answer[8][6] = true;
				answer[9][6] = true;
				answer[1][6] = true;
				answer[8][6] = true;
				answer[1][7] = true;
				answer[8][7] = true;
				answer[2][8] = true;
				answer[7][8] = true;
				return answer;
			}
			if(y == 2) {
				this.puzzleName = "Star";
				answer[3][0] = true;
				answer[4][0] = true;
				answer[9][0] = true;
				for(int i = 3; i < 6; i ++)
					answer[i][1] = true;
				for(int i = 7; i < 10; i ++)
					answer[i][1] = true;
				answer[3][2] = true;
				for(int i = 5; i < 10; i ++)
					answer[i][2] = true;
				for(int i = 1; i < 5; i ++)
					answer[i][3] = true;
				answer[7][3] = true;
				answer[8][3] = true;
				for(int i = 0; i < 8; i ++)
					answer[i][4] = true;
				for(int i = 0; i < 8; i ++)
					answer[i][5] = true;
				for(int i = 1; i < 9; i ++)
					answer[i][6] = true;
				for(int i = 3; i < 10; i ++)
					answer[i][7] = true;
				for(int i = 3; i < 6; i ++)
					answer[i][8] = true;
				for(int i = 7; i < 10; i ++)
					answer[i][8] = true;
				answer[3][9] = true;
				answer[4][9] = true;
				answer[9][9] = true;
				return answer;
			}
			if(y == 3) {
				this.puzzleName = "Car";
				for(int i = 5; i < 9; i ++)
					answer[i][0] = true;
				answer[4][1] = true;
				for(int i = 6; i < 10; i ++)
					answer[i][1] = true;
				for(int i = 4; i < 8; i ++)
					answer[i][2] = true;
				answer[9][2] = true;
				for(int i = 1; i < 10; i ++)
					answer[i][3] = true;
				answer[0][4] = true;
				answer[1][4] = true;
				for(int i = 4; i < 9; i ++)
					answer[i][4] = true;
				for(int i = 0; i < 9; i ++)
					answer[i][5] = true;
				answer[0][6] = true;
				answer[1][6] = true;
				for(int i = 4; i < 10; i ++)
					answer[i][6] = true;
				for(int i = 0; i < 3; i ++)
					answer[i][7] = true;
				for(int i = 4; i < 8; i ++)
					answer[i][7] = true;
				answer[9][7] = true;
				for(int i = 1; i < 10; i ++)
					answer[i][8] = true;
				for(int i = 4; i < 9; i ++)
					answer[i][9] = true;
				return answer;
			}
			if(y == 4) {
				this.puzzleName = "Cactus";
				for(int i = 3; i < 7; i ++)
					answer[i][0] = true;
				for(int i = 3; i < 8; i ++)
					answer[i][1] = true;
				answer[6][2] = true;
				answer[7][2] = true;
				for(int i = 1; i < 10; i ++)
					answer[i][3] = true;
				for(int i = 0; i < 10; i ++)
					answer[i][4] = true;
				for(int i = 0; i < 10; i ++)
					answer[i][5] = true;
				for(int i = 1; i < 10; i ++)
					answer[i][6] = true;
				answer[4][7] = true;
				answer[5][7] = true;
				for(int i = 1; i < 6; i ++)
					answer[i][8] = true;
				for(int i = 1; i < 5; i ++)
					answer[i][9] = true;
				return answer;
			}
			else
				return answer;
		}
		else
			return answer;
	}
	
	private String[][] getRightGuide(int a, int b, boolean[][] solution) {
		String[][] guide = new String[a][a];
		if(b == -1)
			return guide;
		int count = 0;
		int y = 0;
		for(int i = 0; i < rightGuide.length; i ++) {
			for(int j = 0; j < rightGuide[i].length; j ++) {
				if(solution[i][j] == true) {
					while(solution[i][j] == true) {
						this.solutionSize ++;
						count ++;
						j ++;
						if(j >= rightGuide[i].length)
							break;
					}
					guide[i][y] = Integer.toString(count);
					count = 0;
					y ++;
				}
			}
			y = 0;
		}
		return guide;
	}
	
	private String[][] getBottomGuide(int a, int b, boolean[][] solution) {
		String[][] guide = new String[a][a];
		if(b == -1)
			return guide;
		int count = 0;
		int x = 0;
		for(int i = 0; i < bottomGuide.length; i ++) {
			for(int j = 0; j < bottomGuide[i].length; j ++) {
				if(solution[j][i] == true) {
					while(solution[j][i] == true) {
						count ++;
						j ++;
						if(j >= bottomGuide[i].length)
							break;
					}
					guide[x][i] = Integer.toString(count);
					count = 0;
					x ++;
				}
			}
			x = 0;
		}
		return guide;
	}
	
	public void shadeCell(int x, int y) {
		if(!shadeBoard[x][y]) {
			if(markBoard[x][y] == true)
				markBoard[x][y] = false;
			shadeBoard[x][y] = true;
			return;
		}
		return;
	}
	
	public void unshadeCell(int x, int y) {
		if(shadeBoard[x][y] == true) {
			shadeBoard[x][y] = false;
			return;
		}
		return;
	}
	
	public boolean isShaded(int x, int y) {
		if(!shadeBoard[x][y])
			return false;
		else
			return true;
	}
	
	public void markCell(int x, int y) {
		if(!markBoard[x][y]) {
			if(shadeBoard[x][y] == true)
				shadeBoard[x][y] = false;
			markBoard[x][y] = true;
			return;
		}
		return;
	}
	
	public void unmarkCell(int x, int y) {
		if(markBoard[x][y] == true) {
			markBoard[x][y] = false;
			return;
		}
		return;
	}
	
	public boolean isMarked(int x, int y) {
		if(!markBoard[x][y])
			return false;
		else
			return true;
	}
}
