package Display;

import Bindings.ButtonBindings;
import Mechanics.Block;
import Mechanics.Task;
import Mechanics.Tile;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
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

/**
 * Main menu class, which is responsible for behaviour of the program
 * regarding to user interaction while the game is not started or is paused.
 * Menu allows user to switch selection between MenuItems,
 * which are an integral part of it.
 * @author Rafal Lazicki
 */
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
    private ObservableList<String> colorChoices = FXCollections.observableList(colors.getPalette());

    public enum Mode {START, PAUSE, RUNNING}

    public enum ButtonName {Play, Settings, Volume, Size, Color_palette, Change_level, Easy, Medium, Hard, Restart, Resume, Quit}


    /**
     * Game menu constructor takes care of events that need to be executed just at the start of the program.
     * In this block of code, every button shall be assigned a specified function.
     * Moreover, background animations are being started.
     *
     */
    GameMenu() {
        setProperties();
        ButtonBindings.bind();
        getChildren().addAll(bg, floatiesContainer, primaryItems, secondaryItems);
        game.addTask("Floaties", 1, () -> goFloaty(makeFloatie(), floatiesContainer), false);
    }

    /**
     * Init function organises the buttons that should be displayed.
     * Moves primary buttons to the front of the screen and enables
     * switching selection between the set of chosen ones.
     */
    void init() {
        selection = buttons.get(0);
        selection.setHovered();
        primaryButtons.addAll(getButtons(Play, Settings, Quit));
        primaryItems.getChildren().addAll(primaryButtons);
        setActiveButtons(primaryButtons);
        moveButtons(0, primaryButtons, null);
    }

    /**
     * Sets basic visual appearance settings of the items that the menu contains.
     */
    private void setProperties() {
        this.setPrefSize(WIDTH, HEIGHT);
        bg.setFill(WHITE);
        primaryItems.setSpacing(10);
        secondaryItems.setSpacing(10);
        primaryItems.setAlignment(Pos.CENTER);
        secondaryItems.setAlignment(Pos.CENTER);
    }

    /**
     * Sets the set of buttons that user can choose from and interact with.
     * @param newButtons the set of buttons to be set as interactive at the moment.
     */
    private void setActiveButtons(List<MenuItem> newButtons) {
        activeButtons.forEach(b -> b.setDefault());
        activeButtons.clear();
        activeButtons.addAll(newButtons);
        selection = activeButtons.get(0);
        selection.setHovered();
    }

    /**
     * Moves additional buttons to the front, and makes them interactive.
     * @param newButtons the set of buttons that extend interacted object
     */
    public void extendWith(List<MenuItem> newButtons) {
        secondaryItems.getChildren().clear();
        secondaryButtons.addAll(newButtons);
        secondaryItems.getChildren().addAll(secondaryButtons);
        moveButtons(0, secondaryButtons, () -> setActiveButtons(secondaryButtons));
        moveButtons(-WIDTH, primaryButtons, null);
    }

    /**
     * Moves the items that previous selection was extended with, but only if they were,
     * out of the screen and allows primary ones to be selectable back again.
     */
    private void closeExtension() {
        if (secondaryButtons.size() == 0) return;
        moveButtons(0, primaryButtons, () -> setActiveButtons(primaryButtons));
        moveButtons(WIDTH, secondaryButtons, () -> {
            secondaryButtons.forEach(b -> b.setDefault());
            secondaryButtons.clear();
        });
    }

    /**
     * Sets the button that should be selected
     * @param dir direction according to which the selection should be changed.
     */
    public void switchSelection(int dir) {
        soundHandler.playSound(buttonHover);
        selection.setDefault();
        Collections.rotate(activeButtons, -dir);
        selection = activeButtons.get(0);
        selection.setHovered();
    }

    /**
     * Executes functions assigned to the currently selected button.
     */
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

    /**
     * Toggles program state, and proceeds to run code depending on the state.
     * This function can either start the game, close or show menu.
     */
    public void toggleMenu() {
        if (mode == START) closeExtension();
        else if (mode == PAUSE) resume();
        else if (mode == RUNNING) pause();
    }

    /**
     * Initialises the game with given difficulty level and displays appropriate animations.
     * @param level difficulty level, at which the game should be started on.
     */
    public void startGame(int level) {
        if (!game.isInitialized()) {
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

    /**
     * Changes the program's sound volume.
     */
    public void changeVolume() {
        int volume = soundHandler.getVolume();
        volume = (volume + 10) % 110;
        getButton(Volume).setText("Volume: " + volume + "%");
        soundHandler.setVolume(volume);
    }

    /**
     * Rotates color palette array and changes color appearance
     * of specified objects, depending on palette's color set.
     */
    public void changeColorPalette() {
        Collections.rotate(colorChoices, -1);
        String paletteName = colorChoices.get(0);
        getButton(Color_palette).setText("Color palette: " + paletteName);
        colors.setActive(paletteName);

        new Thread(() -> Platform.runLater(() -> {
            gameHandler.getOccupied().forEach(b -> b.setColor(colors.getRandom()));
            floaties.forEach(f -> f.setColor(colors.getRandom()));
            if (mode == PAUSE) {
                gameHandler.activeBlock().setColor(colors.getRandom());
                gameHandler.nextBlock().setColor(colors.getRandom());
                sideBar.getNextBlock().setColor(colors.getRandom());
            }
        })).start();
    }

    /**
     * Switches state of the program and displays specified animations.
     */
    private static void pause() {
        mode = PAUSE;
        bg.setOpacity(0.3);
        bg.setFill(BLACK);
        FadeTransition ft = new FadeTransition(new Duration(200), gameMenu);
        ft.setToValue(1);
        ft.play();
        game.pause();
    }
    /**
     * Switches state of the program and displays specified animations.
     */
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

    /**
     * Displays quit animation and closes the program.
     */
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

    /**
     * Creates a 'floatie' object, which intention is to look nice in the background of the menu.
     * @return a floatie
     */
    private Pane makeFloatie() {
        Block.BlockType bt = Block.BlockType.atRandom();
        Block floatie = new Block(bt.width() / 2, bt.height() / 2, bt, colors.getRandom());
        floaties.add(floatie);
        Pane floatieBox = new BorderPane();
        floatie.showOn(floatieBox);
        return floatieBox;
    }

    /**
     * Makes a 'floatie' float (move) on the given canvas and assignes its route,
     * as well as rotation speed and velocity.
     * @param floatieBox a canvas that contains all the 'floates'.
     * @param pane 'floatie' object, which behaviour will be determined.
     */
    public void goFloaty(Pane floatieBox, Pane pane) {
        int safeBuffer = 4 * Tile.side;
        int maxDist = Math.max(WIDTH, HEIGHT) + safeBuffer;
        Random random = new Random();
        int fromX = random.nextInt(maxDist);
        int toX = random.nextInt(maxDist);
        int fromY = random.nextInt(2) * (maxDist + safeBuffer) - safeBuffer;
        int toY = Math.abs(fromY - maxDist) - safeBuffer;
        double timeMs = random.nextInt(4000) + 5000;
        double rotation = random.nextInt(500) + 360;

        TranslateTransition tr = new TranslateTransition(new Duration(timeMs), floatieBox);
        RotateTransition rt = new RotateTransition(new Duration(timeMs), floatieBox);
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
        pane.getChildren().add(floatieBox);
        tr.setInterpolator(Interpolator.LINEAR);
        tr.setOnFinished((event) -> {
            pane.getChildren().remove(floatieBox);
            if (floatieBox.getChildren().get(floatieBox.getChildren().size() - 1).getClass().getName().equals("Mechanics.Tile")) {
                Tile t = (Tile) floatieBox.getChildren().get(1);
                floaties.remove(t.getBlock());
            }
        });
        tr.play();
    }

    /**
     * Animates movement of the given items, and executes the task afterwards.
     * @param to designation point on X axis
     * @param items items that shall be moved
     * @param task task that shall be executed right after the animation finishes
     */
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

    /**
     * Searches for the given MenuItems contained by the menu using their names and returns them.
     * @param names names of the wanted MenuItems
     * @return List of MenuItems, which matched the parameters.
     */
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

