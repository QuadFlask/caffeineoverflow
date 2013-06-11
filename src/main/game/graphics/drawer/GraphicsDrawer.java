package main.game.graphics.drawer;

import main.game.common.font.BMPFont;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GraphicsDrawer {

	public static enum POSITION {
		CENTER, LT, RT, LB, RB, VC, HC
	}

	private GraphicsDrawer() { // �ν��Ͻ� �� ���Ѵ�!
	}

	public static void drawImage(Graphics g, final BufferedImage img, POSITION p, Rectangle rect, final int width, final int height) {
		switch (p) {
			case CENTER: {
				int[] pos = getCenteredPosition(width, height, rect);
				drawImage(g, img, pos[0], pos[1], width, height);
				break;
			}
			case LT: {
				drawImage(g, img, rect.x, rect.y, width, height);
				break;
			}
			case RT: {
				drawImage(g, img, rect.x + rect.width - width, rect.y, width, height);
				break;
			}
			case LB: {
				drawImage(g, img, rect.x, rect.y + rect.height - height, width, height);
				break;
			}
			case RB: {
				drawImage(g, img, rect.x + rect.width - width, rect.y + rect.height - height, width, height);
				break;
			}
			case VC: {
				drawImage(g, img, rect.x, getCenteredY(height, rect), width, height);
				break;
			}
			case HC: {
				drawImage(g, img, getCenteredY(width, rect), rect.y, width, height);
				break;
			}
			default: {

			}
		}
		g.dispose();
	}

	public static void drawImage(Graphics g, final BufferedImage img, final int ix, final int iy, final int width, final int height) {
		g.drawImage(img, ix, iy, width, height, null);
	}

	public static void drawImageAtCenter(Graphics g, final BufferedImage img, final int ix, final int iy, final int width, final int height) {
		g.drawImage(img, ix - width / 2, iy - height / 2, width, height, null);
	}

	public static void drawImage(Graphics g, final String fileName, final int ix, final int iy, final int width, final int height) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(fileName));
			g.drawImage(img, ix, iy, width, height, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void drawImageWithRotation(Graphics g, BufferedImage img, int ix, int iy, double theta) {
		final int hw = img.getWidth() / 2;
		final int hh = img.getHeight() / 2;

		Graphics2D g2d = (Graphics2D) g;
		//		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		//		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.rotate(theta, ix + hw, iy + hh);
		g2d.drawImage(img, ix, iy, hw * 2, hh * 2, null);
		g2d.rotate(-theta, ix + hw, iy + hh);
	}

	public static int[] getCenteredPosition(int w, int h, Rectangle rect) {
		return new int[] { rect.x + (rect.width - w) / 2, rect.y + (rect.height - h) / 2 };
	}

	public static int getCenteredX(int w, Rectangle rect) {
		return (rect.width - w) / 2;
	}

	public static int getCenteredY(int h, Rectangle rect) {
		return (rect.height - h) / 2;
	}

	public static int getRightAligned(int w, Rectangle rect) {
		return rect.x + rect.width - w;
	}

	public static void drawBitmapText(Graphics g, final String text, final int size, final int gap, final Color color, final int ix, final int iy) {
		int x = ix;
		int cursor = 0;
		int[][] data;
		String mText = text.toUpperCase();
		for (int i = 0; i < text.length(); i++) {
			data = BMPFont.LETTERS.get(mText.charAt(i));
			if (data == null) {
				data = BMPFont.LETTERS.get('?');
			}
			drawBluckBoxes(g, x, iy, size, gap, data, color);
			cursor += data[0].length + 1;
			x = ix + cursor * size;
		}
	}

	public static int getTextWidth(final String text, final int size) {
		int cursor = 0;
		String mText = text.toUpperCase();
		int[][] textarr;
		for (int i = 0; i < text.length(); i++) {
			textarr = BMPFont.LETTERS.get(mText.charAt(i));
			if (textarr != null) {
				cursor += textarr[0].length + 1;
			} else {
				cursor += BMPFont.LETTERS.get('?').length + 1;
			}
		}
		return cursor * size;
	}

	/**
	 * 
	 * @param g
	 * @param fontName
	 * @param fontSize
	 * @param fontStyle
	 *            : Font.Plain
	 * @param fontColor
	 * @param ix
	 * @param iy
	 * @param text
	 */
	public static void drawText(Graphics g, final String fontName, int fontSize, int fontStyle, int fontColor, int ix, int iy, final String text) {
		g.setColor(new Color(fontColor));
		g.setFont(new Font(fontName, 0, fontSize));
		g.drawString(text, ix, iy);
	}

	public static void drawNumber(Graphics g, final int fx, final int iy, final int sx, final int gap, final int gap_letter, final int number) {
		String s = "" + number;
		if (number == 0) {
			s = "0";
		}
		int[][] n;

		int x = fx - sx * 3;

		for (int i = s.length() - 1; i >= 0; i--) {
			n = BMPFont.NUMBERS[Character.getNumericValue(s.charAt(i))];
			drawBluckBoxes(g, x, iy, sx, gap, n);
			x -= sx * 3 + gap_letter;
		}
	}

	public static void drawNumber(Graphics g, final int fx, final int iy, final int sx, final int gap, final int gap_letter, final int num, final Color color) {
		String s = "" + num;
		if (num == 0) {
			s = "0";
		}
		int[][] n;

		int x = fx - sx * 3;

		for (int i = s.length() - 1; i >= 0; i--) {
			n = BMPFont.NUMBERS[Character.getNumericValue(s.charAt(i))];
			drawBluckBoxes(g, x, iy, sx, gap, n, color);
			x -= sx * 3 + gap_letter;
		}
	}

	public static void drawBluckBoxes(Graphics g, final int ix, final int iy, final int sx, final int gap, final int[][] data) {
		final int h = data.length;
		final int w = data[0].length;

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (data[y][x] != 0) {
					g.setColor(new Color(data[y][x]));
					g.fillRect(ix + x * sx + gap, iy + y * sx + gap, sx - gap, sx - gap);
				}
			}
		}
	}

	public static void drawBluckBoxes(Graphics g, final int ix, final int iy, final int sx, final int gap, final int[][] data, final Color col) {
		final int h = data.length;
		final int w = data[0].length;

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (data[y][x] != 0) {
					g.setColor(col);
					g.fillRect(ix + x * sx + gap, iy + y * sx + gap, sx - gap, sx - gap);
				}
			}
		}
	}

	public static void drawBluckBoxesWithRotaion(Graphics g, final int ix, final int iy, final int sx, final int gap, final int[][] data, final Color col, final double rotation) {
		final int h = data.length;
		final int w = data[0].length;

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.rotate(rotation, ix + sx * data[0].length / 2, iy + sx * data.length / 2);
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (data[y][x] != 0) {
					g.setColor(col);
					g2d.fillRect(ix + x * sx + gap, iy + y * sx + gap, sx - gap, sx - gap);
				}
			}
		}
		g2d.rotate(-rotation, ix + sx * data[0].length / 2, iy + sx * data.length / 2);
	}

	public static void drawBluckBoxesByGauge(Graphics g, final int ix, final int iy, final int sx, final int sy, final int gap, int value, int total, final Color gauge, final Color line) {
		drawBluckBoxesByHLine(g, ix, iy, sx, sy, gap, value, gauge);

		drawBluckBoxesByHLine(g, ix, iy - sy / 2, sx, sy / 2, gap, total, line);
		drawBluckBoxesByHLine(g, ix, iy + sy, sx, sy / 2, gap, total, line);
		//		drawBluckBoxesByVLine(g, ix - sy + gap, iy - sy - gap, sy, sy, gap, 3, line);
		//		drawBluckBoxesByVLine(g, ix + total * sx + gap, iy - sy - gap, sy, sy, gap, 3, line);
		//		g.fillRect(ix - sx + gap, iy, sy - gap, sy - gap);
		//		g.fillRect(ix + total * sx + gap, iy, sy - gap, sy - gap);
	}

	public static void drawBluckBoxesByHLine(Graphics g, final int ix, final int iy, final int sx, final int sy, final int gap, int count, final Color col) {
		g.setColor(col);
		for (int x = 0; x < count; x++) {
			g.fillRect(ix + x * sx + gap, iy, sx - gap, sy - gap);
		}
	}

	public static void drawBluckBoxesByVLine(Graphics g, final int ix, final int iy, final int sx, final int sy, final int gap, int count, final Color col) {
		g.setColor(col);
		for (int y = 0; y < count; y++) {
			g.fillRect(ix, iy + y * sy + gap, sx - gap, sy - gap);
		}
	}
}
