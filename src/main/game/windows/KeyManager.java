package main.game.windows;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KeyManager implements KeyListener {
	public static final int KEY_DELAY = 0;
	public static final int KEY_FIRST_DELAY = 0;

	private final ArrayList<Integer> _keyCodes = new ArrayList<Integer>(16);

	private static boolean[] keyIsFirst = new boolean[256];
	private static int[] keyDelayList = new int[256];
	private static int[] keyGapList = new int[256];

	private ArrayList<KeyPressingCallback> callbackList;
	private HashMap<Integer, Boolean> pressed;

	public KeyManager() {
		callbackList = new ArrayList<KeyPressingCallback>();
		pressed = new HashMap<>();
		clearPressed();
	}

	public synchronized void refresh() {
		_keyCodes.clear();

		for (Integer keyCode : pressed.keySet()) {
			if (keyCode > 256) return;
			if (pressed.get(keyCode) && keyDelayList[keyCode] >= keyGapList[keyCode]) {
				keyDelayList[keyCode] = 0;

				if (keyGapList[keyCode] == 0) keyGapList[keyCode] = KEY_FIRST_DELAY;
				else keyGapList[keyCode] = KEY_DELAY;

				_keyCodes.add(keyCode);
			}
		}

		if (_keyCodes.size() > 0) for (KeyPressingCallback c : new ArrayList<KeyPressingCallback>(callbackList))
			c.onPressing(_keyCodes);
		else if (_keyCodes.size() == 0) for (KeyPressingCallback c : callbackList)
			c.onNotPressing();

		for (int i = 0; i < keyDelayList.length; i++) {
			keyDelayList[i]++;
		}
	}

	public void addCallback(KeyPressingCallback callback) {
		callbackList.add(callback);
	}

	public void addCallback(List<KeyPressingCallback> callback) {
		callbackList.addAll(callback);
	}

	public void removeCallback(KeyPressingCallback callback) {
		callbackList.remove(callback);
	}

	public void reset() {
		callbackList.clear();
		pressed.clear();
	}

	public void clearPressed() {
		for (Integer keyCode : pressed.keySet()) {
			pressed.put(keyCode, false);
		}
		for (int i = 0; i < keyIsFirst.length; i++) {
			keyIsFirst[i] = true;
			keyGapList[i] = 0;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		pressed.put(e.getKeyCode(), true);
		for (KeyPressingCallback c : callbackList)
			c.onPressing(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		pressed.put(keyCode, false);
		keyIsFirst[keyCode] = true;
		keyDelayList[keyCode] = 0;
		keyGapList[keyCode] = 0;
		for (KeyPressingCallback c : callbackList)
			c.onReleased(e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
