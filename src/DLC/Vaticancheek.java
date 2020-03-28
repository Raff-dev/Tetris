package DLC;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Paths;
import java.util.Random;

import static Display.Window.*;
import static Display.Window.game;

public class Vaticancheek {

    public Vaticancheek() {
        System.out.println("hchuchucjcujcucj");
        game.addTask("JP2", 2, () -> gameMenu.goFloaty(vaticancheek(), gameMenu), false);
    }

    private Node vaticancheek() {
        System.out.println("chujujuj");
        sideBar.setLevelText(2137);
        int i = new Random().nextInt(2);
        Image image = new Image(Paths.get("src/DLC/images/papaj" + i + ".png").toUri().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(120);
        imageView.setFitWidth(120);
        imageView.setPreserveRatio(true);
        return imageView;
    }
}
