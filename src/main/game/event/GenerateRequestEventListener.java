package main.game.event;

import java.util.EventListener;

public interface GenerateRequestEventListener extends EventListener {
	public void onReqeust(GenerateRequestEvent event);
}
