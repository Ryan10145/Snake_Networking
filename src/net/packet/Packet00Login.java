package net.packet;

import net.client.ClientThread;
import net.server.ServerThread;

public class Packet00Login extends Packet
{
	private String username;

	public Packet00Login(byte[] data)
	{
		super(00);
		this.username = readData(data);
	}

	public Packet00Login(String username)
	{
		super(00);
		this.username = username;
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
		return ("00" + this.username).getBytes();
	}

	public String getUsername()
	{
		return username;
	}
}
