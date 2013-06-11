package flask.imageprocessing;

import flask.utils.Matrix;
import flask.utils.MatrixF;
import flask.utils.Vec2D;

import java.awt.*;
import java.awt.geom.Point2D;

/*

 <Code from>

 http://b.mytears.org/2007/10/599
 http://trac.unfix.net/browser/snippet/image_projection/projection.c


 ported by Flask. 
 12.07.28


 <Usage>

 PerspectiveProjection projection = new PerspectiveProjection(w, h);
 Matrix m = projection.getProjectionMatrix(new Point(0, 0), new Point(500, 0), new Point(500, 500), new Point(0, 500));
 // It's looks original image(500*500).

 int[] dst = new int[w * h];

 dst = projection.backwardWarping(image.getRGB(0, 0, w, h, null, 0, w), dst, m);

 // draw dst.

 bfdimg.setRGB(0, 0, w, h, dst, 0, w);

 g2d.drawImage(bfdimg, 0, 0, w, h, null);

 */

public class PerspectiveProjection {

	private int width;
	private int height;

	/**
	 * Constructor
	 * 
	 * @param w
	 *            : width
	 * @param h
	 *            : height
	 */
	public PerspectiveProjection(int w, int h) {
		setWidth(w);
		setHeight(h);
	}

	public PerspectiveProjection(int size) {
		setWidth(size);
		setHeight(size);
	}

	/**
	 * get a matrix for perspective projection. it's clockwise position.
	 * 
	 * @param a
	 *            : Left Top
	 * @param b
	 *            : Right Top
	 * @param c
	 *            : Right Bottom
	 * @param d
	 *            : Left Bottom
	 * @return matrix instance(3x3)
	 */
	public Matrix getProjectionMatrix(Vec2D a, Vec2D b, Vec2D c, Vec2D d) {
		double x[] = { 0, width, 0, width };
		double y[] = { 0, 0, height, height }; // �ҽ� (���� ����)

		double _x[] = { a.x, b.x, d.x, c.x };
		double _y[] = { a.y, b.y, d.y, c.y }; // Ʈ������ ��� (�������϶� ǥ�� �ȵ�....��..? �׷���,. 0.001 ����.��)

		return getProjectionMatrix(x, y, _x, _y);
	}

	public MatrixF getProjectionMatrixF(Vec2D a, Vec2D b, Vec2D c, Vec2D d) {
		float x[] = { 0, width, 0, width };
		float y[] = { 0, 0, height, height }; // �ҽ� (���� ����)

		float _x[] = { (float) a.x, (float) b.x, (float) d.x, (float) c.x };
		float _y[] = { (float) a.y, (float) b.y, (float) d.y, (float) c.y }; // Ʈ������ ��� (�������϶� ǥ�� �ȵ�....��..? �׷���,. 0.001 ����.��)

		return getProjectionMatrixF(x, y, _x, _y);
	}

	/**
	 * get a matrix for perspective projection. it's clockwise position.
	 * 
	 * @param a
	 *            : Left Top
	 * @param b
	 *            : Right Top
	 * @param c
	 *            : Right Bottom
	 * @param d
	 *            : Left Bottom
	 * @return matrix instance(3x3)
	 */
	public Matrix getProjectionMatrix(Point2D.Double a, Point2D.Double b, Point2D.Double c, Point2D.Double d) {
		double x[] = { 0, width, 0, width };
		double y[] = { 0, 0, height, height }; // �ҽ� (���� ����)

		double _x[] = { a.x, b.x, d.x, c.x };
		double _y[] = { a.y, b.y, d.y, c.y }; // Ʈ������ ��� (�������϶� ǥ�� �ȵ�....��..? �׷���,. 0.001 ����.��)

		return getProjectionMatrix(x, y, _x, _y);
	}

	/**
	 * get a matrix for perspective projection. it's clockwise position.
	 * 
	 * @param a
	 *            : Left Top
	 * @param b
	 *            : Right Top
	 * @param c
	 *            : Right Bottom
	 * @param d
	 *            : Left Bottom
	 * @return matrix instance(3x3)
	 */
	public Matrix getProjectionMatrix(Point2D.Float a, Point2D.Float b, Point2D.Float c, Point2D.Float d) {
		double x[] = { 0, width, 0, width };
		double y[] = { 0, 0, height, height }; // �ҽ� (���� ����)

		double _x[] = { a.x, b.x, d.x, c.x };
		double _y[] = { a.y, b.y, d.y, c.y }; // Ʈ������ ��� (�������϶� ǥ�� �ȵ�....��..? �׷���,. 0.001 ����.��)

		return getProjectionMatrix(x, y, _x, _y);
	}

	/**
	 * get a matrix for perspective projection. it's clockwise position.
	 * 
	 * @param a
	 *            : Left Top
	 * @param b
	 *            : Right Top
	 * @param c
	 *            : Right Bottom
	 * @param d
	 *            : Left Bottom
	 * @return matrix instance(3x3)
	 */
	public Matrix getProjectionMatrix(Point a, Point b, Point c, Point d) {
		double x[] = { 0, width, 0, width };
		double y[] = { 0, 0, height, height }; // �ҽ� (���� ����)

		double _x[] = { a.x, b.x, d.x, c.x };
		double _y[] = { a.y, b.y, d.y, c.y }; // Ʈ������ ��� (�������϶� ǥ�� �ȵ�....��..? �׷���,. 0.001 ����.��)

		return getProjectionMatrix(x, y, _x, _y);
	}

	/**
	 * get a matrix for perspective projection. not a clickwise.
	 * 
	 * @param x
	 *            : extract range from source data [4]
	 * @param y
	 *            : extract range from source data [4]
	 * @param _x
	 *            : transform shape [4]
	 * @param _y
	 *            : transform shape [4]
	 * @return matrix instance(3x3)
	 */
	public Matrix getProjectionMatrix(double x[], double y[], double _x[], double _y[]) {
		Matrix a, b, c;

		Matrix a_inv;
		Matrix projection;

		a = new Matrix(8, 8);
		b = new Matrix(8, 1);

		a.var[0][0] = x[0];
		a.var[0][1] = y[0];
		a.var[0][2] = 1.0;
		a.var[0][6] = -1 * _x[0] * x[0];
		a.var[0][7] = -1 * _x[0] * y[0];

		a.var[1][0] = x[1];
		a.var[1][1] = y[1];
		a.var[1][2] = 1.0;
		a.var[1][6] = -1 * _x[1] * x[1];
		a.var[1][7] = -1 * _x[1] * y[1];

		a.var[2][0] = x[2];
		a.var[2][1] = y[2];
		a.var[2][2] = 1.0;
		a.var[2][6] = -1 * _x[2] * x[2];
		a.var[2][7] = -1 * _x[2] * y[2];

		a.var[3][0] = x[3];
		a.var[3][1] = y[3];
		a.var[3][2] = 1.0;
		a.var[3][6] = -1 * _x[3] * x[3];
		a.var[3][7] = -1 * _x[3] * y[3];

		a.var[4][3] = x[0];
		a.var[4][4] = y[0];
		a.var[4][5] = 1.0;
		a.var[4][6] = -1 * x[0] * _y[0];
		a.var[4][7] = -1 * y[0] * _y[0];

		a.var[5][3] = x[1];
		a.var[5][4] = y[1];
		a.var[5][5] = 1.0;
		a.var[5][6] = -1 * x[1] * _y[1];
		a.var[5][7] = -1 * y[1] * _y[1];

		a.var[6][3] = x[2];
		a.var[6][4] = y[2];
		a.var[6][5] = 1.0;
		a.var[6][6] = -1 * x[2] * _y[2];
		a.var[6][7] = -1 * y[2] * _y[2];

		a.var[7][3] = x[3];
		a.var[7][4] = y[3];
		a.var[7][5] = 1.0;
		a.var[7][6] = -1 * x[3] * _y[3];
		a.var[7][7] = -1 * y[3] * _y[3];

		b.var[0][0] = _x[0];
		b.var[1][0] = _x[1];
		b.var[2][0] = _x[2];
		b.var[3][0] = _x[3];
		b.var[4][0] = _y[0];
		b.var[5][0] = _y[1];
		b.var[6][0] = _y[2];
		b.var[7][0] = _y[3];

		a_inv = Matrix.inverse(a);
		c = Matrix.multiple(a_inv, b);

		projection = new Matrix(3, 3);
		if (Double.isNaN(c.var[0][0])) {
			projection.var = new double[][] { { 1.0, 0, 0 }, { 0, 1.0, 0 }, { 0, 0, 1.0 } };
		} else {
			projection.var[0][0] = c.var[0][0];
			projection.var[0][1] = c.var[1][0];
			projection.var[0][2] = c.var[2][0];

			projection.var[1][0] = c.var[3][0];
			projection.var[1][1] = c.var[4][0];
			projection.var[1][2] = c.var[5][0];

			projection.var[2][0] = c.var[6][0];
			projection.var[2][1] = c.var[7][0];
			projection.var[2][2] = 1.0;
		}

		return projection;
	}

	public MatrixF getProjectionMatrixF(float x[], float y[], float _x[], float _y[]) {
		MatrixF a, b, c;

		MatrixF a_inv;
		MatrixF projection;

		a = new MatrixF(8, 8);
		b = new MatrixF(8, 1);

		a.var[0][0] = x[0];
		a.var[0][1] = y[0];
		a.var[0][2] = 1.0f;
		a.var[0][6] = -1 * _x[0] * x[0];
		a.var[0][7] = -1 * _x[0] * y[0];

		a.var[1][0] = x[1];
		a.var[1][1] = y[1];
		a.var[1][2] = 1.0f;
		a.var[1][6] = -1 * _x[1] * x[1];
		a.var[1][7] = -1 * _x[1] * y[1];

		a.var[2][0] = x[2];
		a.var[2][1] = y[2];
		a.var[2][2] = 1.0f;
		a.var[2][6] = -1 * _x[2] * x[2];
		a.var[2][7] = -1 * _x[2] * y[2];

		a.var[3][0] = x[3];
		a.var[3][1] = y[3];
		a.var[3][2] = 1.0f;
		a.var[3][6] = -1 * _x[3] * x[3];
		a.var[3][7] = -1 * _x[3] * y[3];

		a.var[4][3] = x[0];
		a.var[4][4] = y[0];
		a.var[4][5] = 1.0f;
		a.var[4][6] = -1 * x[0] * _y[0];
		a.var[4][7] = -1 * y[0] * _y[0];

		a.var[5][3] = x[1];
		a.var[5][4] = y[1];
		a.var[5][5] = 1.0f;
		a.var[5][6] = -1 * x[1] * _y[1];
		a.var[5][7] = -1 * y[1] * _y[1];

		a.var[6][3] = x[2];
		a.var[6][4] = y[2];
		a.var[6][5] = 1.0f;
		a.var[6][6] = -1 * x[2] * _y[2];
		a.var[6][7] = -1 * y[2] * _y[2];

		a.var[7][3] = x[3];
		a.var[7][4] = y[3];
		a.var[7][5] = 1.0f;
		a.var[7][6] = -1 * x[3] * _y[3];
		a.var[7][7] = -1 * y[3] * _y[3];

		b.var[0][0] = _x[0];
		b.var[1][0] = _x[1];
		b.var[2][0] = _x[2];
		b.var[3][0] = _x[3];
		b.var[4][0] = _y[0];
		b.var[5][0] = _y[1];
		b.var[6][0] = _y[2];
		b.var[7][0] = _y[3];

		a_inv = MatrixF.inverse(a);
		c = MatrixF.multiple(a_inv, b);

		projection = new MatrixF(3, 3);
		if (Float.isNaN(c.var[0][0])) {
			projection.var = new float[][] { { 1.0f, 0, 0 }, { 0, 1.0f, 0 }, { 0, 0, 1.0f } };
		} else {
			projection.var[0][0] = c.var[0][0];
			projection.var[0][1] = c.var[1][0];
			projection.var[0][2] = c.var[2][0];

			projection.var[1][0] = c.var[3][0];
			projection.var[1][1] = c.var[4][0];
			projection.var[1][2] = c.var[5][0];

			projection.var[2][0] = c.var[6][0];
			projection.var[2][1] = c.var[7][0];
			projection.var[2][2] = 1.0f;
		}

		return projection;
	}

	public int[] forwardWarping(int[] src, int[] dst, Matrix p) {
		int i, j, x, y;
		double w;

		if (dst == null)
			dst = new int[width * height];

		if (p.row != 3 || p.col != 3)
			return null;

		for (j = 0; j < height; j++) {
			for (i = 0; i < width; i++) {
				w = p.var[2][0] * i + p.var[2][1] * j + p.var[2][2];

				x = (int) ((p.var[0][0] * i + p.var[0][1] * j + p.var[0][2]) / w);
				y = (int) ((p.var[1][0] * i + p.var[1][1] * j + p.var[1][2]) / w);

				if (!boundCheck(x, y))
					continue;

				dst[y * width + x] = src[j * width + i];
			}
		}

		return dst;
	}

	public int[] forwardWarping_interpolate(int[] src, int[] dst, Matrix p) {
		double px, py, w;
		int i, j, x, y;

		double[] pixel;
		double[] ratio;

		double[] wx = new double[2];
		double[] wy = new double[2];

		pixel = new double[width * height];
		ratio = new double[width * height];

		if (p.row != 3 || p.col != 3)
			return null;

		for (j = 0; j < height; j++) {
			for (i = 0; i < width; i++) {

				w = p.var[2][0] * i + p.var[2][1] * j + p.var[2][2];

				px = (p.var[0][0] * i + p.var[0][1] * j + p.var[0][2]) / w;
				py = (p.var[1][0] * i + p.var[1][1] * j + p.var[1][2]) / w;

				x = (int) Math.floor(px);
				y = (int) Math.floor(py);

				wx[1] = px - x;
				wx[0] = 1.0 - wx[1];

				wy[1] = py - y;
				wy[0] = 1.0 - wy[1];

				if (boundCheck(x, y)) {
					pixel[y * width + x] += src[j * width + i] * wx[0] * wy[0];
					ratio[y * width + x] += wx[0] * wy[0];
				}

				if (boundCheck(x + 1, y)) {
					pixel[y * width + (x + 1)] += src[j * width + i] * wx[1] * wy[0];
					ratio[y * width + (x + 1)] += wx[1] * wy[0];
				}

				if (boundCheck(x, y + 1)) {
					pixel[(y + 1) * width + x] += src[j * width + i] * wx[0] * wy[1];
					ratio[(y + 1) * width + x] += wx[0] * wy[1];
				}

				if (boundCheck(x + 1, y + 1)) {
					pixel[(y + 1) * width + (x + 1)] += src[j * width + i] * wx[1] * wy[1];
					ratio[(y + 1) * width + (x + 1)] += wx[1] * wy[1];
				}
			}
		}
		for (j = 0; j < height; j++)
			for (i = 0; i < width; i++)
				dst[j * width + i] = (int) Math.round(pixel[j * width + i] / ratio[j * width + i]);

		return dst;
	}

	public int[] backwardWarping(int[] src, int[] dst, Matrix p) {
		double w;

		int i, j, x, y;

		Matrix inv = Matrix.inverse(p);

		for (j = 0; j < height; j++) {
			for (i = 0; i < width; i++) {
				w = inv.var[2][0] * i + inv.var[2][1] * j + inv.var[2][2];

				x = (int) ((inv.var[0][0] * i + inv.var[0][1] * j + inv.var[0][2]) / w);
				y = (int) ((inv.var[1][0] * i + inv.var[1][1] * j + inv.var[1][2]) / w);

				if (!boundCheck(x, y))
					continue;

				dst[j * width + i] = src[y * width + x];
			}
		}

		return dst;
	}
	
	public int[] backwardWarping(int[] src, int[] dst, MatrixF p) {
		double w;

		int i, j, x, y;

		MatrixF inv = MatrixF.inverse(p);

		for (j = 0; j < height; j++) {
			for (i = 0; i < width; i++) {
				w = inv.var[2][0] * i + inv.var[2][1] * j + inv.var[2][2];

				x = (int) ((inv.var[0][0] * i + inv.var[0][1] * j + inv.var[0][2]) / w);
				y = (int) ((inv.var[1][0] * i + inv.var[1][1] * j + inv.var[1][2]) / w);

				if (!boundCheck(x, y))
					continue;

				dst[j * width + i] = src[y * width + x];
			}
		}

		return dst;
	}

	public int[] backwardWarping_interpolate(int[] src, int[] dst, Matrix p) {

		double w, pixel, ratio, px, py;

		double[] wx = new double[2];
		double[] wy = new double[2];

		int i, j, x, y;

		Matrix inv = Matrix.inverse(p);

		for (j = 0; j < height; j++) {
			for (i = 0; i < width; i++) {
				ratio = pixel = 0.0;
				w = inv.var[2][0] * i + inv.var[2][1] * j + inv.var[2][2];

				px = (inv.var[0][0] * i + inv.var[0][1] * j + inv.var[0][2]) / w;
				py = (inv.var[1][0] * i + inv.var[1][1] * j + inv.var[1][2]) / w;

				wx[1] = px - Math.floor(px);
				wx[0] = 1.0 - wx[1];

				wy[1] = py - Math.floor(py);
				wy[0] = 1.0 - wy[1];

				x = (int) Math.floor(px);
				y = (int) Math.floor(py);

				if (boundCheck(x, y)) {
					pixel += wx[0] * wy[0] * src[y * width + x];
					ratio += wx[0] * wy[0];
				}
				if (boundCheck(x + 1, y)) {
					pixel += wx[1] * wy[0] * src[y * width + x + 1];
					ratio += wx[1] * wy[0];
				}
				if (boundCheck(x, y + 1)) {
					pixel += wx[0] * wy[1] * src[(y + 1) * width + x];
					ratio += wx[0] * wy[1];
				}
				if (boundCheck(x + 1, y + 1)) {
					pixel += wx[1] * wy[1] * src[(y + 1) * width + x + 1];
					ratio += wx[1] * wy[1];
				}
				dst[j * width + i] = (int) Math.floor(pixel / ratio + 0.5);
			}
		}

		return dst;
	}

	public boolean boundCheck(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return false;
		return true;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int h) {
		height = h;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int w) {
		width = w;
	}

}
