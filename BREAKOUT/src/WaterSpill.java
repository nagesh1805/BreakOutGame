import java.awt.Color;

import acm.graphics.*;
import acm.program.*;
import acm.util.RandomGenerator;
public class WaterSpill extends GCompound {
	
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	public WaterSpill(){
		double x,y ;
		GOval drops[] = new GOval[30];
		for(int i=0;i<30;i++){
			x = rgen.nextDouble(20,50);
			if(rgen.nextBoolean(0.5)){
				x=-x;
			}
			y = rgen.nextDouble(20,50);
			drops[i]=waterDrop();
		
			add(drops[i],x,-y);
			
			
		}
		
		markAsComplete();
	}
	
	public GOval waterDrop(){
		double a = rgen.nextDouble(1.0,2.0);
		double b = rgen.nextDouble(3.0,15.0);
		GOval oval=new GOval(a,b);
		
		oval.setFilled(true);
		oval.setColor(new Color(100,149,237));
		
		return oval;
	}

}
