package main.game.view.ui;

import java.awt.Color;
import java.awt.Graphics2D;

public class ProgressBar implements DrawableComponent {
	private int cursor = 0;
	private float f_cursor = 0;
	private int width = 10;
	private int thickness = 10;
	private float speed = 0.5f;
	private int screen_width = 100;
	private int posx, posy;

	public ProgressBar(int width, int thickness, float speed) {
		this.screen_width = width;
		this.thickness = thickness;
		this.speed = speed;
	}

	public ProgressBar(int width, int thickness, float speed, int y) {
		this.screen_width = width;
		this.thickness = thickness;
		this.speed = speed;
		this.posy = y;
	}

	public void setPosition(int x, int y) {
		posx = x;
		posy = y;
	}

	@Override
	public boolean draw(Graphics2D g) {
		g.setColor(Color.white);
		for (int y = 0; y < thickness; y++) {
			for (int x = -width - thickness; x < screen_width; x += 2 * width) {
				g.fillRect(posx + x + y + cursor, posy + y, width, 1);
			}
		}
		f_cursor += speed;
		cursor = (int) f_cursor;
		cursor %= 2 * width;
		return true;
	}

}
