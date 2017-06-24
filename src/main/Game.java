package main;

import javax.swing.*;

public class Game
{
	public static JFrame frame;

	public static void main(String[] args)
	{
		frame = new JFrame("Snake");
		frame.addWindowListener(new WindowHandler());
		frame.setContentPane(new GamePanel());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
