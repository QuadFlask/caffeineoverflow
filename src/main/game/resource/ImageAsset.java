package main.game.resource;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import main.game.common.D;

public class ImageAsset {
	
	private static HashMap<IMAGE_ID, String> IMAGE_DIRECTORY = new HashMap<IMAGE_ID, String>();
	private static HashMap<IMAGE_ID, BufferedImage> IMAGE_LIST = new HashMap<IMAGE_ID, BufferedImage>();
	
	public static enum IMAGE_ID {
		FOOTER,
		HERO_NORMAL,
		ENEMY0,
		ENEMY3,
		ENEMY4,
		ENEMY5,
		ENEMY6,
		ENEMY7,
		ENEMY8,
		ENEMY9,
		ENEMY10,
		HERO_RIGHT,
		HERO_LEFT,
		LOGO,
		BULLET1,
		BULLET2,
		BULLET3,
		GAUGEBAR,
		COFFEE_BEAN,
		GLOW_POINT_GREEN,
		GLOW_POINT_RED,
		ROCK,
		SHEILD,
		WRENCH,
		BLACKHOLE,
		GLOW_POINT_YELLOW,
	}
	
	static {
		for (IMAGE_ID id : IMAGE_ID.values())
			IMAGE_DIRECTORY.put(id, imageDirectoryResolver(id));
		
		IMAGE_ID item = null;
		try {
			Iterator<IMAGE_ID> iter = IMAGE_DIRECTORY.keySet().iterator();
			while (iter.hasNext()) {
				item = iter.next();
				System.out.println("loading Files... " + item.toString());
				IMAGE_LIST.put(item, ImageIO.read(new File(IMAGE_DIRECTORY.get(item))));
			}
		} catch (IOException e) {
			if (item != null) System.err.println("error on " + item.toString());
			e.printStackTrace();
		}
	}
	
	private static String imageDirectoryResolver(IMAGE_ID id) {
		return String.format("%s/%s/%s.png", AssetConfigs.ASSET_ROOT, AssetConfigs.IMAGE_ROOT, id.name().toLowerCase());
	}
	
	public static int[][] image2Int(IMAGE_ID id) {
		return image2Int(id, D.EXCLUDE_COLOR);
	}
	
	public static List<int[][]> image2Int(IMAGE_ID... ids) {
		List<int[][]> result = new ArrayList<int[][]>(ids.length);
		for (IMAGE_ID id : ids)
			result.add(image2Int(id));
		return result;
	}
	
	public static int[][] image2Int(IMAGE_ID id, int excludeColor) {
		int[] argb = argbExtraction(id);
		for (int i = 0; i < argb.length; i++)
			if (argb[i] == excludeColor) argb[i] = 0;
		
		Dimension d = getImageSize(id);
		return convert2TwoDivision(argb, d.width, d.height);
	}
	
	public static Color[][] makeColorArray(int[][] shape) {
		int w = shape[0].length;
		int h = shape.length;
		Color[][] colors = new Color[h][w];
		
		for (int y = 0; y < shape.length; y++) {
			for (int x = 0; x < shape[0].length; x++) {
				if (shape[y][x] != 0) {
					colors[y][x] = new Color(shape[y][x], true);
				}
			}
		}
		
		return colors;
	}
	
	public static Color[][] makeColorArray(int[][] shape, Color singleColor) {
		int w = shape[0].length;
		int h = shape.length;
		Color[][] colors = new Color[h][w];
		
		for (int y = 0; y < shape.length; y++) {
			for (int x = 0; x < shape[0].length; x++) {
				if (shape[y][x] != 0) {
					colors[y][x] = singleColor;
				}
			}
		}
		
		return colors;
	}
	
	private static int[] argbExtraction(IMAGE_ID id) {
		Dimension rect = getImageSize(id);
		int[] result = new int[rect.width * rect.height];
		return IMAGE_LIST.get(id).getRGB(0, 0, rect.width, rect.height, result, 0, rect.width);
	}
	
	private static int[][] convert2TwoDivision(int[] arr, int w, int h) {
		int[][] result = new int[h][w];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				result[y][x] = arr[x + y * w];
			}
		}
		return result;
	}
	
	public static BufferedImage getImage(IMAGE_ID id) {
		return IMAGE_LIST.get(id);
	}
	
	private static Dimension getImageSize(IMAGE_ID id) {
		return new Dimension(IMAGE_LIST.get(id).getWidth(), IMAGE_LIST.get(id).getHeight());
	}
}
