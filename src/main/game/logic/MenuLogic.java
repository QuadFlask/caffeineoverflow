package main.game.logic;

import main.game.view.View;

public class MenuLogic implements Logic {
	private int screenWidth, screenHeight;
	private View view;
	
	public MenuLogic(int screenWidth, int screenHeight, View view) {
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.view = view;
	}
	
	@Override
	public void free() {}
	
}
