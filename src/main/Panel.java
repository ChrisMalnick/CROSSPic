package main;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import entity.Board;

public class Panel extends JPanel implements Runnable, KeyListener {
	
	private static final long serialVersionUID = 0L;
	
	/*Dimensions*/
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	
	/*Thread Parameters*/
	private Thread thread;
	private boolean running;
	private static final int FPS = 30;
	private long targetTime = 1000 / FPS;
	
	/*Color Values*/
	public static int red = 255;
	public static int green;
	public static int blue;
	
	/*Opacity*/
	private float alpha;
	
	/*Fonts*/
	private Font title;
	public static Font menu;
	
	/*State*/
	public static int state;
	public static int num = 1;
	private static final int maxNum = 33;
	private int letter;
	private int selection;
	
	/*Board*/
	private int size;
	private Board board;
	
	/*Puzzle Info*/
	private String id;
	private String name;
	private String creator;
	private String clearDate;
	private String minutes;
	private String seconds;
	private String strikes;
	private String checks;
	
	/*Options*/
	public static boolean background = true;
	public static boolean highlight;
	public static String userName;
	
	/*Completion*/
	private static final int maxChecks = 297;
	private int checksEarned;
	
	public Panel() {
		
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setOpaque(true);
		setBackground(getBackgroundColor());
		setFocusable(true);
		requestFocus();
		
	}
	
	public void addNotify() {
		
		super.addNotify();
		
		if(thread == null) {
			
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
			
		}
		
	}
	
	private void initialize() {
		
		try {
			
			title = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/cp_title.ttf")).deriveFont(Font.PLAIN, 72);
			menu = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/cp_menu.ttf")).deriveFont(Font.PLAIN, 24);
			
		}
		
		catch(Exception e) {
			
			e.printStackTrace();
			
		}
		
		loadConfig();
		
		if(userName == null || userName.equals("")) {
			
			userName = (String)JOptionPane.showInputDialog(Frame.frame, "Enter Name:", "Choose a Nickname", JOptionPane.PLAIN_MESSAGE, null, null, null);
			saveConfig();
			
		}
		
		running = true;
		
	}
	
	public void run() {
		
		initialize();
		
		long start;
		long elapsed;
		long wait;
		
		/*Loop*/
		while(running) {
			
			start = System.nanoTime();
			
			update();
			repaint();
			
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000;
			
			if(wait < 0)
				wait = 5;
			
			try {
				
				Thread.sleep(wait);
				
			}
			
			catch(Exception e) {
				
				e.printStackTrace();
				
			}
			
		}
		
	}
	
	private void update() {
		
		updateColor();
		updateOpacity();
		this.setBackground(getBackgroundColor());
		
	}
	
	private void updateColor() {
		
		if(red == 255 && green == 0 && blue != 255)
			blue ++;
		
		else if(red != 0 && green == 0 && blue == 255)
			red --;
		
		else if(red == 0 && green != 255 && blue == 255)
			green ++;
		
		else if(red == 0 && green == 255 && blue != 0)
			blue --;
		
		else if(red != 255 && green == 255 && blue == 0)
			red ++;
		
		else if(red == 255 && green != 0 && blue == 0)
			green --;
		
	}
	
	private void updateOpacity() {
		
		alpha += 0.05f;
		
		if(alpha >= 1.0f)
			alpha = 1.0f;
		
	}
	
	public static Color getBackgroundColor() {
		
		if(background)
			return new Color(red, green, blue);
		
		else
			return Color.WHITE;
		
	}
	
	public static Color getSelectionColor() {
		
		if(background)
			return Color.WHITE;
		
		else
			return new Color(red, green, blue);
		
	}
	
	public synchronized void paintChildren(Graphics g) {
		
		Graphics2D GFX = (Graphics2D) g;
		GFX.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		GFX.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//GFX.drawLine(320, 0, 320, 480);
		//GFX.drawLine(0, 240, 640, 240);
		
		switch(state) {
		
		case 0 : paintMenuState(GFX);				break;
		case 1 : paintPuzzleSelectionState(GFX);	break;
		case 2 : paintDrawSelectionState(GFX);		break;
		case 3 : paintPuzzleState(GFX);				break;
		case 4 : paintDrawState(GFX);				break;
		case 5 : paintPauseState(GFX);				break;
		case 6 : paintWinState(GFX);				break;
		case 7 : paintLoseState(GFX);				break;
		case 8 : paintOptionsState(GFX);			break;
		
		}
		
	}
	
	private void paintMenuState(Graphics2D G2D) {
		
		G2D.setColor(Color.BLACK);
		G2D.drawString("2016 Chris Malnick", 532, 478);
		
		G2D.setFont(title);
		G2D.setColor(getSelectionColor());
		G2D.drawString("CROSSPic", 56, 102);
		G2D.setColor(Color.BLACK);
		G2D.drawString("CROSSPic", 60, 106);
		
		G2D.setFont(menu);
		
		if(userName == null || userName.equals(""))
			G2D.drawString("Welcome User", 60, 140);
		
		else
			G2D.drawString("Welcome " + userName, 60, 140);
		
		G2D.setColor(getSelectionColor());
		
		switch(selection) {
		
		case 0 : G2D.drawString("Puzzle Mode", 188, 218);	break;
		case 1 : G2D.drawString("Draw Mode", 206, 258);		break;
		case 2 : G2D.drawString("Options", 242, 298);		break;
		case 3 : G2D.drawString("Quit", 278, 338);			break;
		
		}
		
		G2D.setColor(Color.BLACK);
		G2D.drawString("Puzzle Mode", 190, 220);
		G2D.drawString("Draw Mode", 208, 260);
		G2D.drawString("Options", 244, 300);
		G2D.drawString("Quit", 280, 340);
		
	}
	
	private void paintPuzzleSelectionState(Graphics2D G2D) {
		
		G2D.setFont(menu);
		
		G2D.setColor(Color.BLACK);
		G2D.drawString("Select a puzzle", 155, 100);
		
		G2D.drawString("Completion [" + (int) (((double) checksEarned / maxChecks) * 100) + "]", 155, 140);
		
		G2D.drawString("Puzzle", 100, 180);
		G2D.drawString("Grid Size", 400, 180);
		
		G2D.setColor(getSelectionColor());
		
		switch(selection) {
		
		case 0 : G2D.drawString("10x10", 440, 218);	paintData(G2D);	break;
		case 1 : G2D.drawString("16x16", 440, 258);	paintData(G2D);	break;
		case 2 : G2D.drawString("20x20", 428, 298);	paintData(G2D);	break;
		case 3 : G2D.drawString("Back", 273, 398);					break;
		
		}
		
		G2D.setColor(Color.BLACK);
		G2D.drawString("10x10", 442, 220);
		G2D.drawString("16x16", 442, 260);
		G2D.drawString("20x20", 430, 300);
		G2D.drawString("Back", 275, 400);
		
	}
	
	private void paintDrawSelectionState(Graphics2D G2D) {
		
		G2D.setFont(menu);
		
		G2D.setColor(Color.BLACK);
		G2D.drawString("Select a grid size", 140, 100);
		
		G2D.setColor(getSelectionColor());
		
		switch(selection) {
		
		case 0 : G2D.drawString("10x10", 273, 218);	break;
		case 1 : G2D.drawString("16x16", 273, 258);	break;
		case 2 : G2D.drawString("20x20", 261, 298);	break;
		case 3 : G2D.drawString("Back", 273, 338);	break;
		
		}
		
		G2D.setColor(Color.BLACK);
		G2D.drawString("10x10", 275, 220);
		G2D.drawString("16x16", 275, 260);
		G2D.drawString("20x20", 263, 300);
		G2D.drawString("Back", 275, 340);
		
	}
	
	private void paintPuzzleState(Graphics2D G2D) {
		
		board.draw(G2D);
		
	}
	
	private void paintDrawState(Graphics2D G2D) {
		
		board.draw(G2D);
		
	}
	
	private void paintPauseState(Graphics2D G2D) {
		
		G2D.setFont(menu);
		
		G2D.setColor(Color.BLACK);
		
		board.hud.draw(G2D);
		
		G2D.drawString("Paused", 250, 100);
		
		G2D.setColor(getSelectionColor());
		
		switch(selection) {
		
		case 0 : G2D.drawString("Resume", 242, 218);	break;
		case 1 : G2D.drawString("Restart", 236, 258);	break;
		case 2 : G2D.drawString("Quit", 278, 298);		break;
		
		}
		
		G2D.setColor(Color.BLACK);
		G2D.drawString("Resume", 244, 220);
		G2D.drawString("Restart", 238, 260);
		G2D.drawString("Quit", 280, 300);
		
	}
	
	private void paintWinState(Graphics2D G2D) {
		
		G2D.setFont(menu);
		G2D.setColor(Color.BLACK);
		
		board.hud.draw(G2D);
		
		G2D.drawString("Puzzle Solved", 172, 100);
		G2D.drawString(board.puzzle.getName(), 172, 140);
		
		G2D.drawRect(WIDTH / 2 - (size / 2) * 8, HEIGHT / 2 - (size / 2) * 8, size * 8, size * 8);
		
		for(int i = 0; i < board.puzzle.getBoard().length; i ++)
			for(int j = 0; j < board.puzzle.getBoard()[i].length; j ++)
				if(board.puzzle.getBoard()[i][j])
					G2D.fillRect((WIDTH / 2 - (size / 2) * 8) + j * 8, (HEIGHT / 2 - (size / 2) * 8) + i * 8, 8, 8);
		
		G2D.drawRect(270, 340, 20, 20);
		G2D.drawRect(310, 340, 20, 20);
		G2D.drawRect(350, 340, 20, 20);
		
		if(board.hud.getChecks() >= 1) {
			
			G2D.drawLine(272, 350, 280, 358);
			G2D.drawLine(280, 358, 288, 342);
			
		}
		
		if(board.hud.getChecks() >= 2) {
			
			G2D.drawLine(312, 350, 320, 358);
			G2D.drawLine(320, 358, 328, 342);
			
		}
		
		if(board.hud.getChecks() >= 3) {
			
			G2D.drawLine(352, 350, 360, 358);
			G2D.drawLine(360, 358, 368, 342);
			
		}
		
		G2D.drawString("Press Enter", 196, 400);
		
	}
	
	private void paintLoseState(Graphics2D G2D) {
		
		G2D.setFont(menu);
		
		G2D.setColor(Color.BLACK);
		
		board.hud.draw(G2D);
		
		if(board.hud.getTimeUp())
			G2D.drawString("Time Up", 243, 100);
		
		else
			G2D.drawString("Three Strikes", 172, 100);
		
		G2D.setColor(getSelectionColor());
		
		switch(selection) {
		
		case 0 : G2D.drawString("Restart", 236, 258);	break;
		case 1 : G2D.drawString("Quit", 278, 298);		break;
		
		}
		
		G2D.setColor(Color.BLACK);
		G2D.drawString("Restart", 238, 260);
		G2D.drawString("Quit", 280, 300);
		
	}
	
	private void paintOptionsState(Graphics2D G2D) {
		
		G2D.setFont(menu);
		
		G2D.setColor(Color.BLACK);
		G2D.drawString("Options", 244, 100);
		
		G2D.setColor(getSelectionColor());
		
		switch(selection) {
		
		case 0 : G2D.drawString("Background", 128, 178);		break;
		case 1 : G2D.drawString("Cursor Highlight", 128, 258);	break;
		case 2 : G2D.drawString("Change Name", 128, 338);		break;
		case 3 : G2D.drawString("Back", 273, 398);				break;
		
		}
		
		G2D.setColor(Color.BLACK);
		
		G2D.drawString("Background", 130, 180);
		
		G2D.drawRect(130, 200, 20, 20);
		G2D.drawString("On", 170, 220);
		
		G2D.drawRect(250, 200, 20, 20);
		G2D.drawString("Off", 290, 220);
		
		if(background) {
			
			G2D.drawLine(132, 210, 140, 218);
			G2D.drawLine(140, 218, 148, 202);
			
		}
		
		else {
			
			G2D.drawLine(252, 210, 260, 218);
			G2D.drawLine(260, 218, 268, 202);
			
		}
		
		G2D.drawString("Cursor Highlight", 130, 260);
		
		G2D.drawRect(130, 280, 20, 20);
		G2D.drawString("On", 170, 300);
		
		G2D.drawRect(250, 280, 20, 20);
		G2D.drawString("Off", 290, 300);
		
		if(highlight) {
			
			G2D.drawLine(132, 290, 140, 298);
			G2D.drawLine(140, 298, 148, 282);
			
		}
		
		else {
			
			G2D.drawLine(252, 290, 260, 298);
			G2D.drawLine(260, 298, 268, 282);
			
		}
		
		G2D.drawString("Change Name", 130, 340);
		G2D.drawString("Back", 275, 400);
		
	}
	
	private void paintData(Graphics2D G2D) {
		
		if(id != null && !id.equals(""))
			G2D.drawString(id, 100, 218);
		
		if(clearDate != null && !clearDate.equals("")) {
			
			G2D.setFont(new Font("Default", Font.BOLD, 24));
			G2D.drawString(clearDate, 100, 298);
			G2D.setFont(menu);
			
		}
		
		if(minutes != null && !minutes.equals("") && seconds != null && !seconds.equals("")) {
			
			G2D.drawString(minutes, 100, 338);
			
			G2D.fillOval(150, 319, 8, 8);
			G2D.fillOval(150, 329, 8, 8);
			
			G2D.drawString(seconds, 162, 338);
			
		}
		
		if(strikes != null && !strikes.equals("")) {
			
			int s = Integer.parseInt(strikes);
			
			G2D.drawRect(100, 358, 20, 20);
			G2D.drawRect(140, 358, 20, 20);
			G2D.drawRect(180, 358, 20, 20);
			
			if(s >= 1) {
				
				G2D.drawLine(102, 360, 118, 376);
				G2D.drawLine(102, 376, 118, 360);
				
			}
			
			if(s >= 2) {
				
				G2D.drawLine(142, 360, 158, 376);
				G2D.drawLine(142, 376, 158, 360);
				
			}
			
			if(s >= 3) {
				
				G2D.drawLine(182, 360, 198, 376);
				G2D.drawLine(182, 376, 198, 360);
				
			}
			
		}
		
		if(checks != null && !checks.equals("")) {
			
			int c = Integer.parseInt(checks);
			
			G2D.drawRect(100, 398, 20, 20);
			G2D.drawRect(140, 398, 20, 20);
			G2D.drawRect(180, 398, 20, 20);
			
			if(c >= 1) {
				
				if(name != null && !name.equals(""))
					G2D.drawString(name, 100, 258);
				
				G2D.drawLine(102, 408, 110, 416);
				G2D.drawLine(110, 416, 118, 400);
				
			}
			
			if(c >= 2) {
				
				G2D.drawLine(142, 408, 150, 416);
				G2D.drawLine(150, 416, 158, 400);
				
			}
			
			if(c >= 3) {
				
				G2D.drawLine(182, 408, 190, 416);
				G2D.drawLine(190, 416, 198, 400);
				
			}
			
		}
		
	}
	
	public void keyPressed(KeyEvent e) {
		
		int key = e.getKeyCode();
		
		switch(state) {
		
		case 0 : updateMenuState(key);				break;
		case 1 : updatePuzzleSelectionState(key);	break;
		case 2 : updateDrawSelectionState(key);		break;
		case 3 : updatePuzzleState(key);			break;
		case 4 : updateDrawState(key);				break;
		case 5 : updatePauseState(key);				break;
		case 6 : updateWinState(key);				break;
		case 7 : updateLoseState(key);				break;
		case 8 : updateOptionsState(key);			break;
		
		}
		
	}
	
	public void keyReleased(KeyEvent e) {}
	
	public void keyTyped(KeyEvent e) {}
	
	private void updateMenuState(int k) {
		
		if(k == KeyEvent.VK_D || k == KeyEvent.VK_ENTER) {
			
			if(selection == 0) {
				
				loadData();
				state = 1;
				
			}
			
			else if(selection == 1)
				state = 2;
			
			else if(selection == 2)
				state = 8;
			
			else if (selection == 3)
				System.exit(0);
			
			reset();
			
		}
		
		else if(k == KeyEvent.VK_UP) {
			
			if(selection == 0)
				selection = 3;
			
			else
				selection --;
			
		}
		
		else if(k == KeyEvent.VK_DOWN) {
			
			if(selection == 3)
				selection = 0;
			
			else
				selection ++;
			
		}
		
	}
	
	private void updatePuzzleSelectionState(int k) {
		
		if(k == KeyEvent.VK_D || k == KeyEvent.VK_ENTER) {
			
			if(selection == 0)
				size = 10;
			
			else if(selection == 1)
				size = 16;
			
			else if (selection == 2)
				size = 20;
			
			else if (selection == 3) {
				
				state = 0;
				reset();
				return;
				
			}
			
			construct();
			reset();
			
		}
		
		else if(k == KeyEvent.VK_UP) {
			
			if(selection == 0)
				selection = 3;
			
			else
				selection --;
			
			if(selection != 3) {
				
				letter = selection;
				loadData();
				
			}
			
		}
		
		else if(k == KeyEvent.VK_DOWN) {
			
			if(selection == 3)
				selection = 0;
			
			else
				selection ++;
			
			if(selection != 3) {
				
				letter = selection;
				loadData();
				
			}
			
		}
		
		else if(k == KeyEvent.VK_LEFT) {
			
			if(selection != 3) {
				
				num --;
				
				if(num < 1)
					num = maxNum;
				
				loadData();
				
			}
			
		}
		
		else if(k == KeyEvent.VK_RIGHT) {
			
			if(selection != 3) {
				
				num ++;
				
				if(num > maxNum)
					num = 1;
				
				loadData();
				
			}
			
		}
		
		else if(k == KeyEvent.VK_E || k == KeyEvent.VK_B) {
			
			state = 0;
			reset();
			
		}
		
	}
	
	private void updateDrawSelectionState(int k) {
		
		if(k == KeyEvent.VK_D || k == KeyEvent.VK_ENTER) {
			
			if(selection == 0)
				size = 10;
			
			else if(selection == 1)
				size = 16;
			
			else if (selection == 2)
				size = 20;
			
			else if (selection == 3) {
				
				state = 0;
				reset();
				return;
				
			}
			
			construct();
			reset();
			
		}
		
		else if(k == KeyEvent.VK_UP) {
			
			if(selection == 0)
				selection = 3;
			
			else
				selection --;
			
		}
		
		else if(k == KeyEvent.VK_DOWN) {
			
			if(selection == 3)
				selection = 0;
			
			else
				selection ++;
			
		}
		
		else if(k == KeyEvent.VK_E || k == KeyEvent.VK_B) {
			
			state = 0;
			reset();
			
		}
		
	}
	
	private void updatePuzzleState(int k) {
		
		if(k == KeyEvent.VK_D) {
			
			board.fill(board.cursor.getCol(), board.cursor.getRow());
			
			if(board.threeStrikes()) {
				
				board.hud.setTiming(false);
				state = 7;
				reset();
				
			}
			
			if(board.solved()) {
				
				board.hud.setTiming(false);
				board.hud.addCheck();
				
				if(board.hud.timeLeft(size))
					board.hud.addCheck();
				
				if(board.hud.getStrikes() == 0)
					board.hud.addCheck();
				
				if(checks != null && !checks.equals("")) {
					
					if(board.hud.getChecks() > Integer.parseInt(checks)) {
						
						checksEarned += (board.hud.getChecks() - Integer.parseInt(checks));
						saveConfig();
						
					}
					
				}
				
				else {
					
					checksEarned += board.hud.getChecks();
					saveConfig();
					
				}
				
				saveData();
				state = 6;
				reset();
				
			}
			
		}
		
		else if(k == KeyEvent.VK_E)
			board.mark(board.cursor.getCol(), board.cursor.getRow());
		
		else if(k == KeyEvent.VK_UP)
			board.cursor.moveUp();
		
		else if(k == KeyEvent.VK_DOWN)
			board.cursor.moveDown();
		
		else if(k == KeyEvent.VK_LEFT)
			board.cursor.moveLeft();
		
		else if(k == KeyEvent.VK_RIGHT)
			board.cursor.moveRight();
		
		else if(k == KeyEvent.VK_P) {
			
			board.hud.setTiming(false);
			state = 5;
			reset();
			
		}
		
	}
	
	private void updateDrawState(int k) {
		
		if(k == KeyEvent.VK_D)
			board.fill(board.cursor.getCol(), board.cursor.getRow());
		
		else if(k == KeyEvent.VK_E)
			board.erase(board.cursor.getCol(), board.cursor.getRow());
		
		else if(k == KeyEvent.VK_C)
			board.clear();
		
		else if(k == KeyEvent.VK_I)
			board.invert();
		
		else if(k == KeyEvent.VK_M)
			board.mirror();
		
		else if(k == KeyEvent.VK_F)
			board.flip();
		
		else if(k == KeyEvent.VK_L)
			board.load();
		
		else if(k == KeyEvent.VK_S)
			board.save();
		
		else if(k == KeyEvent.VK_UP)
			board.cursor.moveUp();
		
		else if(k == KeyEvent.VK_DOWN)
			board.cursor.moveDown();
		
		else if(k == KeyEvent.VK_LEFT)
			board.cursor.moveLeft();
		
		else if(k == KeyEvent.VK_RIGHT)
			board.cursor.moveRight();
		
		else if(k == KeyEvent.VK_B) {
			
			state = 2;
			reset();
			
		}
		
	}
	
	private void updatePauseState(int k) {
		
		if(k == KeyEvent.VK_D || k == KeyEvent.VK_ENTER) {
			
			if(selection == 0) {
				
				board.hud.setTiming(true);
				state = 3;
				
			}
			
			else if(selection == 1)
				construct();
			
			else if (selection == 2)
				state = 1;
			
			reset();
			
		}
		
		else if(k == KeyEvent.VK_P || k == KeyEvent.VK_E || k == KeyEvent.VK_B) {
			
			board.hud.setTiming(true);
			state = 3;
			reset();
			
		}
		
		else if(k == KeyEvent.VK_UP) {
			
			if(selection == 0)
				selection = 2;
			
			else
				selection --;
			
		}
		
		else if(k == KeyEvent.VK_DOWN) {
			
			if(selection == 2)
				selection = 0;
			
			else
				selection ++;
			
		}
		
	}
	
	private void updateWinState(int k) {
		
		if(k == KeyEvent.VK_ENTER) {
			
			loadData();
			state = 1;
			reset();
			
		}
		
	}
	
	private void updateLoseState(int k) {
		
		if(k == KeyEvent.VK_D || k == KeyEvent.VK_ENTER) {
			
			if(selection == 0)
				construct();
			
			else if (selection == 1)
				state = 1;
			
			reset();
			
		}
		
		else if(k == KeyEvent.VK_UP || k == KeyEvent.VK_DOWN) {
			
			if(selection == 0)
				selection = 1;
			
			else if(selection == 1)
				selection = 0;
			
		}
		
	}
	
	private void updateOptionsState(int k) {
		
		if(k == KeyEvent.VK_D || k == KeyEvent.VK_ENTER) {
			
			if(selection == 0) {
				
				if(background)
					background = false;
				
				else
					background = true;
				
			}
			
			else if(selection == 1) {
				
				if(highlight)
					highlight = false;
				
				else
					highlight = true;
				
			}
			
			else if(selection == 2) {
				
				String s = (String)JOptionPane.showInputDialog(Frame.frame, "Enter Name:", "Choose a New Nickname", JOptionPane.PLAIN_MESSAGE, null, null, null);
				
				if(s != null && !s.equals(""))
					userName = s;
				
			}
			
			else if(selection == 3) {
				
				saveConfig();
				state = 0;
				reset();
				
			}
			
		}
		
		else if(k == KeyEvent.VK_UP) {
			
			if(selection == 0)
				selection = 3;
			
			else
				selection --;
			
		}
		
		else if(k == KeyEvent.VK_DOWN) {
			
			if(selection == 3)
				selection = 0;
			
			else
				selection ++;
			
		}
		
		else if(k == KeyEvent.VK_LEFT || k == KeyEvent.VK_RIGHT) {
			
			if(selection == 0) {
				
				if(background)
					background = false;
				
				else
					background = true;
				
			}
			
			else if(selection == 1) {
				
				if(highlight)
					highlight = false;
				
				else
					highlight = true;
				
			}
			
		}
		
		else if(k == KeyEvent.VK_E || k == KeyEvent.VK_B) {
			
			saveConfig();
			state = 0;
			reset();
			
		}
		
	}
	
	private void loadData() {
		
		String s = "";
		
		switch(letter) {
		
		case 0 : s = "10_";	break;
		case 1 : s = "16_";	break;
		case 2 : s = "20_";	break;
		
		}
		
		try {
			
			File load = new File(System.getProperty("user.dir") + "/puzzles/cp_" + s + num + ".dat");
			
			FileReader fileReader = new FileReader(load.getAbsoluteFile());
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			String line;
			int row = 0;
			
			while(row <= 8 && (line = bufferedReader.readLine()) != null) {
				
				if(row == 1)
					id = line;
				
				else if(row == 2)
					name = line;
				
				else if(row == 3)
					creator = line;
				
				else if(row == 4)
					clearDate = line;
				
				else if(row == 5)
					minutes = line;
				
				else if(row == 6)
					seconds = line;
				
				else if(row == 7)
					strikes = line;
				
				else if(row == 8)
					checks = line;
				
				row ++;
				
			}
			
			bufferedReader.close();
			fileReader.close();
			
		}
		
		catch(IOException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	private void saveData() {
		
		try {
			
			File save = new File(System.getProperty("user.dir") + "/puzzles/cp_" + size + "_" + num + ".dat");
			
			if(!save.exists())
				save.createNewFile();
			
			FileWriter fileWriter = new FileWriter(save.getAbsoluteFile());
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			
			String line = "";
			
			bufferedWriter.write(size						+ System.getProperty("line.separator")
								+ board.puzzle.getID()		+ System.getProperty("line.separator")
								+ board.puzzle.getName()	+ System.getProperty("line.separator")
								);
			
			if(creator != null && !creator.equals(""))
				bufferedWriter.write(creator);
			
			bufferedWriter.write(System.getProperty("line.separator") + getCurrentDateTime() + System.getProperty("line.separator"));
			
			if(minutes != null && !minutes.equals("") && seconds != null && !seconds.equals("")) {
				
				if((board.hud.getMinutes() > Integer.parseInt(minutes)) || (board.hud.getMinutes() == Integer.parseInt(minutes) && board.hud.getSeconds() > Integer.parseInt(seconds)))
					bufferedWriter.write(board.hud.minutesToString()	+ System.getProperty("line.separator")
										+ board.hud.secondsToString()	+ System.getProperty("line.separator")
										);
				
				else
					bufferedWriter.write(minutes	+ System.getProperty("line.separator")
										+ seconds	+ System.getProperty("line.separator")
										);
				
			}
			
			else
				bufferedWriter.write(board.hud.minutesToString()	+ System.getProperty("line.separator")
									+ board.hud.secondsToString()	+ System.getProperty("line.separator")
									);
			
			if(strikes != null && !strikes.equals("")) {
				
				if(board.hud.getStrikes() < Integer.parseInt(strikes))
					bufferedWriter.write(board.hud.getStrikes() + System.getProperty("line.separator"));
				
				else
					bufferedWriter.write(strikes + System.getProperty("line.separator"));
				
			}
			
			else
				bufferedWriter.write(board.hud.getStrikes() + System.getProperty("line.separator"));
			
			if(checks != null && !checks.equals("")) {
				
				if(board.hud.getChecks() > Integer.parseInt(checks))
					bufferedWriter.write(board.hud.getChecks() + System.getProperty("line.separator"));
				
				else
					bufferedWriter.write(checks + System.getProperty("line.separator"));
				
			}
			
			else
				bufferedWriter.write(board.hud.getChecks() + System.getProperty("line.separator"));
			
			for(int i = 0; i < size; i ++) {
				
				for(int j = 0; j < size; j ++) {
					
					if(board.puzzle.getBoard()[i][j])
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
		
		catch(IOException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	private void loadConfig() {
		
		try {
			
			File load = new File(System.getProperty("user.dir") + "/cp_config.ini");
			
			FileReader fileReader = new FileReader(load.getAbsoluteFile());
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			String line;
			int row = 0;
			
			while((line = bufferedReader.readLine()) != null) {
				
				if(row == 0) {
					
					if(line.equals("1"))
						background = true;
					
					else if(line.equals("0"))
						background = false;
					
				}
				
				else if(row == 1) {
					
					if(line.equals("1"))
						highlight = true;
					
					else if(line.equals("0"))
						highlight = false;
					
				}
				
				else if(row == 2)
					userName = line;
				
				else if(row == 3)
					checksEarned = Integer.parseInt(line);
				
				row ++;
				
			}
			
			bufferedReader.close();
			fileReader.close();
			
		}
		
		catch(IOException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	private void saveConfig() {
		
		try {
			
			File save = new File(System.getProperty("user.dir") + "/cp_config.ini");
			
			if(!save.exists())
				save.createNewFile();
			
			FileWriter fileWriter = new FileWriter(save.getAbsoluteFile());
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			
			if(background)
				bufferedWriter.write("1" + System.getProperty("line.separator"));
			
			else
				bufferedWriter.write("0" + System.getProperty("line.separator"));
			
			if(highlight)
				bufferedWriter.write("1" + System.getProperty("line.separator"));
			
			else
				bufferedWriter.write("0" + System.getProperty("line.separator"));
			
			if(userName != null && !userName.equals(""))
				bufferedWriter.write(userName);
			
			bufferedWriter.write(System.getProperty("line.separator") + Integer.toString(checksEarned));
			
			bufferedWriter.close();
			fileWriter.close();
			
		}
		
		catch(IOException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	private String getCurrentDateTime() {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
		
	}
	
	private void construct() {
		
		if(state == 1 || state == 5 || state == 7) {
			
			board = new Board(size, true);
			state = 3;
			
		}
		
		else if(state == 2) {
			
			board = new Board(size, false);
			state = 4;
			
		}
		
	}
	
	private void reset() {
		
		alpha = 0.0f;
		selection = 0;
		
		if(state == 1)
			selection = letter;
		
	}
	
}
