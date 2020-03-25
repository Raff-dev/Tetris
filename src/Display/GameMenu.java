package Display;

import Bindings.ButtonBindings;
import Mechanics.Block;
import Mechanics.Tile;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
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
import static Display.SoundHandler.Sound.*;
import static Display.Window.*;
import static javafx.scene.paint.Color.*;

public class GameMenu extends StackPane {

    private static Mode mode = START;
    private static int volume;

    private static Rectangle bg = new Rectangle(Window.WIDTH, Window.HEIGHT);
    private static VBox primaryItems = new VBox();
    private static VBox secondaryItems = new VBox();
    private static Pane floaties = new Pane();
    private static MenuItem selection;
    private static MenuItem title = new MenuItem("TETRIS");

    public static List<MenuItem> buttons = new ArrayList<>();
    private List<MenuItem> activeButtons = new ArrayList<>();
    private List<MenuItem> primaryButtons = new ArrayList<>();
    private List<MenuItem> secondaryButtons = new ArrayList<>();

    public enum Mode {START, PAUSE, RUNNING}

    public enum ButtonName {Play, Settings, Volume, Size, Color_palette, Change_level, Easy, Medium, Hard, Restart, Resume, Quit}

    GameMenu() {
        setProperties();
        ButtonBindings.bind();
        getChildren().addAll(bg, floaties, primaryItems, secondaryItems, title);
        game.addTask("Floaties", 1, () -> makeFloaties(), false);
    }

    void init() {
        selection = buttons.get(0);
        selection.setHovered();
        primaryButtons.addAll(getButtons(Play, Settings, Quit));
        primaryItems.getChildren().addAll(primaryButtons);
        setActiveButtons(primaryButtons);
        moveButtons(0, primaryButtons, () -> {

        });
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

        public void setText(String text) {
            this.text.setText(text);
        }

        @Override
        public void execute() {
            task.execute();
        }
    }

    private void setActiveButtons(List<MenuItem> newButtons) {
        activeButtons.forEach(b -> b.setDefault());
        activeButtons.clear();
        activeButtons.addAll(newButtons);
        selection = activeButtons.get(0);
        selection.setHovered();
    }

    public void extendWith(List<MenuItem> newButtons) {
        secondaryItems.getChildren().clear();
        secondaryButtons.addAll(newButtons);
        secondaryItems.getChildren().addAll(secondaryButtons);
        moveButtons(-WIDTH, primaryButtons, () -> {

        });
        moveButtons(0, secondaryButtons, () -> {

        });
        setActiveButtons(secondaryButtons);
    }

    public void closeExtension() {
        if (secondaryButtons.size() == 0) return;
        moveButtons(0, primaryButtons, () -> {
            setActiveButtons(primaryButtons);
        });
        moveButtons(WIDTH, secondaryButtons, () -> {
            secondaryButtons.forEach(b -> b.setDefault());
            secondaryButtons.clear();
        });
    }

    public void switchSelection(int dir) {
        soundHandler.playSound(buttonHover);
        selection.setDefault();
        Collections.rotate(activeButtons, -dir);
        selection = activeButtons.get(0);
        selection.setHovered();
    }

    public void select() {
        buttons.stream().filter(b -> b == selection).findFirst().get().execute();
        soundHandler.playSound(buttonSelect);
    }

    public void rightArrow() {
        soundHandler.playSound(denied);
    }

    public void leftArrow() {
        if (secondaryButtons.size() > 0) closeExtension();
        else soundHandler.playSound(denied);
    }

    public void toggleMenu() {
        System.out.println(mode);
        if (mode == START) closeExtension();
        else if (mode == PAUSE) resume();
        else if (mode == RUNNING) pause();
    }

    public void startGame(int level) {
        game.init();
        game.setLevel(level);
        mode = RUNNING;
        FadeTransition ft = new FadeTransition(new Duration(400), gameMenu);
        ft.setToValue(0);
        ft.setInterpolator(Interpolator.EASE_IN);
        ft.setOnFinished((event) -> {
            primaryItems.getChildren().clear();
            primaryButtons.clear();
            primaryButtons.addAll(getButtons(Resume, Settings, Change_level, Restart, Quit));
            primaryItems.getChildren().addAll(primaryButtons);
            closeExtension();
        });
        ft.play();
    }

    public void changeVolume() {
        int volume = soundHandler.getVolume();
        volume = (volume + 10) % 110;
        getButton(Volume).setText("Volume: " + volume + "%");
        soundHandler.setVolume(volume);
    }

    public void changeColorPalette() {
        soundHandler.playSound(denied);
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

    public void openSettings() {
        extendWith(getButtons(Volume, Size, Color_palette));
    }

    public static void restart() {
        gameHandler.init();
        resume();
        System.out.println("restart");
    }

    public static void quit() {
        Platform.exit();
        System.exit(0);
    }

    private void makeFloaties() {
        int safeBuffer = 4 * Tile.side;
        int maxDist = Math.max(WIDTH, HEIGHT) + safeBuffer;
        int fromX, toX, fromY, toY;
        Random random = new Random();
        double timeMs = random.nextInt(4000) + 5000;
        double rotation = random.nextInt(500) + 360;

        fromX = random.nextInt(maxDist);
        toX = random.nextInt(maxDist);
        fromY = random.nextInt(2) * (maxDist + safeBuffer) - safeBuffer;
        toY = Math.abs(fromY - maxDist) - safeBuffer;

        Block.BlockType bt = Block.BlockType.atRandom();
        Color c = Block.colorAtRandom();
        Block floatie = new Block(bt.width() / 2, bt.height() / 2, bt, c);
        Pane p = new BorderPane();
        floatie.showOn(p);
        floaties.getChildren().add(p);
        TranslateTransition tr = new TranslateTransition(new Duration(timeMs), p);
        RotateTransition rt = new RotateTransition(new Duration(timeMs), p);
        rt.setToAngle(rotation);
        rt.play();

        if (random.nextBoolean()) {
            tr.setFromX(fromX);
            tr.setFromY(fromY);
            tr.setToX(toX);
            tr.setToY(toY);
        } else {
            tr.setFromX(fromY);
            tr.setFromY(fromX);
            tr.setToX(toY);
            tr.setToY(toX);
        }
        tr.setInterpolator(Interpolator.LINEAR);
        tr.setOnFinished((event) -> floaties.getChildren().remove(p));
        tr.play();
    }


    private void moveButtons(int to, List<MenuItem> items, Task task) {
        for (int i = 0; i < items.size(); i++) {
            TranslateTransition tr = new TranslateTransition();
            tr.setInterpolator(Interpolator.EASE_OUT);
            tr.setDuration(new Duration(500));
            tr.setToX(to);
            tr.setNode(items.get(i));
            tr.setOnFinished((e) -> task.execute());
            tr.setDelay(new Duration(100 + i * 100));
            tr.play();
        }
    }

    public List<MenuItem> getButtons(ButtonName... names) {
        Stream<MenuItem> buttonsStream = buttons.stream().filter(
                b -> Arrays.asList(names).contains(b.name));
        return buttonsStream.collect(Collectors.toList());
    }

    public MenuItem getButton(ButtonName name) {
        return buttons.stream().filter(b -> b.name == name).findFirst().get();
    }

    public Mode getMode() {
        return mode;
    }
}

