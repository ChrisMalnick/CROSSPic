package main;

import javax.swing.JFrame;

public class Frame {
	
	public static JFrame frame;
	
	public static void main(String[] args) {
		
		frame = new JFrame("CROSSPic");
		frame.setSize(647, 510);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Panel());
		frame.setVisible(true);
		
	}

}
