package Display;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

public class SoundHandler {
    private double volume = 0.5;

    public enum Sound {init,denied,buttonHover,buttonSelect,blockRotate,blockLanded,lineClear}

    public SoundHandler() {
        playSound(Sound.init);
    }

    public void playSound(Sound soundName) {
        String sound = Paths.get(
                "src/resources/audio/" + soundName + ".wav").toUri().toString();
        Media media = new Media(sound);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
    }

    public void setVolume(double volume){
        this.volume=volume;
    }

}
