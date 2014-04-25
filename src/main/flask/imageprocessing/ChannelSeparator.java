package main.flask.imageprocessing;

public class ChannelSeparator {

	public static final int[][] getRGBWChannels(int[] src, int width, int height) {
		int[][] result = new int[4][src.length];
		int a, r, g, b, rgb, pos;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pos = y * width + x;

				rgb = src[pos];
				a = (rgb >> 24) & 0xff;
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;

				result[0][pos] = (r << 24) | (a << 16) | (a << 8) | a;
				result[1][pos] = (g << 24) | (a << 16) | (a << 8) | a;
				result[2][pos] = (b << 24) | (a << 16) | (a << 8) | a;
				result[3][pos] = (a << 24) | (a << 16) | (a << 8) | a;
			}
		}

		return result;
	}

	public static final int[][] getRGBWChannelsWithColors(int[] src, int width, int height) {
		int[][] result = new int[4][src.length];
		int a, r, g, b, rgb, pos;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pos = y * width + x;

				rgb = src[pos];
				a = (rgb >> 24) & 0xff;
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;

				result[0][pos] = (r << 24) | (a << 16) | (0 << 8) | 0;
				result[1][pos] = (g << 24) | (0 << 16) | (a << 8) | 0;
				result[2][pos] = (b << 24) | (0 << 16) | (0 << 8) | a;
				a = (r + g + b) / 3;
				result[3][pos] = (a << 24) | 0xffffff;
			}
		}

		return result;
	}

}
