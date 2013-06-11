package flask.utils;

/**
 * @author Flask
 * 
 */
public class Unicode {
	public static String decode(String unicode) throws Exception {
		StringBuffer str = new StringBuffer();
		char ch = 0;
		for (int i = unicode.indexOf("\\u"); i > -1; i = unicode.indexOf("\\u")) {
			ch = (char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16);
			str.append(unicode.substring(0, i));
			str.append(String.valueOf(ch));
			unicode = unicode.substring(i + 6);
		}
		str.append(unicode);
		return str.toString();
	}

	public static String encode(String unicode) throws Exception {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < unicode.length(); i++) {
			if (((int) unicode.charAt(i) == 32)) {
				str.append(" ");
				continue;
			}
			str.append("\\u");
			str.append(StringUtil.restrictLengthAndFillZero(Integer.toHexString((int) unicode.charAt(i)), 4));
		}
		return str.toString();
	}

	public static String Han_UnicodeNumberToString(int i, int m, int f) {
		i -= 0x1100;
		m -= 0x1161;
		f -= 0x11a8;
		char c = (char) ((((i * 588) + m * 28) + f) + 44032);
		return String.valueOf(c);
	}

	public static String Han_UnicodeNumberToString(int[] s) throws IllegalArgumentException {
		if (s.length != 3) throw new IllegalArgumentException();
		s[0] -= 0x1100;
		s[1] -= 0x1161;
		s[2] -= 0x11a8;
		char c = (char) ((((s[0] * 588) + s[1] * 28) + s[2]) + 44032);
		return String.valueOf(c);
	}

	public static int[] Han_CharacterToIMFUnicode(char s) {
		int[] result = new int[3];
		int a = s - 44032;
		result[0] = 0x1100 + ((a / 28) / 21);
		result[1] = 0x1161 + ((a / 21) % 21);
		result[2] = 0x11a8 + (a % 28);
		return result;
	}
}
