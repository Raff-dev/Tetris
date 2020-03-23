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
    private ArrayList<MenuItem> difficulty = new ArrayList<>();
    private VBox playBox;
    private int selection = 0;

    public enum Mode {
        START, PAUSE, PLAY
    }

    GameMenu() {
        this.setTranslateX(Window.WIDTH * 0.5);
        this.setTranslateY(Window.HEIGHT * 0.5);
        this.setPrefSize(WIDTH, HEIGHT);
        bg.setFill(BLACK);
        bg.setOpacity(0.3);

        String[] optionsText = new String[]{"Play", "Settings", "Restart", "Quit"};
        String[] difficultyText = new String[]{"Easy", "Medium", "Hard"};
        Task[] optionsActions = new Task[]{this::chooseDifficulty, this::openSettings, this::restart, this::quit};

        for (int i = 0; i < optionsText.length; i++) {
            if (i == 1) IntStream.range(0, difficultyText.length).forEach(j ->
                    difficulty.add(new MenuItem(difficultyText[j], () -> setDifficulty(j))));
            buttons.add(new MenuItem(optionsText[i], optionsActions[i]));
        }

        playBox = new VBox(5);
        playBox.getChildren().add(buttons.get(0));
        //playbox.getChildren().addAll(difficulty);
        setSpacing(10);
        buttons.get(0).setHovered();
        getChildren().add(playBox);
        getChildren().addAll(buttons.get(1), buttons.get(2), buttons.get(3));
        setAlignment(Pos.CENTER);

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
        if (mode == Mode.PAUSE) chooseDifficulty();
        else if (mode == Mode.PLAY) pause();
    }

    private void chooseDifficulty() {
        if (playBox.getChildren().size() > 1) playBox.getChildren().removeAll(difficulty);
        else playBox.getChildren().addAll(difficulty);
    }

    private void setDifficulty(int level) {
        game.increaseLevel(level * 4);
        mode = PLAY;
        window.getChildren().removeAll(bg, this);
        game.init();
    }

    private void pause() {
        mode = PAUSE;
        window.getChildren().addAll(bg, this);
        game.pause();
    }

    private void run() {
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
        //add name of action to a button
        //make selection skip
        //rotate name list??
    }

    private void openSettings() {
        System.out.println("settings");
    }

    private void restart() {
        gameHandler.reset();
        run();
        System.out.println("restart");
    }

    private void quit() {
        Platform.exit();
        System.exit(0);
    }

    public Mode getMode() {
        return mode;
    }
}

