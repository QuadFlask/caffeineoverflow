package main.game.event;

public class OnChangeEvent {
	public enum CONTROLLER {
		GAME,
		MENU,
		RANK
	}
	
	public CONTROLLER controller;
	public int data;
	
	public OnChangeEvent(CONTROLLER Controller, int data) {
		controller = Controller;
		this.data = data;
	}
}