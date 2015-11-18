import java.awt.Color;
import java.util.Arrays;

import edu.princeton.cs.algs4.Picture;


public class SeamCarver {
	private Picture picture;
	private double energy [] [];
	
	public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
	{
		this.picture = new Picture(picture);
		energy = new double [width()] [height()];
		
		for (int i = 0; i < width(); i++) 
			for (int j = 0; j < height(); j++)
				energy [i] [j] = energy (i,j);
		
	}
	
	public Picture picture()                          // current picture
	{
		return new Picture(this.picture);
	}
	
	public int width()                            // width of current picture
	{
		return this.picture.width();
	}
	
	public int height()                           // height of current picture
	{
		return this.picture.height();
	}
	
	private  double gradient(Color p, Color n)     
	{
		return  Math.pow(Math.abs(p.getRed() - n.getRed()),2)  + 
			 Math.pow(Math.abs(p.getGreen() - n.getGreen()), 2) +
			 Math.pow(Math.abs(p.getBlue() - n.getBlue()), 2);
	}
	
	private void checkNull (Object o) {
		if (o == null)
			throw new java.lang.NullPointerException ();
	}
	
	public  double energy(int x, int y)               // energy of pixel at column x and row y
	{
		if (x < 0 || y < 0 || x >= width() || y >= height())
			throw new java.lang.IndexOutOfBoundsException();
		
		if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1)
			return (double) 1000;
		else {
			Color xp = picture.get(x - 1, y);
			Color xn = picture.get(x + 1, y);
			Color yp = picture.get(x, y - 1);
			Color yn = picture.get(x, y + 1);
			return Math.sqrt(gradient(xp, xn) + gradient(yp, yn));
		}
	}
	
	/*private double [] adjustment (double length [] [], int h, int w) {
		double adj [];
		
		if (w == 0) 
			adj = new double [] {length [h-1] [w], length [h-1] [w+1]};
		else if (w == length[0].length - 1)
			adj = new double [] {length [h-1] [w-1], length [h-1] [w]};
		else
			adj = new double [] {length [h-1] [w-1], length [h-1] [w], length [h-1] [w+1]};
		
		return adj;
	}*/
	
	private int [] intVerticalSeam(double [] [] energy) {
		 double length [] [] = new double [energy[0].length] [energy.length];
		 int parent [] [] = new int [energy[0].length] [energy.length];
		 
		 for (int i = 0; i < energy[0].length; i++) 
				for (int j = 0; j < energy.length; j++) {
					if (i == 0) {
						parent [i][j] = 0;
						length [i][j] = energy [j] [i];
					}
					else {						
						
						if (energy.length == 1) {
							length [i][j] = length [i-1][j];
							parent [i][j] = j;
						}
						else {
							
							//double adj [] = adjustment (length , i, j);
							
							/*for (int n = 0 ;  n < adj.length ; n++) {
								if (n == 0) {
									length [i][j] = adj[n];
									parent [i][j] = j == 0 ? j + n : j + n - 1;
								}
								else if (adj[n] < length [i][j]) {
									length [i][j] = adj[n];
									parent [i][j] = j == 0 ? j + n : j + n - 1;
								}
							}*/
							if (j == 0) {
								if 	(length [i-1][j] < length [i-1][j+1]) {
									length [i][j] = length [i-1][j];
									parent [i][j] = j;
								}
								else {
									length [i][j] = length [i-1][j+1];
									parent [i][j] = j + 1;
								}
							}
							else if (j == energy.length - 1) {
								if 	(length [i-1][j] < length [i-1][j-1]) {
									length [i][j] = length [i-1][j];
									parent [i][j] = j;
								}
								else {
									length [i][j] = length [i-1][j-1];
									parent [i][j] =  j - 1;
								}
							}
							else {
								if 	(length [i-1][j] <= length [i-1][j+1] && length [i-1][j] <= length [i-1][j-1]) {
									length [i][j] = length [i-1][j];
									parent [i][j] = j;
								}
								else if (length [i-1][j+1] <= length [i-1][j] && length [i-1][j+1] <= length [i-1][j-1])
								{
									length [i][j] = length [i-1][j+1];
									parent [i][j] = j + 1;
								}
								else {
									length [i][j] = length [i-1][j-1];
									parent [i][j] =  j - 1;
								}
							}
						}					
						
						length [i][j] += energy [j] [i];
					}
				}				
		 
		 int min = 0;
		 double minlength = Double.MAX_VALUE;
		 
		 for (int i = 0; i < length [0].length; i++) {
			 if (length [energy[0].length - 1] [i] < minlength) {
				 minlength = length [energy[0].length - 1] [i];
				 min = i;
			 }
		 }
		 
		 int [] ret = new int [energy[0].length];
		 int par = min; //parent [min/width()][min%width()];
		 int cnt = energy[0].length - 1;
		 
		 while (cnt != -1) {
			 ret [cnt] = par;
			 par = parent [cnt--][par];
		 }	 
		 
		/*for (int i = 0; i < energy[0].length; i++) 
				for (int j = 0; j < energy.length; j++) {
					if (j == 0)
						System.out.println();
					System.out.print(length[i][j] + " ");
				}*/
		 
		/* System.out.println(" -----");
		 for (int i = 0; i < energy[0].length; i++) 
				for (int j = 0; j < energy.length; j++) {
					if (j == 0)
						System.out.println();
					System.out.print((int) parent[i][j] + " ");
				}*/
		 //System.out.println();
		 //System.out.println(Arrays.toString(ret));
		 return ret;
	}
	
	public int[] findVerticalSeam()               // sequence of indices for horizontal seam
	{
		 return intVerticalSeam (energy);
	}
	
	
	public   int[] findHorizontalSeam ()                 // sequence of indices for vertical seam
	{
		double [] [] energyInt = new double [height()] [width()];
		for (int i = 0; i < width(); i++) 
			for (int j = 0; j < height(); j++)
				energyInt [j] [i] = energy [i] [j];
		return intVerticalSeam (energyInt);
	}
	
	private void checkSeam (int[] seam, String type) {
		if ("h".equals(type) && (seam.length != width() || height() == 1) ||
				"v".equals(type) && (seam.length != height() || width() == 1))
			throw new java.lang.IllegalArgumentException();		
		
		int prev = seam [0];
		//System.out.println(Arrays.toString(seam));
		
		for (int i : seam) {
			//System.out.println(prev + " " + i);
			if (i < 0 || "h".equals(type) && i >= height() 
					|| "v".equals(type) && i >= width() 
					|| Math.abs(prev - i) > 1) {
				throw new java.lang.IllegalArgumentException();	
				}
			prev = i;
		}			
		
	}
	
	public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
	{
		checkNull(seam);
		checkSeam(seam, "h");
		Picture pictureOut = new Picture (width(), height() - 1);
		
		for (int i = 0; i < pictureOut.width(); i++) 
			for (int j = 0; j < pictureOut.height(); j++) 
				if (j < seam[i])
					pictureOut.set(i, j, picture.get(i, j));
				else 
					pictureOut.set(i, j , picture.get(i, j+1));

		picture = pictureOut;
		
		double [] [] energyTmp = new double [width()] [height()];
		
		for (int i = 0; i < width(); i++) 
			for (int j = 0; j < height(); j++)
				if (j == seam[i] || j == seam[i] - 1)
					energyTmp [i] [j] = energy (i,j);
				else if (j < seam[i])
					energyTmp [i] [j] = energy [i] [j];
				else 
					energyTmp [i] [j] = energy [i] [j+1];
					
		
		energy = energyTmp;
	}
	
	
	public    void removeVerticalSeam(int[] seam) {
		
		checkNull(seam);
		checkSeam(seam, "v");
		
		Picture pictureOut = new Picture (width() - 1, height());
		
		for (int i = 0; i < pictureOut.height(); i++) 
			for (int j = 0; j < pictureOut.width (); j++) 
				if (j < seam[i])
					pictureOut.set(j, i, picture.get(j, i));
				else {
					pictureOut.set(j, i , picture.get(j+1, i));
				}

		picture = pictureOut;
		
		double [] [] energyTmp = new double [width()] [height()];
		
		/*System.out.println(width());
		System.out.println(height());*/
		for (int i = 0; i < height(); i++) 
			for (int j = 0; j < width(); j++)
				if (j == seam[i] || j == seam[i] - 1)
					energyTmp [j] [i] = energy (j,i);
				else if (j < seam[i])
					energyTmp [j] [i] = energy [j] [i];
				else 
					energyTmp [j] [i] = energy [j+1] [i];
		
		energy = energyTmp;
	}
}
