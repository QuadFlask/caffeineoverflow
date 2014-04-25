package main.flask.utils;

import java.sql.Time;

public class TimeUtil {
	static Time time = new Time(0);

	public static String getTimeStamp() {
		time.setTime(System.currentTimeMillis());
		String stamp = String.format("%02d:%02d:%02d", time.getHours(), time.getMinutes(), time.getSeconds());
		return stamp;
	}

	public static String getTimeStamp_millis() {
		time.setTime(System.currentTimeMillis());
		String stamp = String.format("%02d:%02d:%02d.%03d", time.getHours(), time.getMinutes(), time.getSeconds(), time.getTime() % 1000);
		return stamp;
	}

	public static String getTimeStampWithYMD() {
		time.setTime(System.currentTimeMillis());
		String stamp = String.format("%04d%02d%02d%02d%02d%02d", time.getYear(), time.getMonth(), time.getDate(), time.getHours(), time.getMinutes(), time.getSeconds());
		return stamp;
	}
}
