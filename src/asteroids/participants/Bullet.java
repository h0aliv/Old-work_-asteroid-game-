package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Path2D;
import asteroids.destroyers.*;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;


/** 
 * bullet shot by ship 
 * */
public class Bullet extends Participant implements AsteroidDestroyer
{
    private Shape outline;
    private Controller controller;
    
    /**
     * Constructs a bullet at the specified coordinates and set its direction.
     */
    public Bullet(int x, int y, Controller controller, double rotation) {
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
        accelerate(13);
        new ParticipantCountdownTimer(this, "expire", 1500);
    }
    
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    @Override
    public void collidedWith (Participant p)
    {
        // TODO Auto-generated method stub
        if (p instanceof ShipDestroyer)
        {
            // Expire the ship from the game
            controller.reduce_bullet();
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
            controller.reduce_bullet();
            Participant.expire(this);
        }
    }
}
