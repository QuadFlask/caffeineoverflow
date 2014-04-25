package main.flask.utils;

public class Vec2DF {
	public float x, y;

	/**
	 * create new Vector2 object.
	 * initialize to (0, 0)
	 */
	public Vec2DF() {
		setValues(0, 0);
	}

	/**
	 * create new Vector2 object.
	 *
	 * @param _x : x
	 * @param _y : y
	 */
	public Vec2DF(float _x, float _y) {
		x = _x;
		y = _y;
	}

	/**
	 * create new Vector2 object.
	 *
	 * @param _x : x
	 * @param _y : y
	 */
	public Vec2DF(double _x, double _y) {
		x = (float) _x;
		y = (float) _y;
	}

	/**
	 * create new Vector2 object.
	 *
	 * @param $magnitude : x
	 */
	public Vec2DF(float $magnitude) {
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
		float t = x;
		x = y;
		y = t;
	}

	/**
	 * length of Vector2
	 *
	 * @return
	 */
	public float magnitude() {
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * set normalize
	 */
	public void normalize() {
		double m = magnitude();
		if (m > 0) {
			x *= 1 / m;
			y *= 1 / m;
		}
	}

	/**
	 * get normalized
	 */
	public Vec2DF getNormalized() {
		double m = magnitude();
		if (m > 0) {
			return new Vec2DF((float) (x * 1f / m), (float) (y * 1f / m));
		} else {
			return new Vec2DF(0, 0);
		}
	}

	/**
	 * @param n
	 */
	public void multiply(double n) {
		x *= n;
		y *= n;
	}

	/**
	 * @param n
	 * @return
	 */
	public Vec2DF getMultiply(double n) {
		return new Vec2DF((float) (x * n), (float) (y * n));
	}

	/**
	 * @param v
	 */
	public void add(Vec2DF v) {
		x += v.x;
		y += v.y;
	}

	public Vec2DF getAdd(Vec2DF v) {
		return new Vec2DF(x + v.x, y + v.y);
	}

	/**
	 * @param v
	 */
	public void sub(Vec2DF v) {
		x -= v.x;
		y -= v.y;
	}

	/**
	 * @param v
	 * @return
	 */
	public Vec2DF getSub(Vec2DF v) {
		return new Vec2DF(x - v.x, y - v.y);
	}

	/**
	 * @param v
	 * @param scale
	 */
	public void addScaledVector(Vec2DF v, float scale) {
		x += v.x * scale;
		y += v.y * scale;
	}

	/**
	 * @param v
	 * @return
	 */
	public float getScalarProduct(Vec2DF v) {
		return x * v.x + y * v.y;
	}

	/**
	 * @param v
	 * @return
	 */
	public Vec2DF getcomponentProduct(Vec2DF v) {
		return new Vec2DF(x * v.x, y * v.y);
	}

	/**
	 * @param v
	 */
	public void componentProduct(Vec2DF v) {
		x *= v.x;
		y *= v.y;
	}

	/**
	 * @param theta
	 */
	public void rotate(double theta) {
		float tx = (float) (x * Math.cos(theta) - y * Math.sin(theta));
		float ty = (float) (x * Math.sin(theta) + y * Math.cos(theta));
		x = tx;
		y = ty;
	}

	/**
	 * @param theta
	 * @return
	 */
	public Vec2DF getRotate(double theta) {
		return new Vec2DF((float) (x * Math.cos(theta) - y * Math.sin(theta)), (float) (x * Math.sin(theta) + y * Math.cos(theta)));
	}

	/**
	 * @param angle
	 */
	public void rotateBy60(float angle) {
		float theta = angle * 0.017453292519943f;
		rotate(theta);
	}

	/**
	 * @param angle
	 * @return
	 */
	public Vec2DF getRotateBy60(float angle) {
		float theta = angle * 0.017453292519943f;
		return getRotate(theta);
	}

	/**
	 * @param $x
	 * @param $y
	 */
	public void setValues(float $x, float $y) {
		x = $x;
		y = $y;
	}

	public void setValues(double $x, double $y) {
		x = (float) $x;
		y = (float) $y;
	}

	/**
	 * @param $n
	 */
	public void setValueX(float $n) {
		x = $n;
	}

	/**
	 * @param $n
	 */
	public void setValueY(float $n) {
		y = $n;
	}

	/**
	 * get clone of this
	 */
	public Vec2DF clone() {
		return new Vec2DF(x, y);
	}

	/**
	 *
	 */
	public void clear() {
		x = y = 0;
	}

	public String toString() {
		return "(x=" + x + ", y=" + y + ")";
	}
}
