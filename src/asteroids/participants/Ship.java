package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.*;
import asteroids.destroyers.*;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import sounds.soundforgame;

/**
 * Represents ships
 */
public class Ship extends Participant implements AsteroidDestroyer
{
    /** The outline of the ship */
    private Shape outline;

    /** Game controller */
    private Controller controller;
    private int flash = 1;
    soundforgame sound = new soundforgame();
    /**
     * Constructs a ship at the specified coordinates that is pointed in the given direction.
     */
    public Ship (int x, int y, double direction, Controller controller)
    {
        this.controller = controller;
        setPosition(x, y);
        setRotation(direction);

        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(21, 0);
        poly.lineTo(-21, 12);
        poly.lineTo(-14, 10);
        poly.lineTo(-14, -10);
        poly.lineTo(-21, -12);
        poly.closePath();
        outline = poly;

        // Schedule an acceleration in two seconds
        new ParticipantCountdownTimer(this, "move", 2000);
    }
    /** add thrust */
    public void emitfire() {
        
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(21, 0);
        poly.lineTo(-21, 12);
        poly.lineTo(-14, 10);
        poly.lineTo(-14, -10);
        poly.lineTo(-21, -12);
        poly.closePath();
        if(flash == 1) {
            poly.moveTo(-14, -5);
            poly.lineTo(-24, 0);
            poly.lineTo(-14, 5);
            flash = 0;
        }
        else {
            flash = 1;
        }
        outline = poly;
    }
    
    /** ship is idle remove thrust*/
    public void idle() {
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(21, 0);
        poly.lineTo(-21, 12);
        poly.lineTo(-14, 10);
        poly.lineTo(-14, -10);
        poly.lineTo(-21, -12);
        poly.closePath();
        outline = poly;
    }
    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getXNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getX();
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getYNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getY();
    }

    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    /**
     * Customizes the base move method by imposing friction
     */
    @Override
    public void move ()
    {
        //applyFriction(SHIP_FRICTION);
        
        super.move();
        System.out.println(super.getSpeed());
    }

    /**
     * Turns right by Pi/16 radians
     */
    public void turnRight ()
    {
        rotate(Math.PI / 16);
    }

    /**
     * Turns left by Pi/16 radians
     */
    public void turnLeft ()
    {
        rotate(-Math.PI / 16);
    }

    /**
     * Accelerates by SHIP_ACCELERATION
     */
    public void accelerate ()
    {
        accelerate(SHIP_ACCELERATION);
    }
    /** gradually decelerate when idle */
    public void deaccelerate ()
    {
        deaccelerate(SHIP_ACCELERATION);
    }
    
    /**
     * When a Ship collides with a ShipDestroyer, it expires
     */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipDestroyer)
        {
            // Expire the ship from the game
            Participant.expire(this);
            controller.addParticipant(new debris((int)getX(), (int)getY()));
            controller.addParticipant(new debris((int)getX(), (int)getY()));
            controller.addParticipant(new debris((int)getX(), (int)getY()));
            controller.addParticipant(new debris((int)getX(), (int)getY()));
            // Tell the controller the ship was destroyed
            controller.shipDestroyed();
        }
        
        if (p instanceof NeutralDestroyer)
        {
            // Expire the ship from the game
            Participant.expire(this);
            controller.addParticipant(new debris((int)getX(), (int)getY()));
            controller.addParticipant(new debris((int)getX(), (int)getY()));
            controller.addParticipant(new debris((int)getX(), (int)getY()));
            controller.addParticipant(new debris((int)getX(), (int)getY()));
            // Tell the controller the ship was destroyed
            controller.shipDestroyed();
        }
    }

    /**
     * This method is invoked when a ParticipantCountdownTimer completes its countdown.
     */
    @Override
    public void countdownComplete (Object payload)
    {
        // Give a burst of acceleration, then schedule another
        // burst for 200 msecs from now.
        if (payload.equals("move"))
        {
            new ParticipantCountdownTimer(this, "move", 20);
        }
    }
}
