package flask.imageprocessing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ScreenCapture {

	public static enum IMAGE_FORMAT {
		JPG, PNG
	};

	public static void captureAndSave(int x, int y, int width, int height, String fileName, IMAGE_FORMAT format) {
		BufferedImage screencapture = null;
		try {
			screencapture = new Robot().createScreenCapture(new Rectangle(x, y, width, height));
			File file = new File("fileName" + format.toString());
			ImageIO.write(screencapture, format.toString(), file);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public static BufferedImage capture(int x, int y, int width, int height) {
		try {
			return new Robot().createScreenCapture(new Rectangle(x, y, width, height));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
}
