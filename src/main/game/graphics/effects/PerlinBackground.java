package main.game.graphics.effects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.flask.imageprocessing.IntegerBlender;
import main.flask.utils.PerlinNoiseGenerator;
import sun.awt.image.IntegerInterleavedRaster;

public class PerlinBackground {
	protected static final Color TRANSPARENT = new Color(0, true);
	private static PerlinBackground me;

	private BufferedImage fixed, floating;
	private Graphics2D g_fixed, g_floating;
	private int width, height;
	private double floating_x = 10000 * (1 + Math.random()), floating_y = 100000000 * (1 + Math.random());
	private double zoom = 0.05;
	private int baseColor = 0xff0022, layerColor = 0xccff00;

	private static float perlin_base = 32;
	private static float perlin_amplitude = 128;

	private Runnable perlinBackgroundMoving;

	public PerlinBackground(int w, int h) {
		width = w;
		height = h;

		fixed = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		floating = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		g_fixed = (Graphics2D) fixed.getGraphics();
		g_floating = (Graphics2D) floating.getGraphics();

		g_fixed.setBackground(TRANSPARENT);
		g_floating.setBackground(TRANSPARENT);

		g_fixed.clearRect(0, 0, width, height);
		g_floating.clearRect(0, 0, width, height);

		generateMap(fixed, 100000 * Math.random(), 100000 * Math.random(), baseColor);
		move(10);

		me = this;
		perlinBackgroundMoving = new Runnable() {
			@Override
			public void run() {
				synchronized (me) {
					move(-.5f);
				}
			}
		};
	}

	private void generateMap(BufferedImage b, double baseX, double baseY, int rgb) {
		IntegerInterleavedRaster srcRaster = (IntegerInterleavedRaster) b.getRaster();
		int[] srcData = srcRaster.getDataStorage();
		double c;
		int x, y;

		for (y = 0; y < height; y++) {
			for (x = 0; x < width; x++) {
				c = PerlinNoiseGenerator.pNoise((baseX + x) * zoom, (baseY + y) * zoom, 0.6, 6);
				c *= perlin_amplitude;
				c += perlin_base;
				if (c > 255.0) c = 255.0;
				if (c < 0.0) c = 0.0;
				srcData[width * y + x] = (int) c << 24 | rgb;
			}
		}
	}

	public void move(double yAmount) {
		floating_y += yAmount * zoom;
		// g_floating.copyArea(0, 0, width, height, 0, yAmount);
		//
		// generateMap(floating, floating_x, floating_y, 0, 0, width, yAmount);
		generateMap(floating, floating_x, floating_y, layerColor);
		merge();
	}

	private void merge() {
		IntegerInterleavedRaster fixedRaster = (IntegerInterleavedRaster) fixed.getRaster();
		IntegerInterleavedRaster floatingRaster = (IntegerInterleavedRaster) floating.getRaster();

		int[] fixedData = fixedRaster.getDataStorage();
		int[] floatingData = floatingRaster.getDataStorage();

		IntegerBlender.screen(fixedData, floatingData);
	}

	public synchronized BufferedImage getFloting() {
		return floating;
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	public void setPerlinFactor(float base, float amplitude) {
		perlin_base = base;
		perlin_amplitude = amplitude;
	}

	public Runnable getPerlinBackgroundMoving() {
		return perlinBackgroundMoving;
	}
}
