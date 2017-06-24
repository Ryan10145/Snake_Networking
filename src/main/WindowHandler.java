package main;

import net.packet.Packet01Disconnect;
import state.states.ConnectState;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowHandler implements WindowListener
{
    WindowHandler()
    {

    }

    public void windowOpened(WindowEvent e)
    {

    }

    public void windowClosing(WindowEvent e)
    {
        Packet01Disconnect disconnectPacket = new Packet01Disconnect(ConnectState.getClientThread().getCurrentPlayer().getUsername());
        disconnectPacket.writeData(ConnectState.getClientThread());
    }

    public void windowClosed(WindowEvent e)
    {

    }

    public void windowIconified(WindowEvent e)
    {

    }

    public void windowDeiconified(WindowEvent e)
    {

    }

    public void windowActivated(WindowEvent e)
    {

    }

    public void windowDeactivated(WindowEvent e)
    {

    }
}
