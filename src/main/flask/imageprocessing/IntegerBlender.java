package main.flask.imageprocessing;

import java.awt.image.BufferedImage;

import sun.awt.image.IntegerInterleavedRaster;

public class IntegerBlender {

	public final static int ADD = 1;
	public final static int SCREEN = 2;
	public final static int MULTIPLY = 3;

	public static final BufferedImage apply(BufferedImage src, float f) {
		IntegerInterleavedRaster srcRaster = (IntegerInterleavedRaster) src.getRaster();
		int[] srcData = srcRaster.getDataStorage();
		int i = 0, rgb0, a0, r0, g0, b0;

		for (int L = srcData.length; i < L; i++) {
			rgb0 = srcData[i];
			a0 = (int) (((rgb0 >> 24) & 0xff) * f);
			r0 = (int) (((rgb0 >> 16) & 0xff) * f);
			g0 = (int) (((rgb0 >> 8) & 0xff) * f);
			b0 = (int) ((rgb0 & 0xff) * f);

			a0 = a0 < 0xff ? a0 : 0xff;
			r0 = r0 < 0xff ? r0 : 0xff;
			g0 = g0 < 0xff ? g0 : 0xff;
			b0 = b0 < 0xff ? b0 : 0xff;

			srcData[i] = (a0 << 24) | (r0 << 16) | (g0 << 8) | b0;
		}

		return src;
	}

	public static final int[] add(int[] src, int[] dst) {
		int i = 0, rgb0, rgb1, a0, r0, g0, b0;

		for (int L = src.length; i < L; i++) {
			rgb0 = src[i];
			rgb1 = dst[i];
			a0 = (rgb0 >> 8) & 0xff0000;
			r0 = rgb0 & 0xff0000;
			g0 = rgb0 & 0xff00;
			b0 = rgb0 & 0xff;

			a0 += (rgb1 >> 8) & 0xff0000;
			r0 += rgb1 & 0xff0000;
			g0 += rgb1 & 0xff00;
			b0 += rgb1 & 0xff;

			a0 = a0 < 0xff0000 ? a0 : 0xff0000;
			r0 = r0 < 0xff0000 ? r0 : 0xff0000;
			g0 = g0 < 0xff00 ? g0 : 0xff00;
			b0 = b0 < 0xff ? b0 : 0xff;

			dst[i] = (a0 << 8) | r0 | g0 | b0;
		}

		return dst;
	}

	public static final int[] screen(int[] src, int[] dst) {
		int i = 0, _L = src.length, rgb0, rgb1;
		float a0, r0, g0, b0;
		float a1, r1, g1, b1;
		float ar0, ar1;
		for (; i < _L; i++) {
			rgb0 = src[i];
			rgb1 = dst[i];
			a0 = (rgb0 >> 24) & 0xff;
			r0 = (rgb0 >> 16) & 0xff;
			g0 = (rgb0 >> 8) & 0xff;
			b0 = rgb0 & 0xff;

			a1 = (rgb1 >> 24) & 0xff;
			r1 = (rgb1 >> 16) & 0xff;
			g1 = (rgb1 >> 8) & 0xff;
			b1 = rgb1 & 0xff;

			ar0 = a0 / 255f;
			ar1 = a1 / 255f;

			a0 = (0xff - (((0xff - a1) * (0xff - a0)) / 0xff));

			r0 = (0xff - (((0xff - r1 * ar1) * (0xff - r0 * ar0)) / 0xff));
			g0 = (0xff - (((0xff - g1 * ar1) * (0xff - g0 * ar0)) / 0xff));
			b0 = (0xff - (((0xff - b1 * ar1) * (0xff - b0 * ar0)) / 0xff));

			a0 = a0 < 0xff ? a0 : 0xff;
			r0 = r0 < 0xff ? r0 : 0xff;
			g0 = g0 < 0xff ? g0 : 0xff;
			b0 = b0 < 0xff ? b0 : 0xff;

			dst[i] = ((int) a0 << 24) | ((int) r0 << 16) | ((int) g0 << 8) | (int) b0;
		}

		return dst;
	}

	public static final int[] multiply(int[] srcData, float f) {
		int i = 0, rgb0, a0, r0, g0, b0;

		for (int L = srcData.length; i < L; i++) {
			rgb0 = srcData[i];
			a0 = (int) (((rgb0 >> 24) & 0xff) * f);
			r0 = (int) (((rgb0 >> 16) & 0xff) * f);
			g0 = (int) (((rgb0 >> 8) & 0xff) * f);
			b0 = (int) ((rgb0 & 0xff) * f);

			a0 = a0 < 0xff ? a0 : 0xff;
			r0 = r0 < 0xff ? r0 : 0xff;
			g0 = g0 < 0xff ? g0 : 0xff;
			b0 = b0 < 0xff ? b0 : 0xff;

			srcData[i] = (a0 << 24) | (r0 << 16) | (g0 << 8) | b0;
		}

		return srcData;
	}

	public static final int[] multiply(int[] src, int[] dst) {
		int i = 0, rgb0, rgb1, a0, r0, g0, b0;

		for (int L = src.length; i < L; i++) {
			rgb0 = src[i];
			rgb1 = dst[i];
			a0 = (rgb0 >> 24) & 0xff;
			r0 = (rgb0 >> 16) & 0xff;
			g0 = (rgb0 >> 8) & 0xff;
			b0 = rgb0 & 0xff;

			a0 *= (rgb0 >> 24) & 0xff;
			r0 *= (rgb0 >> 16) & 0xff;
			g0 *= (rgb0 >> 8) & 0xff;
			b0 *= rgb1 & 0xff;

			a0 >>= 8;
			r0 >>= 8;
			g0 >>= 8;
			b0 >>= 8;

			a0 = a0 < 0xff ? a0 : 0xff;
			r0 = r0 < 0xff ? r0 : 0xff;
			g0 = g0 < 0xff ? g0 : 0xff;
			b0 = b0 < 0xff ? b0 : 0xff;

			dst[i] = (a0 << 24) | (r0 << 16) | (g0 << 8) | b0;
		}

		return src;
	}

	public static final int[] overlay(int[] src, int[] dst) {
		int i = 0, rgb0, rgb1, a0, r0, g0, b0, a1, r1, g1, b1;

		for (int L = src.length; i < L; i++) {
			rgb0 = src[i];
			rgb1 = dst[i];
			a0 = (rgb0 >> 24) & 0xff;
			r0 = (rgb0 >> 16) & 0xff;
			g0 = (rgb0 >> 8) & 0xff;
			b0 = rgb0 & 0xff;

			a1 = (rgb1 >> 24) & 0xff;
			r1 = (rgb1 >> 16) & 0xff;
			g1 = (rgb1 >> 8) & 0xff;
			b1 = rgb1 & 0xff;

			a0 = (a0 < 0x7f) ? a0 * a1 / 0x7f : 0xff - (0xff - a0) * (0xff - a1) / 0x7f;
			r0 = (r0 < 0x7f) ? r0 * r1 / 0x7f : 0xff - (0xff - r0) * (0xff - r1) / 0x7f;
			g0 = (g0 < 0x7f) ? g0 * g1 / 0x7f : 0xff - (0xff - g0) * (0xff - g1) / 0x7f;
			b0 = (b0 < 0x7f) ? b0 * b1 / 0x7f : 0xff - (0xff - b0) * (0xff - b1) / 0x7f;

			a0 = a0 < 0xff ? a0 : 0xff;
			r0 = r0 < 0xff ? r0 : 0xff;
			g0 = g0 < 0xff ? g0 : 0xff;
			b0 = b0 < 0xff ? b0 : 0xff;

			dst[i] = (a0 << 24) | (r0 << 16) | (g0 << 8) | b0;
		}

		return dst;
	}

	public static final int[] overlay(int[] src, int[] dst, int[] res) {
		int i = 0, rgb0, rgb1, a0, r0, g0, b0, a1, r1, g1, b1;
		float ar0, ar1;

		for (int L = src.length; i < L; i++) {
			rgb0 = src[i];
			rgb1 = dst[i];
			a0 = (rgb0 >> 24) & 0xff;
			r0 = (rgb0 >> 16) & 0xff;
			g0 = (rgb0 >> 8) & 0xff;
			b0 = rgb0 & 0xff;

			a1 = (rgb1 >> 24) & 0xff;
			r1 = (rgb1 >> 16) & 0xff;
			g1 = (rgb1 >> 8) & 0xff;
			b1 = rgb1 & 0xff;

			ar0 = a0 / 255f;
			ar1 = a1 / 255f;

			a0 = (a1 < 128) ? (int) (2 * a0 * a1 / 255) : (int) (255 * (1 - 2 * (1 - a0 / 255.0) * (1 - a1 / 255.0)));
			r0 = (r1 < 128) ? (int) (2 * r0 * r1 / 255 * ar0 * ar1) : (int) (255 * (1 - 2 * (1 - r0 * ar0 / 255.0) * (1 - r1 * ar1 / 255.0)));
			g0 = (g1 < 128) ? (int) (2 * g0 * g1 / 255 * ar0 * ar1) : (int) (255 * (1 - 2 * (1 - g0 * ar0 / 255.0) * (1 - g1 * ar1 / 255.0)));
			b0 = (b1 < 128) ? (int) (2 * b0 * b1 / 255 * ar0 * ar1) : (int) (255 * (1 - 2 * (1 - b0 * ar0 / 255.0) * (1 - b1 * ar1 / 255.0)));

			// a0 = (a1 < 0x7f) ? (a0 * a1) / 0x7f : 0xff - (0xff - a0) * (0xff
			// - a1) / 0x7f;
			// r0 = (r1 < 0x7f) ? (r0 * r1) / 0x7f : 0xff - (0xff - r0) * (0xff
			// - r1) / 0x7f;
			// g0 = (g1 < 0x7f) ? (g0 * g1) / 0x7f : 0xff - (0xff - g0) * (0xff
			// - g1) / 0x7f;
			// b0 = (b1 < 0x7f) ? (b0 * b1) / 0x7f : 0xff - (0xff - b0) * (0xff
			// - b1) / 0x7f;

			a0 = a0 <= 0xff ? a0 : 0xff;
			r0 = r0 <= 0xff ? r0 : 0xff;
			g0 = g0 <= 0xff ? g0 : 0xff;
			b0 = b0 <= 0xff ? b0 : 0xff;

			res[i] = (a0 << 24) | (r0 << 16) | (g0 << 8) | b0;
		}

		return res;
	}

	public static final BufferedImage filtering(BufferedImage src, BufferedImage dst, int mode) throws Exception {
		IntegerInterleavedRaster srcRaster = (IntegerInterleavedRaster) src.getRaster();
		IntegerInterleavedRaster dstRaster = (IntegerInterleavedRaster) dst.getRaster();

		int[] srcData = srcRaster.getDataStorage();
		int[] dstData = dstRaster.getDataStorage();

		getMethod(mode).filerting(srcData, dstData);

		return dst;
	}

	private static final FilteringCallback getMethod(int mode) throws Exception {
		switch (mode) {
		case ADD:
			return new FilteringCallback() {
				@Override
				public int[] filerting(int[] srcData, int[] dstData) {
					return add(srcData, dstData);
				}
			};
		case SCREEN:
			return new FilteringCallback() {
				@Override
				public int[] filerting(int[] srcData, int[] dstData) {
					return screen(srcData, dstData);
				}
			};
		case MULTIPLY:
			return new FilteringCallback() {
				@Override
				public int[] filerting(int[] srcData, int[] dstData) {
					return multiply(srcData, dstData);
				}
			};
		default:
			throw new Exception();
		}
	}

	static interface FilteringCallback {
		int[] filerting(int[] srcData, int[] dstData);
	}
}
