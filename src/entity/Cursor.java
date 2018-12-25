package entity;

import java.awt.Graphics2D;

import main.Panel;

public class Cursor {
	
	private int col;
	private int row;
	
	private int upperBound;
	
	public Cursor(int s) {
		
		super();
		upperBound = s;
		
	}
	
	public int getCol() {
		
		return col;
		
	}
	
	public int getRow() {
		
		return row;
		
	}
	
	public void moveUp() {
		
		row --;
		
		if(row < 0)
			row = upperBound - 1;
		
	}
	
	public void moveDown() {
		
		row ++;
		
		if(row > upperBound - 1)
			row = 0;
		
	}
	
	public void moveLeft() {
		
		col --;
		
		if(col < 0)
			col = upperBound - 1;
		
	}
	
	public void moveRight() {
		
		col ++;
		
		if(col > upperBound - 1)
			col = 0;
		
	}
	
	public void draw(Graphics2D G2D) {
		
		G2D.setColor(Panel.getSelectionColor());
		
		if(Panel.highlight) {
			
			G2D.fillRect(col * 16, 0, 16, (upperBound + upperBound / 2) * 16);
			G2D.fillRect(0, row * 16, (upperBound + upperBound / 2) * 16, 16);
			
			G2D.setColor(Panel.getBackgroundColor());
			G2D.fillRect(col * 16, row * 16, 16, 16);
			
		}
		
		else {
			
			G2D.fillRect(col * 16 + 1, upperBound * 16 + 1, 15, (upperBound / 2) * 16 - 1);
			G2D.fillRect(upperBound * 16 + 1, row * 16 + 1, (upperBound / 2) * 16 - 1, 15);
			
			G2D.drawRect(col * 16, row * 16, 16, 16);
			
		}
		
	}
	
}
