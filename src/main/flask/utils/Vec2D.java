package main.flask.utils;

import java.io.Serializable;

public class Vec2D implements Cloneable, Serializable {
	private static final long serialVersionUID = -4836206214806980385L;
	public double x, y;

	/**
	 * create new Vector2 object. initialize to (0, 0)
	 */
	public Vec2D() {
		setValues(0, 0);
	}

	/**
	 * create new Vector2 object.
	 * 
	 * @param _x
	 *            : x
	 * @param _y
	 *            : y
	 */
	public Vec2D(double _x, double _y) {
		x = _x;
		y = _y;
	}

	public Vec2D(Vec2D cl) {
		x = cl.x;
		y = cl.y;
	}

	/**
	 * create new Vector2 object.
	 * 
	 * @param $magnitude
	 *            : x
	 */
	public Vec2D(double $magnitude) {
		x = $magnitude;
	}

	/**
	 * x*=-1, y*=-1
	 */
	public void invert() {
		x = -x;
		y = -y;
	}

	/**
	 * x=y, y=x;
	 */
	public void invertXY() {
		double t = x;
		x = y;
		y = t;
	}

	/**
	 * length of Vector2
	 * 
	 * @return
	 */
	public double magnitude() {
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * set normalize
	 */
	public void normalize() {
		double m = Math.sqrt(x * x + y * y);
		if (m > 0) {
			x *= 1. / m;
			y *= 1. / m;
		}
	}

	/**
	 * get normalized
	 */
	public Vec2D getNormalized() {
		double m = Math.sqrt(x * x + y * y);
		if (m > 0) {
			return new Vec2D(x * 1 / m, y * 1 / m);
		} else {
			return new Vec2D(0, 0);
		}
	}

	public void setMagnitude(double newMagnitude) {
		double m = Math.sqrt(x * x + y * y);
		if (m > 0) {
			x *= newMagnitude / m;
			y *= newMagnitude / m;
		}
	}

	public double distanceWith(Vec2D b) {
		return Math.sqrt((x - b.x) * (x - b.x) + (y - b.y) * (y - b.y));
	}

	public double distanceWith2(Vec2D b) {
		return (x - b.x) * (x - b.x) + (y - b.y) * (y - b.y);
	}

	/**
	 * 
	 * @param n
	 */
	public void multiply(double n) {
		x *= n;
		y *= n;
	}

	/**
	 * 
	 * @param n
	 * @return
	 */
	public Vec2D getMultiply(double n) {
		return new Vec2D(x * n, y * n);
	}

	/**
	 * 
	 * @param v
	 */
	public void add(Vec2D v) {
		x += v.x;
		y += v.y;
	}

	public void add(double vx, double vy) {
		x += vx;
		y += vy;
	}

	public void addWithMultiply(Vec2D v, double multiply) {
		x += v.x * multiply;
		y += v.y * multiply;
	}

	public Vec2D getAdd(Vec2D v) {
		return new Vec2D(x + v.x, y + v.y);
	}

	public Vec2D getAdd(double vx, double vy) {
		return new Vec2D(x + vx, y + vy);
	}

	/**
	 * 
	 * @param v
	 */
	public void sub(Vec2D v) {
		x -= v.x;
		y -= v.y;
	}

	public void sub(double dx, double dy) {
		x -= dx;
		y -= dy;
	}

	/**
	 * 
	 * @param v
	 * @return
	 */
	public Vec2D getSub(Vec2D v) {
		return new Vec2D(x - v.x, y - v.y);
	}

	/**
	 * 
	 * @param v
	 * @return
	 */
	public Vec2D getSub(double dx, double dy) {
		return new Vec2D(x - dx, y - dy);
	}

	/**
	 * 
	 * @param v
	 * @param scale
	 */
	public void addScaledVector(Vec2D v, double scale) {
		x += v.x * scale;
		y += v.y * scale;
	}

	/**
	 * 
	 * @param v
	 * @return
	 */
	public double getScalarProduct(Vec2D v) {
		return x * v.x + y * v.y;
	}

	/**
	 * 
	 * @param v
	 * @return
	 */
	public Vec2D getcomponentProduct(Vec2D v) {
		return new Vec2D(x * v.x, y * v.y);
	}

	/**
	 * 
	 * @param v
	 */
	public void componentProduct(Vec2D v) {
		x *= v.x;
		y *= v.y;
	}

	/**
	 * 
	 * @param theta
	 */
	public void rotate(double theta) {
		double tx = x * Math.cos(theta) - y * Math.sin(theta);
		double ty = x * Math.sin(theta) + y * Math.cos(theta);
		x = tx;
		y = ty;
	}

	/**
	 * 
	 * @param theta
	 * @return
	 */
	public Vec2D getRotate(double theta) {
		return new Vec2D(x * Math.cos(theta) - y * Math.sin(theta), x * Math.sin(theta) + y * Math.cos(theta));
	}

	/**
	 * 
	 * @param angle
	 */
	public void rotateBy60(double angle) {
		double theta = angle * 0.017453292519943;
		rotate(theta);
	}

	/**
	 * 
	 * @param angle
	 * @return
	 */
	public Vec2D getRotateBy60(double angle) {
		double theta = angle * 0.017453292519943;
		return getRotate(theta);
	}

	/**
	 * 
	 * @param $x
	 * @param $y
	 */
	public void setValues(double $x, double $y) {
		x = $x;
		y = $y;
	}

	/**
	 * 
	 * @param $n
	 */
	public void setValueX(double $n) {
		x = $n;
	}

	/**
	 * 
	 * @param $n
	 */
	public void setValueY(double $n) {
		y = $n;
	}

	public void setXY(Vec2D v) {
		x = v.x;
		y = v.y;
	}

	/**
	 * get clone of this
	 */
	@Override
	public Vec2D clone() {
		return new Vec2D(x, y);
	}

	/**
	 * 
	 */
	public void clear() {
		x = y = 0;
	}

	@Override
	public String toString() {
		return "Vec2D [x=" + x + ", y=" + y + ", magnitude()=" + magnitude() + "]";
	}

	public double rotation() {
		return Math.atan2(y, x);
	}

	public double rotationAs60D() {
		return rotation() / Math.PI * 180;
	}

}
