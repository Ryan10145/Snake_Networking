package net.packet;

import net.client.ClientThread;
import net.server.ServerThread;

public class Packet06ErrorPlayers extends Packet
{
    public Packet06ErrorPlayers(byte[] data)
    {
        super(06);
    }

    public Packet06ErrorPlayers()
    {
        super(06);
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
        return ("06").getBytes();
    }
}
