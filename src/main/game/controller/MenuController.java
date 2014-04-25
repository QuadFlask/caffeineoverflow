package main.game.controller;

import java.awt.Color;
import java.awt.Graphics2D;

import main.game.common.D;
import main.game.event.OnChangeEventDispatcher;
import main.game.graphics.GraphicsDrawer;
import main.game.logic.Logic;
import main.game.logic.MenuLogic;
import main.game.view.MenuView;
import main.game.view.View;
import main.game.view.ui.DrawableComponent;
import main.game.view.ui.ProgressBar;

public class MenuController extends OnChangeEventDispatcher implements Controller {
	private MenuView view;
	private Logic logic;

	private boolean showWaitBox = false;

	public MenuController() {
		view = new MenuView(D.SCREEN_WIDTH, D.SCREEN_HEIGHT);
		logic = new MenuLogic(D.SCREEN_WIDTH, D.SCREEN_HEIGHT, view);
		((MenuView) view).setLogic(logic);
		((MenuView) view).setController(this);
	}

	@Override
	public View getView() {
		return view;
	}

	public void free() {
		view.free();
		logic.free();
	}

	public void showErrorBox() {
		view.addDrawable(new DrawableComponent() {
			int count = 200;

			@Override
			public boolean draw(Graphics2D g) {
				GraphicsDrawer.drawBitmapTextCenter(g, "can't conntect to \"" + D.HOST + ":" + D.PORT + "\"", 3, 1, Color.orange, 250, 5);
				return count-- > 0;
			}
		});
	}

	private static final Color ALERTBOX_BACKGROUND_COLOR = new Color(0xaa000000, true);
	private static final int PROGRESSBAR_THICKNESS = 10;

	public void showWaitBox(boolean isServer) {
		showWaitBox = true;

		view.removeKeyCallback();

		if (isServer) {
			view.addDrawable(new DrawableComponent() {
				ProgressBar progressTop = new ProgressBar(D.SCREEN_WIDTH, PROGRESSBAR_THICKNESS, 0.5f);
				ProgressBar progressBottom = new ProgressBar(D.SCREEN_WIDTH, PROGRESSBAR_THICKNESS, 0.5f, D.SCREEN_HEIGHT - PROGRESSBAR_THICKNESS);

				@Override
				public boolean draw(Graphics2D g) {
					g.setColor(ALERTBOX_BACKGROUND_COLOR);
					g.fillRect(0, 0, D.SCREEN_WIDTH, D.SCREEN_HEIGHT);

					GraphicsDrawer.drawBitmapTextCenter(g, "waiting for connect...", 3, 1, Color.yellow, 250, 350);
					GraphicsDrawer.drawBitmapTextCenter(g, "[your ip : \"" + D.HOST + ":" + D.PORT + "\"]", 3, 1, Color.white, 250, 370);
					GraphicsDrawer.drawBitmapTextCenter(g, "[esc : cancle]", 3, 1, Color.pink, 250, 470);

					progressTop.draw(g);
					progressBottom.draw(g);

					return showWaitBox;
				}
			});
		} else {
			view.addDrawable(new DrawableComponent() {
				ProgressBar progressTop = new ProgressBar(D.SCREEN_WIDTH, PROGRESSBAR_THICKNESS, 0.5f);
				ProgressBar progressBottom = new ProgressBar(D.SCREEN_WIDTH, PROGRESSBAR_THICKNESS, 0.5f, D.SCREEN_HEIGHT - PROGRESSBAR_THICKNESS);

				@Override
				public boolean draw(Graphics2D g) {
					g.setColor(ALERTBOX_BACKGROUND_COLOR);
					g.fillRect(0, 0, D.SCREEN_WIDTH, D.SCREEN_HEIGHT);

					GraphicsDrawer.drawBitmapTextCenter(g, "conntect to \"" + D.HOST + ":" + D.PORT + "\"", 3, 1, Color.yellow, 250, 350);
					GraphicsDrawer.drawBitmapTextCenter(g, "plz wait...", 3, 1, Color.yellow, 250, 370);

					progressTop.draw(g);
					progressBottom.draw(g);

					return showWaitBox;
				}
			});
		}
	}

	public void hideWaitBox() {
		showWaitBox = false;
		view.addKeyCallback();
	}
}
