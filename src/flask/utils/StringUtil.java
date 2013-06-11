package flask.utils;

import java.util.ArrayList;

public class StringUtil {

	public static final char LF = 0x000A;

	public static String addComma(int n) {
		String str = "" + n;
		String result = "";
		int L = str.length();
		int j = 0;

		for (int i = L; i > 0; i -= 3) {
			j = i - 3;
			if (j < 0)
				j = 0;
			result = str.substring(j, i) + "," + result;
		}
		return result.substring(0, result.length() - 1);
	}

	public static String addComma(long n) {
		String str = "" + n;
		String result = "";
		int L = str.length();
		int j = 0;

		for (int i = L; i > 0; i -= 3) {
			j = i - 3;
			if (j < 0)
				j = 0;
			result = str.substring(j, i) + "," + result;
		}
		return result.substring(0, result.length() - 1);
	}

	public static String addZero(int n, int disit) {
		String r = "" + n;
		int l = r.length();
		if (l >= disit) {

		} else {
			for (int i = 0; i < disit - l; i++) {
				r = "0" + r;
			}
		}
		return r;
	}

	public static String addZero(long n, int disit) {
		String r = "" + n;
		int l = r.length();
		if (l >= disit) {

		} else {
			for (int i = 0; i < disit - l; i++) {
				r = "0" + r;
			}
		}
		return r;
	}

	public static String addZero(String s, int disit) {
		int l = s.length();
		if (disit <= l)
			return s;
		disit = disit - l;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < disit; i++) {
			sb.append("0");
		}
		sb.append(s);
		
		return sb.toString();
	}

	public static String setNumberFormat(String s, String format) {
		String result = "";

		for (int i = 0, j = 0; i < format.length(); i++) {
			if (format.charAt(i) == '0') {
				result += s.charAt(j++);
			} else {
				result += format.charAt(i);
			}
		}

		return result;
	}

	public static String restrictLength(String s, int length) {
		if (s.length() < length)
			return s;
		else
			return s.substring(0, length);
	}

	public static String restrictLengthAndFillZero(String s, int length) {
		StringBuffer str = new StringBuffer();
		if (s.length() < length) {
			int l = length - s.length();
			for (int i = 0; i < l; i++) {
				str.append("0");
			}
			str.append(s);
		} else {
			return s;
		}
		return str.toString();
	}

	public static String hexToText(String hex) {
		hex = hex.replace("0x", "");
		if (hex.length() < 2 || hex.length() % 2 == 1)
			return null;
		char c = 0;
		String full = "";
		String t;
		for (int i = 0; i < hex.length(); i += 2) {
			t = hex.substring(i, i + 2);
			c = (char) Integer.parseInt(t, 16);
			// null �����ϱ�
			if (c != 0x0000)
				full += c;
		}
		return full;
	}

	public static String textToHex(String txt) {
		// �ؽ�Ʈ ���� 1�� == 1char == 1Byte
		char c;
		byte b;
		StringBuffer result = new StringBuffer();
		String s;
		for (int i = 0; i < txt.length(); i++) {
			c = txt.charAt(i);
			s = ByteUtil.byte2Hex((byte) c);
			result.append(s);
		}
		return result.toString();
	}

	public static boolean foolSearch(String[] base, String target) {
		for (int i = 0; i < base.length; i++) {
			if (base[i].equals(target)) {
				return true;
			}
		}
		return false;
	}

	public static int[] foolSearch_getIndexes(String[] base, String target) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < base.length; i++) {
			if (base[i].equals(target)) {
				result.add(i);
			}
		}

		int[] temp = new int[result.size()];

		for (int i = 0; i < temp.length; i++) {
			temp[i] = result.get(i);
		}

		return temp;
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuffer hex = new StringBuffer();
		int b;

		for (int i = 0; i < bytes.length; i++) {
			b = bytes[i] & 0xff;
			hex.append(Integer.toHexString(b));
		}

		return hex.toString();
	}
	
	public static String[] getDivideBy3(String ori, int i1, int i2) {
		String s1 = ori.substring(0, i1);
		String s2 = ori.substring(i1, ori.length() - i2);
		String s3 = ori.substring(ori.length() - i2, ori.length());
		return new String[]{s1, s2, s3};
	}
	
	public static class TimeParser {
		public static final int MM_00_00_00 = 0;
		public static final int MM_0_00_00 = 1;
		public static final int MM_N_00_00 = 2;
		public static final int T00_00_00 = 3;
		public static final int T0_00_00 = 4;
		public static final int N_00_00 = 5;
		private static ArrayList<String> a;

		public static int[] parse(String ori, int format) {
			int[] result = new int[3]; // ��, ��, ��

			switch (format) {
				case MM_00_00_00 : {
					result[0] = Integer.parseInt(ori.substring(3, 5));
					result[1] = Integer.parseInt(ori.substring(6, 8));
					result[2] = Integer.parseInt(ori.substring(9, 11));
					String m = ori.substring(0, 2);
					if (m.equals("����") || m.toUpperCase().equals("AM")) {

					} else if (m.equals("����") || m.toUpperCase().equals("PM")) {
						if (result[0] != 12)
							result[0] += 12;
					} else {
						return null;
					}
					return result;
				}
				case MM_0_00_00 : {
					result[0] = Integer.parseInt(ori.substring(3, 4));
					result[1] = Integer.parseInt(ori.substring(5, 7));
					result[2] = Integer.parseInt(ori.substring(8, 10));
					String m = ori.substring(0, 2);
					if (m.equals("����") || m.toUpperCase().equals("AM")) {

					} else if (m.equals("����") || m.toUpperCase().equals("PM")) {
						if (result[0] != 12)
							result[0] += 12;
					} else {
						return null;
					}
					return result;
				}
				case MM_N_00_00 : {
					if (ori.length() == 10) {
						return parse(ori, MM_0_00_00);
					} else if (ori.length() == 11) {
						return parse(ori, MM_00_00_00);
					} else if (ori.length() == 8) {
						return parse(ori, T00_00_00);
					} else if (ori.length() == 7) {
						return parse(ori, T0_00_00);
					} else {
						return null;
					}
				}
				case T00_00_00 : {
					result[0] = Integer.parseInt(ori.substring(0, 2));
					result[1] = Integer.parseInt(ori.substring(3, 5));
					result[2] = Integer.parseInt(ori.substring(6, 8));
					return result;
				}
				case T0_00_00 : {
					result[0] = Integer.parseInt(ori.substring(0, 1));
					result[1] = Integer.parseInt(ori.substring(2, 4));
					result[2] = Integer.parseInt(ori.substring(5, 7));
					return result;
				}
				case N_00_00 : {
					if (ori.length() == 7) {
						return parse(ori, T00_00_00);
					} else if (ori.length() == 8) {
						return parse(ori, T0_00_00);
					} else {
						return null;
					}
				}
			}
			return null;
		}
	}
}
