package main.flask.imageprocessing;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import sun.awt.image.ByteInterleavedRaster;

public class ByteBlender {
	
	public final static int ADD = 1;
	public final static int SCREEN = 2;
	public final static int MULTIPLY = 3;
	
	// Blend.addARGBFast(bitmapData, Blend.applyARGBFast(bitmapData,
	// bitmapDataTemp, width, height, blurFilter.getMatrix())); // 38fps 26.31ms
	// bufferedImage.setRGB(0, 0, getWidth(), getHeight(), bitmapData, 0,
	// getWidth());
	// Blend.multiplyNumberARGB(bitmapData, 0.485f);
	
	// TODO apply
	public static final byte[] apply(byte src[], byte[] dst, int w, int h, float[][] matrix) {
		float f;
		final int mh = (matrix.length - 1) / 2;
		final int mw = (matrix[0].length - 1) / 2;
		int a, r, g, b, index;
		
		for (int y = mh; y < h - mh; y++) {
			for (int x = mw; x < w - mw; x++) {
				index = (x + y * w) * 4;
				a = r = g = b = 0;
				
				for (int i = -mh; i <= mh; i++) {
					for (int j = -mw; j <= mw; j++) {
						f = matrix[i][j];
						a += (int) (((src[index] < 0) ? src[index] + 256 : src[index]) * f);
						if (a > 0xff) a = 0xff;
						
						r += (int) (((src[index] < 0) ? src[index] + 256 : src[index]) * f);
						if (r > 0xff) r = 0xff;
						
						g += (int) (((src[index] < 0) ? src[index] + 256 : src[index]) * f);
						if (g > 0xff) g = 0xff;
						
						b += (int) (((src[index] < 0) ? src[index] + 256 : src[index]) * f);
						if (b > 0xff) b = 0xff;
					}
				}
				
				dst[index++] = (byte) a;
				dst[index++] = (byte) r;
				dst[index++] = (byte) g;
				dst[index++] = (byte) b;
			}
		}
		
		return dst;
	}
	
	public static final byte[] apply(byte src[], byte[] dst, int w, int h, int mw, int mh, final float f) {
		int a, r, g, b, index;
		
		for (int y = mh; y < h - mh; y++) {
			for (int x = mw; x < w - mw; x++) {
				index = (x + y * w) * 4;
				a = r = g = b = 0;
				
				for (int i = -mh; i <= mh; i++) {
					for (int j = -mw; j <= mw; j++) {
						a += (int) (((src[index] < 0) ? src[index] + 256 : src[index]) * f);
						if (a > 0xff) a = 0xff;
						
						r += (int) (((src[index] < 0) ? src[index] + 256 : src[index]) * f);
						if (r > 0xff) r = 0xff;
						
						g += (int) (((src[index] < 0) ? src[index] + 256 : src[index]) * f);
						if (g > 0xff) g = 0xff;
						
						b += (int) (((src[index] < 0) ? src[index] + 256 : src[index]) * f);
						if (b > 0xff) b = 0xff;
					}
				}
				
				dst[index++] = (byte) a;
				dst[index++] = (byte) r;
				dst[index++] = (byte) g;
				dst[index++] = (byte) b;
			}
		}
		
		return dst;
	}
	
	public static final BufferedImage apply(BufferedImage src, float f) {
		ByteInterleavedRaster srcRaster = (ByteInterleavedRaster) src.getRaster();
		byte[] srcData = srcRaster.getDataStorage();
		int length = srcData.length - 4;
		int t;
		
		// ABGR
		for (int i = 0; i < length;) {
			t = (int) (f * ((srcData[i] < 0) ? srcData[i] + 256 : srcData[i]));
			if (t > 0xff) t = 0xff;
			srcData[i++] = (byte) t;
			// srcData[i] = (byte) 0xff;
			
			t = (int) (f * ((srcData[i] < 0) ? srcData[i] + 256 : srcData[i]));
			if (t > 0xff) t = 0xff;
			srcData[i++] = (byte) t;
			
			t = (int) (f * ((srcData[i] < 0) ? srcData[i] + 256 : srcData[i]));
			if (t > 0xff) t = 0xff;
			srcData[i++] = (byte) t;
			
			t = (int) (f * ((srcData[i] < 0) ? srcData[i] + 256 : srcData[i]));
			if (t > 0xff) t = 0xff;
			srcData[i++] = (byte) t;
		}
		
		return src;
	}
	
	public static final byte[] add(byte[] src, byte[] dst) {
		int l = src.length / 4;
		int t;
		for (int i = 0; i < l;) {
			t = ((src[i] < 0) ? src[i] + 256 : src[i]) + ((dst[i] < 0) ? dst[i] + 256 : dst[i]);
			if (t > 0xff) t = 0xff;
			src[i++] = (byte) t;
			
			t = ((src[i] < 0) ? src[i] + 256 : src[i]) + ((dst[i] < 0) ? dst[i] + 256 : dst[i]);
			if (t > 0xff) t = 0xff;
			src[i++] = (byte) t;
			
			t = ((src[i] < 0) ? src[i] + 256 : src[i]) + ((dst[i] < 0) ? dst[i] + 256 : dst[i]);
			if (t > 0xff) t = 0xff;
			src[i++] = (byte) t;
			
			t = ((src[i] < 0) ? src[i] + 256 : src[i]) + ((dst[i] < 0) ? dst[i] + 256 : dst[i]);
			if (t > 0xff) t = 0xff;
			src[i++] = (byte) t;
		}
		return dst;
	}
	
	public static final byte[] screen(byte[] src, byte[] dst) {
		int l = src.length / 4;
		int t;
		for (int i = 0; i < l;) {
			t = 0xff - (((0xff - dst[i]) * (0xff - src[i])) / 0xff);
			if (t > 0xff) t = 0xff;
			src[i++] = (byte) t;
			
			t = 0xff - (((0xff - dst[i]) * (0xff - src[i])) / 0xff);
			if (t > 0xff) t = 0xff;
			src[i++] = (byte) t;
			
			t = 0xff - (((0xff - dst[i]) * (0xff - src[i])) / 0xff);
			if (t > 0xff) t = 0xff;
			src[i++] = (byte) t;
			
			t = 0xff - (((0xff - dst[i]) * (0xff - src[i])) / 0xff);
			if (t > 0xff) t = 0xff;
			src[i++] = (byte) t;
		}
		return dst;
	}
	
	public static final byte[] multiply(byte[] src, byte[] dst) {
		int l = src.length / 4;
		int t;
		for (int i = 0; i < l;) {
			t = (int) (((src[i] < 0) ? src[i] + 256 : src[i]) * ((dst[i] < 0) ? dst[i] + 256 : dst[i]) / 255.0f);
			if (t > 0xff) t = 0xff;
			src[i++] = (byte) t;
			
			t = (int) (((src[i] < 0) ? src[i] + 256 : src[i]) * ((dst[i] < 0) ? dst[i] + 256 : dst[i]) / 255.0f);
			if (t > 0xff) t = 0xff;
			src[i++] = (byte) t;
			
			t = (int) (((src[i] < 0) ? src[i] + 256 : src[i]) * ((dst[i] < 0) ? dst[i] + 256 : dst[i]) / 255.0f);
			if (t > 0xff) t = 0xff;
			src[i++] = (byte) t;
			
			t = (int) (((src[i] < 0) ? src[i] + 256 : src[i]) * ((dst[i] < 0) ? dst[i] + 256 : dst[i]) / 255.0f);
			if (t > 0xff) t = 0xff;
			src[i++] = (byte) t;
		}
		return dst;
	}
	
	public static final byte[] multiply(byte[] src, float f) {
		int l = src.length / 4;
		int t;
		for (int i = 0; i < l;) {
			t = (int) (((src[i] < 0) ? src[i] + 256 : src[i]) * f);
			if (t > 0xff) t = 0xff;
			src[i++] = (byte) t;
			
			t = (int) (((src[i] < 0) ? src[i] + 256 : src[i]) * f);
			if (t > 0xff) t = 0xff;
			src[i++] = (byte) t;
			
			t = (int) (((src[i] < 0) ? src[i] + 256 : src[i]) * f);
			if (t > 0xff) t = 0xff;
			src[i++] = (byte) t;
			
			t = (int) (((src[i] < 0) ? src[i] + 256 : src[i]) * f);
			if (t > 0xff) t = 0xff;
			src[i++] = (byte) t;
		}
		return src;
	}
	
	public static final BufferedImage filtering(BufferedImage src, BufferedImage dst, int mode) throws Exception {
		ByteInterleavedRaster srcRaster = (ByteInterleavedRaster) src.getRaster();
		ByteInterleavedRaster dstRaster = (ByteInterleavedRaster) dst.getRaster();
		
		byte[] srcData = srcRaster.getDataStorage();
		byte[] dstData = dstRaster.getDataStorage();
		
		getMethod(mode).filerting(srcData, dstData);
		
		return dst;
	}
	
	private static final FilteringCallback getMethod(int mode) throws Exception {
		switch (mode) {
			case ADD :
				return new FilteringCallback() {
					@Override
					public byte[] filerting(byte[] srcData, byte[] dstData) {
						return add(srcData, dstData);
					}
				};
			case SCREEN :
				return new FilteringCallback() {
					@Override
					public byte[] filerting(byte[] srcData, byte[] dstData) {
						return screen(srcData, dstData);
					}
				};
			case MULTIPLY :
				return new FilteringCallback() {
					@Override
					public byte[] filerting(byte[] srcData, byte[] dstData) {
						return multiply(srcData, dstData);
					}
				};
			default :
				throw new Exception();
		}
	}
	
	static interface FilteringCallback {
		byte[] filerting(byte[] srcData, byte[] dstData);
	}
}
