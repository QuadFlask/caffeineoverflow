package main.game.graphics.effects;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;

import sun.awt.image.ByteInterleavedRaster;
import main.flask.utils.PerlinNoiseGenerator;

public class PerlinMap extends BufferedImage {
	private double x = 0, y = 0;
	private double base = 0, factor = 128;
	private int rgb = 0xffffff;
	private final int width, height;

	public PerlinMap(int width, int height, int imageType, IndexColorModel cm) {
		super(width, height, imageType, cm);
		this.width = width;
		this.height = height;
	}

	public void setBase(double base) {
		this.base = base;
	}

	public void setFactor(double factor) {
		this.factor = factor;
	}

	public void setColor(int rgb) {
		this.rgb = rgb;
	}

	public void updateMap() {
		double zoom = 0.05;
		double xx = 0, yy = 0, c;
		int color;

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				xx = i + x;
				yy = j + y;

				c = PerlinNoiseGenerator.pNoise(xx * zoom, yy * zoom, 0.9, 6);
				c *= factor;
				c += base;
				color = (int) c;
				if (color > 0xff) color = 0xff;
				if (color < 0) color = 0;
				color = color << 24 | rgb;

				setRGB(i, j, color);
			}
		}
	}

	public void updateWithByte_Alpha() {
		ByteInterleavedRaster srcRaster = (ByteInterleavedRaster) getRaster();
		byte[] srcData = srcRaster.getDataStorage();

		double zoom = 0.05;
		double xx = 0, yy = 0, c;
		int index, color;
		byte r = (byte) (rgb & 0xff0000 >> 16);
		byte g = (byte) (rgb & 0xff00 >> 8);
		byte b = (byte) (rgb & 0xff);

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				index = i + j * width;
				xx = i + x;
				yy = j + y;

				c = PerlinNoiseGenerator.pNoise(xx * zoom, yy * zoom, 0.9, 6);
				c *= factor;
				c += base;
				color = (int) c;
				if (color > 0xff) color = 0xff;
				if (color < 0) color = 0;

				srcData[index] = (byte) color;
				srcData[index + 1] = r;
				srcData[index + 2] = g;
				srcData[index + 3] = b;
			}
		}
	}

	public void move(double x, double y) {
		this.x += x;
		this.y += y;
	}

}
