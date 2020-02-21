package asteroids.participants;

import static asteroids.game.Constants.RANDOM;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.Random;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.NeutralDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;

/** represent Alien ship */
public class UFO extends Participant implements NeutralDestroyer
{
    private Controller controller;
    private Shape outline;
    private int turn = 0;
    private Random rand = new Random();
    /**
     * Constructs a ufo at the specified coordinates.
     */
    public UFO(int variety, int size, double x, double y, int speed, Controller controller) {
        setPosition(x, y);
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(0, 0);
        poly.lineTo(10, 12);
        poly.lineTo(30, 12);
        poly.lineTo(40, 0);
        poly.lineTo(30, -12);
        poly.lineTo(10, -12);
        poly.closePath();
        poly.moveTo(13, -12);
        poly.lineTo(16, -20);
        poly.lineTo(24, -20);
        poly.lineTo(27, -12);
        
        if(size > 1) {
            poly.transform(AffineTransform.getScaleInstance(0.5, 0.5));
        }
        
        outline = poly;
        setVelocity(speed, 0);
        this.controller = controller;
    }
    
    /** change direction*/
    public void turn() {
        turn = rand.nextInt(4);
        if(turn == 0) {
            setDirection(-Math.PI / 3);
            
        }
        else if(turn == 1){
            setDirection(Math.PI / 3);
            
        }
        else {
            setDirection(0);
        }
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
        if (p instanceof AsteroidDestroyer)
        {
            controller.ufoDestroyed();
            controller.addParticipant(new debris((int)getX(), (int)getY()));
            controller.addParticipant(new debris((int)getX(), (int)getY()));
            controller.addParticipant(new debris((int)getX(), (int)getY()));
            controller.addParticipant(new debris((int)getX(), (int)getY()));
            Participant.expire(this);
            
        }
        else if (p instanceof ShipDestroyer)
        {
            // Expire the ship from the game
            controller.ufoDestroyed();
            controller.addParticipant(new debris((int)getX(), (int)getY()));
            controller.addParticipant(new debris((int)getX(), (int)getY()));
            controller.addParticipant(new debris((int)getX(), (int)getY()));
            controller.addParticipant(new debris((int)getX(), (int)getY()));
            Participant.expire(this);
            
        }
        
    }
}
