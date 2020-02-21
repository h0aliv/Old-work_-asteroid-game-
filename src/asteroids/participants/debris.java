package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.Random;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

/** represent debris*/
public class debris extends Participant 
{
    private Shape outline;
    
    
    /**
     * Constructs a debris at the specified coordinates and give it random rotation and shape.
     */
    public debris(int x, int y) {
        Random rand = new Random();
        /**generate debris*/
        if(rand.nextInt(50) < 30) {
            setPosition(x + rand.nextInt(50) , y + rand.nextInt(50));
            setPosition(x - rand.nextInt(50) , y - rand.nextInt(50));
            setRotation(rand.nextDouble() * Math.PI * 2);
            Path2D.Double poly = new Path2D.Double();
            poly.moveTo(0, 0);
            poly.lineTo(1, 0);
            poly.lineTo(1, 1);
            poly.lineTo(0, 1);
            poly.closePath();
            outline = poly;
            accelerate(1);
            new ParticipantCountdownTimer(this, "expire", 700);
        }
        else {
            setPosition(x + rand.nextInt(50) , y + rand.nextInt(50));
            setPosition(x - rand.nextInt(50) , y - rand.nextInt(50));
            setRotation(rand.nextDouble() * Math.PI * 2);
            Path2D.Double poly = new Path2D.Double();
            poly.moveTo(0, 0);
            poly.lineTo(0, 11);
            outline = poly;
            accelerate(1);
            new ParticipantCountdownTimer(this, "expire", 700);
        }
    }
    
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    @Override
    public void collidedWith (Participant p)
    {
        
    }
    
    @Override
    public void countdownComplete (Object payload)
    {
        // Give a burst of acceleration, then schedule another
        // burst for 700 msecs from now.
        if (payload.equals("expire"))
        {
            
            Participant.expire(this);
        }
    }
}
