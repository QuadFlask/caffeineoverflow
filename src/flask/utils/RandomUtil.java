package flask.utils;

import java.util.Random;

public final class RandomUtil {
	private static final Random rand = new Random();

	public static boolean probability(double probability) {
		return rand.nextDouble() <= probability;
	}

	public static double range(double a, double b) {
		return a + b * rand.nextDouble();
	}

	public static double range(double a) {
		return a * rand.nextDouble();
	}
}
