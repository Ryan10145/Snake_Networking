package net.packet;

import net.client.ClientThread;
import net.server.ServerThread;

public class Packet03Move extends Packet
{
    private String username;
    private int direction;
    private int col;
    private int row;

    public Packet03Move(byte[] data)
    {
        super(03);
        this.direction = Integer.parseInt(readData(data).substring(0, 1));
        String[] parts = readData(data).substring(1).split(":");
        this.col = Integer.parseInt(parts[0]);
        this.row = Integer.parseInt(parts[1]);
        this.username = parts[2];
    }

    public Packet03Move(String username, int direction, int col, int row)
    {
        super(03);
        this.username = username;
        this.direction = direction;
        this.col = col;
        this.row = row;
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
        return ("03" + this.getDirection() + this.col + ":" + this.row + ":" + this.username).getBytes();
    }

    public String getUsername()
    {
        return username;
    }

    public int getDirection()
    {
        return direction;
    }

    public int[] getCoordinates()
    {
        return new int[] {col, row};
    }
}
