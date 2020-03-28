package DLC.Vaticancheek;

import DLC.DLC;
import javafx.collections.ListChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.nio.file.Paths;
import java.util.Random;

import static Display.Window.*;
import static Display.Window.game;

public class Vaticancheek {

    public Vaticancheek() {
        colors.getPalette().add("Vatican");
        gameMenu.getColorChoices().addListener(
                (ListChangeListener<String>) change -> {
                    String paletteName = gameMenu.getColorChoices().get(0);
                    if (paletteName.equals("Vatican")) game.addTask("JP2", 2,
                            () -> gameMenu.goFloaty(vaticancheek(), game), true);
                    else game.removeTask("JP2", true);
                });
    }

    private Pane vaticancheek() {
        sideBar.setLevelText(2137);
        int i = new Random().nextInt(2);
        Image image = new Image(Paths.get("src/DLC/Vaticancheek/images/papaj" + i + ".png").toUri().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(120);
        imageView.setFitWidth(120);
        imageView.setPreserveRatio(true);
        return new Pane(imageView);
    }
}
