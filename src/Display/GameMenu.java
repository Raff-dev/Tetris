package Display;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Display.Window.game;
import static Display.Window.window;
import static javafx.scene.paint.Color.*;

public class GameMenu extends VBox {
    private boolean isopen = true;
    private static int WIDTH = (int) (Game.WIDTH * 0.5);
    private static int HEIGHT = (int) (Game.HEIGHT * 0.5);
    private Rectangle bg = new Rectangle(0, 0, Window.WIDTH, Window.HEIGHT);
    private int selection = 0;

    GameMenu() {
        this.setTranslateX(Window.WIDTH * 0.5 - WIDTH * 0.5);
        this.setPrefSize(WIDTH, HEIGHT);
        bg.setFill(BLACK);
        bg.setOpacity(0.3);
        Arrays.asList(new String[]{"Play","Settings","Restart","Quit"}).
                forEach(MenuItem::new);
        setAlignment(Pos.CENTER);
        window.getChildren().addAll( bg,this);
    }

    private class MenuItem extends StackPane {
        public MenuItem(String name) {
            Rectangle bg = new Rectangle(WIDTH, 50);
            bg.setFill(CADETBLUE);
            Text text = new Text(name);
            text.setFill(WHITE);
            text.setFont(Font.font(50));
            setAlignment(Pos.CENTER);
            getChildren().addAll(bg,text);
            GameMenu.this.getChildren().add(this);
        }

        public MenuItem(Node... children) {
            super(children);
        }
    }
    public void toggle() {
        if (isopen = !isopen) {
            this.setVisible(true);
            game.pause();
        } else{
            this.setVisible(false);
            game.resume();
        }
    }

    public static void select() {

    }

    public static void switchSelection(boolean dir) {

    }

    public boolean isIsopen() {
        return isopen;
    }
}
