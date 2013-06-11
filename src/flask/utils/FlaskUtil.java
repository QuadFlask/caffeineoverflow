package flask.utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author Flask
 * 
 */
public final class FlaskUtil {
	
	public final static double RAD = 180 / Math.PI;
	public final static double inverseRAD = Math.PI / 180;
	public final static Vec2D Zpoint = new Vec2D(0, 0);
	
	public final static Color[] COLOR_BLACK2WHITE = { new Color(0x000000), new Color(0xffffff) };
	public final static Color[] COLOR_WHITE2BLACK = { new Color(0xffffff), new Color(0x000000) };
	public final static Color[] COLOR_FIRE = { new Color(0xff0000), new Color(0xffff00), new Color(0xffffff) };
	public final static Color[] COLOR_FIRE_ALPHA = { new Color(0), new Color(0xff880000), new Color(0x88ff0000), new Color(0xccffff00), new Color(0xffffffff) };
	public final static Color[] COLOR_FIRE_INVERSE = { new Color(0xffffff), new Color(0xffff00), new Color(0xff0000) };
	public final static Color[] COLOR_BLUE_FIRE = { new Color(0x0000ff), new Color(0x00ffff), new Color(0xffffff) };
	public final static Color[] COLOR_BLUE_FIRE_INVERSE = { new Color(0xffffff), new Color(0x00ffff), new Color(0x0000ff) };
	public final static Color[] COLOR_RAINBOW = { new Color(0xff0000), new Color(0xffff00), new Color(0x00ff00), new Color(0x00ffff), new Color(0x0000ff), new Color(0xff00ff) };
	public final static Color[] COLOR_RAINBOW_INVERSE = { new Color(0xff00ff), new Color(0x0000ff), new Color(0x00ffff), new Color(0x00ff00), new Color(0xffff00), new Color(0xff0000) };
	public final static Color[] COLOR_RAINBOW_REDEND = { new Color(0xff0000), new Color(0xffff00), new Color(0x00ff00), new Color(0x00ffff), new Color(0x0000ff), new Color(0xff00ff), new Color(0xff0000) };
	public final static Color[] COLOR_RAINBOW_REDEND_INVERSE = { new Color(0xff0000), new Color(0xff00ff), new Color(0x0000ff), new Color(0x00ffff), new Color(0x00ff00), new Color(0xffff00), new Color(0xff0000) };
	
	public final static void trace(Object... args) {
		int i = 0, l = args.length - 1;
		for (; i < l; i++) {
			System.out.print(args[i] + ", ");
		}
		System.out.println(args[l]);
	}
	
	public final static double linearInterpolate(double from, double to, double t) {
		if (t >= 1)
			return from;
		else if (t <= 0)
			return to;
		return (to - from) * t + from;
	}
	
	public final static float linearInterpolate(float from, float to, float t) {
		if (t >= 1)
			return from;
		else if (t <= 0)
			return to;
		return (to - from) * t + from;
	}
	
	public final static Vec2D linearInterpolate(Vec2D from, Vec2D to, double t) {
		if (t >= 1)
			t = 1;
		else if (t <= 0)
			t = 0;
		Vec2D v = new Vec2D();
		v.x = linearInterpolate(from.x, to.x, t);
		v.y = linearInterpolate(from.y, to.y, t);
		return v;
	}
	
	/**
	 * ������ƽ �Լ�(S-Line)
	 * 
	 * @param $step
	 *            : logistic ���� ���� [0, 1]
	 * @param $from
	 *            : ó����
	 * @param $to
	 *            : ������ ��
	 * @return : logistic �Լ� ���԰�
	 */
	public final static double logistic(double $step, double $from, double $to) {
		double gap = $to - $from;
		double v = 1 / (1 + Math.exp(-$step)); // (0~1)
		double resultv = $from + gap * v;
		
		return resultv;
	}
	
	/**
	 * ������ƽ �Լ�(S-Line) : (0~1) ����ȭ
	 * 
	 * @param $n
	 *            : ���� ����
	 * @return : ���� logistic�Լ���
	 */
	public final static double unit_logistic(double $n) {
		double v;
		v = 1 / (1 + Math.exp(-8 * ($n - .5))); // (0~1) -> (0~1)
		if (v < 0)
			v = 0;
		else if (v > 1)
			v = 1;
		return v;
	}
	
	/**
	 * ���巯ƽ ������ �Լ�
	 * 
	 * @param t
	 *            : ����
	 * @param B
	 *            : Ŀ�� ����Ʈ
	 * @return : ������ � ��
	 */
	public final static Vec2D unit_Quadratic_Bezier(double t, Vec2D B) {
		if (t < 0 || t > 1)
			return null;//throw new Error("t must in [0, 1] : + bezier_curve()");
			
		t *= 2;
		
		Vec2D _B = new Vec2D(B.x, B.y);
		Vec2D P = new Vec2D();
		Vec2D Unitpoint = new Vec2D(1, 1);
		
		if (t < 1) {
			P.x = (1 - t * t) * Zpoint.x + 2 * t * (1 - t) * B.x + t * t * Unitpoint.x;
			P.y = (1 - t * t) * Zpoint.y + 2 * t * (1 - t) * B.y + t * t * Unitpoint.y;
		} else {
			t = t - 1;
			_B.x = 1 - B.x;
			_B.y = 1 - B.y;
			P.y = 1 + (1 - t * t) * Zpoint.x + 2 * t * (1 - t) * _B.y + t * t * Unitpoint.x;
			P.x = 1 + (1 - t * t) * Zpoint.y + 2 * t * (1 - t) * _B.x + t * t * Unitpoint.y;
		}
		
		return P;
	}
	
	/**
	 * ���п� ���� ��Ī�� ���
	 * 
	 * @param A
	 *            : ���� l �� ���� ��
	 * @param B
	 *            : ���� l �� �ٸ��� ��
	 * @param p
	 *            : ��Ī�� ��
	 * @param intersection
	 *            : ������ ���Ұ�����
	 * @return : ��Ī�� ���
	 */
	public final static Vec2D getAxialSymmetry(Vec2D A, Vec2D B, Vec2D p, boolean intersection) {
		Vec2D at = new Vec2D();
		double diff_x, diff_y;
		
		diff_x = A.x - B.x;
		diff_y = A.y - B.y;
		
		//175, 95
		double l_a = diff_y / diff_x;
		double l_b = A.y - (l_a * A.x);
		//		double lp_a = -1 / l_a;
		//		double lp_b = p.y - (lp_a * p.x);
		
		at.x = (p.x + l_a * p.y - l_a * l_b) / (l_a * l_a + 1);
		at.y = l_a * (p.x + l_a * p.y - l_a * l_b) / (l_a * l_a + 1) + l_b;
		
		if (intersection)
			return at;
		
		diff_x = p.x - at.x;
		diff_y = p.y - at.y;
		
		at.x = at.x - diff_x;
		at.y = at.y - diff_y;
		
		return at;
	}
	
	//�����߽� ���ϱ� - Center of Mass
	
	/**
	 * 
	 * @param p0
	 *            : ����Ʈ0
	 * @param p1
	 *            : ����Ʈ1
	 * @return : �� ������ ����
	 */
	public final static Vec2D getMidPoint(Vec2D p0, Vec2D p1) {
		return new Vec2D((p0.x + p1.x) / 2, (p0.y + p1.y) / 2);
	}
	
	/**
	 * 
	 * @param p0
	 * @param p1
	 * @param i
	 * @return : n ���� ���� ����Ʈ
	 */
	private final static Vec2D getNdivedPoint(Vec2D p0, Vec2D p1, int i) {
		Vec2D v = new Vec2D(p1.x - p0.x, p1.y - p0.y);
		return new Vec2D(p0.x + (int) (v.x / i), p0.y + (int) (v.y / i));
	}
	
	/**
	 * �����߽� ã��
	 * 
	 * @param pointList
	 *            : �ٰ����� ����Ʈ ����Ʈ
	 * @return : �����߽�
	 */
	public final static Vec2D getCenterOfMass(Vec2D[] pointList) {
		int i = 2;
		int L = pointList.length;
		Vec2D CofM = getMidPoint(pointList[0], pointList[1]);
		
		while (i < L) {
			CofM = getNdivedPoint(CofM, pointList[i], ++i);
		}
		return CofM;
	}
	
	public final static ArrayList getArrayFromTo(ArrayList arrlist, int from, int to) {
		ArrayList result = new ArrayList();
		int i = 0;
		int L = arrlist.size();
		
		if (from <= to) {
			for (i = from; i <= to; i++) {
				result.add(arrlist.get(i));
			}
		} else {
			for (i = from; i < L; i++) {
				result.add(arrlist.get(i));
			}
			for (i = 0; i <= to; i++) {
				result.add(arrlist.get(i));
			}
		}
		return result;
	}
	
	public final static double demicalTo(double number, int down) {
		return Math.floor(number * Math.pow(10, down)) / Math.pow(10, down);
	}
	
	public final static Vec2D getExtendedIntersection(Vec2D a1, Vec2D a2, Vec2D b1, Vec2D b2) {
		double f1 = (a2.y - a1.y) / (a2.x - a1.x);
		double f2 = (b2.y - b1.y) / (b2.x - b1.x);
		
		if (f1 == f2)
			return null;
		
		Vec2D inter = new Vec2D();
		if (Double.isInfinite(f1)) {
			inter.x = a1.x;
			inter.y = f2 * inter.x + b1.y;
			return inter;
		} else if (Double.isInfinite(f2)) {
			inter.x = b1.x;
			inter.y = f1 * inter.x + a1.y;
			return inter;
		} else {
			inter.x = (f1 * a1.x + f2 * b1.x - a1.y + b1.y) / (f1 - f2);
			inter.y = f2 * inter.x + b1.y;
			return inter;
		}
	}
	
	public final static Vec2D getIntersection(Vec2D a1, Vec2D a2, Vec2D b1, Vec2D b2) {
		double f1 = (a2.y - a1.y) / (a2.x - a1.x);
		double f2 = (b2.y - b1.y) / (b2.x - b1.x);
		double g1 = a1.y - f1 * a1.x;
		double g2 = b1.y - f2 * b1.x;
		
		if (f1 == f2)
			return null;
		
		Vec2D inter;
		
		if (a1.x == a2.x)
			inter = new Vec2D(a1.x, f2 * a1.x + g2);
		else if (b1.x == b2.x)
			inter = new Vec2D(b1.x, f1 * b1.x + g1);
		else {
			inter = new Vec2D((g2 - g1) / (f1 - f2), 0);
			inter.y = f1 * inter.x + g2;
		}
		if (isRect(inter, a1, a2) && isRect(inter, b1, b2))
			return inter;
		return null;
	}
	
	public final static boolean isRect(Vec2D a, Vec2D b, Vec2D c) { // [b, c]
		if (a.x >= Math.min(b.x, c.x) && a.x <= Math.max(b.x, c.x) && a.y >= Math.min(b.y, c.y) && a.y <= Math.max(b.y, c.y))
			return true;
		return false;
	}
	
	public final static double getArea(Vec2D s[]) {
		double area = 0;
		int i, L = s.length - 1;
		
		for (i = 0; i < L; i++) {
			area += s[i].x * s[i + 1].y;
			area -= s[i + 1].x * s[i].y;
		}
		area += s[L].x * s[0].y;
		area -= s[0].x * s[L].y;
		return Math.abs(area * 0.5);
	}
	
	//�ܽ�
	public final static Vec2D getCircumcenter(Vec2D a, Vec2D b, Vec2D c) {
		double f1 = (b.x - a.x) / (a.y - b.y);
		Vec2D m1 = new Vec2D((a.x + b.x) / 2, (a.y + b.y) / 2);
		double g1 = m1.y - f1 * m1.x;
		
		double f2 = (c.x - b.x) / (b.y - c.y);
		Vec2D m2 = new Vec2D((b.x + c.x) / 2, (b.y + c.y) / 2);
		double g2 = m2.y - f2 * m2.x;
		
		if (f1 == f2)
			return null;
		
		else if (a.y == b.y)
			return new Vec2D(m1.x, f2 * m1.x + g2);
		else if (c.y == b.y)
			return new Vec2D(m2.x, f1 * m2.x + g1);
		
		double x = (g2 - g1) / (f1 - f2);
		return new Vec2D(x, f1 * x + g1);
	}
	
	public final static Vec2D[] getTangentLineOfCircle2Point(Vec2D A, double r, Vec2D point) {
		//TODO this is not tested
		getTangentLineOfCircle2Circle(A, r, point, 0); // being test
		return null;
	}
	
	public final static Vec2D[] getTangentLineOfCircle2Circle(Vec2D A, double r, Vec2D B, double R) {
		Vec2D[] result = new Vec2D[4];
		
		double dx = A.x - B.x;
		double dy = A.y - B.y;
		double a, t, d = Math.sqrt(dx * dx + dy * dy);
		t = Math.abs(d * d + (R - r) * (R - r));
		
		if (r < R) {
			a = Math.sqrt(t + r * r); //a, R
			
			Vec2D[] temp = getIntersectionCircle2Circle(A, a, B, R);
			result[0] = temp[0];
			result[1] = temp[1];
			
			a = Math.sqrt(t + R * R); //r, a
			
			temp = getIntersectionCircle2Circle(B, a, A, r);
			result[2] = temp[0];
			result[3] = temp[1];
		} else if (r > R) {
			a = Math.sqrt(t + r * r); //a, R
			
			Vec2D[] temp = getIntersectionCircle2Circle(A, a, B, R);
			result[0] = temp[0];
			result[1] = temp[1];
			
			a = Math.sqrt(t + R * R); //r, a
			
			temp = getIntersectionCircle2Circle(A, r, B, a);
			result[2] = temp[0];
			result[3] = temp[1];
			
		} else if (r == R) {
			a = Math.sqrt(t + r * r); //a, R
			
			Vec2D[] temp = getIntersectionCircle2Circle(A, a, B, R);
			result[0] = temp[0];
			result[1] = temp[1];
			
			temp = getIntersectionCircle2Circle(A, r, B, a);
			result[2] = temp[1];
			result[3] = temp[0];
		} else {
			return null;
		}
		return result;
	}
	
	public final static double getDistance(Vec2D A, Vec2D B) {
		double dx = A.x - B.x;
		double dy = A.y - B.y;
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	public final static Vec2D[] getIntersectionCircle2Circle(Vec2D A, double r, Vec2D B, double R) {
		Vec2D[] result;
		
		double d = getDistance(A, B);
		
		if (d == 0) { // ���� ��ħ -> �ȸ����ų� ������ ���Ѱ�
			return null;
		} else if (d > R + r) { // ������ ����
			return null;
		} else if (d == R + r) { // ���� 1��
			result = new Vec2D[1];
			result[0] = new Vec2D(0.5 * (A.x + B.x), 0.5 * (A.y + B.y));
		} else { // ���� 2��
			result = new Vec2D[2];
			
			double k = (r * r + d * d - R * R) / (2 * r * d);
			double th1 = Math.acos(k);
			if (k >= 1) {
				th1 = Math.acos(k - 1) + Math.PI / 2;
			}
			
			if (th1 > Math.PI / 2)
				th1 = Math.PI - th1;
			
			double th = Math.acos((B.x - A.x) / d);
			
			if (B.x - A.x < 0)
				th = Math.PI - th;
			if (B.x - A.x <= 0 && B.y - A.y >= 0)
				th = Math.PI - th;
			else if (B.x - A.x <= 0 && B.y - A.y <= 0)
				th = Math.PI + th;
			else if (B.x - A.x >= 0 && B.y - A.y <= 0)
				th = 2 * Math.PI - th;
			
			result[0] = new Vec2D(A.x + r * Math.cos(th - th1), A.y + r * Math.sin(th - th1));
			result[1] = new Vec2D(A.x + r * Math.cos(th + th1), A.y + r * Math.sin(th + th1));
		}
		return result;
	}
	
	public final static void swap(Object obj0, Object obj1, Object temp) {
		temp = obj0;
		obj0 = obj1;
		obj0 = temp;
	}
	
	private final static int partition(int[] list, int left, int right) {
		int pivot, temp = 0;
		int low = left;
		int high = right - 1;
		pivot = list[left];
		
		do {
			do {
				low++;
			} while (low <= right && list[low] > pivot);
			
			do {
				high--;
			} while (high >= left && list[high] > pivot);
			
			if (low < high)
				swap(list[low], list[high], temp);
		} while (low < high);
		
		swap(list[left], list[high], temp);
		
		return high;
	}
	
	//	public final static quick_sort(Object list, left, right) {
	//		if (left < right) {
	//			var q:int = partition(list, left, right);
	//			quick_sort(list, left, q - 1);
	//			quick_sort(list, q+1, right);
	//		}
	//	}
	
	public final static void angular_sort(Vec2D[] plist) {
		int i = 0;
		int j = 0;
		int L = plist.length;
		Vec2D avgP = new Vec2D(0, 0);
		double[] qlist = new double[L];
		double[] qlist_copy = new double[L];
		Vec2D[] resultarr = new Vec2D[L];
		
		for (i = 0; i < L; i++) {
			avgP.x += plist[i].x;
			avgP.y += plist[i].y;
		}
		
		avgP.multiply(1 / L);
		
		for (i = 0; i < L; i++) {
			qlist[i] = Math.atan2(plist[i].y - avgP.y, plist[i].x - avgP.x);
		}
		
		System.arraycopy(qlist, 0, qlist_copy, 0, L);
		
		Arrays.sort(qlist_copy);
		
		for (i = 0; i < L; i++) {
			for (j = 0; j < L; j++) {
				if (qlist_copy[i] == qlist[j]) {
					resultarr[i] = plist[j];
				}
			}
		}
		
		System.arraycopy(resultarr, 0, plist, 0, L);
		//		for (i = 0; i < L; i++) {
		//			plist[i] = resultarr[i];
		//		}
	}
	
	public final static void angular_sort(Point[] plist) {
		int i = 0;
		int j = 0;
		int L = plist.length;
		Point avgP = new Point();
		double[] qlist = new double[L];
		double[] qlist_copy = new double[L];
		Point[] resultarr = new Point[L];
		
		for (; i < L; i++) {
			avgP.x += plist[i].x;
			avgP.y += plist[i].y;
		}
		avgP.x *= 1 / L;
		avgP.y *= 1 / L;
		
		for (i = 0; i < L; i++) {
			qlist[i] = Math.atan2(plist[i].y - avgP.y, plist[i].x - avgP.x);
		}
		
		System.arraycopy(qlist, 0, qlist_copy, 0, L);
		
		Arrays.sort(qlist_copy);
		
		for (i = 0; i < L; i++) {
			for (j = 0; j < L; j++) {
				if (qlist_copy[i] == qlist[j]) {
					resultarr[i] = plist[j];
				}
			}
		}
		
		for (i = 0; i < L; i++) {
			plist[i] = resultarr[i];
		}
	}
	
	//*************************************//
	//                Color                //
	//*************************************//
	public final static Color colorInterpolate(Color from, Color to, double t) {
		if (t <= 0)
			return from;
		else if (t >= 1)
			return to;
		int nr = (int) (linearInterpolate(from.getRed(), to.getRed(), t) + .5);
		int ng = (int) (linearInterpolate(from.getGreen(), to.getGreen(), t) + .5);
		int nb = (int) (linearInterpolate(from.getBlue(), to.getBlue(), t) + .5);
		int na = (int) (linearInterpolate(from.getAlpha(), to.getAlpha(), t) + .5);
		return new Color(nr, ng, nb, na);
	}
	
	//	public final static Color colorInterpolate(Color from, Color to, double t) {
	//		if (t < 0)
	//			return from;
	//		else if (t >= 1)
	//			return to;
	//		int nr = (int) (linearInterpolate(from.getRed(), to.getRed(), t) + .5);
	//		int ng = (int) (linearInterpolate(from.getGreen(), to.getGreen(), t) + .5);
	//		int nb = (int) (linearInterpolate(from.getBlue(), to.getBlue(), t) + .5);
	//		return new Color(nr, ng, nb);
	//	}
	
	public final static Color colorInterpolateWithalpha(Color from, Color to, double t) {
		if (t <= 0)
			return from;
		else if (t >= 1)
			return to;
		int na = (int) (linearInterpolate(from.getAlpha(), to.getAlpha(), t) + .5);
		int nr = (int) (linearInterpolate(from.getRed(), to.getRed(), t) + .5);
		int ng = (int) (linearInterpolate(from.getGreen(), to.getGreen(), t) + .5);
		int nb = (int) (linearInterpolate(from.getBlue(), to.getBlue(), t) + .5);
		return new Color(na, nr, ng, nb);
	}
	
	public final static Color colorInterpolate(Color[] list, double t) {
		if (t <= 0)
			return list[0];
		else if (t >= 1)
			return list[list.length - 1];
		int L = list.length;
		if (L < 2)
			return list[0];
		float gap = 1.0f / (L - 1);
		double j = (double) (t * (L - 1));
		
		int i = (int) j;
		
		return colorInterpolate(list[i], list[i + 1], (t - i * gap) * (L - 1));
	}
	
	public final static int colorInterpolate(int i, int f, float t) {
		if (t == 0.f) {
			return i;
		} else if (t == 1.f) {
			return f;
		}
		
		int[] irgb = HEXtoRGB(i);
		int[] frgb = HEXtoRGB(f);
		
		int r = (int) linearInterpolate(irgb[0], frgb[0], t);
		int g = (int) linearInterpolate(irgb[1], frgb[1], t);
		int b = (int) linearInterpolate(irgb[2], frgb[2], t);
		
		return 0xff << 24 | r << 16 | g << 8 | b;
	}
	
	public final static int color2Int(Color c) {
		int nr = (int) c.getRed();
		int ng = (int) c.getGreen();
		int nb = (int) c.getBlue();
		int na = (int) c.getAlpha();
		return (na << 24) | (nr << 16) | (ng << 8) | nb;
	}
	
	public final static int max(int... arr) {
		int $big = arr[0], i = arr.length;
		while (i-- > 0)
			if ($big < arr[i])
				$big = arr[i];
		return $big;
	}
	
	public final static int min(int... arr) {
		int $little = arr[0], i = arr.length;
		while (i-- > 0)
			if ($little > arr[i])
				$little = arr[i];
		return $little;
	}
	
	public final static double max(double... arr) {
		double $big = arr[0];
		int i = arr.length;
		while (i-- > 0)
			if ($big < arr[i])
				$big = arr[i];
		return $big;
	}
	
	public final static double min(double... arr) {
		double $little = arr[0];
		int i = arr.length;
		while (i-- > 0)
			if ($little > arr[i])
				$little = arr[i];
		return $little;
	}
	
	public final static float max(float... arr) {
		float $big = arr[0];
		int i = arr.length;
		while (i-- > 0)
			if ($big < arr[i])
				$big = arr[i];
		return $big;
	}
	
	public final static double min(float... arr) {
		float $little = arr[0];
		int i = arr.length;
		while (i-- > 0)
			if ($little > arr[i])
				$little = arr[i];
		return $little;
	}
	
	/////////////////////
	// ColorConversion //
	/////////////////////
	
	public final static int getGrayMode(int hex) {
		int[] data;
		int sum = 0;
		if (hex >= 0xff000000) {
			data = HEXtoARGB(hex);
			sum = data[1] + data[2] + data[3];
		} else {
			data = HEXtoRGB(hex);
			sum = data[0] + data[1] + data[2];
		}
		sum /= 3.0;
		sum = (int) (sum + 0.5);
		
		return sum << 16 | sum << 8 | sum;
	}
	
	public final static double[] RGBtoHSV(double $R, double $G, double $B) {
		$R = ($R > 1) ? $R / 0xff : $R;
		$G = ($G > 1) ? $G / 0xff : $G;
		$B = ($B > 1) ? $B / 0xff : $B;
		
		double H = 0;
		double S = 0;
		double V = 0;
		
		double max = max($R, $G, $B);
		double min = min($R, $G, $B);
		
		double mmgap = max - min;
		
		//get H;
		if (max == min) {
			H = 0;
		} else {
			if ($R == max) {
				H = 60 * ($G - $B) / mmgap + 360;
				H %= 360;
			} else if ($G == max) {
				H = 60 * ($B - $R) / mmgap + 120;
			} else if ($B == max) {
				H = 60 * ($R - $G) / mmgap + 240;
			} else {
				H = 0;
				//throw new Error ("Exception Error : RGBtoHSV()");
			}
		}
		
		//get S;
		if (max == 0) {
			S = 0;
		} else {
			S = mmgap / max;
		}
		
		//get V;
		V = max;
		double[] result = new double[] { H, S, V };
		return result;
	}
	
	/**
	 * 
	 * @param $R
	 *            : red
	 * @param $G
	 *            : green
	 * @param $B
	 *            : blue
	 * @return : [H, S, L]
	 */
	public final static double[] RGBtoHLS(double $R, double $G, double $B) {
		$R = Math.abs($R);
		$G = Math.abs($G);
		$B = Math.abs($B);
		
		$R = ($R > 1) ? $R / 0xff : $R;
		$G = ($G > 1) ? $G / 0xff : $G;
		$B = ($B > 1) ? $B / 0xff : $B;
		
		double H = 0;
		double S = 0;
		double L = 0;
		
		double max = max($R, $G, $B);
		double min = min($R, $G, $B);
		
		double mmgap = max - min;
		
		//get H;
		if (max == min) {
			H = 0;
		} else {
			if ($R == max) {
				H = 60 * ($G - $B) / mmgap + 360;
				H %= 360;
			} else if ($G == max) {
				H = 60 * ($B - $R) / mmgap + 120;
			} else if ($B == max) {
				H = 60 * ($R - $G) / mmgap + 240;
			} else {
				H = 0;
				//throw new Error ("Exception Error : RGBtoHSL()");
			}
		}
		
		//get L;
		L = (max + min) / 2;
		
		//get S;
		if (max == min) {
			S = 0;
		} else {
			if (L <= .5) {
				L = mmgap / (max + min);
			} else if (L > .5) {
				L = mmgap / (2 - (max + min));
			}
		}
		
		double[] result = new double[] { H, S, L };
		
		return result;
	}
	
	/**
	 * 
	 * @param $HEX
	 *            : 0x000000 ��� 16���
	 * @return : [H, S, V]
	 */
	public final static double[] HEXtoHSV(int $HEX) {
		int[] RGBarr = new int[3];
		double[] HSVarr = new double[3];
		RGBarr = HEXtoRGB($HEX);
		
		HSVarr = RGBtoHSV(RGBarr[0], RGBarr[1], RGBarr[2]);
		return HSVarr;
	}
	
	/**
	 * 
	 * @param $value
	 *            : 16�����
	 * @return : [R, G, B]
	 */
	public final static int[] HEXtoRGB(int $value) {
		int[] returnArr = new int[3];
		
		returnArr[2] = $value & 0xff;
		returnArr[1] = ($value >> 8) & 0xff;
		returnArr[0] = ($value >> 16) & 0xff;
		
		return returnArr;
	}
	
	private static int[] HEXtoARGB(int $value) {
		int[] returnArr = new int[4];
		
		returnArr[3] = $value & 0xff;
		returnArr[2] = ($value >> 8) & 0xff;
		returnArr[1] = ($value >> 16) & 0xff;
		returnArr[0] = ($value >> 24) & 0xff;
		
		return returnArr;
	}
	
	public final static int HEXtoRGB(String $hex) {
		
		return Integer.parseInt($hex, 16);
	}
	
	/**
	 * 
	 * @param r
	 *            : red
	 * @param g
	 *            : green
	 * @param b
	 *            : blue
	 * @return : 0x000000 16�����
	 */
	public final static String RGBtoHEX(int r, int g, int b) {
		String sr, sg, sb;
		sr = Integer.toHexString(r);
		if (sr.length() == 1)
			sr = "0" + sr;
		sg = Integer.toHexString(g);
		if (sg.length() == 1)
			sg = "0" + sg;
		sb = Integer.toHexString(b);
		if (sb.length() == 1)
			sb = "0" + sb;
		return "0x" + sr + sg + sb;
	}
	
	/**
	 * 
	 * @param $A
	 *            : alpha
	 * @param $R
	 *            : red
	 * @param $G
	 *            : green
	 * @param $B
	 *            : blue
	 * @return : 0x00000000 16�����
	 */
	public final static String RGBtoHEX(int $A, int $R, int $G, int $B) {
		return "0x" + Integer.toHexString($A) + Integer.toHexString($R) + Integer.toHexString($G) + Integer.toHexString($B);
	}
	
	/**
	 * 
	 * @param r
	 *            : red
	 * @param g
	 *            : green
	 * @param b
	 *            : blue
	 * @return : 0x000000 16�����
	 */
	public final static String RGBtoHEX(double r, double g, double b) {
		String sr, sg, sb;
		sr = Integer.toHexString((int) (r + .5));
		if (sr.length() == 1)
			sr = "0" + sr;
		sg = Integer.toHexString((int) (g + .5));
		if (sg.length() == 1)
			sg = "0" + sg;
		sb = Integer.toHexString((int) (b + .5));
		if (sb.length() == 1)
			sb = "0" + sb;
		return "0x" + sr + sg + sb;
	}
	
	/**
	 * 
	 * @param $A
	 *            : alpha
	 * @param $R
	 *            : red
	 * @param $G
	 *            : green
	 * @param $B
	 *            : blue
	 * @return : 0x00000000 16�����
	 */
	public final static String RGBtoHEX(double $A, double $R, double $G, double $B) {
		return "0x" + Integer.toHexString((int) ($A + .5)) + Integer.toHexString((int) ($R + .5)) + Integer.toHexString((int) ($G + .5)) + Integer.toHexString((int) ($B + .5));
	}
	
	/**
	 * 
	 * @param $H
	 *            : Hue
	 * @param $L
	 *            : Lightness
	 * @param $S
	 *            : Saturation
	 * @return : 0x000000 16�����
	 */
	public final static String HLStoHEX(double $H, double $S, double $L) {
		double q = 0;
		double[] Trgb = new double[3];
		double[] Crgb = new double[3];
		
		if ($L < .5) {
			q = $L * (1 + $S);
		} else if ($L >= .5) {
			q = $L + $S - ($L * $S);
		}
		
		double Hk = $H / 360;
		Trgb[0] = Hk + 1 / 3;
		Trgb[1] = Hk;
		Trgb[2] = Hk - 1 / 3;
		
		int i;
		
		for (i = 0; i < 3; i++) {
			if (Trgb[i] < 0) {
				Trgb[i] = Trgb[i] + 1;
			} else if (Trgb[i] > 1) {
				Trgb[i] = Trgb[i] - 1;
			}
			Crgb[i] = Trgb[i];
		}
		
		return RGBtoHEX(Crgb[0] * 255.0, Crgb[1] * 255.0, Crgb[2] * 255.0);
	}
	
	/**
	 * 
	 * @param $H
	 *            : Hue
	 * @param $S
	 *            : Saturation
	 * @param $V
	 *            : Value, Brightness
	 * @return : 0x000000 16�����
	 */
	public final static String HSVtoHEX(double $H, double $S, double $V) {
		int Hi = (int) Math.floor($H / 60 + .5) % 6;
		double f = $H / 60 - Math.floor($H / 60);
		
		double p = $V * (1 - $S);
		double q = $V * (1 - $S * f);
		double t = $V * (1 - $S * (1 - f));
		
		double R;
		double G;
		double B;
		
		switch (Hi) {
			case 0 : {
				R = $V;
				G = t;
				B = p;
				break;
			}
			case 1 : {
				R = q;
				G = $V;
				B = p;
				break;
			}
			case 2 : {
				R = p;
				G = $V;
				B = t;
				break;
			}
			case 3 : {
				R = p;
				G = q;
				B = $V;
				break;
			}
			case 4 : {
				R = t;
				G = p;
				B = $V;
				break;
			}
			case 5 : {
				R = $V;
				G = p;
				B = q;
				break;
			}
			default : {
				R = G = B = 0;
				throw new IllegalArgumentException("ù��° �Ķ����($H)�� �߸�Ǿ���ϴ�. : " + $H);
			}
		}
		R *= 255.0;
		G *= 255.0;
		B *= 255.0;
		
		return RGBtoHEX(R, G, B);
	}
	
	public final static int HSVtoRGB(double $H, double $S, double $V) {
		int Hi = (int) Math.floor($H / 60.) % 6;
		double f = $H / 60. - Math.floor($H / 60.);
		
		double p = $V * (1 - $S);
		double q = $V * (1 - $S * f);
		double t = $V * (1 - $S * (1 - f));
		
		double R;
		double G;
		double B;
		
		switch (Hi) {
			case 0 : {
				R = $V;
				G = t;
				B = p;
				break;
			}
			case 1 : {
				R = q;
				G = $V;
				B = p;
				break;
			}
			case 2 : {
				R = p;
				G = $V;
				B = t;
				break;
			}
			case 3 : {
				R = p;
				G = q;
				B = $V;
				break;
			}
			case 4 : {
				R = t;
				G = p;
				B = $V;
				break;
			}
			case 5 : {
				R = $V;
				G = p;
				B = q;
				break;
			}
			default : {
				//throw new Error("ù��° �Ķ����($H)�� �߸�Ǿ���ϴ�. : " +$H);
				R = G = B = 0;
				break;
			}
		}
		R *= 255.0;
		G *= 255.0;
		B *= 255.0;
		//		System.out.println(((int) (R + .5)) + " " + ((int) (G + .5)) + " " + ((int) (B + .5)));
		//(r0 << 16) | (g0 << 8) | b0;
		return (((int) (R + .5)) << 16) | (((int) (G + .5)) << 8) | ((int) (B + .5));
	}
	
	/*
	 * public final static double getAverage(double sum[], double newv){ double
	 * avg = 0; int L = sum.length; for(int i=0;i<L-1;i++){ sum[i] = sum[i+1];
	 * avg += sum[i]; } sum[L] = newv; avg += newv;
	 * 
	 * return avg/L; }
	 */
}