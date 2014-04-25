package main.game.common;

import java.awt.*;
import java.awt.event.KeyEvent;

public class D { // contain main.game properties. have to make it read from
					// xml/txt file when initializing.
	public static final int SCREEN_WIDTH = 500;
	public static final int SCREEN_HEIGHT = 700;
	public static final int FPS = 60;
	public static final String FORM_TITLE = "Caffeine Overflow - ver.ALPHA";
	public static final int[] USING_KEYCODES = new int[] { KeyEvent.VK_LEFT,//
			KeyEvent.VK_RIGHT,//
			KeyEvent.VK_UP,//
			KeyEvent.VK_DOWN,//
			KeyEvent.VK_SPACE,//
			KeyEvent.VK_ENTER,//
			KeyEvent.VK_ESCAPE,//
			KeyEvent.VK_SHIFT,//
			KeyEvent.VK_Z,//
			KeyEvent.VK_1,//
			KeyEvent.VK_2,//
			KeyEvent.VK_3,//
			KeyEvent.VK_4,//
			KeyEvent.VK_5,//
			KeyEvent.VK_6,//
			KeyEvent.VK_7,//
			KeyEvent.VK_8,//
			KeyEvent.VK_9,//
			KeyEvent.VK_0, //
	};
	public static final Font FONT_CONSOLAS = new Font("consolas", Font.PLAIN, 11);
	public static final Color FONT_DEBUG = Color.green;
	public static final int DEFAULT_BOX_SIZE = 3;
	public static final int EXCLUDE_COLOR = 0xffff00ff;
	public static final boolean SKIP_NETWORK = true;

	public static boolean GRAPHIC_HIGH = false;
	public static boolean BGM_ON = false;

	public static final int PORT = 2331;
	public static String HOST = "localhost"; // "localhost"; //
												// 220.149.42.66

}
