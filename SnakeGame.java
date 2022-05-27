package hw4;

import javax.swing.JFrame;

public class SnakeGame {
	public static void main(String args [])
	{
		JFrame frame = new JFrame("Snake Game");
		frame.setResizable(true);
		SnakeGamePanel panel = new SnakeGamePanel();
		frame.add(panel);
		frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
	}
}