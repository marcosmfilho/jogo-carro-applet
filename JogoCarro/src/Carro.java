import java.awt.Rectangle;

public class Carro extends BaseVectorShape{
    
	//bounding rectangle
    public Rectangle getBounds() {
        Rectangle r;
        r = new Rectangle((int)(getX())+6,(int)(getY())-6, 40,80);
        return r;
    }

    Carro() {
        setShape(new Rectangle(40,80));
        setAlive(false);
    }
}


