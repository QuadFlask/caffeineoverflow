package main.game.controller;

import main.game.view.View;

public interface Controller {

	public View getView();

	public void free();
}
