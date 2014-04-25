package main.flask.utils;

public class ByteUtil {

	public static String toHEXString(byte[] data) {
		String result = "";
		for (byte i : data) {
			result += byte2Hex(i);
		}
		return result;
	}

	public static String byte2Hex(byte b) {
		int ia = (b >> 4) & 0xf;
		int ib = b & 0xf;
		String sa = Integer.toHexString(ia);
		String sb = Integer.toHexString(ib);

		return sa + sb;
	}

	public static byte[] Hex2Byte(String hex) {
		int n;
		hex = hex.replace("0x", "");
		if (hex.length() % 2 != 0) return null;

		byte[] result = new byte[hex.length() / 2];
		byte b1, b2;
		for (int i = 0; i < hex.length()/2; i++) {
			b1 = (byte) Integer.parseInt(hex.substring(i*2, (i+1)*2), 16);
			result[i] = b1;
		}

		return result;
	}

//	public static byte[] toBytes(String data, int t) {
//		byte[] result = new byte[t];
//		byte[] temp = new byte[2];
//
//		for (int i = 0; i < t; i += 2) {
//			temp = Hex2Byte(Integer.parseInt(data.substring(i, i + 2)));
//			result[i] = temp[0];
//			result[i + 1] = temp[1];
//		}
//
//		return result;
//	}

}

// hell o123
// 6865 6c6c 6f31 3233