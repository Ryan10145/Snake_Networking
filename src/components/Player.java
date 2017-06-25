package components;

import main.GamePanel;
import state.states.ConnectState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Player
{
	private LinkedList<int[]> segments;
	private int length;
	private int direction;

	private int initialColumn;
	private int initialRow;
	private int tileLength;
	private int initialDirection;

	private int frame;

	private boolean gameOver;

	Player(int startingColumn, int startingRow, int tileLength, int direction)
	{
		this.initialColumn = startingColumn;
		this.initialRow = startingRow;
		this.tileLength = tileLength;
		this.initialDirection = direction;

		init();
	}

	public void init()
	{
		segments = new LinkedList<>();
		segments.add(new int[] {initialColumn, initialRow});

		length = segments.size();
		this.direction = initialDirection;

		frame = 0;

		gameOver = false;
	}

	public void update(ArrayList<PlayerMP> players)
	{
		if(frame % 5 == 0 && !gameOver)
		{
			int[] headCoordinatesCopy = segments.get(0).clone();
			switch(direction)
			{
			case 0:
				if(headCoordinatesCopy[1]-- > 0) break;
				gameOver = true;
				break;
			case 1:
				if(headCoordinatesCopy[0]++ < (GamePanel.WIDTH / tileLength) - 1) break;
				gameOver = true;
				break;
			case 2:
				if(headCoordinatesCopy[1]++ < (GamePanel.HEIGHT / tileLength) - 1) break;
				gameOver = true;
				break;
			case 3:
				if(headCoordinatesCopy[0]-- > 0) break;
				gameOver = true;
				break;
			}

			if(hasCoordinates(headCoordinatesCopy)) gameOver = true;
			for(Player otherPlayer : players)
			{
				if(!otherPlayer.equals(this))
				{
					if(otherPlayer.hasCoordinates(headCoordinatesCopy)) gameOver = true;
				}
			}

			if(!gameOver)
			{
				if(segments.size() == length)
				{
					segments.add(0, headCoordinatesCopy);
					segments.removeLast();
				}
				else
				{
					segments.add(0, headCoordinatesCopy);
				}
			}
		}

		frame++;
	}

	public void draw(Graphics2D g2d, Color segmentColor, Color headColor)
	{
		for(int[] coordinates : segments)
		{
			if(!gameOver) g2d.setColor(segmentColor);
			else g2d.setColor(segmentColor.darker());
			g2d.fillRect(coordinates[0] * tileLength, coordinates[1] * tileLength,
					tileLength, tileLength);
		}

		if(!gameOver) g2d.setColor(headColor);
		else g2d.setColor(headColor.darker());
		g2d.fillRect(getHead()[0] * tileLength, getHead()[1] * tileLength,
				tileLength, tileLength);
	}

	public void keyPressed(int key) //0 for arrows, 1 for wasd, 2 for other client
	{
		switch(key)
		{
		case KeyEvent.VK_UP:
			ConnectState.getClientThread().setDirection(0);
			break;
		case KeyEvent.VK_RIGHT:
			ConnectState.getClientThread().setDirection(1);
			break;
		case KeyEvent.VK_DOWN:
			ConnectState.getClientThread().setDirection(2);
			break;
		case KeyEvent.VK_LEFT:
			ConnectState.getClientThread().setDirection(3);
			break;
		}
	}

	public boolean hasCoordinates(int[] coordinates)
	{
		for(int[] segment : segments)
		{
			if(Arrays.equals(segment, coordinates))
			{
				return true;
			}
		}

		return false;
	}

	public void setDirection(int direction)
	{
		this.direction = direction;
	}

	public void addToLength(int addend)
	{
		length += addend;
	}

	private int[] getHead()
	{
		return segments.get(0).clone();
	}

	public int getLength()
	{
		return length;
	}

	public boolean isGameOver()
	{
		return gameOver;
	}

	public void setGameOver(boolean gameOver)
	{
		this.gameOver = gameOver;
	}
}
