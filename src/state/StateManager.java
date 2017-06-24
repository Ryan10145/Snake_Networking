package state;

import state.states.ConnectState;
import state.states.PlayState;

import java.awt.*;

public class StateManager
{
	private static final int NUMBER_OF_STATES = 2;

	public static final int CONNECT_STATE = 0;
	public static final int PLAY_STATE = 1;

	private final State[] states;
	private int currentState;

	public StateManager()
	{
		states = new State[NUMBER_OF_STATES];
		currentState = CONNECT_STATE;
		loadState(currentState);
	}

	public void update()
	{
		if(states[currentState] != null) states[currentState].update();
	}

	public void draw(Graphics2D g2d)
	{
		if(states[currentState] != null) states[currentState].draw(g2d);
	}

	private void loadState(int state)
	{
		if(state == CONNECT_STATE) states[state] = new ConnectState(this);
		if(state == PLAY_STATE) states[state] = new PlayState();
	}

	public void setState(int state)
	{
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}

	private void unloadState(int state)
	{
		states[state] = null;
	}

	public void keyPressed(int key)
	{
		states[currentState].keyPressed(key);
	}

	public void keyReleased(int key)
	{
		states[currentState].keyReleased(key);
	}
}
