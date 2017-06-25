package net.packet;

import net.client.ClientThread;
import net.server.ServerThread;

public class Packet04GenerateFood extends Packet
{
    private int column;
    private int row;

    public Packet04GenerateFood(byte[] data)
    {
        super(04);
        String[] information = readData(data).split(":");
        this.column = Integer.parseInt(information[0]);
        this.row = Integer.parseInt(information[1]);
    }

    public Packet04GenerateFood(int column, int row)
    {
        super(04);
        this.column = column;
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
        return ("04" + this.column + ":" + this.row).getBytes();
    }

    public int[] getCoordinates()
    {
        return new int[] {column, row};
    }
}
