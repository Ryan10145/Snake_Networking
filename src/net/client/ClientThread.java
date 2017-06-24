package net.client;

import components.PlayerMP;
import net.packet.*;
import state.states.PlayState;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;

public class ClientThread extends Thread
{
	private InetAddress ipAddress;
	private DatagramSocket socket;

	private ArrayList<PlayerMP> players = new ArrayList<>();
	private PlayerMP currentPlayer;

	public ClientThread(String ipAddress)
	{
		try
		{
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void run()
	{
		//noinspection InfiniteLoopStatement
		while(true)
		{
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);

			try
			{
				socket.receive(packet);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}

			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
		}
	}

	private void parsePacket(byte[] data, InetAddress address, int port)
	{
		String message = new String(data).trim();
		Packet.PacketType type = Packet.lookUpPacket(message.substring(0, 2));
		switch(type)
		{
			case INVALID:

				break;
			case LOGIN:

				break;
			case DISCONNECT:
				handleDisconnect(new Packet01Disconnect(data));
				break;
			case NEW_PLAYER:
				handleNewPlayer(new Packet02NewPlayer(data), address, port);
				break;
			case MOVE:

				break;
			case GENERATE_FOOD:

				break;
			case PAUSE:
				Packet05Pause packet = new Packet05Pause(data);
				PlayState.setPaused(packet.isPaused());
				break;
			case ERROR_PLAYERS:
				JOptionPane.showMessageDialog(null, "Unable to connect, too many players",
						"Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
				break;
		}
	}

	private void handleDisconnect(Packet01Disconnect disconnectPacket)
	{
		System.out.println(disconnectPacket.getUsername() + " has left.");
		removePlayer(disconnectPacket.getUsername());
	}

	private void removePlayer(String username)
	{
		Iterator iterator = players.iterator();
		while(iterator.hasNext())
		{
			PlayerMP player = (PlayerMP) iterator.next();
			if(player.getUsername().equals(username))
			{
				iterator.remove();
				break;
			}
		}
	}

	private void handleNewPlayer(Packet02NewPlayer packet, InetAddress address, int port)
	{
		System.out.println(packet.getUsername() + " has joined.");
		int playerIndex = -1;
		for(int i = 0; i < 4; i++)
		{
			if(players.size() <= i)
			{
				playerIndex = i;
				break;
			}
			else if(players.get(i) == null)
			{
				playerIndex = i;
				break;
			}
		}
		PlayerMP player = PlayState.createNewPlayer(playerIndex, address, port, packet.getUsername(),
				packet.isCurrentPlayer());
		if(player != null) players.add(player);

		if(packet.isCurrentPlayer()) currentPlayer = player;
	}

	public void sendData(byte[] data)
	{
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		try
		{
			socket.send(packet);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public ArrayList<PlayerMP> getPlayers()
	{
		return players;
	}

	public PlayerMP getCurrentPlayer()
	{
		return currentPlayer;
	}

	public void setPaused(boolean isPaused)
	{
		Packet05Pause pausePacket = new Packet05Pause(isPaused);
		pausePacket.writeData(this);
	}
}
