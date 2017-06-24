package net.server;

import components.PlayerMP;
import net.client.ClientThread;
import net.packet.*;
import state.states.ConnectState;
import state.states.PlayState;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread
{
	private DatagramSocket socket;
	private List<PlayerMP> players = new ArrayList<>();

	public ServerThread()
	{
		try
		{
			this.socket = new DatagramSocket(1331);
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
				handleLogin(new Packet00Login(data), address, port);
				break;
			case DISCONNECT:
				handleDisconnect(new Packet01Disconnect(data), address, port);
				break;
			case NEW_PLAYER:
				break;
			case MOVE:
				break;
			case GENERATE_FOOD:
				break;
			case PAUSE:
				System.out.println("Received the pause packet");
				Packet05Pause packetPause = new Packet05Pause(data);
				packetPause.writeData(this);
				break;
			case ERROR_PLAYERS:

				break;
		}
	}

	private void handleLogin(Packet00Login packet, InetAddress address, int port)
	{
		System.out.println(packet.getUsername() + " has connected.");
		PlayerMP player = PlayState.createNewPlayer(ConnectState.getClientThread().getPlayers().size(),
				address, port, packet.getUsername(), false);
		if(player != null) addPlayer(player, packet);

		else
		{
			Packet06ErrorPlayers errorPacket = new Packet06ErrorPlayers();
			sendData(errorPacket.getData(), address, port);

			System.out.println(packet.getUsername() + " cannot join due to too many players.");
		}
	}

	private void addPlayer(PlayerMP connectingPlayer, Packet00Login loginPacket)
	{
		//Check if the connection already exists, if it does, make sure it is correct
		boolean alreadyConnected = false;
		for(PlayerMP playerMP : players)
		{
			if(playerMP.getUsername().equalsIgnoreCase(connectingPlayer.getUsername()))
			{
				if(playerMP.getIpAddress() == null)
				{
					playerMP.setIpAddress(connectingPlayer.getIpAddress());
				}

				if(playerMP.getPort() == -1)
				{
					playerMP.setPort(connectingPlayer.getPort());
				}

				alreadyConnected = true;
			}
		}

		//If the connection does not exist, then add the new
		if(!alreadyConnected)
		{
			players.add(connectingPlayer);
		}

		for(int i = 0; i < players.size(); i++)
		{
			PlayerMP playerMP = players.get(i);

			boolean isClientPlayer = false;
			if(i == players.size() - 1) isClientPlayer = true;

			if(i != players.size() - 1)
			{
				//Tell the old client that the new player exists
				//On the last iteration, this should not happen, as it is
				Packet02NewPlayer packet = new Packet02NewPlayer(loginPacket.getUsername(), isClientPlayer);
				sendData(packet.getData(), playerMP.getIpAddress(), playerMP.getPort());
			}

			//Tell the new client that the iterated player exists
			//The last one sent should tell the client that it is itself
			Packet02NewPlayer packet = new Packet02NewPlayer(playerMP.getUsername(), isClientPlayer);
			sendData(packet.getData(), connectingPlayer.getIpAddress(), connectingPlayer.getPort());
		}
	}

	private void handleDisconnect(Packet01Disconnect packet, InetAddress address, int port)
	{
		System.out.println(packet.getUsername() + " has left.");
		removePlayer(packet);
	}

	private void removePlayer(Packet01Disconnect disconnectPacket)
	{
		players.remove(getPlayerMP(disconnectPacket.getUsername()));
		disconnectPacket.writeData(this);
	}

	private PlayerMP getPlayerMP(String username)
	{
		for(PlayerMP player : players)
		{
			if(player.getUsername().equals(username)) return player;
		}

		return null;
	}

	private void sendData(byte[] data, InetAddress ipAddress, int port)
	{
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try
		{
			socket.send(packet);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data)
	{
		for(PlayerMP playerMP : players)
		{
			sendData(data, playerMP.getIpAddress(), playerMP.getPort());
		}
	}
}
