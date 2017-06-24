package net.packet;

import net.client.ClientThread;
import net.server.ServerThread;

public class Packet02NewPlayer extends Packet
{
	private String username;
	private boolean isCurrentPlayer;

	public Packet02NewPlayer(byte[] data)
	{
		super(02);
		this.isCurrentPlayer = (readData(data).substring(0, 1).equals("1"));
		this.username = readData(data).substring(1);
	}

	public Packet02NewPlayer(String username, boolean isCurrentPlayer)
	{
		super(02);
		this.username = username;
		this.isCurrentPlayer = isCurrentPlayer;
	}

	public void writeData(ClientThread clientThread)
	{
		clientThread.sendData(getData());
	}

	public void writeData(ServerThread serverThread)
	{
		serverThread.sendDataToAllClients(getData());
	}

	public byte[] getData()
	{
		return ("02" + (this.isCurrentPlayer ? "1" : "0") + this.username).getBytes();
	}

	public String getUsername()
	{
		return username;
	}

	public boolean isCurrentPlayer()
	{
		return isCurrentPlayer;
	}
}
