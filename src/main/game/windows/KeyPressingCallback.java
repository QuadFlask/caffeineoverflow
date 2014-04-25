package main.game.windows;

import java.util.List;

public interface KeyPressingCallback {
	public void onPressing(int keyCode);

	public void onPressing(List<Integer> keyCodes);
	
	public void onNotPressing();

	public void onReleased(int keyCode);
}
