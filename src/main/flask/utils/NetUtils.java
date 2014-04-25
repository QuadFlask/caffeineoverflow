package main.flask.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;

public class NetUtils {
	public final static String getIMacAddress() {
		try {
			InetAddress addr = InetAddress.getLocalHost();

			//			String ipAddr = addr.getHostAddress();
			//			String hostName = addr.getHostName();

			NetworkInterface ni = NetworkInterface.getByInetAddress(addr);
			byte[] mac = ni.getHardwareAddress();
			StringBuffer macAddr = new StringBuffer();

			int l = mac.length;
			for (int i = 0; i < l; i++) {
				macAddr.append(String.format("%02X%s", mac[i], (i < l - 1) ? "-" : ""));
			}

			return macAddr.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public final static String getIPAddress() {
		try {
			InetAddress addr = InetAddress.getLocalHost();

			return addr.getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public final static String getHostName() {
		try {
			InetAddress addr = InetAddress.getLocalHost();

			return addr.getHostName();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean IPv4Validation(String ip) {
		String[] classes = ip.split("\\.");
		if (ip.toUpperCase().endsWith("LOCALHOST")) {
			return true;
		}
		if (classes.length != 4 || ip.charAt(ip.length() - 1) == '.') {
			return false;
		}
		String port;
		if (ip.contains(":")) {
			//			System.out.println(classes[3]);
			String[] temp = classes[3].split(":");
			classes[3] = temp[0];
			if (temp.length == 2) {
				port = temp[1];
				if (Integer.valueOf(port) >= 100000) {
					return false;
				}
			} else {
				return false;
			}
		}
		try {
			for (int i = 0; i < classes.length; i++) {
				if (Integer.valueOf(classes[i]) > 255) {
					return false;
				}
			}
		} catch (NumberFormatException e) {
			if (ip.toUpperCase().endsWith("LOCALHOST")) {
				return true;
			}
		}
		return true;
	}
}
