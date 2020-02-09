package org.bro.Music;

import org.bro.CrossyBro;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;


public class Music {

    private Clip clip;
    private String filePath;

    public Music(String filePath){
        this.filePath = filePath;
    }

    public void stop(){
        if (clip.isRunning()){
            clip.stop();
        }
    }

    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f){
            throw new IllegalArgumentException("Volume must be between 0f and 1f.");
        }
        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(20f * (float) Math.log10(volume));
    }

    public void startMusic(int loop) {
        URL soundURL;
        AudioInputStream audioInputStream = null;

        try {
            soundURL = CrossyBro.class.getResource(filePath);
            if (soundURL == null) {
                File file = new File(filePath);
                soundURL = file.toURI().toURL();
            }

            audioInputStream = AudioSystem.getAudioInputStream(soundURL);
        } catch (UnsupportedAudioFileException | IOException e) {
            System.err.println(e.getMessage());
        }

        try {
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // -1 for Infinite Clip Loop | clip.LOOP_CONTINUOUSLY
            clip.loop(loop);
            clip.start();
        } catch (LineUnavailableException | IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
