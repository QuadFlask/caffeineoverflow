package main.flask.imageprocessing;

import main.flask.utils.Vec2DF;

public class Vec2DFPerspectiveProjection {

	public static Vec2DF[] perspectiveAtBound(Vec2DF[] res,  Vec2DF tl, Vec2DF tr, Vec2DF br, Vec2DF bl, float[] bound){
		Vec2DF[] dst = cloning(res);
		Vec2DF rxTop = new Vec2DF(), rxBottom = new Vec2DF();
		Vec2DF ratio = new Vec2DF();

		int length = res.length;
		for (int i = 0; i < length; i++) {
			getRatio(res[i], bound, ratio);
			setRatioBetween(rxTop, tl, tr, ratio.x);
			setRatioBetween(rxBottom, bl, br, ratio.x);
			setRatioBetween(dst[i], rxTop, rxBottom, ratio.y);
			System.out.println(dst[i].toString());
		}

		return dst;
	}

	public static Vec2DF[] perspective(Vec2DF[] res, Vec2DF tl, Vec2DF tr, Vec2DF br, Vec2DF bl) {
		Vec2DF[] dst = cloning(res);
		Vec2DF rxTop = new Vec2DF(), rxBottom = new Vec2DF();
		Vec2DF ratio = new Vec2DF();
		float[] bound = getBounds(res);

		int length = res.length;
		for (int i = 0; i < length; i++) {
			getRatio(res[i], bound, ratio);
			setRatioBetween(rxTop, tl, tr, ratio.x);
			setRatioBetween(rxBottom, bl, br, ratio.x);
			setRatioBetween(dst[i], rxTop, rxBottom, ratio.y);
			System.out.println(dst[i].toString());
		}

		return dst;
	}

	public static Vec2DF setRatioBetween(Vec2DF target, Vec2DF from, Vec2DF to, float ratio) {
		target.setValues(from.x + (to.x - from.x) * ratio, from.y + (to.y - from.y) * ratio);
		System.out.println("ratio between : " + target.toString());
		return target;
	}

	public static Vec2DF getRatio(Vec2DF p, float[] bound, Vec2DF result) {
		float w = bound[2] - bound[0];
		float h = bound[3] - bound[1];

		float x = p.x - bound[0];
		float y = p.y - bound[1];

		result.setValues(x / w, y / h);

		System.out.println("bound ratio : " + result.toString());

		return result;
	}

	public static Vec2DF getRatio(Vec2DF p, float[] bound) {
		return getRatio(p, bound, new Vec2DF());
	}

	public static float[] getBounds(Vec2DF[] src) {
		float[] result = new float[4];

		for (int i = 0; i < src.length; i++) {
			result[0] = Math.min(result[0], src[i].x);
			result[1] = Math.min(result[1], src[i].y);
			result[2] = Math.max(result[2], src[i].x);
			result[3] = Math.max(result[3], src[i].y);
		}

		System.out.println("bound : " + result[0]);
		System.out.println("bound : " + result[1]);
		System.out.println("bound : " + result[2]);
		System.out.println("bound : " + result[3]);

		return result;
	}

	public static Vec2DF[] cloning(Vec2DF[] src) {
		int length = src.length;

		Vec2DF[] dest = new Vec2DF[length];
		for (int i = 0; i < length; i++)
			dest[i] = src[i].clone();

		return dest;
	}
}
