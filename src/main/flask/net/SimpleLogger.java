package main.flask.net;

import java.io.PrintStream;
import java.util.Calendar;

public class SimpleLogger {
	
	public static boolean isLog = true;
	public static PrintStream stream = System.err;

	public static void log(String... logs) {
		String timeStamp = getTimeStamp();
		String indent = getIndent(timeStamp);

		write(timeStamp + logs[0]);

		for (int i = 1; i < logs.length - 1; i++)
			write(indent + logs[0]);

		write(getTimeStamp() + logs[0]);
	}

	public static void log(String log) {
		write(getTimeStamp() + log);
	}

	public static String getTimeStamp() {
		Calendar c = Calendar.getInstance();
		return String.format("[%02d:%02d:%02d.%03d] : ",
				c.get(Calendar.HOUR),
				c.get(Calendar.MINUTE),
				c.get(Calendar.SECOND),
				c.get(Calendar.MILLISECOND)
		);
	}

	private static String getIndent(String str) {
		String indent = "";
		for (int i = 0; i < str.length(); i++)
			indent += " ";
		return indent;
	}
	
	private static void write(String str) {
		if (isLog) stream.println(str);
	}

}
