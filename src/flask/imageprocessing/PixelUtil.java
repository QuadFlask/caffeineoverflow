package flask.imageprocessing;

public class PixelUtil {
	public static final int clamp(double n) {
		if (n >= 0xff)
			return 0xff;
		if (n <= 0)
			return 0;
		return (int) n;
	}

	public static final int clamp(float n) {
		if (n >= 0xff)
			return 0xff;
		if (n <= 0)
			return 0;
		return (int) n;
	}

	public static final int clamp(int n) {
		if (n >= 0xff)
			return 0xff;
		if (n <= 0)
			return 0;
		return n;
	}

	public static int mod(int a, int b) {
		int n = a / b;
		a -= n * b;
		if (a < 0)
			return a + b;
		return a;
	}

	public void apply(int[] _rgbArr, int[] new_rgbArr) {
		new_rgbArr = _rgbArr.clone();
	}
}
