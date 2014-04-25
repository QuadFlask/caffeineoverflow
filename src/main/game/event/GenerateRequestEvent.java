package main.game.event;

import java.util.EventObject;

import main.game.obj.factory.unit.Unit;

public class GenerateRequestEvent extends EventObject {
	private static final long serialVersionUID = 1310831253268128942L;

	public Class<? extends Unit> unitClass;

	public GenerateRequestEvent(Object source) {
		super(source);
	}

}
