package main.flask.imageprocessing;

import java.awt.image.BufferedImage;

import sun.awt.image.IntegerInterleavedRaster;

public class TwirlFilter {

	private static float aaa=0;
	public static void filter(BufferedImage src, BufferedImage dst, float cx, float cy, float range, float stranth) {
		int x, y, w = src.getWidth(), h = src.getHeight(), tx, ty, dw = dst.getWidth(), dh = dst.getHeight();
		float dx, dy, distance, a, angle = aaa;
		aaa+=0.05f;

		IntegerInterleavedRaster srcRaster = (IntegerInterleavedRaster) src.getRaster();
		int[] srcData = srcRaster.getDataStorage();

		IntegerInterleavedRaster dstRaster = (IntegerInterleavedRaster) dst.getRaster();
		int[] dstData = dstRaster.getDataStorage();

		int minY = Math.max(0, (int) (cy - range));
		int maxY = Math.min(h, (int) (cy + range));

		int minX = Math.max(0, (int) (cx - range));
		int maxX = Math.min(w, (int) (cx + range));

		for (y = minY; y < maxY; y++) {
			for (x = minX; x < maxX; x++) {
				dx = x - cx;
				dy = y - cy;
				distance = (float) Math.sqrt(dx * dx + dy * dy);
				if (distance < range) {
					a = (float) Math.atan2(dy, dx) + angle * (range - distance) / range;
					tx = (int) (distance * Math.cos(a) + range );
					ty = (int) (distance * Math.sin(a) + range );
				} else {
					tx = (int) (dx + range);
					ty = (int) (dy + range);
				}

				dstData[tx + ty * dw] = srcData[x + y * w];
			}
		}
	}

	public static void filterInverse(BufferedImage src, BufferedImage dst, float cx, float cy, float range, float stranth) {
		int x, y, w = src.getWidth(), tx = 0, ty = 0, dw = dst.getWidth(), dh = dst.getHeight();
		float distance, theta;

		IntegerInterleavedRaster srcRaster = (IntegerInterleavedRaster) src.getRaster();
		IntegerInterleavedRaster dstRaster = (IntegerInterleavedRaster) dst.getRaster();

		int[] srcData = srcRaster.getDataStorage();
		int[] dstData = dstRaster.getDataStorage();

		for (y = 0; y < dh; y++) {
			for (x = 0; x < dw; x++) {
				x -= dw / 2;
				y -= dh / 2;

				distance = (float) Math.sqrt(x * x + y * y);
				if (distance < range) {
					theta = (float) Math.atan2(y, x);
					tx = (int) (cx + distance * Math.sqrt(distance) * Math.cos(theta) / range + range);
					ty = (int) (cy + distance * Math.sqrt(distance) * Math.sin(theta) / range + range);
				} else {
					tx = (int) (cx + x + range);
					ty = (int) (cy + y + range);
				}

				x += dw / 2;
				y += dh / 2;
				dstData[x + y * dw] = srcData[tx + ty * w];
			}
		}
	}

	public static void zoomLens(BufferedImage src, BufferedImage dst, float cx, float cy, float range, float stranth) {
		int x, y, w = src.getWidth(), tx = 0, ty = 0, dw = dst.getWidth(), dh = dst.getHeight();
		float distance, theta;

		IntegerInterleavedRaster srcRaster = (IntegerInterleavedRaster) src.getRaster();
		IntegerInterleavedRaster dstRaster = (IntegerInterleavedRaster) dst.getRaster();

		int[] srcData = srcRaster.getDataStorage();
		int[] dstData = dstRaster.getDataStorage();

		for (y = 0; y < dh; y++) {
			for (x = 0; x < dw; x++) {
				x -= dw / 2;
				y -= dh / 2;

				distance = (float) Math.sqrt(x * x + y * y);
				if (distance < range) {
					theta = (float) Math.atan2(y, x);
					tx = (int) (cx + distance * distance * Math.cos(theta) / range + range);
					ty = (int) (cy + distance * distance * Math.sin(theta) / range + range);
				} else {
					tx = (int) (cx + x + range);
					ty = (int) (cy + y + range);
				}

				x += dw / 2;
				y += dh / 2;
				dstData[x + y * dw] = srcData[tx + ty * w];
			}
		}
	}
}

//

//

//

//

//

//

//

//
