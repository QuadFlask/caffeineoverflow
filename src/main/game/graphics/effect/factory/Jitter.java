package main.game.graphics.effect.factory;

import java.util.Random;

public class Jitter {
	private static Random rand = new Random();
	private static final float PI2 = (float) (Math.PI * 2);
	
	public static final float fromto(float min, float max) {
		return min + (max - min) * rand.nextFloat();
	}
	
	public static final float range(float center, float range) {
		return center + range * 2f * (rand.nextFloat() - 0.5f);
	}
	
	public static final double fromto(double min, double max) {
		return min + (max - min) * rand.nextDouble();
	}
	
	public static final double range(double center, double range) {
		return center + range * 2f * (rand.nextDouble() - 0.5f);
	}
	
	public static final float[] circularRange(float cx, float cy, float r, float[] result) {
		double rad = Math.random() * PI2;
		r *= Math.random();
		result[0] = cx + (float) (Math.cos(rad) * r);
		result[1] = cy + (float) (Math.sin(rad) * r);
		return result;
	}
}
