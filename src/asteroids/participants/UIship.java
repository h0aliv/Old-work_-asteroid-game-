package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Path2D;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

/**
 * 
 * represent life indicator
 *
 */
public class UIship extends Participant 
{
    private Shape outline;

    /**
     * construct the UI life indicator
     */
    public UIship (int x, int y)
    {
        setPosition(x, y);

        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(0, -21);
        poly.lineTo(12, 21);
        poly.lineTo(10, 14);
        poly.lineTo(-10, 14);
        poly.lineTo(-12, 21);
        poly.closePath();
        outline = poly;
    }
    
    /** remove the life indicator*/
    public void remove() {
        outline = null;
    }
    
    @Override
    protected Shape getOutline ()
    {
        // TODO Auto-generated method stub
        return outline;
    }

    @Override
    public void collidedWith (Participant p)
    {
        // TODO Auto-generated method stub
        
    }

}
