package Display;

import Bindings.ButtonBindings;
import Mechanics.Block;
import Mechanics.Tile;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Display.GameMenu.ButtonName.*;
import static Display.GameMenu.Mode.*;
import static Display.RepetitiveTask.perSecond;
import static Display.SoundHandler.Sound.buttonHover;
import static Display.SoundHandler.Sound.buttonSelect;
import static Display.Window.*;
import static javafx.scene.paint.Color.*;

public class GameMenu extends StackPane {
    public enum Mode {START, PAUSE, RUNNING}

    private static Mode mode = START;
    private static int volume;

    private static Rectangle bg = new Rectangle(Window.WIDTH, Window.HEIGHT);
    private static VBox primaryItems = new VBox();
    private static VBox secondaryItems = new VBox();
    private static MenuItem selection;
    private static MenuItem title = new MenuItem("TETRIS");

    public static List<MenuItem> buttons = new ArrayList<>();
    private List<MenuItem> activeButtons = new ArrayList<>();
    private List<MenuItem> primaryButtons = new ArrayList<>();
    private List<MenuItem> secondaryButtons = new ArrayList<>();

    public enum ButtonName {Play, Settings, Easy, Medium, Hard, Restart, Resume, Quit}

    GameMenu() {
        setProperties();
        ButtonBindings.bind();
        getChildren().addAll(bg, primaryItems, secondaryItems, title);
        for (int i = 0; i < 10; i++) makeFloaties();

    }

    void init() {
        selection = buttons.get(0);
        selection.setHovered();
        primaryButtons.addAll(getButtons(Play, Settings, Quit));
        primaryItems.getChildren().addAll(primaryButtons);
        moveButtons(0, primaryButtons);
        setActiveButtons(primaryButtons);
    }

    private void setProperties() {
        this.setPrefSize(WIDTH, HEIGHT);
        bg.setFill(WHITE);
        primaryItems.setSpacing(10);
        secondaryItems.setSpacing(10);
        primaryItems.setAlignment(Pos.CENTER);
        secondaryItems.setAlignment(Pos.CENTER);
    }

    public static class MenuItem extends StackPane implements Task {
        ButtonName name;
        Task task;
        Text text = new Text();
        Rectangle bg = new Rectangle();

        MenuItem(String name) {
            text.setText(name);
            text.setFill(BLACK);
            getChildren().addAll(text);
        }

        public MenuItem(ButtonName name, Task task) {
            this.name = name;
            this.task = task;
            text.setText(String.valueOf(name));
            setDefault();
            setTranslateX(WIDTH);
            getChildren().addAll(bg, text);
        }

        void setDefault() {
            setTranslateX(0);
            setOpacity(0.8);
            setScaleX(1);
            setScaleY(1);
            bg.setWidth(WIDTH * 0.7);
            bg.setHeight(50);
            bg.setFill(CADETBLUE);
            text.setFont(Font.font(30));
            text.setFill(WHITE);
        }

        void setHovered() {
            setOpacity(1);
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

    public void extendWith(List<MenuItem> newButtons) {
        secondaryItems.getChildren().clear();
        secondaryButtons.addAll(newButtons);
        secondaryItems.getChildren().addAll(secondaryButtons);
        moveButtons(-WIDTH, primaryButtons);
        moveButtons(0, secondaryButtons);
        setActiveButtons(secondaryButtons);
    }

    private void moveButtons(int to, List<MenuItem> items) {
        for (int i = 0; i < items.size(); i++) {
            TranslateTransition tr = new TranslateTransition();
            tr.setInterpolator(Interpolator.EASE_OUT);
            tr.setDuration(new Duration(500));
            tr.setToX(to);
            tr.setNode(items.get(i));
            tr.setDelay(new Duration(100 + i * 100));
            tr.play();
        }
    }

    private void makeFloaties() {
        int maxDist = Math.max(WIDTH, HEIGHT) + 4 * Tile.side;
        int fromX, toX, fromY, toY;
        double velX, velY;
        Random random = new Random();
        int rotation = random.nextInt(5)+5;

        fromX = random.nextInt(maxDist);
        toX = random.nextInt(maxDist);
        fromY = random.nextInt(1) * maxDist;
        toY = Math.abs(fromY - maxDist);
        if (random.nextBoolean()) {
            System.out.println("---------------------------------");
            System.out.println("swaaperoo");
            int temp = fromX;
            fromX = fromY;
            fromY = temp;
            temp = toX;
            toX = toY;
            toY = temp;
        }
        velY = random.nextInt(10);
        velX = velY * (fromX - toX) / (fromY - toY);
        System.out.println("vel:" + velX + " " + velY + " a:" + (velY / velX));
        System.out.println("From:" + fromX + "," + fromY + " To:" + toX + " " + toY);

        Block.BlockType bt = Block.BlockType.atRandom();
        Color c = Block.colorAtRandom();
        Block floatie = new Block(fromX, fromY, bt, c);
        Pane p = new Pane();
        floatie.showOn(p);
        getChildren().add(p);
        RepetitiveTask rt = new RepetitiveTask(true, perSecond(60),
                () -> {
                    p.setRotate(p.getRotate() + rotation);
                    p.setTranslateX(p.getTranslateX() + velX);
                    p.setTranslateY(p.getTranslateY() + velY);
                });
        game.addTask(String.valueOf(toX),rt,false);

    }

    private static void swap(int first, int second) {
        int temp = first;
        first = second;
        second = temp;
    }

    public void closeExtension() {
        if (secondaryButtons.size() == 0) return;
        moveButtons(0, primaryButtons);
        moveButtons(WIDTH, secondaryButtons);
        //primaryButtons.forEach(b -> b.setDefault());
        secondaryButtons.clear();
        setActiveButtons(primaryButtons);
    }

    public void setDifficulty(int level) {
        game.init();
        game.increaseLevel(level);
        mode = RUNNING;
        FadeTransition ft = new FadeTransition(new Duration(400), gameMenu);
        ft.setToValue(0);
        ft.setInterpolator(Interpolator.EASE_IN);
        ft.setOnFinished((event) -> {
            primaryItems.getChildren().clear();
            primaryButtons.clear();
            primaryButtons.addAll(getButtons(Resume, Settings, Restart, Quit));
            primaryItems.getChildren().addAll(primaryButtons);
            closeExtension();
        });
        ft.play();
    }

    private static void pause() {
        mode = PAUSE;
        bg.setOpacity(0.3);
        bg.setFill(BLACK);
        FadeTransition ft = new FadeTransition(new Duration(200), gameMenu);
        ft.setToValue(1);
        ft.play();
        game.pause();
    }

    public static void resume() {
        mode = RUNNING;
        FadeTransition ft = new FadeTransition(new Duration(200), gameMenu);
        ft.setToValue(0);
        ft.play();
        game.resume();
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

    public void select() {
        soundHandler.playSound(buttonSelect);
        buttons.stream().filter(b -> b == selection).findFirst().get().execute();
    }

    public void switchSelection(int dir) {
        soundHandler.playSound(buttonHover);
        selection.setDefault();
        Collections.rotate(activeButtons, -dir);
        selection = activeButtons.get(0);
        selection.setHovered();
    }

    private void setActiveButtons(List<MenuItem> newButtons) {
        activeButtons.clear();
        activeButtons.addAll(newButtons);
        selection = activeButtons.get(0);
        selection.setHovered();
    }

    public List<MenuItem> getButtons(ButtonName... names) {
        Stream<MenuItem> buttonsStream = buttons.stream().filter(
                b -> Arrays.asList(names).contains(b.name));
        return buttonsStream.collect(Collectors.toList());
    }

    public Mode getMode() {
        return mode;
    }
}

