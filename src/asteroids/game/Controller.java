package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Random;
import javax.swing.*;
import asteroids.participants.Asteroid;
import asteroids.participants.Bullet;
import asteroids.participants.Ship;
import asteroids.participants.UIship;
import asteroids.participants.ufobullet;
import sounds.soundforgame;
import asteroids.participants.UFO;

/**
 * Controls a game of Asteroids.
 */
public class Controller implements KeyListener, ActionListener, Iterable<Participant>
{
    /** The state of all the Participants */
    private ParticipantState pstate;
    /** sound for the game  */
    public soundforgame sound = new soundforgame();
    /** The ship (if one is active) or null (otherwise) */
    private Ship ship;
    /** The ufo (if one is active) or null (otherwise) */
    private UFO ufo;
    /** time for medium ufo to chang direction  */
    private int ufotimer = 30;
    /** time for small ufo to chang direction  */
    private int ufotimer1 = 10;
    /** the timer for ufo movement*/
    private int ufomove = 0;
    /** the time that ufo takes to spawn after death*/
    private int ufospawntime = RANDOM.nextInt(250) + 250;
    /** timer for ufo spawn*/
    private int ufotimelaps = 0;
    
    /** When this timer goes off, it is time to refresh the animation */
    private Timer refreshTimer;
    /** turn left indicator*/
    private int direction1 = 0;
    /** turn right indicator*/
    private int direction2 = 0;
    /** accelerate indicator*/
    private int forward = 0;
    /** initial music play speed*/
    private int musicgap = 43;
    /** timer for music*/
    private int musicplaytime = 0;
    /** used to calculate decrease in music play speed*/
    private int beatcount = 0;
    private Random rand = new Random();
    /**
     * The time at which a transition to a new stage of the game should be made. A transition is scheduled a few seconds
     * in the future to give the user time to see what has happened before doing something like going to a new level or
     * resetting the current level.
     */
    private long transitionTime;

    /** Number of lives left */
    private int lives;
    
    /** The game display */
    private Display display;
    /** the game score*/
    public int score = 0;
    /** bullet amount on screen*/
    private int b_count = 0;
    /** ship firing indicator*/
    private int fire = 0;
    /** current level */
    private int level = 1;
    /** life UI*/
    private UIship life1 = new UIship(EDGE_OFFSET, EDGE_OFFSET);
    private UIship life2 = new UIship(EDGE_OFFSET * 2, EDGE_OFFSET);
    private UIship life3 = new UIship(EDGE_OFFSET * 3, EDGE_OFFSET);
    /**
     * Constructs a controller to coordinate the game and screen
     */
    public Controller ()
    {
        // Initialize the ParticipantState
        pstate = new ParticipantState();

        // Set up the refresh timer.
        refreshTimer = new Timer(FRAME_INTERVAL, this);

        // Clear the transitionTime
        transitionTime = Long.MAX_VALUE;

        // Record the display object
        display = new Display(this);

        // Bring up the splash screen and start the refresh timer
        splashScreen();
        display.setVisible(true);
        refreshTimer.start();
    }

    /**
     * This makes it possible to use an enhanced for loop to iterate through the Participants being managed by a
     * Controller.
     */
    @Override
    public Iterator<Participant> iterator ()
    {
        return pstate.iterator();
    }

    /**
     * Returns the ship, or null if there isn't one
     */
    public Ship getShip ()
    {
        return ship;
    }

    /**
     * Configures the game screen to display the splash screen
     */
    private void splashScreen ()
    {
        // Clear the screen, reset the level, and display the legend
        clear();
        display.setLegend("Asteroids");

        // Place four asteroids near the corners of the screen.
        placeAsteroids();
    }

    /**
     * The game is over. Displays a message to that effect.
     */
    private void finalScreen ()
    {
        display.setLegend(GAME_OVER);
        
        display.removeKeyListener(this);
    }

    /**
     * Place a new ship in the center of the screen. Remove any existing ship first.
     */
    private void placeShip ()
    {
        // Place a new ship
        Participant.expire(ship);
        ship = new Ship(SIZE / 2, SIZE / 2, -Math.PI / 2, this);
        addParticipant(ship);
        display.setLegend("");
    }
    
    /**
     * Place a new ufo at random position out side of the screen. Remove any existing ufo first.
     */
    private void placeufo() {
        Participant.expire(ufo);
        if(level > 2) {
            if(rand.nextInt(2) == 0) {
                ufo = new UFO(1, 2, -5, SIZE - RANDOM.nextInt(SIZE - 1), 7, this);
            }
            else {
                ufo = new UFO(1, 2, SIZE + 5, SIZE - RANDOM.nextInt(SIZE - 1), 7, this);
            }
            addParticipant(ufo);
        }
        else if(level == 2) {
            if(rand.nextInt(2) == 0) {
                ufo = new UFO(3, 1, -5, SIZE - RANDOM.nextInt(SIZE - 1), 4, this);
            }
            else {
                ufo = new UFO(3, 1, SIZE + 5, SIZE - RANDOM.nextInt(SIZE - 1), 4, this);
            }
            addParticipant(ufo);
        }

        
    }
    /**
     * Places an asteroid near one corner of the screen. Gives it a random velocity and rotation.
     */
    private void placeAsteroids ()
    {
        addParticipant(new Asteroid(0, 2, EDGE_OFFSET, EDGE_OFFSET, 3, this));
        addParticipant(new Asteroid(1, 2, SIZE - EDGE_OFFSET, EDGE_OFFSET, 4, this));
        addParticipant(new Asteroid(2, 2, SIZE - EDGE_OFFSET, SIZE - EDGE_OFFSET, 1, this));
        addParticipant(new Asteroid(3, 2, SIZE + EDGE_OFFSET, SIZE - EDGE_OFFSET, 2, this));

    }
    /**
     * Places the life indicator
     */
    private void placeUI () {
        life1 = new UIship(20, EDGE_OFFSET - 50);
        life2 = new UIship(60, EDGE_OFFSET - 50);
        life3 = new UIship(100, EDGE_OFFSET -50);
        addParticipant(life1);
        addParticipant(life2);
        addParticipant(life3);
    }
    
    /**
     * add asteroids according to the level
     */
    private void addmoreAsteroids() {
        if(level > 1) {
            for(int i = 1; i <= level - 1; i ++) {
                addParticipant(new Asteroid(0, 2, EDGE_OFFSET - rand.nextInt(EDGE_OFFSET), EDGE_OFFSET - rand.nextInt(EDGE_OFFSET), 3, this));
            }
        }
    }

    /**
     * Clears the screen so that nothing is displayed
     */
    private void clear ()
    {
        pstate.clear();
        display.setLegend("");
        ship = null;
    }
    
    /**
     * Sets things up and begins the next level.
     */
    private void nextlevel ()
    {
        // Clear the screen
        musicgap = 45;
        clear();
        ufotimelaps = 0;
        forward = 0;
        direction1 = 0;
        direction2 = 0;
        ufo = null;
        sound.stopsuacerbig();
        sound.stopsuacersmall();
        
        // Place asteroids
        placeAsteroids();
        addmoreAsteroids();
        // Place the ship
        placeShip();
        placeUI();

        // Start listening to events (but don't listen twice)
        display.removeKeyListener(this);
        display.addKeyListener(this);

        // Give focus to the game screen
        display.requestFocusInWindow();
    }
    
    /**
     * Sets things up and begins a new game.
     */
    private void initialScreen ()
    {
        // Clear the screen
        clear();
        ufotimelaps = 0;
        forward = 0;
        direction1 = 0;
        direction2 = 0;
        sound.stopsuacerbig();
        sound.stopsuacersmall();
        level = 1;
        score = 0;
        ufo = null;
        musicgap = 45;
        // Plac asteroids
        placeAsteroids();
        
        // Place the ship
        placeShip();
        placeUI();
        // Reset statistics
        lives = 3;

        // Start listening to events (but don't listen twice)
        display.removeKeyListener(this);
        display.addKeyListener(this);

        // Give focus to the game screen
        display.requestFocusInWindow();
    }

    /**
     * Adds a new Participant
     */
    public void addParticipant (Participant p)
    {
        pstate.addParticipant(p);
    }

    /**
     * The ship has been destroyed
     */
    public void shipDestroyed ()
    {
        // Null out the ship
        ship = null;
        sound.stopthrust();
        // Display a legend
        //display.setLegend("Ouch!");
        sound.playbangship();
        // Decrement lives
        lives--;

        // Since the ship was destroyed, schedule a transition
        scheduleTransition(END_DELAY);
    }
    
    /**
     * The ufo has been destroyed
     */
    public void ufoDestroyed() {
        ufo = null;
        sound.playbangAlienShip();
        if(level == 2) {
            score += 200;
        }
        else if (level > 2) {
            score += 1000;
        }
        
    }
    
    /**
     * An asteroid has been destroyed
     */
    public void asteroidDestroyed ()
    {
        // If all the asteroids are gone, schedule a transition
        
        if (countAsteroids() == 0)
        {
            scheduleTransition(END_DELAY);

        }
    }

    /**
     * Schedules a transition m msecs in the future
     */
    private void scheduleTransition (int m)
    {
        transitionTime = System.currentTimeMillis() + m;
    }

    /**
     * This method will be invoked because of button presses and timer events.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // The start button has been pressed. Stop whatever we're doing
        // and bring up the initial screen
        
        if (e.getSource() instanceof JButton)
        {
            initialScreen();
        }
        // Time to refresh the screen and deal with keyboard input
        else if (e.getSource() == refreshTimer)
        {

            // It may be time to make a game transition
            if(direction1 == 1 && ship != null) {
                ship.turnLeft();
            }
            if(direction2 == 1 && ship != null) {
                ship.turnRight();
            }
            if(forward == 1 && ship != null) {
                ship.accelerate();
                sound.playthrust();
                ship.emitfire();
            }
            else if(forward == 0 && ship != null){
                ship.deaccelerate();
                sound.stopthrust();
                ship.idle();
            }
            if(fire == 1 && b_count < 7 && ship != null && countAsteroids() != 0) {
                sound.playfire();   
                addParticipant(new Bullet((int) ship.getXNose(), (int) ship.getYNose(), this, ship.getRotation()));
                b_count += 1;
            }
            else {
                
            }
            checklife();
            display.setScore("" + score);
            display.setLevel("" + level);
            performTransition();
            // play music
            musicplaytime += 1;
            if(musicplaytime >= musicgap && ship != null && countAsteroids() != 0) {
                sound.playbeat();
                musicplaytime = 0;
                beatcount += 1;
                if(musicgap > 14 && beatcount > 1) {
                    musicgap -= 1;
                    beatcount = 0;
                }
            }
            //
            checkufo();
            ufomovement();
            
            // Move the participants to their new locations
            pstate.moveParticipants();

            // Refresh screen
            display.refresh();
            
        }
    }

    /**
     * If the transition time has been reached, transition to a new state
     */
    private void performTransition ()
    {
        // Do something only if the time has been reached
        if (transitionTime <= System.currentTimeMillis())
        {
            // Clear the transition time
            transitionTime = Long.MAX_VALUE;
            if(lives > 0 && ship == null) {
                
                direction1 = 0;
                direction2 = 0;
                forward = 0;
                fire = 0;
                placeShip();
            }
            if(countAsteroids() == 0 && ship != null ) {
                level += 1;
                nextlevel();
            }

            // If there are no lives left, the game is over. Show the final
            // screen.
            if (lives <= 0)
            {
                
                Participant.expire(life1);
                finalScreen();
            }
        }
    }

    /**
     * Returns the number of asteroids that are active participants
     */
    private int countAsteroids ()
    {
        int count = 0;
        for (Participant p : this)
        {
            if (p instanceof Asteroid)
            {
                count++;
            }
        }
        return count;
    }

    /**
     * If a key of interest is pressed, record that it is down.
     */
    @Override
    public void keyPressed (KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship != null)
        {
            
            direction2 = 1;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT && ship != null) {
            
            direction1 = 1; 
        }
        if(e.getKeyCode() == KeyEvent.VK_UP && ship != null) {
            
            forward = 1;
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE && ship != null) {
            fire = 1;
        }

    }

    @Override
    public void keyTyped (KeyEvent e)
    {
    }

    @Override
    public void keyReleased (KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship != null) {
            direction2 = 0;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT && ship != null) {
            direction1 = 0;
        }
        if(e.getKeyCode() == KeyEvent.VK_UP && ship != null) {
            forward = 0;
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE && ship != null) {
            fire = 0;
        }
    }
    
    public void reduce_bullet() {
        b_count -= 1;
    }
    
    public void checklife() {
        if(lives == 2 && life3 != null) {
            
            Participant.expire(life3);
            life3 = null;
        }
        else if(lives == 1 && life2 != null) {
            
            Participant.expire(life2);
            life2 = null;
        }
        else if(lives == 0 && life1 != null) {
            Participant.expire(life1);
            life1 = null;
        }
    }
    
    private void ufomovement() {
        if(ufo != null) {
            ufomove += 1;
        }
        if(ufomove > ufotimer && ufo != null && level == 2 && ship != null) {
            addParticipant(new ufobullet((int) ufo.getX(), (int) ufo.getY(), this, RANDOM.nextDouble() * 2 * Math.PI));
        }
        if(ufomove > ufotimer && ufo != null && level > 2 && ship != null) {
            addParticipant(new ufobullet((int) ufo.getX(), (int) ufo.getY(), this, Math.atan((ship.getY() - ufo.getY()) / (ship.getX() - ufo.getX()) )));
        }
        if(ufomove > ufotimer1 && ufo != null && ship != null && level > 2) {
            ufo.turn();
            ufomove = 0;
        }
        
        if(ufomove > ufotimer && ufo != null && ship != null && level == 2) {
            ufo.turn();
            ufomove = 0;
        }
    }
    
    private void checkufo() {
        if(ufo == null && level > 1) {
            ufotimelaps += 1;
            sound.stopsuacerbig();
            sound.stopsuacersmall();
        }
        else {
            if(level > 2) {
                sound.playsaucersmall();
            }
            else if(level > 1){
                sound.playsaucerbig();
            }
            
        }
        if(ufotimelaps >= ufospawntime) {
            placeufo();
            ufospawntime = ufospawntime = RANDOM.nextInt(250) + 250;
            ufotimelaps = 0;
        }
    }
    
}
