package state.states;

import net.client.ClientThread;
import net.packet.Packet00Login;
import net.server.ServerThread;
import state.State;
import state.StateManager;

import javax.swing.*;
import java.awt.*;

public class ConnectState extends State
{
	private StateManager stateManager;
	private static ClientThread clientThread;
	private static ServerThread serverThread;

	public ConnectState(StateManager stateManager)
	{
		this.stateManager = stateManager;
		init();
	}

	public void init() //TODO Refuse all spaces/blank usernames
	{
		Packet00Login packet00Login = new Packet00Login(JOptionPane.showInputDialog("Enter a Username"));

		if(JOptionPane.showConfirmDialog(null, "Do you want to run the server?") == JOptionPane.YES_OPTION)
		{
			serverThread = new ServerThread();
			serverThread.start();
		}

		clientThread = new ClientThread("localhost");
		clientThread.start();
		packet00Login.writeData(clientThread);
		stateManager.setState(StateManager.PLAY_STATE);
	}

	public void update()
	{

	}

	public void draw(Graphics2D g2d)
	{

	}
	public void keyPressed(int key)
	{

	}

	public void keyReleased(int key)
	{

	}

	public static ClientThread getClientThread()
	{
		return clientThread;
	}

	public static ServerThread getServerThread()
	{
		return serverThread;
	}
}
