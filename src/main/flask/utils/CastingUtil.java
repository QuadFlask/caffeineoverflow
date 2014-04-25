package main.flask.utils;

import java.util.ArrayList;

public class CastingUtil {

	public static int[] ArrayListInteger_To_intArray(ArrayList<Integer> input) {
		int L = input.size();
		int[] result = new int[L];
		for (int i = 0; i < L; i++) {
			result[i] = input.get(i);
		}
		return result;
	}

	public static int[] ArrayListInteger_To_intArray(ArrayList<Integer> input, int start, int limit) {
		int[] result = new int[limit];
		for (int i = start, k = 0; i < start + limit; i++, k++) {
			result[k] = input.get(i);
		}
		return result;
	}

}
