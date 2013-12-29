package model;

import java.awt.Color;
import java.io.Serializable;

public class Board implements Serializable{
	
	private class Square implements Serializable{
		private static final long serialVersionUID = 1L;
		private boolean filled;
		private boolean exed;
		private Color color; 
		
		public Square(){
			this.filled = false;  //A square is always not filled when it is made
			this.exed = false; 
			this.color = Color.WHITE;
		}
		
		//returns true if the square is filled
		boolean isFilled(){
			return this.filled;
		}

		void flipSquare(){
			this.filled = !this.filled; 
		}

		void fillSquare(){
			this.filled = true;
		}
		
		void unfillSquare(){
			this.filled = false;
		}

		boolean isExed(){
			return this.exed; 
		}
		
		void flipExed(){ 
			this.exed = !this.exed; 
		}
		
		void markExed(){
			this.exed = true; 
		}
		
		void unmarkExed(){ 
			this.exed = false; 
		}
		
		void setColor(Color color){
			this.color = color; 
		}
		
		Color getColor(){
			return this.color; 
		}
		
		void clearColor(){
			this.color = Color.WHITE; 
		}
		
		void clearSquare(){
			this.filled = false; 
			this.exed = false; 
			this.color = Color.WHITE; 
		}
		
	}
	
	private static final long serialVersionUID = 2L;
	private String name;
	private Square[][] board;
	private int size;
	private int[][] rightGuide;  
	private int[][] bottomGuide;

	public Board(String name, int size){ 
		this.name = name; 
		this.size = size; 
		this.board = new Square[size][size];
		//initialize the board
		for(int col=0; col<this.size; col++){
			for(int row=0; row<this.size; row++){
				board[col][row] = new Square(); 
			}
		}
	}

	public String getName(){ 
		return this.name;
	}
	
	public int getSize(){
		return this.size; 
	}
	
	public boolean isSquareFilled(int col, int row){ 
		return board[col][row].isFilled(); 
	}
	
	public boolean isSquareExed(int col, int row){
		return board[col][row].isExed(); 
	}
	
	public void flipSquareFilled(int col, int row){
		board[col][row].flipSquare(); 	
	}

	public void flipSquareExed(int col, int row){
		board[col][row].flipExed(); 
	}
	
	public void fillSquare(int col, int row){
		board[col][row].fillSquare(); 
	}
	
	public void unfillSquare(int col, int row){
		board[col][row].unfillSquare(); 
	}
	
	public void exSquare(int col, int row){
		board[col][row].markExed(); 
	}
	
	public void unexSquare(int col, int row){
		board[col][row].unmarkExed(); 
	}
	
	public void setSquareColor(int col, int row, Color color){
		board[col][row].setColor(color); 
	}
	
	public Color getSquareColor(int col, int row){
		return board[col][row].getColor(); 
	}
	public void clearSquareColor(int col, int row){
		board[col][row].clearColor(); 
	}
	
	public void clearBoard(){ 
		for(int y=0; y< size; y++){
			for(int x=0; x< size; x++){
				board[y][x].clearSquare();
			}
		}
	}
	
	public void setRightGuide(){ 
		this.rightGuide = getRightGuide(this); 
	}
	
	public void setBottomGuide(){
		this.bottomGuide = getBottomGuide(this); 
	}
	
	public int[][] getRightGuide(){
		return this.rightGuide; 
	}
	
	public int[][] getBottomGuide(){
		return this.bottomGuide;
	}

	@Override
	public String toString(){
		return this.name; 
	}
	
	private static int[][] getRightGuide(Board board){ 
		int[][] guide = new int[board.size/2][board.size];
		int counter = 0; 
		int guideCol = 0; 
		
		for(int row = 0; row < guide[0].length; row++){
			for(int col = 0; col < guide.length; col++){
				if(board.isSquareFilled(col, row)){
					counter++; 
				}
				else{
					if(counter != 0){
						guide[guideCol][row] = counter; 
						counter = 0;
						guideCol++;
					}
				}
			}
			if(counter != 0){
				guide[guideCol][board.size] = counter; 
				counter = 0; 
				guideCol = 0; 
			}
		}

		return guide;
	}
	
	private static int[][] getBottomGuide(Board board){
		int[][]  guide = new int[board.size][board.size/2]; 
		int counter = 0; 
		int guideRow = 0; 
		
		for(int col = 0; col < guide.length; col++){ 
			for(int row = 0; row < guide[col].length; row++){
				if(board.isSquareFilled(col, row)){
					counter++; 
				}
				else{ 
					if(counter != 0){ 
						guide[col][guideRow] = counter; 
						counter = 0; 
						guideRow++; 
					}
				}
			}
			if(counter != 0){
				guide[col][guideRow] = counter; 
				counter = 0; 
				guideRow = 0; 
			}
		}
		return guide; 
	}
}
