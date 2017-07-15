package net.packet;

import net.client.ClientThread;
import net.server.ServerThread;

public class Packet11Score extends Packet
{
    private String username;

    public Packet11Score(byte[] data)
    {
        super(11);
        this.username = readData(data);
    }

    public Packet11Score(String username)
    {
        super(11);
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
        return ("11" + this.username).getBytes();
    }

    public String getUsername()
    {
        return username;
    }
}
