package main.game.event;

import java.util.EventListener;

public interface TickEventListener extends EventListener{
	public void onTick(TickEvent tickEvent);
}
