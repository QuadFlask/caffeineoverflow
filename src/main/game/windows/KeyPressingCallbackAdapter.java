package main.game.windows;

import java.util.List;

public abstract class KeyPressingCallbackAdapter implements KeyPressingCallback {
	
	@Override
	public void onPressing(int keyCode) {}
	
	@Override
	public void onPressing(List<Integer> keyCodes) {}
	
	@Override
	public void onNotPressing() {}
	
	@Override
	public void onReleased(int keyCode) {}
	
}
