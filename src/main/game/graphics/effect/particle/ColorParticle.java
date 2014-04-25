package main.game.graphics.effect.particle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import main.flask.utils.FlaskUtil;

public class ColorParticle extends Particle {
	public static int cacheSize = 16;
	public static final Color[] defaultColor = FlaskUtil.COLOR_FIRE_ALPHA;
	public static final Color[] defaultColorCache;
	static {
		defaultColorCache = createCache(defaultColor);
	}
	
	public Color[] colorCache;
	
	public ColorParticle(float x, float y, float size, Color[] colors) {
		super(x, y, size);
		this.shape = new Rectangle2D.Float(-size / 2, -size / 2, size, size);
		
		if (colors != null) this.colorCache = createCache(colors);
		else this.colorCache = defaultColorCache;
		
		updateWithLife();
	}
	
	@Override
	protected void updateWithLife() {
		float tSize = size * life;
		((RectangularShape) shape).setFrame(-tSize / 2, -tSize / 2, tSize, tSize);
		color = getColorFromCache(life);
	}
	
	protected Color getColorFromCache(float life) {
		return colorCache[(int) (life * (cacheSize - 1))];
	}
	
	protected static Color[] createCache(Color[] colors) {
		Color[] cache = new Color[cacheSize];
		for (int i = 0; i < cacheSize; i++)
			cache[i] = FlaskUtil.colorInterpolate(colors, (float) i / (cacheSize - 1));
		return cache;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.translate((float) position.x, (float) position.y);
		
		g.fill(shape);
		
		g.translate(-(float) position.x, -(float) position.y);
	}
}
