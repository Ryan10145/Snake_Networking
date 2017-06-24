package net.packet;

import net.client.ClientThread;
import net.server.ServerThread;

public abstract class Packet
{
	/* Future Types of Packets:
	 * LOGIN
	 * DISCONNECT
	 * NEW_PLAYER
	 * MOVE
	 * GENERATE_FOOD
	 *
	 */
	public enum PacketType
	{
		INVALID(-1), LOGIN(00), DISCONNECT(01), NEW_PLAYER(02);

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

	public String readData(byte[] data)
	{
		String message = new String(data).trim();
		return message.substring(2);
	}

	public abstract byte[] getData();

	public static PacketType lookUpPacket(int packetId)
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
