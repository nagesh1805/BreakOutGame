import java.awt.Color;

import acm.graphics.*;
import acm.program.*;
import acm.util.RandomGenerator;
public class BrickBreak extends GCompound {
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	public BrickBreak(Color clr) {
	/*	GPolygon tri1= trianglePiece(clr);
		add(tri1,5,10);
		GPolygon rect1= rectanglePiece(clr);
		add(rect1,10,5);*/
		double x,y ;
		GPolygon ply[] = new GPolygon[20];
		for(int i=0;i<20;i++){
			x = rgen.nextDouble(20,50);
			y = rgen.nextDouble(20,50);
			if(i%2==0){
				ply[i]=trianglePiece(clr);
			}
			else{
				ply[i]=rectanglePiece(clr);
			}
			add(ply[i],x,y);
			
			
		}
		
		markAsComplete();
	}
	
	public GPolygon trianglePiece(Color clr){
		double a = rgen.nextDouble(1.0,3.0);
		double b = rgen.nextDouble(1.0,3.0);
		GPolygon tri=new GPolygon();
		tri.addVertex(-a, 0);
		tri.addVertex(a,b);
		//tri.addEdge(a, -b);
		tri.addVertex(-a, b);
		tri.setFilled(true);
		tri.setColor(clr);
		
		return tri;
	}
	
	public GPolygon rectanglePiece(Color clr){
		double a = rgen.nextDouble(3.0,5.0);
		double b = rgen.nextDouble(3.0,5.0);
		GPolygon rect=new GPolygon();
		rect.addVertex(-a, 0);
		rect.addVertex(a,b);
		rect.addVertex(a, -b);
		rect.addVertex(-a, b);
		rect.setFilled(true);
		rect.setColor(clr);
		
		return rect;
	}



}
