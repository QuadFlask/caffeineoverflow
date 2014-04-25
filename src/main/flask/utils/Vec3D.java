package main.flask.utils;

public class Vec3D {
	public double x, y, z;

	public Vec3D() {
	}

	public Vec3D(double $x, double $y, double $z) {
		x = $x;
		y = $y;
		z = $z;
	}

	public Vec3D(double $magnitude) {
		x = $magnitude;
	}

	public static Vec2DF getPerspectiveProjectionIn2DF(Vec3D target, Vec2DF to, float factor, Vec2DF center) {
		factor /= (target.z + factor);
		to.setValues(target.x * factor + center.x, target.y * factor + center.y);
		return to;
	}

	public void invert() {
		x = -x;
		y = -y;
		z = -z;
	}

	public double magnitude() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public void normalize() {
		double m = magnitude();
		if (m > 0) {
			x *= 1 / m;
			y *= 1 / m;
			z *= 1 / m;
		}
	}

	public void setValues(double $x, double $y, double $z) {
		x = $x;
		y = $y;
		z = $z;
	}

	public void setValueX(double $n) {
		x = $n;
	}

	public void setValueY(double $n) {
		y = $n;
	}

	public void setValueZ(double $n) {
		z = $n;
	}

	public void multiply(double n) {
		x *= n;
		y *= n;
		z *= n;
	}

	public Vec3D getMultiply(double n) {
		return new Vec3D(x * n, y * n, z * n);
	}

	public void add(Vec3D v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}

	public Vec3D getAdd(Vec3D v) {
		return new Vec3D(x + v.x, y + v.y, z + v.z);
	}

	public Vec3D getAdd(double x, double y, double z) {
		return new Vec3D(this.x + x, this.y + y, this.z + z);
	}

	public void sub(Vec3D v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}

	public Vec3D getSub(Vec3D v) {
		return new Vec3D(x - v.x, y - v.y, z - v.z);
	}

	public void addScaledVector(Vec3D v, double scale) {
		x += v.x * scale;
		y += v.y * scale;
		z += v.z * scale;
	}

	public double getScalarProduct(Vec3D v) {
		return x * v.x + y * v.y + z * v.z;
	}

	public Vec3D getcomponentProduct(Vec3D v) {
		return new Vec3D(x * v.x, y * v.y, z * v.z);
	}

	public void componentProduct(Vec3D v) {
		x *= v.x;
		y *= v.y;
		z *= v.z;
	}

	public void setOrientation(double $x, double $y, double $z) {

	}

	public Vec3D getPerspectiveProjection(Vec3D camPos, Quaternion camOri) {
		Vec3D d = new Vec3D();
		Vec3D f = new Vec3D();

		double tx = x - camPos.x;
		double ty = y - camPos.y;
		double tz = z - camPos.z;

		double X2 = 2 * camOri.x;
		double Y2 = 2 * camOri.y;
		double Z2 = 2 * camOri.z;

		double wX = camOri.w * X2;
		double wY = camOri.w * Y2;
		double wZ = camOri.w * Z2;
		double xX = camOri.x * X2;
		double xY = camOri.x * Y2;
		double xZ = camOri.x * Z2;
		double yY = camOri.y * Y2;
		double yZ = camOri.y * Z2;
		double zZ = camOri.z * Z2;

		d.x = (1 - (yY + zZ)) * tx + (xY - wZ) * ty + (xZ + wY) * tz;
		d.y = (xY + wZ) * tx + (1 - (xX + zZ)) * ty + (yZ - wX) * tz;
		d.z = (xZ - wY) * tx + (yZ + wX) * ty + (1 - (xX + yY)) * tz;

		// Projection coordinates
		double m = 200 / (200 - d.z);// fLen = 200
		f.x = d.x * m;
		f.y = d.y * m;
		f.z = d.z;

		return f;
	}

	public Vec2D getPerspectiveProjection(Vec3D camPos, Vec3D camOri) {
		Vec3D d = new Vec3D();
		Vec2D f = new Vec2D();
		Vec3D viewer = new Vec3D(0, 0, 200); // viewer

		double cx = Math.cos(camOri.x);
		double cy = Math.cos(camOri.y);
		double cz = Math.cos(camOri.z);

		double sx = Math.sin(camOri.x);
		double sy = Math.sin(camOri.y);
		double sz = Math.sin(camOri.z);

		double tx = x - camPos.x;
		double ty = y - camPos.y;
		double tz = z - camPos.z;

		d.x = cy * (sz * ty + cz * tx) - sy * tz;
		d.y = sx * (cy * tz + sy * (sz * ty + cz * tx)) + cx * (cz * ty - sz * tx);
		d.z = cx * (cy * tz + sy * (sz * ty + cz * tx)) - sx * (cz * ty - sz * tx);

		// d.x = cy*(tx*cz-ty*sz)+tz*sy;
		// d.y = tz*cy*sx+tx*(cz*sx*sy+cx*sz)+ty*(cx*cz-sx*sy*sz);
		// d.z = tz*cx*cy+tx*(sx*sz-cx*cz*sy)+ty*(cz*sx+cx*sy*sz);

		f.x = (d.x - viewer.x) * (viewer.z / d.z);
		f.y = (d.y - viewer.y) * (viewer.z / d.z);
		return f;
	}

	public Vec2D getPerspectiveProjectionIn2D(double factor, Vec2D center) {
		factor /= (z + factor);
		return new Vec2D(x * factor + center.x, y * factor + center.y);
	}

	public Vec2DF getPerspectiveProjectionIn2DF(float factor, Vec2DF center) {
		factor /= (z + factor);
		return new Vec2DF(x * factor + center.x, y * factor + center.y);
	}

	public Vec2DF getPerspectiveProjectionIn2DF(Vec2DF to, float factor, Vec2DF center) {
		factor /= (z + factor);
		to.setValues(x * factor + center.x, y * factor + center.y);
		return to;
	}

	public Vec3D clone() {
		return new Vec3D(x, y, z);
	}

	public void clear() {
		x = y = z = 0;
	}

	public String toString() {
		return "(x=" + x + ", y=" + y + ", z=" + z + ")";
	}
}
