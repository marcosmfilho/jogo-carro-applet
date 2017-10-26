import java.awt.Rectangle;

public class Bala extends BaseVectorShape {

    //bounding rectangle
    public Rectangle getBounds() {
        Rectangle r;
        r = new Rectangle((int)getX()+6, (int) getY(), 2, 10);
        return r;
    }

    Bala() {        
        setShape(new Rectangle(0, 0, 2, 10));
        setAlive(false);
    }
}
