package components;

import java.awt.*;
import java.net.InetAddress;

public class PlayerMP extends Player
{
	private InetAddress ipAddress;
	private int port;
	private String username;
	private boolean local;

	public PlayerMP(int startingColumn, int startingRow, int tileLength, int direction, InetAddress ipAddress,
			int port, String username, boolean local, Color color)
	{
		super(startingColumn, startingRow, tileLength, direction, color);
		this.ipAddress = ipAddress;
		this.port = port;
		this.username = username;
		this.local = local;
	}

	public InetAddress getIpAddress()
	{
		return ipAddress;
	}
	public int getPort()
	{
		return port;
	}
	public void setIpAddress(InetAddress ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public void setPort(int port)
	{
		this.port = port;
	}
	public String getUsername()
	{
		return username;
	}
	public boolean isLocal()
	{
		return local;
	}
}
