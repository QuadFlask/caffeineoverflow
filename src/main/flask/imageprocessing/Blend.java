package main.flask.imageprocessing;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Blend {

	public static enum BLEND {
		NORMAL,
		MULTIPLY,
		LIGHTEN,
		DARKEN,
		DARKERCOLOR,
		LIGHTERCOLOR,
		LINEARDODGE,
		LINEARBURN,
		DIFFERENCE,
		SCREEN,
		EXCLUSION,
		OVERLAY,
		SOFTLIGHT,
		HARDLIGHT,
		COLORDODGE,
		COLORBURN,
		LINEARLIGHT,
		VIVIDLIGHT,
		PINLIGHT,
		HARDMIX
	};

	public static final void addARGB(int[] bd0, int[] bd1) {
		int i = 0;
		for (int L = bd0.length; i < L; i++) {
			int rgb0 = bd0[i];
			int rgb1 = bd1[i];
			int a0 = (rgb0 >> 8) & 0xff0000;
			int r0 = rgb0 & 0xff0000;
			int g0 = rgb0 & 0xff00;
			int b0 = rgb0 & 0xff;

			a0 += (rgb1 >> 8) & 0xff0000;
			r0 += rgb1 & 0xff0000;
			g0 += rgb1 & 0xff00;
			b0 += rgb1 & 0xff;

			a0 = a0 < 0xff0000 ? a0 < 0 ? 0 : a0 : 0xff0000;
			r0 = r0 < 0xff0000 ? r0 < 0 ? 0 : r0 : 0xff0000;
			g0 = g0 < 0xff00 ? g0 < 0 ? 0 : g0 : 0xff00;
			b0 = b0 < 0xff ? b0 < 0 ? 0 : b0 : 0xff;

			bd0[i] = (a0 << 8) | r0 | g0 | b0;
		}
	}

	public static final void addARGBFast(int[] bd0, int[] bd1) {
		int i = 0;
		for (int L = bd0.length; i < L; i++) {
			int rgb0 = bd0[i];
			int rgb1 = bd1[i];
			int a0 = (rgb0 >> 8) & 0xff0000;
			int r0 = rgb0 & 0xff0000;
			int g0 = rgb0 & 0xff00;
			int b0 = rgb0 & 0xff;

			a0 += (rgb1 >> 8) & 0xff0000;
			r0 += rgb1 & 0xff0000;
			g0 += rgb1 & 0xff00;
			b0 += rgb1 & 0xff;

			a0 = a0 < 0xff0000 ? a0 : 0xff0000;
			r0 = r0 < 0xff0000 ? r0 : 0xff0000;
			g0 = g0 < 0xff00 ? g0 : 0xff00;
			b0 = b0 < 0xff ? b0 : 0xff;

			bd0[i] = (a0 << 8) | r0 | g0 | b0;
		}
	}

	public static final void addRGB(int[] bd0, int[] bd1) {
		int i = 0;
		for (int L = bd0.length; i < L; i++) {
			int rgb0 = bd0[i];
			int rgb1 = bd1[i];
			int r0 = rgb0 & 0xff0000;
			int g0 = rgb0 & 0xff00;
			int b0 = rgb0 & 0xff;

			r0 += rgb1 & 0xff0000;
			g0 += rgb1 & 0xff00;
			b0 += rgb1 & 0xff;

			r0 = r0 < 0xff0000 ? r0 : 0xff0000;
			g0 = g0 < 0xff00 ? g0 : 0xff00;
			b0 = b0 < 0xff ? b0 : 0xff;

			bd0[i] = 0xff000000 | r0 | g0 | b0;
		}
	}

	public static final void addRGBFast(int[] bd0, int[] bd1) {
		int i = 0;
		for (int L = bd0.length; i < L; i++) {
			int rgb0 = bd0[i];
			int rgb1 = bd1[i];
			int r0 = rgb0 & 0xff0000;
			int g0 = rgb0 & 0xff00;
			int b0 = rgb0 & 0xff;

			r0 += rgb1 & 0xff0000;
			g0 += rgb1 & 0xff00;
			b0 += rgb1 & 0xff;

			r0 = r0 < 0xff0000 ? r0 : 0xff0000;
			g0 = g0 < 0xff00 ? g0 : 0xff00;
			b0 = b0 < 0xff ? b0 : 0xff;

			bd0[i] = 0xff000000 | r0 | g0 | b0;
		}
	}

	//Result Color = 255 - [((255 - Top Color)*(255 - Bottom Color))/255]
	public static final void screenARGB(int[] bd0, int[] bd1) {
		int i = 0, _L = bd0.length, rgb0, rgb1;
		float a0, r0, g0, b0;
		float a1, r1, g1, b1;
		for (; i < _L; i++) {
			rgb0 = bd0[i];
			rgb1 = bd1[i];
			a0 = (rgb0 >> 24) & 0xff;
			r0 = (rgb0 >> 16) & 0xff;
			g0 = (rgb0 >> 8) & 0xff;
			b0 = rgb0 & 0xff;

			a1 = (rgb1 >> 24) & 0xff;
			r1 = (rgb1 >> 16) & 0xff;
			g1 = (rgb1 >> 8) & 0xff;
			b1 = rgb1 & 0xff;

			a0 = clamp((0xff - (((0xff - a1) * (0xff - a0)) / 0xff)));
			r0 = clamp((0xff - (((0xff - r1) * (0xff - r0)) / 0xff)));
			g0 = clamp((0xff - (((0xff - g1) * (0xff - g0)) / 0xff)));
			b0 = clamp((0xff - (((0xff - b1) * (0xff - b0)) / 0xff)));

			bd0[i] = ((int) a0 << 24) | ((int) r0 << 16) | ((int) g0 << 8) | (int) b0;
		}
	}

	public static final void screenRGB(int[] bd0, int[] bd1) {
		int i = 0, _L = bd0.length, rgb0, rgb1;
		float r0, g0, b0;
		float r1, g1, b1;
		for (; i < _L; i++) {
			rgb0 = bd0[i];
			rgb1 = bd1[i];
			r0 = (rgb0 >> 16) & 0xff;
			g0 = (rgb0 >> 8) & 0xff;
			b0 = rgb0 & 0xff;

			r1 = (rgb1 >> 16) & 0xff;
			g1 = (rgb1 >> 8) & 0xff;
			b1 = rgb1 & 0xff;

			r0 = clamp((0xff - (((0xff - r1) * (0xff - r0)) / 0xff)));
			g0 = clamp((0xff - (((0xff - g1) * (0xff - g0)) / 0xff)));
			b0 = clamp((0xff - (((0xff - b1) * (0xff - b0)) / 0xff)));

			bd0[i] = 0xff000000 | ((int) r0 << 16) | ((int) g0 << 8) | (int) b0;
		}
	}

	public static final void multiplyARGB(int[] bd0, int[] bd1) { // Result Color = (Top Color) * (Bottom Color) /255
		int i = 0, _L = bd0.length;
		int rgb0, a0, r0, g0, b0;
		int rgb1, a1, r1, g1, b1;
		for (; i < _L; i++) {
			rgb0 = bd0[i];
			rgb1 = bd1[i];
			a0 = (rgb0 >> 24) & 0xff;
			r0 = (rgb0 >> 16) & 0xff;
			g0 = (rgb0 >> 8) & 0xff;
			b0 = rgb0 & 0xff;

			a1 = (rgb1 >> 24) & 0xff;
			r1 = (rgb1 >> 16) & 0xff;
			g1 = (rgb1 >> 8) & 0xff;
			b1 = rgb1 & 0xff;

			a0 = clamp(a0 * a1 / 0xff);
			r0 = clamp(r0 * r1 / 0xff);
			g0 = clamp(g0 * g1 / 0xff);
			b0 = clamp(b0 * b1 / 0xff);

			bd0[i] = (a0 << 24) | (r0 << 16) | (g0 << 8) | b0;
		}
	}

	public static final void multiplyNumberARGB(int[] bd0, float f) {
		int i = 0, _L = bd0.length;
		int rgb0, a0, r0, g0, b0;
		for (; i < _L; i++) {
			rgb0 = bd0[i];
			a0 = ((rgb0 >> 24) & 0xff);
			r0 = ((rgb0 >> 16) & 0xff);
			g0 = ((rgb0 >> 8) & 0xff);
			b0 = rgb0 & 0xff;

			a0 = clamp(a0 * f);
			r0 = clamp(r0 * f);
			g0 = clamp(g0 * f);
			b0 = clamp(b0 * f);

			bd0[i] = (a0 << 24) | (r0 << 16) | (g0 << 8) | b0;
		}
	}

	public static final void multiplyNumberRGB(int[] bd0, float f) {
		int i = 0, _L = bd0.length;
		int rgb0, r0, g0, b0;
		for (; i < _L; i++) {
			rgb0 = bd0[i];
			r0 = ((rgb0 >> 16) & 0xff);
			g0 = ((rgb0 >> 8) & 0xff);
			b0 = rgb0 & 0xff;

			r0 = clamp(r0 * f);
			g0 = clamp(g0 * f);
			b0 = clamp(b0 * f);
			// assume alpha = 0xff.
			bd0[i] = 0xff000000 | (r0 << 16) | (g0 << 8) | b0;
		}
	}

	public static final void shiftNumberRGB(int bd0[], int f) {
		int i = 0;
		for (int _L = bd0.length; i < _L; i++) {
			int rgb0 = bd0[i];
			int r0 = rgb0 & 0xff0000;
			int g0 = rgb0 & 0xff00;
			int b0 = rgb0 & 0xff;

			r0 = r0 < 0xff0000 ? r0 : 0xff0000;
			g0 = g0 < 0xff00 ? g0 : 0xff00;
			b0 = b0 < 0xff ? b0 : 0xff;

			bd0[i] = 0xff000000 | r0 >> f & 0xff0000 | g0 >> f & 0xff00 | b0 >> f;
		}
	}

	public static final void shiftNumberARGB(int bd0[], int f) {
		int i = 0;
		for (int _L = bd0.length; i < _L; i++) {
			int rgb0 = bd0[i];
			int a0 = rgb0 & 0xff000000;
			int r0 = rgb0 & 0xff0000;
			int g0 = rgb0 & 0xff00;
			int b0 = rgb0 & 0xff;

			a0 = a0 < 0xff000000 ? a0 : 0xff000000;
			r0 = r0 < 0xff0000 ? r0 : 0xff0000;
			g0 = g0 < 0xff00 ? g0 : 0xff00;
			b0 = b0 < 0xff ? b0 : 0xff;

			bd0[i] = a0 >> f & 0xff000000 | r0 >> f & 0xff0000 | g0 >> f & 0xff00 | b0 >> f;
		}
	}

	public static final int[] getAlphaChannel(int[] bd0) {
		int i = 0, _L = bd0.length, ch[] = new int[_L], c;
		for (; i < _L; i++) {
			c = (bd0[i] >> 24) & 0xff;
			ch[i] = c << 24;
		}
		return ch;
	}

	public static final int[] getRedChannel(int[] bd0) {
		int i = 0, _L = bd0.length, ch[] = new int[_L], c;
		for (; i < _L; i++) {
			c = (bd0[i] >> 16) & 0xff;
			ch[i] = c << 16;
		}
		return ch;
	}

	public static final int[] getGreenChannel(int[] bd0) {
		int i = 0, _L = bd0.length, ch[] = new int[_L], c;
		for (; i < _L; i++) {
			c = (bd0[i] >> 8) & 0xff;
			ch[i] = c << 8;
		}
		return ch;
	}

	public static final int[] getBlueChannel(int[] bd0) {
		int i = 0, _L = bd0.length, ch[] = new int[_L], c;
		for (; i < _L; i++) {
			c = bd0[i] & 0xff;
			ch[i] = c;
		}
		return ch;
	}

	public static final int clamp(double n) {
		if (n > 0xff)
			return 0xff;
		if (n < 0)
			return 0;
		return (int) n;
	}

	public static final int clamp(int n) {
		if (n > 0xff)
			return 0xff;
		if (n < 0)
			return 0;
		return n;
	}

	public static final int[] apply(int[] _rgbArr, int w, int h, float[][] matrix) {
		int range_w = (int) (matrix.length / 2), range_h = (int) (matrix[0].length / 2);
		int x, y, i, j, pos, rgb;//, s = (2 * range_w + 1) * (2 * range_h + 1);
		float a, r, g, b;
		int[] new_rgbArr = new int[w * h];
		//float f = 1.0f/s;;
		float f;

		for (y = 0; y < h; y++) {
			for (x = 0; x < w; x++) {
				a = r = g = b = 0;
				for (i = -range_h; i <= range_h; i++) {
					for (j = -range_w; j <= range_w; j++) {
						pos = (y + i) * w + (x + j);

						if (pos < 0 || pos >= w * h) { // stage�� �Ѿ�°��� 0x00000000���� ó��
							rgb = 0;
						} else {
							rgb = _rgbArr[pos];
							f = matrix[j + range_w][i + range_h];

							a += ((rgb >> 24) & 0xff) * f;
							r += ((rgb >> 16) & 0xff) * f;
							g += ((rgb >> 8) & 0xff) * f;
							b += (rgb & 0xff) * f;
						}
					}
				}
				a = clamp(a + .5f);
				r = clamp(r + .5f);
				g = clamp(g + .5f);
				b = clamp(b + .5f);

				new_rgbArr[y * w + x] = ((int) a << 24) | ((int) r << 16) | ((int) g << 8) | (int) b;
			}
		}
		return new_rgbArr;
	}

	public static final int[] apply(int[] _rgbArr, int w, int h, float[][] matrix, boolean edgeFlag) {
		int range_w = (int) (matrix.length / 2), range_h = (int) (matrix[0].length / 2);
		int x, y, i, j, pos, rgb;//, s = (2 * range_w + 1) * (2 * range_h + 1);
		float a, r, g, b;
		int[] new_rgbArr = new int[w * h];
		//float f = 1.0f/s;;
		float f;

		if (edgeFlag) {
			for (y = 0; y < h; y++) {
				for (x = 0; x < w; x++) {
					a = r = g = b = 0;
					for (i = -range_h; i <= range_h; i++) {
						for (j = -range_w; j <= range_w; j++) {

							if ((y + i >= h && x + j >= w) || (y + i <= 0 && x + j <= 0)) {
								pos = y * w + x;
							} else if (y + i >= h || y + i <= 0) {
								pos = y * w + (x + j);
							} else if (x + j >= w || x + j <= 0) {
								pos = (y + i) * w + x;
							} else {
								pos = (y + i) * w + (x + j);
							}

							if (pos < 0 || pos >= w * h) { // stage�� �Ѿ�°��� 0x00000000���� ó��
								rgb = 0;
							} else {
								rgb = _rgbArr[pos];
								f = matrix[j + range_w][i + range_h];

								a += ((rgb >> 24) & 0xff) * f;
								r += ((rgb >> 16) & 0xff) * f;
								g += ((rgb >> 8) & 0xff) * f;
								b += (rgb & 0xff) * f;
							}
						}
					}
					a = clamp(a + .5f);
					r = clamp(r + .5f);
					g = clamp(g + .5f);
					b = clamp(b + .5f);

					new_rgbArr[y * w + x] = ((int) a << 24) | ((int) r << 16) | ((int) g << 8) | (int) b;
				}
			}
			return new_rgbArr;
		} else {
			return apply(_rgbArr, w, h, matrix);
		}
	}

	public static final int[] apply(int[] _rgbArr, int w, int h, PixelUtil filter) {
		int[] new_rgbArr = new int[w * h];

		filter.apply(_rgbArr, new_rgbArr);

		return new_rgbArr;
	}

	public static final int[] applyRGBFast(int _rgbArr[], int[] tempArray, int w, int h, float[][] matrix) {
		float f = 0.111115F;
		if (tempArray == null)
			tempArray = new int[w * h];
		for (int y = 1; y < h - 1; y++) {
			for (int x = 1; x < w - 1; x++) {
				int ig;
				int ib;
				int ir = ig = ib = 0;
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						int rgb = _rgbArr[(y + i) * w + (x + j)];
						ir += rgb & 0xff0000;
						ig += rgb & 0xff00;
						ib += rgb & 0xff;
					}
				}

				ir >>= 16;
				ig >>= 8;

				ir = (int) ((float) ir * f);
				ig = (int) ((float) ig * f);
				ib = (int) ((float) ib * f);

				ir = ir < 0xff ? ir : 0xff;
				ig = ig < 0xff ? ig : 0xff;
				ib = ib < 0xff ? ib : 0xff;

				tempArray[y * w + x] = 0xff000000 | ir << 16 | ig << 8 | ib;
			}
		}

		return tempArray;
	}

	public static final int[] applyARGBFast(int _rgbArr[], int[] tempArray, int w, int h, float[][] matrix) {
		float f = 0.111115F;
		f = matrix[0][1];

		int ia, ig, ib, ir;
		ir = ia = ig = ib = 0;

		if (tempArray == null)
			tempArray = new int[w * h];

		for (int y = 1; y < h - 1; y++) {
			for (int x = 1; x < w - 1; x++) {
				ir = ia = ig = ib = 0;
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						int rgb = _rgbArr[(y + i) * w + (x + j)];
						ia += (rgb >> 8) & 0xff0000;
						ir += rgb & 0xff0000;
						ig += rgb & 0xff00;
						ib += rgb & 0xff;
					}
				}

				ia >>= 16;
				ir >>= 16;
				ig >>= 8;

				ia = (int) ((float) ia * f);
				ir = (int) ((float) ir * f);
				ig = (int) ((float) ig * f);
				ib = (int) ((float) ib * f);

				ia = ia < 0xff ? ia : 0xff;
				ir = ir < 0xff ? ir : 0xff;
				ig = ig < 0xff ? ig : 0xff;
				ib = ib < 0xff ? ib : 0xff;

				tempArray[y * w + x] = ia << 24 | ir << 16 | ig << 8 | ib;
			}
		}

		return tempArray;
	}

	public static final int[] fadeIn() {
		return null;
	}

	public static final int[] fadeOut() {
		return null;
	}

	public static final int[] fade() {
		return null;
	}

	// Super sampling
	public int[] SSAA() {
		return null;
	}

	// Full-Scene anti-aliasing
	public int[] FSAA() {
		return null;
	}

	// Multi sample
	public int[] MSAA() {
		return null;
	}

	public int[] blending(BufferedImage image, BufferedImage otherImage, BLEND mode, Rectangle rect, float amount) {
		amount = Math.max(0, Math.min(1, amount));

		int[] data = new int[rect.width * rect.height];
		image.getRGB(rect.x, rect.y, rect.width, rect.height, data, 0, 1);
		int[] data2 = otherImage.getRGB(rect.x, rect.y, rect.width, rect.height, data, 0, 1);

		int p = data.length;
		int pix = p * 4;
		int pix1=0, pix2=0;
		int r1, g1, b1;
		int r2, g2, b2;
		int r3, g3, b3;
		int r4, g4, b4;

		int div_2_255 = 2 / 255;

		switch (mode) {
			case NORMAL :
				break;

			case MULTIPLY :
				while (p-- > 0) {
					data2[pix -= 4] = data[pix] * data2[pix] / 255;
					data2[pix1 = pix + 1] = data[pix1] * data2[pix1] / 255;
					data2[pix2 = pix + 2] = data[pix2] * data2[pix2] / 255;
				}
				break;

			case LIGHTEN :
				while (p-- > 0) {
					if ((r1 = data[pix -= 4]) > data2[pix])
						data2[pix] = r1;
					if ((g1 = data[pix1 = pix + 1]) > data2[pix1])
						data2[pix1] = g1;
					if ((b1 = data[pix2 = pix + 2]) > data2[pix2])
						data2[pix2] = b1;
				}
				break;

			case DARKEN :
				while (p-- > 0) {
					if ((r1 = data[pix -= 4]) < data2[pix])
						data2[pix] = r1;
					if ((g1 = data[pix1 = pix + 1]) < data2[pix1])
						data2[pix1] = g1;
					if ((b1 = data[pix2 = pix + 2]) < data2[pix2])
						data2[pix2] = b1;

				}
				break;

			case DARKERCOLOR :
				while (p-- > 0) {
					if (((r1 = data[pix -= 4]) * 0.3 + (g1 = data[pix1 = pix + 1]) * 0.59 + (b1 = data[pix2 = pix + 2]) * 0.11) <= (data2[pix] * 0.3 + data2[pix1] * 0.59 + data2[pix2] * 0.11)) {
						data2[pix] = r1;
						data2[pix1] = g1;
						data2[pix2] = b1;
					}
				}
				break;

			case LIGHTERCOLOR :
				while (p-- > 0) {
					if (((r1 = data[pix -= 4]) * 0.3 + (g1 = data[pix1 = pix + 1]) * 0.59 + (b1 = data[pix2 = pix + 2]) * 0.11) > (data2[pix] * 0.3 + data2[pix1] * 0.59 + data2[pix2] * 0.11)) {
						data2[pix] = r1;
						data2[pix1] = g1;
						data2[pix2] = b1;
					}
				}
				break;

			case LINEARDODGE :
				/*
				 * otherCtx.globalCompositeOperation = "source-over";
				 * otherCtx.drawImage(params.canvas, 0, 0);
				 * otherCtx.globalCompositeOperation = "lighter";
				 * otherCtx.drawImage(image, 0, 0);
				 */

				while (p-- > 0) {
					if ((r3 = data[pix -= 4] + data2[pix]) > 255)
						data2[pix] = 255;
					else
						data2[pix] = r3;
					if ((g3 = data[pix1 = pix + 1] + data2[pix1]) > 255)
						data2[pix1] = 255;
					else
						data2[pix1] = g3;
					if ((b3 = data[pix2 = pix + 2] + data2[pix2]) > 255)
						data2[pix2] = 255;
					else
						data2[pix2] = b3;
				}
				break;

			case LINEARBURN :
				while (p-- > 0) {
					if ((r3 = data[pix -= 4] + data2[pix]) < 255)
						data2[pix] = 0;
					else
						data2[pix] = (r3 - 255);
					if ((g3 = data[pix1 = pix + 1] + data2[pix1]) < 255)
						data2[pix1] = 0;
					else
						data2[pix1] = (g3 - 255);
					if ((b3 = data[pix2 = pix + 2] + data2[pix2]) < 255)
						data2[pix2] = 0;
					else
						data2[pix2] = (b3 - 255);
				}
				break;

			case DIFFERENCE :
				while (p-- > 0) {
					if ((r3 = data[pix -= 4] - data2[pix]) < 0)
						data2[pix] = -r3;
					else
						data2[pix] = r3;
					if ((g3 = data[pix1 = pix + 1] - data2[pix1]) < 0)
						data2[pix1] = -g3;
					else
						data2[pix1] = g3;
					if ((b3 = data[pix2 = pix + 2] - data2[pix2]) < 0)
						data2[pix2] = -b3;
					else
						data2[pix2] = b3;
				}
				break;

			case SCREEN :
				while (p-- > 0) {
					data2[pix -= 4] = (255 - (((255 - data2[pix]) * (255 - data[pix])) >> 8));
					data2[pix1 = pix + 1] = (255 - (((255 - data2[pix1]) * (255 - data[pix1])) >> 8));
					data2[pix2 = pix + 2] = (255 - (((255 - data2[pix2]) * (255 - data[pix2])) >> 8));
				}
				break;

			case EXCLUSION :
				while (p-- > 0) {
					data2[pix -= 4] = (r1 = data[pix]) - (r1 * div_2_255 - 1) * data2[pix];
					data2[pix1 = pix + 1] = (g1 = data[pix1]) - (g1 * div_2_255 - 1) * data2[pix1];
					data2[pix2 = pix + 2] = (b1 = data[pix2]) - (b1 * div_2_255 - 1) * data2[pix2];
				}
				break;

			case OVERLAY :

				while (p-- > 0) {
					if ((r1 = data[pix -= 4]) < 128)
						data2[pix] = data2[pix] * r1 * div_2_255;
					else
						data2[pix] = 255 - (255 - data2[pix]) * (255 - r1) * div_2_255;

					if ((g1 = data[pix1 = pix + 1]) < 128)
						data2[pix1] = data2[pix1] * g1 * div_2_255;
					else
						data2[pix1] = 255 - (255 - data2[pix1]) * (255 - g1) * div_2_255;

					if ((b1 = data[pix2 = pix + 2]) < 128)
						data2[pix2] = data2[pix2] * b1 * div_2_255;
					else
						data2[pix2] = 255 - (255 - data2[pix2]) * (255 - b1) * div_2_255;

				}
				break;

			case SOFTLIGHT :
				while (p-- > 0) {
					if ((r1 = data[pix -= 4]) < 128)
						data2[pix] = ((data2[pix] >> 1) + 64) * r1 * div_2_255;
					else
						data2[pix] = 255 - (191 - (data2[pix] >> 1)) * (255 - r1) * div_2_255;

					if ((g1 = data[pix1 = pix + 1]) < 128)
						data2[pix1] = ((data2[pix1] >> 1) + 64) * g1 * div_2_255;
					else
						data2[pix1] = 255 - (191 - (data2[pix1] >> 1)) * (255 - g1) * div_2_255;

					if ((b1 = data[pix2 = pix + 2]) < 128)
						data2[pix2] = ((data2[pix2] >> 1) + 64) * b1 * div_2_255;
					else
						data2[pix2] = 255 - (191 - (data2[pix2] >> 1)) * (255 - b1) * div_2_255;

				}
				break;

			case HARDLIGHT :
				while (p-- > 0) {
					if ((r2 = data2[pix -= 4]) < 128)
						data2[pix] = data[pix] * r2 * div_2_255;
					else
						data2[pix] = 255 - (255 - data[pix]) * (255 - r2) * div_2_255;

					if ((g2 = data2[pix1 = pix + 1]) < 128)
						data2[pix1] = data[pix1] * g2 * div_2_255;
					else
						data2[pix1] = 255 - (255 - data[pix1]) * (255 - g2) * div_2_255;

					if ((b2 = data2[pix2 = pix + 2]) < 128)
						data2[pix2] = data[pix2] * b2 * div_2_255;
					else
						data2[pix2] = 255 - (255 - data[pix2]) * (255 - b2) * div_2_255;

				}
				break;

			case COLORDODGE :
				while (p-- > 0) {
					if ((r3 = (data[pix -= 4] << 8) / (255 - (r2 = data2[pix]))) > 255 || r2 == 255)
						data2[pix] = 255;
					else
						data2[pix] = r3;

					if ((g3 = (data[pix1 = pix + 1] << 8) / (255 - (g2 = data2[pix1]))) > 255 || g2 == 255)
						data2[pix1] = 255;
					else
						data2[pix1] = g3;

					if ((b3 = (data[pix2 = pix + 2] << 8) / (255 - (b2 = data2[pix2]))) > 255 || b2 == 255)
						data2[pix2] = 255;
					else
						data2[pix2] = b3;
				}
				break;

			case COLORBURN :
				while (p-- > 0) {
					if ((r3 = 255 - ((255 - data[pix -= 4]) << 8) / data2[pix]) < 0 || data2[pix] == 0)
						data2[pix] = 0;
					else
						data2[pix] = r3;

					if ((g3 = 255 - ((255 - data[pix1 = pix + 1]) << 8) / data2[pix1]) < 0 || data2[pix1] == 0)
						data2[pix1] = 0;
					else
						data2[pix1] = g3;

					if ((b3 = 255 - ((255 - data[pix2 = pix + 2]) << 8) / data2[pix2]) < 0 || data2[pix2] == 0)
						data2[pix2] = 0;
					else
						data2[pix2] = b3;
				}
				break;

			case LINEARLIGHT :
				while (p-- > 0) {
					if (((r3 = 2 * (r2 = data2[pix -= 4]) + data[pix] - 256) < 0) || (r2 < 128 && r3 < 0)) {
						data2[pix] = 0;
					} else {
						if (r3 > 255)
							data2[pix] = 255;
						else
							data2[pix] = r3;
					}
					if (((g3 = 2 * (g2 = data2[pix1 = pix + 1]) + data[pix1] - 256) < 0) || (g2 < 128 && g3 < 0)) {
						data2[pix1] = 0;
					} else {
						if (g3 > 255)
							data2[pix1] = 255;
						else
							data2[pix1] = g3;
					}
					if (((b3 = 2 * (b2 = data2[pix2 = pix + 2]) + data[pix2] - 256) < 0) || (b2 < 128 && b3 < 0)) {
						data2[pix2] = 0;
					} else {
						if (b3 > 255)
							data2[pix2] = 255;
						else
							data2[pix2] = b3;
					}
				}
				break;

			case VIVIDLIGHT :
				while (p-- > 0) {
					if ((r2 = data2[pix -= 4]) < 128) {
						if (r2 != 0) {
							if ((r3 = 255 - ((255 - data[pix]) << 8) / (2 * r2)) < 0)
								data2[pix] = 0;
							else
								data2[pix] = r3;
						} else {
							data2[pix] = 0;
						}
					} else if ((r3 = (r4 = 2 * r2 - 256)) < 255) {
						if ((r3 = (data[pix] << 8) / (255 - r4)) > 255)
							data2[pix] = 255;
						else
							data2[pix] = r3;
					} else {
						if (r3 < 0)
							data2[pix] = 0;
						else
							data2[pix] = r3;
					}

					if ((g2 = data2[pix1 = pix + 1]) < 128) {
						if (g2 != 0) {
							if ((g3 = 255 - ((255 - data[pix1]) << 8) / (2 * g2)) < 0)
								data2[pix1] = 0;
							else
								data2[pix1] = g3;
						} else {
							data2[pix1] = 0;
						}
					} else if ((g3 = (g4 = 2 * g2 - 256)) < 255) {
						if ((g3 = (data[pix1] << 8) / (255 - g4)) > 255)
							data2[pix1] = 255;
						else
							data2[pix1] = g3;
					} else {
						if (g3 < 0)
							data2[pix1] = 0;
						else
							data2[pix1] = g3;
					}

					if ((b2 = data2[pix2 = pix + 2]) < 128) {
						if (b2 != 0) {
							if ((b3 = 255 - ((255 - data[pix2]) << 8) / (2 * b2)) < 0)
								data2[pix2] = 0;
							else
								data2[pix2] = b3;
						} else {
							data2[pix2] = 0;
						}
					} else if ((b3 = (b4 = 2 * b2 - 256)) < 255) {
						if ((b3 = (data[pix2] << 8) / (255 - b4)) > 255)
							data2[pix2] = 255;
						else
							data2[pix2] = b3;
					} else {
						if (b3 < 0)
							data2[pix2] = 0;
						else
							data2[pix2] = b3;
					}
				}
				break;

			case PINLIGHT :
				while (p-- > 0) {
					if ((r2 = data2[pix -= 4]) < 128)
						if ((r1 = data[pix]) < (r4 = 2 * r2))
							data2[pix] = r1;
						else
							data2[pix] = r4;
					else if ((r1 = data[pix]) > (r4 = 2 * r2 - 256))
						data2[pix] = r1;
					else
						data2[pix] = r4;

					if ((g2 = data2[pix1 = pix + 1]) < 128)
						if ((g1 = data[pix1]) < (g4 = 2 * g2))
							data2[pix1] = g1;
						else
							data2[pix1] = g4;
					else if ((g1 = data[pix1]) > (g4 = 2 * g2 - 256))
						data2[pix1] = g1;
					else
						data2[pix1] = g4;

					if ((r2 = data2[pix2 = pix + 2]) < 128)
						if ((r1 = data[pix2]) < (r4 = 2 * r2))
							data2[pix2] = r1;
						else
							data2[pix2] = r4;
					else if ((r1 = data[pix2]) > (r4 = 2 * r2 - 256))
						data2[pix2] = r1;
					else
						data2[pix2] = r4;
				}
				break;

			case HARDMIX :
				while (p-- > 0) {
					if ((r2 = data2[pix -= 4]) < 128)
						if (255 - ((255 - data[pix]) << 8) / (2 * r2) < 128 || r2 == 0)
							data2[pix] = 0;
						else
							data2[pix] = 255;
					else if ((r4 = 2 * r2 - 256) < 255 && (data[pix] << 8) / (255 - r4) < 128)
						data2[pix] = 0;
					else
						data2[pix] = 255;

					if ((g2 = data2[pix1 = pix + 1]) < 128)
						if (255 - ((255 - data[pix1]) << 8) / (2 * g2) < 128 || g2 == 0)
							data2[pix1] = 0;
						else
							data2[pix1] = 255;
					else if ((g4 = 2 * g2 - 256) < 255 && (data[pix1] << 8) / (255 - g4) < 128)
						data2[pix1] = 0;
					else
						data2[pix1] = 255;

					if ((b2 = data2[pix2 = pix + 2]) < 128)
						if (255 - ((255 - data[pix2]) << 8) / (2 * b2) < 128 || b2 == 0)
							data2[pix2] = 0;
						else
							data2[pix2] = 255;
					else if ((b4 = 2 * b2 - 256) < 255 && (data[pix2] << 8) / (255 - b4) < 128)
						data2[pix2] = 0;
					else
						data2[pix2] = 255;
				}
				break;
		}

		return data2;
	}
	/*
	 * public void multiplyToalpha(int[] bd0, float f) { int i=0, _L=bd0.length;
	 * int rgb0, a0; for(;i<_L;i++) { rgb0 = bd0[i]; a0 = (int)(((rgb0 >> 24) &
	 * 0xff)); rgb0 = rgb0 & 0x00ffffff;
	 * 
	 * a0 = clamp((int)(a0*f));
	 * 
	 * bd0[i] = (a0 << 24) | rgb0; } }
	 */
}
