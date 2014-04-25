package main.flask.utils;

public class Logger {
	
	private static boolean printTimeStamp = true;
	private static boolean print = true;
	public static String prefix = "";
	
	public static final String LINE_SHORT = "===============";
	public static final String LINE = "=================================";
	public static final String LINE_LONG = "===============================================================";
	
	public static enum LINE_SIZE {
		LONG,
		SHORT,
		NORMAL
	};
	
	public static void printTimeStamp() {
		System.out.print(" [" + TimeUtil.getTimeStamp() + "] > ");
	}
	
	public static void printTimeStamp_millis() {
		System.out.print(" [" + TimeUtil.getTimeStamp_millis() + "] > ");
	}
	
	public static void log(String log) {
		if (isPrint()) {
			if (isPrintTimeStamp())
				printTimeStamp();
			if (prefix != "")
				System.out.print(prefix);
			
			System.out.print(log);
		}
	}
	
	public static void log(String tag, String message) {
		if (isPrint()) {
			if (isPrintTimeStamp())
				printTimeStamp();
			System.out.print(tag + " | " + message);
		}
	}
	
	public static void logln(String log) {
		if (isPrint()) {
			if (isPrintTimeStamp())
				printTimeStamp();
			if (prefix != "")
				System.out.print(prefix);
			System.out.println(log);
		}
	}
	
	public static void logLine() {
		System.out.println(LINE);
	}
	
	public static void logLine(LINE_SIZE size) {
		String s = "";
		switch (size) {
			case LONG :
				s = LINE_LONG;
				break;
			case NORMAL :
				s = LINE;
				break;
			case SHORT :
				s = LINE_SHORT;
				break;
		}
		System.out.println(s);
	}
	
	public static void setPrefix(String prefix) {
		Logger.prefix = prefix;
	}
	
	public static boolean isPrintTimeStamp() {
		return printTimeStamp;
	}
	
	public static void setPrintTimeStamp(boolean printTimeStamp) {
		Logger.printTimeStamp = printTimeStamp;
	}
	
	public static boolean isPrint() {
		return print;
	}
	
	public static void setPrint(boolean print) {
		Logger.print = print;
	}
	
}
