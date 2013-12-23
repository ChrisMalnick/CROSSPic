package game;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class CPGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final int Window_Width = 976;
	private static final int Window_Height = 918;
	private CPGE board;
	private JPanel mainPanel;
	private JPanel gamePanel;
	private JPanel rightGuidePanel;
	private JPanel bottomGuidePanel;
	private JPanel HUD_Panel;
	private JPanel canvasPanel;
	private JPanel gameInfoPanel;
	private JPanel timerPanel;
	private JPanel strikePanel;
	private JPanel menuPanel;
	private JButton[][] gameBoard;
	private JButton[][] rightGuideBoard;
	private JButton[][] bottomGuideBoard;
	private JButton[][] canvasBoard;
	private JButton[] strikes;
	private JButton hint;
	private JButton color;
	private JButton quit;
	private JButton timer;
	private Timer gameTimer;
	private Color shadeColor;
	private int minutes;
	private int seconds;
	private int strikeCount;
	private boolean isEnabled;
	
	public CPGUI(CPGE board) {
		super("CROSSPic!™");
		this.board = board;
		this.gameBoard = new JButton[board.puzzleSize][board.puzzleSize];
		this.rightGuideBoard = new JButton[board.puzzleSize][board.puzzleSize];
		this.bottomGuideBoard = new JButton[board.puzzleSize][board.puzzleSize];
		this.canvasBoard = new JButton[board.puzzleSize][board.puzzleSize];
		this.strikes = new JButton[3];
		if(board.puzzlePack != -1)
			this.hint = new JButton("Hint");
		else
			this.hint = new JButton("Clear");
		this.hint.addMouseListener(new MenuListener(0, 0));
		this.color = new JButton("Color");
		this.color.addMouseListener(new MenuListener(0, 1));
		this.quit = new JButton("Quit");
		this.quit.addMouseListener(new MenuListener(0, 2));
		this.gameTimer = new Timer(1000, new TimeListener());
		this.minutes = board.puzzleTime;
		this.seconds = 0;
		this.strikeCount = 0;
		this.timer = new JButton("Time: " + Integer.toString(minutes) + " : 0" + Integer.toString(seconds));
		this.shadeColor = Color.BLACK;
		this.isEnabled = true;
		this.mainPanel = new JPanel(new GridLayout(2,2));
		this.gamePanel = new JPanel(new GridLayout(board.puzzleSize, board.puzzleSize));
		this.rightGuidePanel = new JPanel(new GridLayout(board.puzzleSize, board.puzzleSize));
		this.bottomGuidePanel = new JPanel(new GridLayout(board.puzzleSize, board.puzzleSize));
		this.HUD_Panel = new JPanel(new GridLayout(2, 2));
		this.canvasPanel = new JPanel(new GridLayout(board.puzzleSize, board.puzzleSize));
		this.gameInfoPanel = new JPanel(new GridLayout(2, 1));
		this.timerPanel = new JPanel(new GridLayout(1, 1));
		this.strikePanel = new JPanel(new GridLayout(1, 3));
		this.menuPanel = new JPanel(new GridLayout(3, 1));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Window_Width, Window_Height);
		this.setResizable(true);
		for(int col = 0; col < gameBoard.length; col ++) {
			for(int row = 0; row < gameBoard[col].length; row ++) {
				JButton cell = new JButton("");
				cell.setBackground(Color.WHITE);
				cell.addMouseListener(new CellListener(col, row));
				this.gameBoard[col][row] = cell;
				this.gamePanel.add(cell);
			}
		}
		for(int col = 0; col < rightGuideBoard.length; col ++) {
			for(int row = 0; row < rightGuideBoard[col].length; row ++) {
				JButton cell = new JButton(board.rightGuide[col][row]);
				cell.setBackground(new Color(190, 210, 235));
				this.rightGuideBoard[col][row] = cell;
				this.rightGuidePanel.add(cell);
			}
		}
		for(int col = 0; col < bottomGuideBoard.length; col ++) {
			for(int row = 0; row < bottomGuideBoard[col].length; row ++) {
				JButton cell = new JButton(board.bottomGuide[col][row]);
				cell.setBackground(new Color(190, 210, 235));
				this.bottomGuideBoard[col][row] = cell;
				this.bottomGuidePanel.add(cell);
			}
		}
		for(int col = 0; col < canvasBoard.length; col ++) {
			for(int row = 0; row < canvasBoard[col].length; row ++) {
				JButton cell = new JButton("");
				cell.setBackground(Color.WHITE);
				this.canvasBoard[col][row] = cell;
				this.canvasPanel.add(cell);
			}
		}
		for(int col = 0; col < strikes.length; col ++) {
			JButton cell = new JButton("");
			this.strikes[col] = cell;
			this.strikePanel.add(cell);
		}
		this.menuPanel.add(hint);
		this.menuPanel.add(color);
		this.menuPanel.add(quit);
		this.timerPanel.add(timer);
		this.gameInfoPanel.add(timerPanel);
		this.gameInfoPanel.add(strikePanel);
		this.HUD_Panel.add(canvasPanel);
		this.HUD_Panel.add(gameInfoPanel);
		this.HUD_Panel.add(menuPanel);
		this.mainPanel.add(gamePanel);
		this.mainPanel.add(rightGuidePanel);
		this.mainPanel.add(bottomGuidePanel);
		this.mainPanel.add(HUD_Panel);
		this.add(mainPanel);
		this.setVisible(true);
		this.gameTimer.start();
	}
	
	private void setGameEnabled(boolean option) {
		this.isEnabled = option;
	}
	
	public void closeCurrentGame() {
		this.dispose();
	}
	
	public void win() {
		JOptionPane.showMessageDialog(this, "It's a " + board.puzzleName + "!", "Puzzle Solved", JOptionPane.INFORMATION_MESSAGE);
		setGameEnabled(false);
		closeCurrentGame();
		CPGE.generateGame();
	}
	
	public void lose() {
		if(strikes[2].getText().equals("X"))
			JOptionPane.showMessageDialog(this, "Three Strikes!", "Puzzle Failed", JOptionPane.ERROR_MESSAGE);
		else if(minutes == 0 && seconds == 0)
			JOptionPane.showMessageDialog(this, "Time's Up!", "Puzzle Failed", JOptionPane.ERROR_MESSAGE);
		setGameEnabled(false);
		closeCurrentGame();
		CPGE.generateGame();
	}
	
	private class TimeListener implements ActionListener {
		public TimeListener() {
			super();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(seconds == 0) {
				if(minutes == 0) {
					gameTimer.stop();
					timer.setText("Time: " + Integer.toString(minutes) + " : 0" + Integer.toString(seconds));
					if(board.puzzlePack != -1)
						lose();
				}
				else {
					minutes --;
					seconds = 59;
				}
			}
			else
				seconds --;
			if(Integer.toString(seconds).length() == 1)
				timer.setText("Time: " + Integer.toString(minutes) + " : 0" + Integer.toString(seconds));
			else
				timer.setText("Time: " + Integer.toString(minutes) + " : " + Integer.toString(seconds));
			if(minutes == 0 && board.puzzlePack != -1)
				hint.setEnabled(false);
		}
	}
	
	private class MenuListener extends MouseAdapter {
		private int x;
		private int y;
		
		public MenuListener(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			if(!isEnabled)
				return;
			else if(x == 0 && y == 0 && hint.isEnabled()) {
				if(board.puzzlePack != -1) {
					int shadedCells = 0;
					int hintCells = 0;
					for(int col = 0; col < board.shadeBoard.length; col ++) {
						for(int row = 0; row < board.shadeBoard[col].length; row ++) {
							if(board.shadeBoard[col][row] == true)
								shadedCells ++;
						}
					}
					hintCells = board.solutionSize - shadedCells;
					if(hintCells > board.puzzleSize / 2)
						hintCells = board.puzzleSize / 2;
					Random hintAnswer = new Random();
					int randomX = 0;
					int randomY = 0;
					for(int i = 0; i < hintCells; i ++) {
						randomX = hintAnswer.nextInt(board.puzzleSize);
						randomY = hintAnswer.nextInt(board.puzzleSize);
						if(!board.solution[randomX][randomY]) {
							i --;
							continue;
						}
						else if(board.isShaded(randomX, randomY)) {
							i --;
							continue;
						}
						else {
							board.shadeCell(randomX, randomY);
							gameBoard[randomX][randomY].setText("");
							gameBoard[randomX][randomY].setBackground(shadeColor);
							gameBoard[randomX][randomY].setEnabled(false);
							canvasBoard[randomX][randomY].setBackground(shadeColor);
						}
					}
					for(int col = 0; col < strikes.length; col ++) {
						if(strikes[col].getText().equals("")) {
							strikes[col].setText("X");
							strikeCount ++;
							break;
						}
					}
					minutes --;
					hint.setEnabled(false);
					if(Arrays.deepEquals(board.shadeBoard, board.solution)) {
						gameTimer.stop();
						win();
					}
				}
				else {
					for(int col = 0; col < board.puzzleSize; col ++) {
						for(int row = 0; row < board.puzzleSize; row ++) {
							JButton cell = gameBoard[col][row];
							JButton block = canvasBoard[col][row];
							if(board.isShaded(col, row)) {
								board.unshadeCell(col, row);
								cell.setBackground(Color.WHITE);
								block.setBackground(Color.WHITE);
							}
							if(board.isMarked(col, row)) {
								board.unmarkCell(col, row);
								cell.setText("");
								block.setText("");
							}
						}
					}
					repaint();
				}
			}
			else if(x == 0 && y == 1) {
				if(shadeColor.equals(Color.BLACK))
					shadeColor = Color.RED;
				else if(shadeColor.equals(Color.RED))
					shadeColor = Color.ORANGE;
				else if(shadeColor.equals(Color.ORANGE))
					shadeColor = Color.YELLOW;
				else if(shadeColor.equals(Color.YELLOW))
					shadeColor = Color.GREEN;
				else if(shadeColor.equals(Color.GREEN))
					shadeColor = Color.BLUE;
				else if(shadeColor.equals(Color.BLUE))
					shadeColor = Color.CYAN;
				else if(shadeColor.equals(Color.CYAN))
					shadeColor = Color.MAGENTA;
				else if(shadeColor.equals(Color.MAGENTA))
					shadeColor = Color.PINK;
				else if(shadeColor.equals(Color.PINK))
					shadeColor = Color.BLACK;
				for(int col = 0; col < board.puzzleSize; col ++) {
					for(int row = 0; row < board.puzzleSize; row ++) {
						JButton cell = gameBoard[col][row];
						JButton block = canvasBoard[col][row];
						if(board.isShaded(col, row)) {
							cell.setBackground(shadeColor);
							block.setBackground(shadeColor);
						}
					}
				}
				repaint();
			}
			else if(x == 0 && y == 2) {
				closeCurrentGame();
				CPGE.generateGame();
			}
		}
	}
	
	private class CellListener extends MouseAdapter {
		private int x;
		private int y;
		
		public CellListener(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			if(!isEnabled)
				return;
			if(e.getButton() == MouseEvent.BUTTON1) {
				if(board.isShaded(x, y) && gameBoard[x][y].isEnabled())
					board.unshadeCell(x, y);
				else if(board.puzzlePack == -1 && board.isMarked(x, y)) {
					board.unmarkCell(x, y);
					board.shadeCell(x, y);
				}
				else if(!board.isMarked(x, y))
					board.shadeCell(x, y);
				if(!board.solution[x][y] && !board.isMarked(x, y)) {
					board.markCell(x, y);
					for(int col = 0; col < strikes.length; col ++) {
						if(!gameBoard[x][y].isEnabled())
							break;
						if(strikes[col].getText().equals("")) {
							strikes[col].setText("X");
							strikeCount ++;
							if(strikeCount == 2)
								hint.setEnabled(false);
							if(strikeCount == 3) {
								gameBoard[x][y].setText("X");
								gameBoard[x][y].setEnabled(false);
								gameTimer.stop();
								lose();
							}
							break;
						}
					}
					if(gameBoard[x][y].isEnabled()) {
						if(minutes != 0)
							minutes --;
						else
							seconds = 0;
					}
					gameBoard[x][y].setEnabled(false);
				}
				else if(board.puzzlePack != -1 && !board.isMarked(x, y))
					gameBoard[x][y].setEnabled(false);
			}
			else {
				if(board.isMarked(x, y)) {
					if(gameBoard[x][y].isEnabled())
						board.unmarkCell(x, y);
				}
				else if(gameBoard[x][y].isEnabled())
					board.markCell(x, y);
			}
			for(int col = 0; col < board.puzzleSize; col ++) {
				for(int row = 0; row < board.puzzleSize; row ++) {
					JButton cell = gameBoard[col][row];
					JButton block = canvasBoard[col][row];
					if(board.isShaded(col, row)) {
						cell.setBackground(shadeColor);
						block.setBackground(shadeColor);
					}
					else if(board.puzzlePack == -1) {
						cell.setBackground(Color.WHITE);
						block.setBackground(Color.WHITE);
					}
					if(board.isMarked(col, row))
						cell.setText("X");
					else
						cell.setText("");
				}
			}
			repaint();
			if(Arrays.deepEquals(board.shadeBoard, board.solution) && board.puzzlePack != -1) {
				gameTimer.stop();
				win();
			}
		}
	}
}
