package main.game.logic;

import main.game.windowframe.WindowForm;

public class Controller {

	public Controller() {
		WindowForm windowForm = new WindowForm("Test", 500, 800, 60);
		windowForm.start();
	}

	public static void main(String[] args) {
		new Controller();
	}
}
