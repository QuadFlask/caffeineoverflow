package main.flask.utils;

/**
 * @author Flask
 *
 */
public class Quaternion {
	public double w;
	public double x;
	public double y;
	public double z;
	
	/**
	 * Constructor of Quaternion
	 * @param $w : w
	 * @param $x : x
	 * @param $y : y
	 * @param $z : z
	 */
	public Quaternion(double $w, double $x, double $y, double $z) {
		w=$w;	x=$x;	y=$y;	z=$z;
	}
	
	/**
	 * Constructor of Quaternion
	 */
	public Quaternion() {
		w = x = z = y = 0;
	}
	
	/**
	 * make clone of Quaternion
	 */
	public Quaternion clone() {
		return new Quaternion(w, x, y, z);
	}
	
	/**
	 * add each elements(this + q)
	 * @param q : other Quaternion object
	 */
	public void add(Quaternion q) {
		w += q.w;	x += q.x;	y += q.y;	z += q.z;
	}
	
	/**
	 * return added Quaternion(this + q)
	 * @param q : other Quaternion object
	 * @return applyed Quaternion
	 */
	public Quaternion getAdd(Quaternion q) {
		Quaternion output = new Quaternion();
		output.w = w + q.w;
		output.x = x + q.x;
		output.y = y + q.y;
		output.z = z + q.z;
		return output;
	}
	
	/**
	 * subtraction each elements(this - q)
	 * @param q : other Quaternion object
	 */
	public void sub(Quaternion q) {
		w -= q.w;	x -= q.x;	y -= q.y;	z -= q.z;
	}
	
	/**
	 * return subtracted Quaternion(this - q)
	 * @param q : other Quaternion object
	 * @return applyed Quaternion
	 */
	public Quaternion getSub(Quaternion q) {
		Quaternion output = new Quaternion();
		output.w = w - q.w;
		output.x = x - q.x;
		output.y = y - q.y;
		output.z = z - q.z;
		return output;
	}
	
	/**
	 * make normalize
	 */
	public void normalize() {
		double mag = Math.sqrt(w * w + x * x + y * y + z * z);
		w /= mag;	x /= mag;	y /= mag;	z /= mag;
	}
	
	/**
	 * get normalize
	 */
	public Quaternion getNormalize() {
		Quaternion output = new Quaternion();
		double mag = Math.sqrt(w * w + x * x + y * y + z * z);
		output.w = w / mag;
		output.x = x / mag;
		output.y = y / mag;
		output.z = z / mag;
		return output;
	}
	
	/**
	 * get Euler angle to Quaternion
	 * @param pitch : x axis angle(rad)
	 * @param yaw : y axis angle(rad)
	 * @param roll : z axis angle(rad)
	 * @return applyed Quaternion
	 */
	public Quaternion getAngle2Quat(double pitch, double yaw, double roll){
		Quaternion f = new Quaternion();
		pitch /= 2;
		yaw /= 2;
		roll /= 2;
		
		double cx = Math.cos(pitch);
		double cy = Math.cos(yaw);
		double cz = Math.cos(roll);

		double sx = Math.sin(pitch);
		double sy = Math.sin(yaw);
		double sz = Math.sin(roll);
		
		f.w = cx * cy * cz + sx * sy * sz;
		f.x = sx * cy * cz - cx * sy * sz;
		f.y = cx * sy * cz + sx * cy * sz;
		f.z = cx * cy * sz - sx * sy * cz;
		
		return f;
	}
	
	/**
	 * set Euler angle to Quaternion
	 * @param pitch : x axis angle(rad)
	 * @param yaw : y axis angle(rad)
	 * @param roll : z axis angle(rad)
	 */
	public void setAngle(double pitch, double yaw, double roll){
		pitch /= 2;
		yaw /= 2;
		roll /= 2;
		
		double cx = Math.cos(pitch);
		double cy = Math.cos(yaw);
		double cz = Math.cos(roll);

		double sx = Math.sin(pitch);
		double sy = Math.sin(yaw);
		double sz = Math.sin(roll);
		
		w = cx * cy * cz + sx * sy * sz;
		x = sx * cy * cz - cx * sy * sz;
		y = cx * sy * cz + sx * cy * sz;
		z = cx * cy * sz - sx * sy * cz;
	}
	
	/**
	 * get Euler angle(rad)
	 * @return double[pitch, yaw, roll] 
	 */
	public double[] getEulerAngle(){
		double[] result = { Math.atan2(2 * (y * z + w * x), w * w - x * x - y * y + z * z), 
							Math.asin(-2 * (x * z - w * y)), 
							Math.atan2(2 * (x * y + w * z), w*w + x*x - y*y - z*z)};
		return result;
	}
	
	/**
	 * pitch - get y axis angle(rad)
	 * @return pitch(rad)
	 */
	public double getPitch(){
		return Math.atan2(2 * (y * z + w * x), w * w - x * x - y * y + z * z);
	}

	/**
	 * yaw - get x axis angle(rad)
	 * @return yaw(rad)
	 */
	public double getYaw() {
		return Math.asin(-2 * (x * z - w * y));
	}

	/**
	 * roll - get z axis angle(rad)
	 * @return roll(rad)
	 */
	public double getRoll() {
		return Math.atan2(2 * (x * y + w * z), w * w + x * x - y * y - z * z);
	}
	
	/**
	 * get w^2 + x^2 + y^2 + z^2
	 * @return double 
	 */
	public double square() {
		return w * w + x * x + y * y + z * z;
	}
	
	public String toString() {
		return "(w="+w+", x="+x+", y="+y+", z="+z+")";
	}
}
