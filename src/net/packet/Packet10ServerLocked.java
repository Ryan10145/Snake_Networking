package net.packet;

import net.client.ClientThread;
import net.server.ServerThread;

public class Packet10ServerLocked extends Packet
{
    public Packet10ServerLocked(byte[] data)
    {
        super(10);
    }

    public Packet10ServerLocked()
    {
        super(10);
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
        return ("10").getBytes();
    }
}
