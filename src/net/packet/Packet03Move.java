package net.packet;

import net.client.ClientThread;
import net.server.ServerThread;

public class Packet03Move extends Packet
{
    private String username;
    private int direction;

    public Packet03Move(byte[] data)
    {
        super(03);
        this.direction = Integer.parseInt(readData(data).substring(0, 1));
        this.username = readData(data).substring(1);
    }

    public Packet03Move(String username, int direction)
    {
        super(03);
        this.username = username;
        this.direction = direction;
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
        return ("03" + this.getDirection() + this.username).getBytes();
    }

    public String getUsername()
    {
        return username;
    }

    public int getDirection()
    {
        return direction;
    }
}
