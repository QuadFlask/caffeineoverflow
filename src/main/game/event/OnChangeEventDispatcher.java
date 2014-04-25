package main.game.event;

import java.util.ArrayList;
import java.util.List;

import main.game.event.OnChangeEvent.CONTROLLER;

public class OnChangeEventDispatcher {
	private List<OnChangeEventListener> listeners = new ArrayList<OnChangeEventListener>();
	public static final OnChangeEvent RETURN_TO_MENU = new OnChangeEvent(CONTROLLER.MENU, 0);
	
	public void dispatch(OnChangeEvent e) {
		for (OnChangeEventListener listener : listeners)
			listener.onChange(e);
	}
	
	public void addOnChangeListener(OnChangeEventListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(OnChangeEventListener listener) {
		listeners.remove(listener);
	}
	
	public void removeAll(OnChangeEventListener listener) {
		listeners.clear();
	}
}