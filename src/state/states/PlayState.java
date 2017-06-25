package state.states;

import components.Player;
import components.PlayerMP;
import main.GamePanel;
import net.client.ClientThread;
import state.State;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.InetAddress;

public class PlayState extends State
{
	private static int[] foodCoordinates;
	private static int tileLength;
	private static boolean paused;

	public PlayState()
	{
		init();
	}

	public void init()
	{
		tileLength = 16;

		foodCoordinates = new int[2];
		generateFood();

		paused = true;
	}

	public void update()
	{
		if(ConnectState.getClientThread().getPlayers().size() > 1 && !paused)
		{
			for(Player player : ConnectState.getClientThread().getPlayers())
			{
				if(!player.isGameOver())
				{
					player.update(ConnectState.getClientThread().getPlayers());
					if(player.hasCoordinates(foodCoordinates))
					{
						player.addToLength(1);
						generateFood();
					}
				}
			}
		}
	}

	private void generateFood()
	{
		int column = (int) (Math.random() * (GamePanel.WIDTH / tileLength));
		int row = (int) (Math.random() * (GamePanel.HEIGHT / tileLength));

		for(Player player : ConnectState.getClientThread().getPlayers())
		{
			if(player.hasCoordinates(new int[] {column, row}))
			{
				generateFood();
				return;
			}
		}

		ConnectState.getClientThread().generateFood(column, row);
	}

	public void draw(Graphics2D g2d)
	{
		g2d.setColor(Color.BLUE);
		g2d.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		for(int i = 0; i < ConnectState.getClientThread().getPlayers().size(); i++)
			//TODO Set different colors for the players, and set their score to be the same color
		{
			Player player = ConnectState.getClientThread().getPlayers().get(i);

			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("default", Font.BOLD, 40));

			int length = g2d.getFontMetrics().stringWidth(Integer.toString(player.getLength()));
			g2d.drawString(Integer.toString(player.getLength()), (GamePanel.WIDTH - length + (i - 2) * 150) / 2, 50);

			player.draw(g2d, Color.ORANGE, Color.YELLOW);
		}

		g2d.setColor(Color.GREEN);
		g2d.fillRect(foodCoordinates[0] * tileLength, foodCoordinates[1] * tileLength, tileLength, tileLength);

		if(paused)
		{
			g2d.setColor(new Color(255, 255, 255, 50));
			g2d.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		}
	}

	public void keyPressed(int key)
	{
		if(!paused)
		{
			for(PlayerMP player : ConnectState.getClientThread().getPlayers())
			{
				if(player.isLocal())
				{
					player.keyPressed(key);
				}
				else player.keyPressed(key);
			}
		}
		if(key == KeyEvent.VK_ESCAPE)
		{
			ConnectState.getClientThread().setPaused(!paused);
		}
		else if(key == KeyEvent.VK_SPACE)
		{
			ConnectState.getClientThread().restart();
		}
	}

	public void keyReleased(int key)
	{

	}

	public static PlayerMP createNewPlayer(int index, InetAddress address, int port, String username, boolean local)
	{
		PlayerMP player;
		switch(index)
		{
		case 0:
			player = new PlayerMP(7, 7, tileLength, 1,
					address, port, username, local);
			break;
		case 1:
			player = new PlayerMP(GamePanel.WIDTH / tileLength - 7, 7, tileLength, 1,
					address, port, username, local);
			break;
		case 2:
			player = new PlayerMP(GamePanel.WIDTH / tileLength - 7, GamePanel.HEIGHT / tileLength - 7, tileLength, 1,
					address, port, username, local);
			break;
		case 3:
			player = new PlayerMP(7, GamePanel.HEIGHT / tileLength - 7, tileLength, 1,
					address, port, username, local);
			break;
		default:
			player = null;
			break;
		}

		return player;
	}

	public static void setPaused(boolean paused)
	{
		PlayState.paused = paused;
	}

	public static void setFoodCoordinates(int column, int row)
	{
		foodCoordinates[0] = column;
		foodCoordinates[1] = row;
	}

	public static void restart()
	{
		for(PlayerMP player : ConnectState.getClientThread().getPlayers())
		{
			player.setGameOver(false);
			player.init();
		}
	}
}
