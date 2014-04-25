package main.game.graphics.effects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Grid {
	private BufferedImage b;
	private int width, height, boxSize;
	private Color[] colors;
	private Color[] BLACK = new Color[] { new Color(0x111111), new Color(0x444444) };
	private Color[] WHITE = new Color[] { new Color(0xcccccc), new Color(0xffffff) };
	
	public Grid(int width, int height, int boxSize) {
		b = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);// BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		this.width = width;
		this.height = height;
		this.boxSize = boxSize;
		this.colors = BLACK;
		
		drawGrid((Graphics2D) b.getGraphics());
	}
	
	public void setBlackEdition(boolean b) {
		if (b) colors = BLACK;
		else colors = WHITE;
	}
	
	private void drawGrid(Graphics2D g) {
		final int yl = this.height / boxSize;
		final int xl = this.width / boxSize;
		
		for (int y = 0; y < yl; y++) {
			for (int x = 0; x < xl; x++) {
				if ((x + y) % 2 == 0) g.setColor(colors[0]);
				else g.setColor(colors[1]);
				g.fillRect(x * boxSize, y * boxSize, boxSize, boxSize);
			}
		}
	}
	
	public BufferedImage getBufferedImage() {
		return b;
	}
}
