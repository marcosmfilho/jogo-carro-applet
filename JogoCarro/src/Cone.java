import java.awt.Rectangle;


	public class Cone extends BaseVectorShape{
	    
		//bounding rectangle
	    public Rectangle getBounds() {
	        Rectangle r;
	        r = new Rectangle((int)getX()+15, (int) getY()-6, 20,30);
	        return r;
	    }

	    Cone() {
	        setShape(new Rectangle(20,30));
	        setAlive(true);
	    }
	}


