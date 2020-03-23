package Display;

import Bindings.ButtonBindings;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Display.GameMenu.ButtonName.*;
import static Display.GameMenu.Mode.*;
import static Display.Window.*;
import static javafx.scene.paint.Color.*;

public class GameMenu extends VBox {
    public enum Mode {START, PAUSE, RUNNING}

    private static Mode mode = START;
    private static int WIDTH = (int) (Game.WIDTH * 0.5);
    private static int HEIGHT = (int) (Game.HEIGHT * 0.5);
    private static Rectangle bg = new Rectangle(0, 0, Window.WIDTH, Window.HEIGHT);
    private static MenuItem selection;

    public static List<MenuItem> buttons = new ArrayList<>();
    private  List<MenuItem> activeButtons = new ArrayList<>();
    private  List<MenuItem> backgroundButtons = new ArrayList<>();

    public enum ButtonName {Play, Settings, Easy, Medium, Hard, Restart, Resume, Quit}

    GameMenu() {
        setProperties();
        ButtonBindings.bind();
        window.getChildren().addAll(bg, this);
    }

     void init() {
        selection = buttons.get(0);
        selection.setHovered();
        setActiveButtons(Play, Settings, Quit);
    }

    private void setProperties() {
        this.setTranslateX(Window.WIDTH * 0.5);
        this.setTranslateY(Window.HEIGHT * 0.5);
        this.setPrefSize(WIDTH, HEIGHT);
        bg.setFill(BLACK);
        bg.setOpacity(0.3);
        setSpacing(10);
        setAlignment(Pos.CENTER);
    }

    public static class MenuItem extends StackPane implements Task {
        ButtonName name;
        Task task;
        Text text = new Text();
        Rectangle bg = new Rectangle();

        public MenuItem(ButtonName name, Task task) {
            this.name = name;
            this.task = task;
            text.setText(String.valueOf(name));
            setDefault();
            getChildren().addAll(bg, text);
        }

        void setDefault() {
            setTranslateX(0);
            setOpacity(1);
            setScaleX(1);
            setScaleY(1);
            bg.setWidth(WIDTH);
            bg.setHeight(50);
            bg.setFill(CADETBLUE);
            text.setFont(Font.font(30));
            text.setFill(WHITE);
            setAlignment(Pos.CENTER);
        }

        void setHovered() {
            bg.setHeight(70);
            text.setFont(Font.font(50));
        }

        @Override
        public void execute() {
            task.execute();
        }
    }

    public void toggle() {
        if (mode == Mode.PAUSE) resume();
        else if (mode == Mode.RUNNING) pause();
    }

    public void extendWith(ButtonName... names) {
        backgroundButtons = activeButtons;
        backgroundButtons.forEach(b -> {
            b.setTranslateX(b.getTranslateX() - 200);
            b.setOpacity(0.5);
            b.setScaleX(0.7);
            b.setScaleY(0.7);
        });
        setActiveButtons(names);
    }

    private  void closeExtension() {
        gameMenu.getChildren().removeAll(activeButtons);
        backgroundButtons.forEach(b -> b.setDefault());
        gameMenu.getChildren().addAll(backgroundButtons);
        activeButtons = backgroundButtons;
    }

    private void setActiveButtons(ButtonName... names) {
        gameMenu.getChildren().removeAll(activeButtons);
        Stream<MenuItem> extension = buttons.stream().filter(
                b -> Arrays.asList(names).contains(b.name));
        activeButtons = extension.collect(Collectors.toList());
        gameMenu.getChildren().addAll(activeButtons);
        selection = activeButtons.get(0);
        selection.setHovered();
    }

    public void setDifficulty(int level) {
        game.init();
        game.increaseLevel(level);
        mode = RUNNING;
        window.getChildren().removeAll(bg, gameMenu);
        gameMenu.getChildren().removeAll(buttons);
        closeExtension();
        setActiveButtons(Resume, Settings, Quit);
    }

    private static void pause() {
        mode = PAUSE;
        window.getChildren().addAll(bg, gameMenu);
        game.pause();
    }

    public static void resume() {
        mode = RUNNING;
        window.getChildren().removeAll(bg, gameMenu);
        game.resume();
    }

    public void select() {
        buttons.stream().filter(b -> b == selection).findFirst().get().execute();
    }

    public void switchSelection(int dir) {
        selection.setDefault();
        Collections.rotate(activeButtons, -dir);
        selection = activeButtons.get(0);
        selection.setHovered();
    }

    public static void openSettings() {
        System.out.println("settings");
    }

    public static void restart() {
        gameHandler.reset();
        resume();
        System.out.println("restart");
    }

    public static void quit() {
        Platform.exit();
        System.exit(0);
    }

    public Mode getMode() {
        return mode;
    }
}

