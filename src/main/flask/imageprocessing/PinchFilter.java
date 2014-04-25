package main.flask.imageprocessing;

import java.awt.image.BufferedImage;

import sun.awt.image.IntegerInterleavedRaster;

public class PinchFilter {
	public static void filter(BufferedImage src, BufferedImage dst, final int cx, final int cy, final float radius, final float angle, final float amount) {
		final int w = src.getWidth(), dw = dst.getWidth(), dh = dst.getHeight(), hw = dw / 2, hh = dh / 2;
		final float radius2 = radius * radius;
		int x, y, tx = 0, ty = 0;
		float dx, dy, distance, a, d, t, s, c;
		
		IntegerInterleavedRaster srcRaster = (IntegerInterleavedRaster) src.getRaster();
		int[] srcData = srcRaster.getDataStorage();
		
		IntegerInterleavedRaster dstRaster = (IntegerInterleavedRaster) dst.getRaster();
		int[] dstData = dstRaster.getDataStorage();
		
		for (y = 0; y < dh; y++) {
			for (x = 0; x < dw; x++) {
				dx = x - hw;
				dy = y - hh;
				distance = dx * dx + dy * dy;
				
				if (distance > radius2) {
					tx = x + cx - hw;
					ty = y + cy - hh;
				} else {
					d = (float) Math.sqrt(distance / radius2);
					t = (float) Math.pow(Math.sin(Math.PI * 0.5 * d), -amount);
					
					dx *= t;
					dy *= t;
					
					a = angle * (1 - d) * (1 - d);
					
					s = (float) Math.sin(a);
					c = (float) Math.cos(a);
					
					tx = (int) (c * dx - s * dy) + cx;
					ty = (int) (s * dx + c * dy) + cy;
				}
				
				dstData[y * dw + x] = srcData[tx + ty * w];
			}
		}
	}
	
	public static void preIndexingFilter(final int srcWidth, BufferedImage dst, final int cx, final int cy, final float radius, final float angle, final float amount) {
		final int dw = dst.getWidth(), dh = dst.getHeight(), hw = dw / 2, hh = dh / 2;
		final float radius2 = radius * radius;
		int x, y, tx = 0, ty = 0;
		float dx, dy, distance, a, d, t, s, c;
		
		IntegerInterleavedRaster dstRaster = (IntegerInterleavedRaster) dst.getRaster();
		int[] dstData = dstRaster.getDataStorage();
		
		for (y = 0; y < dh; y++) {
			for (x = 0; x < dw; x++) {
				dx = x - hw;
				dy = y - hh;
				distance = dx * dx + dy * dy;
				
				if (distance > radius2) {
					tx = x + cx - hw;
					ty = y + cy - hh;
				} else {
					d = (float) Math.sqrt(distance / radius2);
					t = (float) Math.pow(Math.sin(Math.PI * 0.5 * d), -amount);
					
					dx *= t;
					dy *= t;
					
					a = angle * (1 - d) * (1 - d);
					
					s = (float) Math.sin(a);
					c = (float) Math.cos(a);
					
					tx = (int) (c * dx - s * dy) + cx;
					ty = (int) (s * dx + c * dy) + cy;
				}
				
				dstData[y * dw + x] = tx + ty * srcWidth;
			}
		}
	}
	
	// 쓰레딩 관리가 애매한데 동기화가 깨짐..ㅅㅂ
	@Deprecated
	public static void preIndexingFilterQuad(final int quad, final int srcWidth, BufferedImage dst, final int cx, final int cy, final float radius, final float angle, final float amount) {
		final int dw = dst.getWidth(), dh = dst.getHeight(), hw = dw / 2, hh = dh / 2;
		final float radius2 = radius * radius;
		int x, y, tx = 0, ty = 0;
		float dx, dy, distance, a, d, t, s, c;
		
		IntegerInterleavedRaster dstRaster = (IntegerInterleavedRaster) dst.getRaster();
		int[] dstData = dstRaster.getDataStorage();
		
		int sy = (quad / 2) * hh;
		int sx = (quad % 2) * hw;
		
		for (y = sy; y < sy + hh; y++) {
			for (x = sx; x < sx + hw; x++) {
				dx = x - hw;
				dy = y - hh;
				distance = dx * dx + dy * dy;
				
				if (distance > radius2) {
					tx = x + cx - hw;
					ty = y + cy - hh;
				} else {
					d = (float) Math.sqrt(distance / radius2);
					t = (float) Math.pow(Math.sin(Math.PI * 0.5 * d), -amount);
					
					dx *= t;
					dy *= t;
					
					a = angle * (1 - d) * (1 - d);
					
					s = (float) Math.sin(a);
					c = (float) Math.cos(a);
					
					tx = (int) (c * dx - s * dy) + cx;
					ty = (int) (s * dx + c * dy) + cy;
				}
				
				dstData[y * dw + x] = tx + ty * srcWidth;
			}
		}
	}
	
	public static void setWithIndex(BufferedImage src, BufferedImage dst) {
		final int dw = dst.getWidth(), dh = dst.getHeight(), L = dw * dh;
		
		IntegerInterleavedRaster srcRaster = (IntegerInterleavedRaster) src.getRaster();
		int[] srcData = srcRaster.getDataStorage();
		IntegerInterleavedRaster dstRaster = (IntegerInterleavedRaster) dst.getRaster();
		int[] dstData = dstRaster.getDataStorage();
		
		try {
			for (int i = 0; i < L; i++) {
				dstData[i] = srcData[dstData[i]];
			}
		} catch (Exception e) {
		}
	}
}
