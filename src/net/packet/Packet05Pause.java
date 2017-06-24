package net.packet;

import net.client.ClientThread;
import net.server.ServerThread;

public class Packet05Pause extends Packet
{
    private boolean isPaused;

    public Packet05Pause(byte[] data)
    {
        super(05);
        this.isPaused = Integer.parseInt(readData(data)) == 0;
    }

    public Packet05Pause(boolean isPaused)
    {
        super(05);
        this.isPaused = isPaused;
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
        return ("05" + (isPaused ? "1" : "0")).getBytes();
    }

    public boolean isPaused()
    {
        return isPaused;
    }
}
