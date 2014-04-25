package main.flask.imageprocessing;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class MotionBlurFilter {
	private static AffineTransform affine = new AffineTransform();

	/**
	 * get MotionBlured int array.
	 * 
	 * @param src
	 *            : source int array;
	 * @param dst
	 *            : motionblured int array
	 * @param width
	 *            : width
	 * @param height
	 *            : height;
	 * @param angle
	 *            : angle(rad)
	 * @param distance
	 *            : distance of blur
	 * @param zoom
	 *            : zooming value
	 * @param rotation
	 *            : rotation(rad)
	 * @param wrapEdges
	 *            : Edge process flag
	 */
	public static int[] perform(int[] src, int[] dst, int width, int height, double angle, float distance, float zoom, float rotation, boolean wrapEdges) {
		if (dst == null) {
			dst = new int[src.length];
		}

		float sinAngle = (float) Math.sin(angle);
		float cosAngle = (float) Math.cos(angle);

		int cx = width / 2;
		int cy = height / 2;
		int index = 0;

		float imageRadius = (float) Math.sqrt(cx * cx + cy * cy);
		float translateX = (float) (distance * cosAngle);
		float translateY = (float) (distance * -sinAngle);
		float maxDistance = distance + Math.abs(rotation * imageRadius) + zoom * imageRadius;
		int repetitions = (int) maxDistance;
		Point2D.Float p = new Point2D.Float();

		int a = 0, r = 0, g = 0, b = 0, rgb, count = 0;
		int newX, newY;
		float f, s;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				a = r = g = b = count = 0;
				for (int i = 0; i < repetitions; i++) {
					newX = x;
					newY = y;
					f = (float) i / repetitions;

					p.x = x;
					p.y = y;

					affine.setToIdentity();
					affine.translate(cx + f * translateX, cy + f * translateY);
					s = 1 - zoom * f;
					affine.scale(s, s);
					if (rotation != 0) {
						affine.rotate(-rotation * f);
					}
					affine.translate(-cx, -cy);
					affine.transform(p, p);
					newX = (int) p.x;
					newY = (int) p.y;

					if (newX < 0 || newX >= width) {
						if (wrapEdges) {
							newX = PixelUtil.mod(newX, width);
						} else {
							break;
						}
					}

					if (newY < 0 || newY >= width) {
						if (wrapEdges) {
							newY = PixelUtil.mod(newY, height);
						} else {
							break;
						}
					}

					count++;
					rgb = src[newY * width + newX];
					a += (rgb >> 24) & 0xff;
					r += (rgb >> 16) & 0xff;
					g += (rgb >> 8) & 0xff;
					b += rgb & 0xff;
				}

				if (count == 0) {
					dst[index] = src[index];
				} else {
					a = PixelUtil.clamp((int) (a / count));
					r = PixelUtil.clamp((int) (r / count));
					g = PixelUtil.clamp((int) (g / count));
					b = PixelUtil.clamp((int) (b / count));
					dst[index] = (a << 24) | (r << 16) | (g << 8) | b;
				}
				index++;
			}
		}
		return dst;
	}

	/**
	 * get only zoomed MotionBlured int array.
	 * 
	 * @param src
	 *            : source int array;
	 * @param dst
	 *            : motionblured int array
	 * @param width
	 *            : width
	 * @param height
	 *            : height;
	 * @param zoom
	 *            : zooming value
	 * @param wrapEdges
	 *            : Edge process flag
	 * @return : modified rgb array
	 */
	public static int[] zoom(int[] src, int[] dst, int width, int height, float zoom, boolean wrapEdges) {
		if (dst == null) {
			dst = new int[src.length];
		}

		int cx = width / 2;
		int cy = height / 2;
		int index = 0;

		float imageRadius = (float) Math.sqrt(cx * cx + cy * cy);
		float maxDistance = zoom * imageRadius;
		int repetitions = (int) maxDistance;
		Point2D.Float p = new Point2D.Float();

		int a = 0, r = 0, g = 0, b = 0, rgb, count = 0;
		int newX, newY;
		float f, s;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				a = r = g = b = count = 0;
				for (int i = 0; i < repetitions; i++) {
					newX = x;
					newY = y;
					f = (float) i / repetitions;

					p.x = x;
					p.y = y;

					affine.setToIdentity();
					affine.translate(cx + f, cy + f);
					s = 1 - zoom * f;
					affine.scale(s, s);
					affine.translate(-cx, -cy);
					affine.transform(p, p);
					newX = (int) p.x;
					newY = (int) p.y;

					if (newX < 0 || newX >= width) {
						if (wrapEdges) {
							newX = PixelUtil.mod(newX, width);
						} else {
							break;
						}
					}

					if (newY < 0 || newY >= width) {
						if (wrapEdges) {
							newY = PixelUtil.mod(newY, height);
						} else {
							break;
						}
					}

					count++;
					rgb = src[newY * width + newX];
					a += (rgb >> 24) & 0xff;
					r += (rgb >> 16) & 0xff;
					g += (rgb >> 8) & 0xff;
					b += rgb & 0xff;
				}

				if (count == 0) {
					dst[index] = src[index];
				} else {
					a = PixelUtil.clamp((int) (a / count));
					r = PixelUtil.clamp((int) (r / count));
					g = PixelUtil.clamp((int) (g / count));
					b = PixelUtil.clamp((int) (b / count));
					dst[index] = (a << 24) | (r << 16) | (g << 8) | b;
				}
				index++;
			}
		}
		return dst;
	}

	//	public BufferedImage filter(BufferedImage src, BufferedImage dst) {
	//		if (dst == null)
	//			dst = createCompatibleDestImage(src, null);
	//		BufferedImage tsrc = src;
	//		float cx = (float) src.getWidth() * centreX;
	//		float cy = (float) src.getHeight() * centreY;
	//		float imageRadius = (float) Math.sqrt(cx * cx + cy * cy);
	//		float translateX = (float) (distance * Math.cos(angle));
	//		float translateY = (float) (distance * -Math.sin(angle));
	//		float scale = zoom;
	//		int rotation;
	//		float rotate = rotation;
	//		float maxDistance = distance + Math.abs(rotation * imageRadius) + zoom * imageRadius;
	//		int steps = log2((int) maxDistance);
	//
	//		translateX /= maxDistance;
	//		translateY /= maxDistance;
	//		scale /= maxDistance;
	//		rotate /= maxDistance;
	//
	//		if (steps == 0) {
	//			Graphics2D g = dst.createGraphics();
	//			g.drawRenderedImage(src, null);
	//			g.dispose();
	//			return dst;
	//		}
	//
	//		BufferedImage tmp = createCompatibleDestImage(src, null);
	//		for (int i = 0; i < steps; i++) {
	//			Graphics2D g = tmp.createGraphics();
	//			g.drawImage(tsrc, null, null);
	//			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	//			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	//			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
	//
	//			g.translate(cx + translateX, cy + translateY);
	//			g.scale(1.0001 + scale, 1.0001 + scale); // The .0001 works round a bug on Windows where drawImage throws an ArrayIndexOutofBoundException
	//			if (rotation != 0)
	//				g.rotate(rotate);
	//			g.translate(-cx, -cy);
	//
	//			g.drawImage(dst, null, null);
	//			g.dispose();
	//			BufferedImage ti = dst;
	//			dst = tmp;
	//			tmp = ti;
	//			tsrc = dst;
	//
	//			translateX *= 2;
	//			translateY *= 2;
	//			scale *= 2;
	//			rotate *= 2;
	//		}
	//		return dst;
	//	}
	//
	//	private int log2(int n) {
	//		int m = 1;
	//		int log2n = 0;
	//
	//		while (m < n) {
	//			m *= 2;
	//			log2n++;
	//		}
	//		return log2n;
	//	}

}

/*
 * 
 * 
 * package com.jhlabs.image;
 * 
 * import java.awt.*; import java.awt.geom.*; import java.awt.image.*;
 * 
 * public class MotionBlurOp extends AbstractBufferedImageOp { private float
 * centreX = 0.5f, centreY = 0.5f; private float distance; private float angle;
 * private float rotation; private float zoom;
 * 
 * public MotionBlurOp() { }
 * 
 * public MotionBlurOp( float distance, float angle, float rotation, float zoom
 * ) { this.distance = distance; this.angle = angle; this.rotation = rotation;
 * this.zoom = zoom; }
 * 
 * public void setAngle( float angle ) { this.angle = angle; }
 * 
 * public float getAngle() { return angle; }
 * 
 * public void setDistance( float distance ) { this.distance = distance; }
 * 
 * public float getDistance() { return distance; }
 * 
 * public void setRotation( float rotation ) { this.rotation = rotation; }
 * 
 * public float getRotation() { return rotation; }
 * 
 * public void setZoom( float zoom ) { this.zoom = zoom; }
 * 
 * public float getZoom() { return zoom; }
 * 
 * public void setCentreX( float centreX ) { this.centreX = centreX; }
 * 
 * public float getCentreX() { return centreX; }
 * 
 * public void setCentreY( float centreY ) { this.centreY = centreY; }
 * 
 * public float getCentreY() { return centreY; }
 * 
 * public void setCentre( Point2D centre ) { this.centreX =
 * (float)centre.getX(); this.centreY = (float)centre.getY(); }
 * 
 * public Point2D getCentre() { return new Point2D.Float( centreX, centreY ); }
 * 
 * private int log2( int n ) { int m = 1; int log2n = 0;
 * 
 * while (m < n) { m *= 2; log2n++; } return log2n; }
 * 
 * public BufferedImage filter( BufferedImage src, BufferedImage dst ) { if (
 * dst == null ) dst = createCompatibleDestImage( src, null ); BufferedImage
 * tsrc = src; float cx = (float)src.getWidth() * centreX; float cy =
 * (float)src.getHeight() * centreY; float imageRadius = (float)Math.sqrt( cx*cx
 * + cy*cy ); float translateX = (float)(distance * Math.cos( angle )); float
 * translateY = (float)(distance * -Math.sin( angle )); float scale = zoom;
 * float rotate = rotation; float maxDistance = distance +
 * Math.abs(rotation*imageRadius) + zoom*imageRadius; int steps =
 * log2((int)maxDistance);
 * 
 * translateX /= maxDistance; translateY /= maxDistance; scale /= maxDistance;
 * rotate /= maxDistance;
 * 
 * if ( steps == 0 ) { Graphics2D g = dst.createGraphics(); g.drawRenderedImage(
 * src, null ); g.dispose(); return dst; }
 * 
 * BufferedImage tmp = createCompatibleDestImage( src, null ); for ( int i = 0;
 * i < steps; i++ ) { Graphics2D g = tmp.createGraphics(); g.drawImage( tsrc,
 * null, null ); g.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
 * RenderingHints.VALUE_ANTIALIAS_ON ); g.setRenderingHint(
 * RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR
 * ); g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.5f
 * ) );
 * 
 * g.translate( cx+translateX, cy+translateY ); g.scale( 1.0001+scale,
 * 1.0001+scale ); // The .0001 works round a bug on Windows where drawImage
 * throws an ArrayIndexOutofBoundException if ( rotation != 0 ) g.rotate( rotate
 * ); g.translate( -cx, -cy );
 * 
 * g.drawImage( dst, null, null ); g.dispose(); BufferedImage ti = dst; dst =
 * tmp; tmp = ti; tsrc = dst;
 * 
 * translateX *= 2; translateY *= 2; scale *= 2; rotate *= 2; } return dst; }
 * 
 * public String toString() { return "Blur/Motion Blur..."; } }
 */
