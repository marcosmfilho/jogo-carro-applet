import java.awt.Rectangle;


	public class Monstros extends BaseVectorShape{
	    
		//bounding rectangle
	    public Rectangle getBounds() {
	        Rectangle r;
	        r = new Rectangle((int)getX()+6, (int) getY()-6, 20,30);
	        return r;
	    }

	    Monstros() {
	        setShape(new Rectangle(20,30));
	        setAlive(true);
	    }
	}
