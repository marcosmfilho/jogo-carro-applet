import java.awt.Rectangle;

public class Campo extends BaseVectorShape{
    
	//bounding rectangle
    public Rectangle getBounds() {
        Rectangle r;
        r = new Rectangle((int)(getX())+6,(int)(getY())-6, 50,90);
        return r;
    }

    Campo() {
        setShape(new Rectangle(50,90));
        setAlive(false);
    }
}