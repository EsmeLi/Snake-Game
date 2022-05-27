package hw4;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import javax.swing.JOptionPane;

import javax.swing .*;

import java.util.ArrayList;
import java.util.Random;

public class SnakeGamePanel extends JPanel {
	private int dx, dy, fdx, fdy;
	private int direction = 0;
	private int inputD = 0;
	private int appleC = 0;
	private int pause = 0;
	private int DELAY = 250;
	private boolean game = false;
	private Timer timer;
	private Sequence apple;
	private ArrayList <Sequence> snake = new ArrayList <>();
	
	// a constructor for the game panel
	SnakeGamePanel()
	{
		setBackground(Color.BLUE);
		setPreferredSize(new Dimension (320, 320));
		addKeyListener(new DirectionListener());
		setFocusable(true);
		timer = new Timer(DELAY, new MovingListener());
		timer.start();
	}
	
	// an inner class sequence that stores 2D coordinates of apples and snake’s body
	private class Sequence
	{
		public int x;
		public int y;
		public Sequence(int a, int b)
		{
			x = a;
			y = b;
		}
	}
	
	// a class that that implements the interface KeyListener to keep track of the 
	// (final) pressed key during any delay time
	private class DirectionListener implements KeyListener
	{
		// set different change value for coordinate x and y in response to key pressed
		@Override
		public void keyPressed(KeyEvent e) 
		{
			switch(e.getKeyCode ())
			{
			case KeyEvent.VK_UP:
			inputD = 1; dx = 0; dy = -20;
			    break;
			case KeyEvent.VK_DOWN:
				inputD = -1; dx = 0; dy = 20;
				break;
			case KeyEvent.VK_LEFT:
				inputD = 2; dx = -20; dy = 0;
				break;
			case KeyEvent.VK_RIGHT:
				inputD = -2; dx = 20; dy = 0;
				break;
			case KeyEvent.VK_SPACE:
				pause = -100;
				break;
			}
		}
		public void keyReleased(KeyEvent e) 
		{}
		public void keyTyped(KeyEvent e) 
		{}
	}
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		// check if the game is on its initial stage
		if (!game)
		{
			Random r = new Random();
			int xc = r.nextInt(16);
			int yc = r.nextInt(16);
		    g.setColor(Color.RED);  
		    g.fillRect(xc*20,yc*20,20,20);
		    snake.add(new Sequence(xc*20,yc*20));
		    int xr = r.nextInt(16);
			int yr = r.nextInt(16);
			while (xr==xc && yr==yc)
			{
				xr = r.nextInt(16);
				yr = r.nextInt(16);
			}
		    g.setColor(Color.GREEN);  
		    g.fillRect(xr*20,yr*20,20,20);
		    apple = new Sequence(xr*20,yr*20);
		    game = true;
		}
		// paint the game after the snake starts to move
		else
		{
			g.setColor(Color.GREEN); 
			g.fillRect(apple.x,apple.y,20,20);
			for (Sequence s:snake)
			{
				g.setColor(Color.RED);  
			    g.fillRect(s.x,s.y,20,20);
			}
		}
	 }
	
	// methods that restart a new game by resetting timer and snake
	private void newGame()
	{
		game = false;
		inputD = -direction;
		fdx=0;
		fdy=0;
		snake.clear();
		appleC = 0;
		timer.restart();
		DELAY = 250;
	    timer.setDelay(DELAY);
	    
	}
	
	// methods that generate a new apple
	private void nextApple ()
	{
		int count, xa, ya;
		Random r = new Random();
		do
		{
			count = 0;
			xa = r.nextInt(16);
			ya = r.nextInt(16);
			for (Sequence s:snake)
			{
				if (s.x == xa*20 && s.y == ya*20)
				{
					count ++;
					break;
				}
			}
		}
		while (count > 0);
		apple = new Sequence(xa*20, ya*20);
			
	}
	
	// method with action that need to be implemented each time period
	private class MovingListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event){
			repaint();
			// pause if space is pressed
			if (pause==-100)
			{
				int a = JOptionPane.showConfirmDialog(null,
						"Pause, continue?",
		                "Game Paused",
		                JOptionPane.DEFAULT_OPTION,
		                JOptionPane.PLAIN_MESSAGE);  
				if(a==JOptionPane.OK_OPTION){  
				    pause = 0;
				}  
			}
			
			// check if the speed need to increase
			if (appleC == 4 && DELAY>50)
			{
				DELAY = DELAY - 20;
				timer.setDelay(DELAY);
				appleC = 0;
			}
			
			// check if the input direction is valid and manipulate the change of x and y
			if (inputD != -direction)
			{
				direction = inputD;
				fdx = dx;
				fdy = dy;
			}
			
			// check if the snake is going to eat an apple
			if (snake.get(0).x+fdx == apple.x && snake.get(0).y+fdy == apple.y)
			{
				appleC ++;
				snake.add(0, apple);
				nextApple();
			}
			
			// if nothing above happen, snake moves
			else
			{
				for (int i=snake.size()-1; i > 0; i--) 
				{
					snake.get(i).x = snake.get(i-1).x;
					snake.get(i).y = snake.get(i-1).y;
				}
				snake.set(0,new Sequence(snake.get(0).x+fdx, snake.get(0).y+fdy));
			}
			if (isGameOver())
			{
				int a = JOptionPane.showConfirmDialog(null,
		                "Game Over, New Game?",
		                "Game Message",
		                JOptionPane.DEFAULT_OPTION,
		                JOptionPane.PLAIN_MESSAGE);  
				if(a==JOptionPane.OK_OPTION){  
				    newGame();
				}  
			}
			repaint();
		}
	}
	
	// return a boolean of whether game is over
	private boolean isGameOver ()
	{
		if (snake.get(0).x>300 || snake.get(0).y>300 || snake.get(0).x<0 || snake.get(0).y<0)
		{
			return true;
		}
		for (int i=1; i<snake.size(); i++)
		{
			if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y)
			{
				return true;
			}
		}
		return false;
	}

}
