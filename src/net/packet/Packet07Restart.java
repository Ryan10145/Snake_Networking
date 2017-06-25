package net.packet;

import net.client.ClientThread;
import net.server.ServerThread;

public class Packet07Restart extends Packet
{
    public Packet07Restart(byte[] data)
    {
        super(07);
    }

    public Packet07Restart()
    {
        super(07);
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
        return ("07").getBytes();
    }
}
