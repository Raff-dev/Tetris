package Display;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.*;
import java.util.stream.IntStream;

import static Display.GameMenu.Mode.*;
import static Display.Window.*;
import static javafx.scene.paint.Color.*;

public class GameMenu extends VBox {
    private static Mode mode = START;
    private static int WIDTH = (int) (Game.WIDTH * 0.5);
    private static int HEIGHT = (int) (Game.HEIGHT * 0.5);
    private Rectangle bg = new Rectangle(0, 0, Window.WIDTH, Window.HEIGHT);
    private ArrayList<MenuItem> buttons = new ArrayList<>();
    private int selection = 0;

    GameMenu() {
        this.setTranslateX(Window.WIDTH * 0.5 - WIDTH * 0.5);
        this.setPrefSize(WIDTH, HEIGHT);
        bg.setFill(BLACK);
        bg.setOpacity(0.3);

        String[] optionsText = new String[]{"Play", "Settings", "Restart", "Quit"};
        String[] difficultyText = new String[]{"Easy", "Medium", "Hard"};
        Task[] optionsActions = new Task[]{this::toggle, this::openSettings, this::restart, this::quit};

        for (int i = 0; i < optionsText.length; i++) {
            if (i == 1) IntStream.range(0, difficultyText.length).forEach(j ->
                    buttons.add(new MenuItem(difficultyText[j], () -> game.difficulty(j))));
            buttons.add(new MenuItem(optionsText[i], optionsActions[i]));
        }

        buttons.get(0).setHovered();
        getChildren().addAll(buttons);
        setSpacing(10);
        window.getChildren().addAll(bg, this);
    }

    private static class MenuItem extends StackPane implements Task {
        Task task;
        Text text = new Text();
        Rectangle bg = new Rectangle();

        MenuItem(String name, Task task) {
            this.task = task;
            text.setText(name);
            setDefault();
            getChildren().addAll(bg, text);
        }

        void setDefault() {
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
        //put start somewhere else
        if (mode == Mode.START) start();
        else if (mode == Mode.PAUSE) play();
        else if (mode == Mode.PLAY) pause();
    }

    private void play() {
        mode = PLAY;
        window.getChildren().removeAll(bg, this);
        game.resume();
    }

    private void pause() {
        mode = PAUSE;
        window.getChildren().addAll(bg, this);
        game.pause();
    }

    private void start() {
        mode = PLAY;
        window.getChildren().removeAll(bg, this);
        game.resume();
    }

    public void select() {
        buttons.get(selection).execute();
    }

    public void switchSelection(int dir) {
        buttons.get(selection).setDefault();
        selection += dir;
        if (selection < 0 || selection == buttons.size())
            selection = Math.max(0, -dir * (buttons.size() - 1));
        buttons.get(selection).setHovered();

    }

    private void openSettings() {
        System.out.println("settings");
    }

    private void restart() {
        gameHandler.reset();
        play();
        System.out.println("restart");
    }

    private void quit() {
        Platform.exit();
        System.exit(0);
    }

    public Mode getMode() {
        return mode;
    }

    public enum Mode {
        START, PAUSE, PLAY
    }

}

