package net.packet;

import net.client.ClientThread;
import net.server.ServerThread;

public abstract class Packet
{
	public enum PacketType
	{
		INVALID(-1), LOGIN(00), DISCONNECT(01), NEW_PLAYER(02), MOVE(03), GENERATE_FOOD(04),
			PAUSE(05), ERROR_PLAYERS(06), RESTART(07);

		private int packetId;

		PacketType(int packetId)
		{
			this.packetId = packetId;
		}

		public int getPacketId()
		{
			return packetId;
		}
	}

	private byte packetId;

	public Packet(int packetId)
	{
		this.packetId = (byte) packetId;
	}

	public abstract void writeData(ClientThread clientThread);
	public abstract void writeData(ServerThread serverThread);

	String readData(byte[] data)
	{
		String message = new String(data).trim();
		if(message.length() > 2) return message.substring(2);
		else return "";
	}

	public abstract byte[] getData();

	private static PacketType lookUpPacket(int packetId)
	{
		for(PacketType packetType : PacketType.values())
		{
			if(packetType.getPacketId() == packetId) return packetType;
		}

		return PacketType.INVALID;
	}

	public static PacketType lookUpPacket(String packetId)
	{
		try
		{
			return lookUpPacket(Integer.parseInt(packetId));
		}
		catch(NumberFormatException e)
		{
			return PacketType.INVALID;
		}
	}
}
