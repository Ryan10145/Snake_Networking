package net.client;

import components.Player;
import components.PlayerMP;
import net.packet.Packet;
import net.packet.Packet00Login;
import net.packet.Packet02NewPlayer;
import state.states.PlayState;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class ClientThread extends Thread
{
	private InetAddress ipAddress;
	private DatagramSocket socket;

	private static ArrayList<PlayerMP> players = new ArrayList<>();

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

			break;
		case NEW_PLAYER:
			handleNewPlayer(new Packet02NewPlayer(data), address, port);
			break;
		}
	}

	private void handleNewPlayer(Packet02NewPlayer packet, InetAddress address, int port)
	{
		System.out.println(packet.getUsername() + " has joined.");
		PlayerMP player = PlayState.createNewPlayer(players.size(), address, port, packet.getUsername(),
				packet.isCurrentPlayer());
		System.out.println(packet.isCurrentPlayer());
		if(player != null) players.add(player);
		else
		{
			//TODO Send Error Packet
			System.out.println(packet.getUsername() + " cannot join due to too many players.");
		}
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

	public static ArrayList<PlayerMP> getPlayers()
	{
		return players;
	}
}
