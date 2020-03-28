package Display;

import Bindings.ButtonBindings;
import Mechanics.Block;
import Mechanics.Tile;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
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
    private static Rectangle bg = new Rectangle(Window.WIDTH, Window.HEIGHT);
    private static VBox primaryItems = new VBox();
    private static VBox secondaryItems = new VBox();
    private static Pane floatiesContainer = new Pane();
    private static MenuItem selection;

    private ObservableList<Block> floaties = FXCollections.observableArrayList();
    public static List<MenuItem> buttons = new ArrayList<>();
    private List<MenuItem> activeButtons = new ArrayList<>();
    private List<MenuItem> primaryButtons = new ArrayList<>();
    private List<MenuItem> secondaryButtons = new ArrayList<>();
    private ObservableList<String> colorChoices =
            FXCollections.observableList(colors.getPalette());

    public enum Mode {START, PAUSE, RUNNING}

    public enum ButtonName {Play, Settings, Volume, Size, Color_palette, Change_level, Easy, Medium, Hard, Restart, Resume, Quit}

    GameMenu() {
        colorChoices.addAll(colors.getPalette());
        setProperties();
        ButtonBindings.bind();
        getChildren().addAll(bg, floatiesContainer, primaryItems, secondaryItems);
        game.addTask("Floaties", 1, () -> goFloaty(makeFloatie(), floatiesContainer), false);
    }

    void init() {
        selection = buttons.get(0);
        selection.setHovered();
        primaryButtons.addAll(getButtons(Play, Settings, Quit));
        primaryItems.getChildren().addAll(primaryButtons);
        setActiveButtons(primaryButtons);
        moveButtons(0, primaryButtons, null);
    }

    private void setProperties() {
        this.setPrefSize(WIDTH, HEIGHT);
        bg.setFill(WHITE);
        primaryItems.setSpacing(10);
        secondaryItems.setSpacing(10);
        primaryItems.setAlignment(Pos.CENTER);
        secondaryItems.setAlignment(Pos.CENTER);
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
        moveButtons(0, secondaryButtons, () -> setActiveButtons(secondaryButtons));
        moveButtons(-WIDTH, primaryButtons, null);
    }

    private void closeExtension() {
        if (secondaryButtons.size() == 0) return;
        moveButtons(0, primaryButtons, () -> setActiveButtons(primaryButtons));
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
        if (mode == START) closeExtension();
        else if (mode == PAUSE) resume();
        else if (mode == RUNNING) pause();
    }

    public void startGame(int level) {
        if (!game.isRunning()) {
            game.init();
            sideBar.init();
        }
        gameHandler.setStartLevel(level);
        gameHandler.start();
        game.resume();

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
        Collections.rotate(colorChoices, -1);
        String paletteName = colorChoices.get(0);
        getButton(Color_palette).setText("Color palette: " + paletteName);
        colors.setActive(paletteName);

        BlockTask blockTask;
        blockTask = (b) -> b.setColor(colors.getRandom());

        new Thread(() -> Platform.runLater(() -> {
            gameHandler.getOccupied().forEach(b -> blockTask.execute(b));
            floaties.forEach(f -> blockTask.execute(f));
            if (mode == PAUSE) {
                blockTask.execute(gameHandler.activeBlock());
                blockTask.execute(gameHandler.nextBlock());
                blockTask.execute(sideBar.getNextBlock());
            }
        })).start();
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
        gameHandler.start();
        resume();
    }

    public void quit() {
        FillTransition fit = new FillTransition(new Duration(1200), bg);
        FadeTransition fat = new FadeTransition(new Duration(1000), floatiesContainer);
        fat.setToValue(0);
        fat.play();
        fit.setToValue(BLACK);
        fit.setInterpolator(Interpolator.EASE_IN);
        fit.play();
        moveButtons(WIDTH, activeButtons, null);
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                System.exit(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private Node makeFloatie() {
        Block.BlockType bt = Block.BlockType.atRandom();
        Block floatie = new Block(bt.width() / 2, bt.height() / 2, bt, colors.getRandom());
        floaties.add(floatie);
        Pane p = new BorderPane();
        floatie.showOn(p);
        return p;
    }

    public void goFloaty(Node node, Pane pane) {
        int safeBuffer = 4 * Tile.side;
        int maxDist = Math.max(WIDTH, HEIGHT) + safeBuffer;
        Random random = new Random();
        int fromX = random.nextInt(maxDist);
        int toX = random.nextInt(maxDist);
        int fromY = random.nextInt(2) * (maxDist + safeBuffer) - safeBuffer;
        int toY = Math.abs(fromY - maxDist) - safeBuffer;
        double timeMs = random.nextInt(4000) + 5000;
        double rotation = random.nextInt(500) + 360;

        TranslateTransition tr = new TranslateTransition(new Duration(timeMs), node);
        RotateTransition rt = new RotateTransition(new Duration(timeMs), node);
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
        pane.getChildren().add(node);
        tr.setInterpolator(Interpolator.LINEAR);
        tr.setOnFinished((event) -> {
            pane.getChildren().remove(node);
            floaties.remove(node);
        });
        tr.play();
    }

    private void moveButtons(int to, List<MenuItem> items, Task task) {
        for (int i = 0; i < items.size(); i++) {
            TranslateTransition tr = new TranslateTransition();
            tr.setInterpolator(Interpolator.EASE_OUT);
            tr.setDuration(new Duration(500));
            tr.setToX(to);
            tr.setNode(items.get(i));
            tr.setDelay(new Duration(100 + i * 100));
            tr.play();
        }
        if (task != null) task.execute();
    }

    public List<MenuItem> getButtons(ButtonName... names) {
        Stream<MenuItem> buttonsStream = buttons.stream().filter(
                b -> Arrays.asList(names).contains(b.name));
        return buttonsStream.collect(Collectors.toList());
    }

    private MenuItem getButton(ButtonName name) {
        return buttons.stream().filter(b -> b.name == name).findFirst().get();
    }

    public Mode getMode() {
        return mode;
    }

    public ObservableList<Block> getFloaties() {
        return floaties;
    }

    public ObservableList<String> getColorChoices() {
        return colorChoices;
    }
}

