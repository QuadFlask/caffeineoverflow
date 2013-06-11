package flask.utils;

public class Vec2D {
	public double x, y;

	/**
	 * create new Vector2 object.
	 * initialize to (0, 0)
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
		double m = magnitude();
		if (m > 0) {
			x *= 1 / m;
			y *= 1 / m;
		}
	}

	/**
	 * get normalized
	 */
	public Vec2D getNormalized() {
		double m = magnitude();
		if (m > 0) {
			return new Vec2D(x * 1 / m, y * 1 / m);
		}else{
			return new Vec2D(0, 0);
		}
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

	public Vec2D getAdd(Vec2D v) {
		return new Vec2D(x + v.x, y + v.y);
	}

	/**
	 * 
	 * @param v
	 */
	public void sub(Vec2D v) {
		x -= v.x;
		y -= v.y;
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

	/**
	 * get clone of this
	 */
	public Vec2D clone() {
		return new Vec2D(x, y);
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
