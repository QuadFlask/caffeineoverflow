package main.flask.utils;

public class GetOSEnviroment {
	
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static Boolean[] OSs = new Boolean[4];
	private static int WINDOWS = 0;
	private static int MAC = 1;
	private static int UNIX = 2;
	private static int SOLARIS = 3;
	
	static {
		OSs[WINDOWS] = OS.indexOf("win") >= 0;
		OSs[MAC] = OS.indexOf("mac") >= 0;
		OSs[UNIX] = OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0;
		OSs[SOLARIS] = OS.indexOf("sunos") >= 0;
	}
	
	public static Boolean isWindows() {
		return OSs[WINDOWS];
	}
	
	public static Boolean isMac() {
		return OSs[MAC];
	}
	
	public static Boolean isUnix() {
		return OSs[UNIX];
	}
	
	public static Boolean isSolaris() {
		return OSs[SOLARIS];
	}
}
