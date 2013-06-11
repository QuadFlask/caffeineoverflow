package flask.imageprocessing;

public class BlurFilter extends PixelUtil {
	public static final int TYPE_BOX = 0;
	public static final int TYPE_LINEAR = 1;
	public static final int TYPE_GAUSSIAN = 2;
	public static final int TYPE_SIMPLE_CROSS1x1 = 3;
	
	private float[][] matrix;
	private int width;
	private int heigth;
	private float radius;
	//private double strength;
	private int type;
	
	public BlurFilter(){}
	/**
	 * 
	 * @param w
	 * @param h
	 * @param $type
	 */
	public BlurFilter(int w, int h, int $type){
		if(w <= 0) w = 1;
		if(h <= 0) h = 1;
		w *=2; --w;
		h *=2; --h;
		
		width = (int)w;
		heigth = (int)h;
		radius = w;
		//strength = h;
		type = $type;
		
		switch(type){
			case TYPE_BOX:{
				matrix = makeMatrix_Box(width, heigth);
				break;
			}
			case TYPE_LINEAR:{
				matrix = makeMatrix_Linear(width, heigth);
				break;
			}
			case TYPE_GAUSSIAN:{
				matrix = makeMatrix_Gaussian((int)radius);
				break;
			}
			case TYPE_SIMPLE_CROSS1x1:{
				matrix = makeMatrix_SimpleCross1x1(width);
				break;
			}
			default:{
//				util.trace("default blured : TYPE_BOX");
				matrix = makeMatrix_Box(width, heigth);
				break;
			}
		}
	}
	
	/**
	 * 
	 * @param r
	 */
	public BlurFilter(int r) {
		this(r, r, TYPE_GAUSSIAN);
	}
	
	public BlurFilter(float r) {
		radius = r;
		matrix = makeMatrix_Gaussian(radius);
	}
	
	/**
	 * This constructor is for only TYPE_SIMPLE_CROSS1x1
	 * @param k : strength
	 * @param type : TYPE_SIMPLE_CROSS1x1 or TYPE_GAUSSIAN
	 */
	public BlurFilter(float k, int type){
		if(type == TYPE_SIMPLE_CROSS1x1){
			matrix = makeMatrix_SimpleCross1x1(k);
		}else if(type == TYPE_GAUSSIAN){
			radius = k;
			matrix = makeMatrix_Gaussian(radius);
		}
	}
	
	/**
	 * 
	 * @param w
	 * @param h
	 */
	public BlurFilter(int w, int h) {
		this(w, h, TYPE_BOX);
	}	
	
	protected float[][] makeMatrix_Box(int w, int h){
		float[][] result = new float[w][h];
		int i, j;
		
		for(i=0;i<h;i++){
			for(j=0;j<w;j++){
				result[j][i] = (float)1.0/(w*h);
			}
		}
		return  result;
	}
	
	protected float[][] makeMatrix_Linear(int w, int h){
		float[][] result = new float[w][h];
		float sum = 0;
		double d;
		int i, j;
		
		for(i=0;i<h;i++){
			for(j=0;j<w;j++){
				d = Math.sqrt(Math.abs(j-w/2)*Math.abs(j-w/2) + Math.abs(i-h/2)*Math.abs(i-h/2));
				d = 1/d;
				if(Double.isInfinite(d)) d = 1.0;
				sum += d;
				result[j][i] = (float)d;
			}
		}
		
		for(i=0;i<h;i++){
			for(j=0;j<w;j++){
				result[j][i] = (float)(result[j][i]/sum);
			}
		}

		return result;
	}
	
	protected float[][] makeMatrix_Gaussian(int r){
		if(r<=1) return makeMatrix_Box(1, 1);
		float[][] result = new float[r][r];
		double sum = 0;
		int i, j;
		
		double y, d;
		double sigma = r/3;
		
		for(i=0;i<r;i++){
			for(j=0;j<r;j++){
				d = Math.abs(j-r/2)*Math.abs(j-r/2) + Math.abs(i-r/2)*Math.abs(i-r/2);
				y = Math.exp(-d/(2*sigma*sigma)) / Math.sqrt(2*Math.PI*sigma);
				sum += y;
				result[j][i] = (float)y;
			}
		}
		
		for(i=0;i<r;i++){
			for(j=0;j<r;j++){
				result[j][i] = (float)(result[j][i]/sum);
			}
		}
		
		return  result;
	}
	
	protected float[][] makeMatrix_Gaussian(float r){
		if(r<=1) return makeMatrix_Box(1, 1);
		int i, j, ir = (int)(r)*2+3;
		float[][] result = new float[ir][ir];
		System.out.println(ir+", "+ir);
		double sum = 0;
		
		double y, d;
		double sigma = r/3;
		
		for(i=0;i<ir;i++){
			for(j=0;j<ir;j++){
				d = Math.abs(j-r/2)*Math.abs(j-r/2) + Math.abs(i-r/2)*Math.abs(i-r/2);
				y = Math.exp(-d/(2*sigma*sigma)) / Math.sqrt(2*Math.PI*sigma);
				sum += y;
				result[j][i] = (float)y;
			}
		}
		
		for(i=0;i<ir;i++){
			for(j=0;j<ir;j++){
				result[j][i] = (float)(result[j][i]/sum);
			}
		}
		
		return  result;
	}
	
	protected float[][] makeMatrix_SimpleCross1x1(float centerStrength){
		float[][] result = new float[3][3];
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				if(i==1) result[i][j] = 1.f/9;
				else if(j==1) result[i][j] = 1.f/9;
				else result[i][j] = 0;
			}
		}
		result[1][1] = centerStrength;
		return  result;
	}
	
	
	/**
	 * return matrix.
	 * @return float[][] matrix
	 */
	public float[][] getMatrix(){
		return matrix.clone();
	}
	
	public float[][] getMatrix(float strength){
		float[][] result = matrix.clone();
		for(int i=0;i<result.length;i++) {
			for(int j=0;j<result[0].length;j++) {
				result[i][j] *= strength;
			}
		}
		return matrix.clone();
	}

	/*
	public void printIt(){
		int i, j;
		
		for(i=0;i<heigth;i++){
			for(j=0;j<width-1;j++){
				System.out.print(matrix[j][i]+", \t");
			}
			System.out.println(matrix[width-1][i]);
		}
	}
	*/

}
