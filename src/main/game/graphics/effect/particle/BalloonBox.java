package main.game.graphics.effect.particle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import main.flask.utils.Vec2D;
import main.game.graphics.GraphicsDrawer;

public class BalloonBox extends Particle {
	private String text;
	private int width = 0;
	private int textSize;
	private int tap = 4, bottomY;
	
	public BalloonBox(Vec2D pos, String text, int textSize) {
		super(pos);
		this.text = text;
		this.textSize = textSize;
		this.color = new Color(0xaa000000, true);
		makeShape();
	}
	
	protected void makeShape() {
		this.width = GraphicsDrawer.getTextWidth(text, (int) textSize) + tap * 2;
		this.shape = new RoundRectangle2D.Float((int) (-width / 2), (int) (-textSize * 2), width, size * 2, size, size);
		bottomY = (int) size * 2 - textSize * 2;
	}
	
	@Override
	public void draw(Graphics2D g) {
		position.y -= life * 1.5f;
		g.translate((int) position.x, (int) position.y);
		
		g.setColor(color);
		g.fill(shape);
		
		g.fillPolygon(new int[] { -tap, +tap, 0 }, new int[] { bottomY, bottomY, bottomY + tap }, 3);
		
		GraphicsDrawer.drawBitmapText(g, text, (int) textSize, 0, Color.white, -width / 2 + tap, (int) (size / 2 - textSize / 2));
		
		g.translate(-(int) position.x, -(int) position.y);
	}
	
	@Override
	public boolean integrate() {
		return false;
	}
	
	@Override
	public boolean integrate(double duration) {
		return false;
	}
	
	@Override
	protected void updateWithLife() {}
	
	@Override
	public void addForce(Vec2D f) {}
	
	@Override
	public void addForce(float fx, float fy) {}
	
	@Override
	public void clearAccumulator() {}
	
}
