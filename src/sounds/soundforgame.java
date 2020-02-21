package sounds;

import java.io.BufferedInputStream;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class soundforgame
{
    private Clip fireClip;
    
    private Clip bangship;
    private Clip beat1;
    private Clip beat2;
    private Clip bangLarge;
    private Clip bangMedium;
    private Clip bangSmall;
    
    private Clip thrust;
    private Clip bangAlienShip;
    private Clip saucerbig;
    private Clip saucersmall;
    
    private int gap = 0;
    
    public soundforgame() {
        fireClip = createClip("/sounds/fire.wav");
       
        bangship = createClip("/sounds/bangShip.wav");
        beat1 = createClip("/sounds/beat1.wav");
        beat2 = createClip("/sounds/beat2.wav");
        bangLarge = createClip("/sounds/bangLarge.wav");
        bangMedium = createClip("/sounds/bangMedium.wav");
        bangSmall = createClip("/sounds/bangSmall.wav");
        
        thrust = createClip("/sounds/thrust.wav");
        bangAlienShip = createClip("/sounds/bangAlienShip.wav");
        saucerbig = createClip("/sounds/saucerBig.wav");
        saucersmall = createClip("/sounds/saucerSmall.wav");
    }
    
    public void playfire() {
        if (fireClip.isRunning())
        {
            fireClip.stop();
        }
        fireClip.setFramePosition(0);
        fireClip.start();
    }
    
    public void playbangship() {
        if (bangship.isRunning())
        {
            bangship.stop();
        }
        bangship.setFramePosition(0);
        bangship.start();
    }
    
    public void playbeat() {
        if(gap == 0) {
            beat1.setFramePosition(0);
            beat1.start();
            gap = 1;
        }
        else {
            beat2.setFramePosition(0);
            beat2.start();
            gap = 0;
        }

 
    }
    
    public void playbangsmall() {
        if (bangSmall.isRunning())
        {
            bangSmall.stop();
        }
        bangSmall.setFramePosition(0);
        bangSmall.start();
    }
    
    public void playbangmedium() {
        if (bangMedium.isRunning())
        {
            bangMedium.stop();
        }
        bangMedium.setFramePosition(0);
        bangMedium.start();
    }
    
    public void playbanglarge() {
        if (bangLarge.isRunning())
        {
            bangLarge.stop();
        }
        bangLarge.setFramePosition(0);
        bangLarge.start();
    }
    
    public void playthrust() {
        thrust.start();
        thrust.loop(10);
    }
    
    public void stopthrust() {
        thrust.stop();
    }
    
    public void playsaucerbig() {
        saucerbig.start();
        saucerbig.loop(10);
    }
    
    public void stopsuacerbig() {
        saucerbig.stop();
    }
    
    public void playsaucersmall() {
        saucersmall.start();
        saucersmall.loop(10);
    }
    
    public void stopsuacersmall() {
        saucersmall.stop();
    }
    
    public void playbangAlienShip() {
        if (bangAlienShip.isRunning())
        {
            bangAlienShip.stop();
        }
        bangAlienShip.setFramePosition(0);
        bangAlienShip.start();
    }
    
    public Clip createClip (String soundFile)
    {
        // Opening the sound file this way will work no matter how the
        // project is exported. The only restriction is that the
        // sound files must be stored in a package.
        try (BufferedInputStream sound = new BufferedInputStream(getClass().getResourceAsStream(soundFile)))
        {
            // Create and return a Clip that will play a sound file. There are
            // various reasons that the creation attempt could fail. If it
            // fails, return null.
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            return clip;
        }
        catch (LineUnavailableException e)
        {
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
        catch (UnsupportedAudioFileException e)
        {
            return null;
        }
    }
}
