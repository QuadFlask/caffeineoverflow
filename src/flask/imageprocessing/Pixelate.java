package flask.imageprocessing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Flask
 */
public class Pixelate {

	public static BufferedImage pixelate(int[] src, int width, int height, Config config) {
		if (config == null)
			config = new Config.builder().build();
		return pixelate(src, width, height, config.boxsize, config.whitelevel, config.zoom, config.colors, config.alpha, config.blackOut, config.type);
	}

	;

	public static BufferedImage pixelate(BufferedImage b, double boxsize, int zoom, double startHueAngle, float alpha, boolean blackOut) {
		return pixelate(b, boxsize, 0xff010101, zoom, startHueAngle, alpha, blackOut);
	}

	public static BufferedImage pixelate(BufferedImage b, double boxsize, int whitelevel, int zoom, final Color[] colors, float alpha, boolean blackOut, SHAPE_TYPE type) {
		int scansize = b.getWidth();
		int[] src;

		if (blackOut) {
			src = inversedGray(b.getRGB(0, 0, b.getWidth(), b.getHeight(), null, 0, scansize));
		} else {
			src = getGrayScale(b.getRGB(0, 0, b.getWidth(), b.getHeight(), null, 0, scansize));
		}

		return pixelate(src, scansize, b.getHeight(), boxsize, whitelevel, zoom, colors, alpha, blackOut, type);
	}

	/**
	 * pixelate
	 *
	 * @param b             : target
	 * @param boxsize       : maximum box size
	 * @param zoom          : zoom rate
	 * @param startHueAngle : Hue angle, in HSV color model [0~360]
	 * @param alpha         : box's alpha (0~1f)
	 * @param blackOut      : blackout. true -> draw darken
	 * @return : pixelated bufferedimage
	 */
	public static BufferedImage pixelate(BufferedImage b, double boxsize, int whitelevel, int zoom, double startHueAngle, float alpha, boolean blackOut) {
		int scansize = b.getWidth();
		int[] src;
		if (blackOut) {
			src = inversedGray(b.getRGB(0, 0, b.getWidth(), b.getHeight(), null, 0, scansize));
		} else {
			src = getGrayScale(b.getRGB(0, 0, b.getWidth(), b.getHeight(), null, 0, scansize));
		}
		ArrayList<PixelI> pixelList = new ArrayList<PixelI>();
		int index = 0;
		double colorInc = 360.0 / b.getHeight();

		PixelI p;
		int color;
		for (int y = 0; y < b.getHeight(); y++) {
			color = flask.utils.FlaskUtil.HSVtoRGB(startHueAngle, 1, 1);
			startHueAngle += colorInc;
			for (int x = 0; x < b.getWidth(); x++) {
				index = x + y * scansize;
				if (src[index] >= whitelevel) {
					p = new PixelI(x, y);
					p.size = (int) ((src[index] & 0xff) * boxsize / 255.0);
					p.color = color;
					pixelList.add(p);
				}
			}
		}

		BufferedImage result = new BufferedImage(b.getWidth() * zoom, b.getHeight() * zoom, BufferedImage.TYPE_INT_ARGB);

		Graphics g = result.getGraphics();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setBackground(Color.white);
		g.clearRect(0, 0, result.getWidth(), result.getHeight());
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

		for (PixelI pixel : pixelList) {
			g2d.setColor(new Color(pixel.color));
			g2d.fillRect((pixel.x * zoom), (pixel.y * zoom), pixel.size, pixel.size);
		}

		return result;
	}

	public static BufferedImage pixelate(int[] src, int width, int height, double boxsize, int whitelevel, int zoom, final Color[] colors, float alpha, boolean blackOut, SHAPE_TYPE type) {
		ArrayList<PixelC> pixelList = new ArrayList<PixelC>();
		int index = 0;
		double t = 0;
		double inc = 1.0 / height;

		PixelC p;
		Color color;
		for (int y = 0; y < height; y++) {
			color = flask.utils.FlaskUtil.colorInterpolate(colors, t);
			t += inc;
			for (int x = 0; x < width; x++) {
				index = x + y * width;
				if (src[index] >= whitelevel) {
					p = new PixelC(x, y);
					p.size = (int) ((src[index] & 0xff) * boxsize / 255.0);
					p.color = color;
					pixelList.add(p);
				}
			}
		}

		return drawShapes(width, height, zoom, alpha, type, pixelList);
	}

	private static BufferedImage drawShapes(int width, int height, int zoom, float alpha, SHAPE_TYPE type, ArrayList<PixelC> pixelList) {
		BufferedImage result = new BufferedImage(width * zoom, height * zoom, BufferedImage.TYPE_INT_ARGB);

		Graphics g = result.getGraphics();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setBackground(Color.white);
		g.clearRect(0, 0, result.getWidth(), result.getHeight());
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

		switch (type) {
			case OVAL_FILL:
				for (PixelC pixel : pixelList) {
					g2d.setColor(pixel.color);
					g2d.fillOval(pixel.x * zoom, pixel.y * zoom, pixel.size, pixel.size);
				}
				break;
			case OVAL_LINE:
				for (PixelC pixel : pixelList) {
					g2d.setColor(pixel.color);
					g2d.drawOval(pixel.x * zoom, pixel.y * zoom, pixel.size, pixel.size);
				}
				break;
			case RECT_FILL:
				for (PixelC pixel : pixelList) {
					g2d.setColor(pixel.color);
					g2d.fillRect(pixel.x * zoom, pixel.y * zoom, pixel.size, pixel.size);
				}
				break;
			case RECT_LINE:
				for (PixelC pixel : pixelList) {
					g2d.setColor(pixel.color);
					g2d.drawRect(pixel.x * zoom, pixel.y * zoom, pixel.size, pixel.size);
				}
				break;
			case X:
				int x0,
						x1,
						y0,
						y1;
				for (PixelC pixel : pixelList) {
					g2d.setColor(pixel.color);
					x0 = (pixel.x - 1) * zoom;
					x1 = (pixel.x + 1) * zoom;
					y0 = (pixel.y - 1) * zoom;
					y1 = (pixel.y + 1) * zoom;
					g2d.drawLine(x0, y0, x1, y1);
					g2d.drawLine(x0, y1, x1, y0);
				}
				break;
			default:
				break;
		}

		return result;
	}

	/**
	 * scaling
	 *
	 * @param b    : target
	 * @param rate : new size = current size / rate
	 * @return : scaled bufferedimage
	 */
	public static BufferedImage scale(BufferedImage b, int rate) {
		int width = b.getWidth() / rate;
		int height = b.getHeight() / rate;
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		result.getGraphics().drawImage(b.getScaledInstance(width, height, BufferedImage.SCALE_FAST), 0, 0, width, height, null);
		return result;
	}

	public static int[] getGrayScale(int[] rgb) {
		int[] result = new int[rgb.length];
		for (int i = 0; i < rgb.length; i++) {
			result[i] = 0xff000000 | flask.utils.FlaskUtil.getGrayMode(rgb[i]);
		}
		return result;
	}

	public static int[] inversedGray(int[] rgb) {
		int[] result = new int[rgb.length];
		for (int i = 0; i < rgb.length; i++) {
			result[i] = 0xff000000 | (0xffffff - flask.utils.FlaskUtil.getGrayMode(rgb[i]));
		}
		return result;
	}

	public static enum SHAPE_TYPE {
		RECT_FILL, RECT_LINE, OVAL_FILL, OVAL_LINE, X,
	}

}

class Config {
	final double boxsize;
	final int whitelevel;
	final int zoom;
	final Color[] colors;
	final float alpha;
	final boolean blackOut;
	final Pixelate.SHAPE_TYPE type;

	private Config(builder b) {
		boxsize = b.boxsize;
		whitelevel = b.whitelevel;
		zoom = b.zoom;
		colors = b.colors;
		alpha = b.alpha;
		blackOut = b.blackOut;
		type = b.type;
	}

	public float getAlpha() {
		return alpha;
	}

	public double getBoxsize() {
		return boxsize;
	}

	public Color[] getColors() {
		return colors;
	}

	public Pixelate.SHAPE_TYPE getType() {
		return type;
	}

	public int getWhitelevel() {
		return whitelevel;
	}

	public int getZoom() {
		return zoom;
	}

	public boolean isBlackOut() {
		return blackOut;
	}

	public static class builder {
		double boxsize = 16;
		int whitelevel = 0xff010101;
		int zoom = 4;
		Color[] colors = flask.utils.FlaskUtil.COLOR_RAINBOW;
		float alpha = .2f;
		boolean blackOut = true;
		Pixelate.SHAPE_TYPE type = Pixelate.SHAPE_TYPE.RECT_LINE;

		public builder() {
		}

		public Config build(){
			return new Config(this);
		}

		public builder setAlpha(float alpha) {
			this.alpha = alpha;
			return this;
		}

		public builder setBlackOut(boolean blackOut) {
			this.blackOut = blackOut;
			return this;
		}

		public builder setBoxsize(double boxsize) {
			if (boxsize < 1) {
				boxsize = 1;
			}
			this.boxsize = boxsize;
			return this;
		}

		public builder setColors(Color[] colors) {
			this.colors = colors;
			return this;
		}

		public builder setType(Pixelate.SHAPE_TYPE type) {
			this.type = type;
			return this;
		}

		public builder setWhitelevel(int whitelevel) {
			this.whitelevel = whitelevel;
			return this;
		}

		public builder setZoom(int zoom) {
			this.zoom = zoom;
			return this;
		}
	}


}

abstract class Pixel {
	public int x, y, size;
}

class PixelC extends Pixel {
	public Color color;

	public PixelC(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

class PixelI extends Pixel {
	public int color;

	public PixelI(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

//		Rectangle2D r2d = new Rectangle2D.Float();
//g2d.fillRect((int) ((pixel.x - pixel.size / 2.0) * zoom + .5), (int) ((pixel.y - pixel.size / 2.0) * zoom + .5), pixel.size, pixel.size);
//			g2d.fillOval((int) ((pixel.x - pixel.size / 2.0) * zoom + .5), (int) ((pixel.y - pixel.size / 2.0) * zoom + .5), pixel.size, pixel.size);
//			r2d.setRect(pixel.x * zoom, pixel.y * zoom, pixel.size, pixel.size);
//			r2d.setRect(((pixel.x - pixel.size / 2.0) * zoom + .5),  ((pixel.y - pixel.size / 2.0) * zoom + .5), pixel.size, pixel.size);
//.getRGB(0, 0, result.getWidth(), result.getHeight(), null, 0, result.getWidth());
//case STAR :
//				Polygon star = new Polygon();
//				star.addPoint(0, -52);
//				star.addPoint(17, -23);
//				star.addPoint(50, -16);
//				star.addPoint(28, 10);
//				star.addPoint(30, 43);
//				star.addPoint(0, 30);
//				star.addPoint(-31, 43);
//				star.addPoint(-29, 10);
//				star.addPoint(-51, -16);
//				star.addPoint(-18, -23);
////				star.
//
//				for (PixelC pixel : pixelList) {
//					g2d.setColor(pixel.color);
////										g2d.drawRect(pixel.x * zoom, pixel.y * zoom, pixel.size, pixel.size);
//					g2d.draw(star);
//				}
//				break;