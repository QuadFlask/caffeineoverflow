package flask.utils;

public class MatrixF {

	public int row;
	public int col;
	public float[][] var;

	public MatrixF(int row, int col) {
		setRow(row);
		setCol(col);

		var = new float[row][col];

		for (int i = 0; i < row; i++) {
			var[i] = new float[col];
		}
	}

	public static MatrixF inverse(MatrixF m) {
		MatrixF inv;
		MatrixF n;
		int iter, i, j, k;

		float v;

		float tmp;
		int max_key;

		if (m.col != m.row)
			return null;

		iter = m.row;
		inv = new MatrixF(m.row, m.col);
		n = new MatrixF(m.row, m.col * 2);

		// copy it
		for (j = 0; j < iter; j++)
			for (i = 0; i < iter; i++)
				n.var[j][i] = m.var[j][i];

		// insert identity matrix
		for (i = 0; i < iter; i++)
			n.var[i][i + iter] = 1.0f;

		// start gauss elimination
		for (i = 0; i < iter; i++) {

			// find max
			max_key = i;
			for (j = i + 1; j < iter; j++)
				if (n.var[j][i] > n.var[max_key][i])
					max_key = j;

			// swap with current row
			if (max_key != i) {
				for (j = 0; j < iter * 2; j++) {
					tmp = n.var[i][j];
					n.var[i][j] = n.var[max_key][j];
					n.var[max_key][j] = tmp;
				}
			}

			// normalize
			v = n.var[i][i];
			for (j = i + 1; j < iter * 2; j++)
				n.var[i][j] /= v;

			for (j = i + 1; j < iter; j++) {
				v = n.var[j][i];
				n.var[j][i] = 0.0f;
				for (k = i + 1; k < iter * 2; k++) {
					n.var[j][k] -= n.var[i][k] * v;
				}
			}

		}
		for (i = iter - 2; i >= 0; i--) {

			for (j = i; j >= 0; j--) {
				v = n.var[j][i + 1];
				for (k = 0; k < iter * 2; k++) {
					n.var[j][k] -= n.var[i + 1][k] * v;
				}
			}
		}

		// copy it
		for (j = 0; j < iter; j++)
			for (i = 0; i < iter; i++)
				inv.var[j][i] = n.var[j][i + iter];

		return inv;
	}

	public static MatrixF multiple(MatrixF a, MatrixF b) {
		MatrixF m;
		int col, row, iter;
		int i, j, k;

		if (a.col != b.row)
			return null;

		row = a.row;
		col = b.col;

		iter = a.col;

		m = new MatrixF(row, col);
		for (j = 0; j < row; j++) {
			for (i = 0; i < col; i++) {
				for (k = 0; k < iter; k++) {
					m.var[j][i] += a.var[j][k] * b.var[k][i];
				}
			}
		}
		return m;
	}

	public void print() {
		System.out.println("------------------");
		int i, j;
		for (j = 0; j < row; j++) {
			for (i = 0; i < col; i++) {
				System.out.print(var[j][i] + " , ");
			}
			System.out.println("");
		}
		System.out.println("------------------");
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

}
