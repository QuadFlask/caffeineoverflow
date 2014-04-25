package main.game.event;

import java.util.EventObject;

public class TickEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5717835266349812523L;
	
	/**
	 * Constructs a prototypical Event.
	 * 
	 * @param source
	 *            The object on which the Event initially occurred.
	 * @throws IllegalArgumentException
	 *             if source is null.
	 */
	public TickEvent(Object source) {
		super(source);
	}
	
	public TickEvent(Tick tick) {
		super(tick);
	}
	
	public long getFPSFromTick() {
		return ((Tick) getSource()).fps;
	}
	
	public float getElaspedTimeFromTick() {
		return ((Tick) getSource()).elaspedTime;
	}
}
