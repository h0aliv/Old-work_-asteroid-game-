package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Path2D;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import asteroids.destroyers.*;


/**
 * 
 * bullet shot by ufo
 *
 */
public class ufobullet extends Participant implements NeutralDestroyer
{
    private Shape outline;
    private Controller controller;
    
    /**
     * Constructs a ufobullet at the specified coordinates and set its direction.
     */
    public ufobullet(int x, int y, Controller controller, double rotation) {
        this.controller = controller;
        setPosition(x, y);
        super.setRotation(rotation);
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(0, 0);
        poly.lineTo(1, 0);
        poly.lineTo(1, 1);
        poly.lineTo(0, 1);
        poly.closePath();
        outline = poly;
        accelerate(10);
        new ParticipantCountdownTimer(this, "expire", 1500);
    }
    
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }
    
    /** check collision*/
    @Override
    public void collidedWith (Participant p)
    {
        // TODO Auto-generated method stub
        if (p instanceof ShipDestroyer)
        {
            // Expire the ship from the game
            
            Participant.expire(this);
        }
        else if(p instanceof AsteroidDestroyer) {
            Participant.expire(this);
        }
    }
    
    @Override
    public void countdownComplete (Object payload)
    {
        // Give a burst of acceleration, then schedule another
        // burst for 200 msecs from now.
        if (payload.equals("expire"))
        {
            
            Participant.expire(this);
        }
    }
}
