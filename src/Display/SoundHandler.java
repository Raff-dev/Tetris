package Display;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

/**
 * Takes care of playing sounds.
 * @author Rafal Lazicki
 */
public class SoundHandler {
    private double volume = 0.4;

    public enum Sound {init, denied, buttonHover, buttonSelect, blockRotate, blockLanded, lineClear}

    SoundHandler() {
        playSound(Sound.init);
    }

    /**
     * Finds the files path and plays it
     * @param soundName name of the sound file that should be played
     */
    public void playSound(Sound soundName) {
        String sound = Paths.get(
                "src/resources/audio/" + soundName + ".wav").toUri().toString();
        Media media = new Media(sound);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
    }

    void setVolume(double volume) {
        this.volume = volume * 0.01;
    }

    int getVolume() {
        return (int) (volume * 100);
    }
}
