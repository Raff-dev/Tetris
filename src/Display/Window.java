package Display;

import DLC.DLC;
import Mechanics.Game;
import Mechanics.GameHandler;
import Mechanics.InputHandler;
import Mechanics.Tile;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Window class is responsible for initialization of most
 * of the program's components and the graphical display itself.
 * @author Rafal Lazicki
 */
public class Window extends Application {
    static int WIDTH = 15 * Tile.side, HEIGHT = 20 * Tile.side;
    public static final Colors colors = new Colors();
    public static final Game game = new Game();
    public static final SideBar sideBar = new SideBar();
    public static final GameMenu gameMenu = new GameMenu();
    private static final Pane window = new Pane(game, sideBar, gameMenu);
    public static final Scene scene = new Scene(window, WIDTH, HEIGHT);
    public static final InputHandler inputHandler = new InputHandler();
    public static final GameHandler gameHandler = new GameHandler();
    public static final SoundHandler soundHandler = new SoundHandler();

    /**
     * Start function initializes the graphical interface with specified settings
     * and starts the menu and game's thread .
     * @param stage JavaFX application object required for the display of the program.
     */
    @Override
    public void start(final Stage stage) {
        stage.setScene(scene);
        stage.setTitle(("Tetris"));
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        stage.show();
        gameMenu.init();
        new Thread(game).start();
        new DLC();
    }

/**
 * Main function takes care of launching the JavaFX application
 * @param args any arguments passed to the program execution.
*/
 public static void main(final String[] args) {
        launch(args);
    }
}
