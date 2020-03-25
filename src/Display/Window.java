package Display;

import Mechanics.GameHandler;
import Mechanics.InputHandler;
import Mechanics.Tile;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

public class Window extends Application {
    static int WIDTH = 15 * Tile.side, HEIGHT = 20 * Tile.side;
    public static final Game game = new Game();
    public static final SideBar sideBar = new SideBar();
    public static final GameMenu gameMenu = new GameMenu();
    private static final Pane window = new Pane(game, sideBar, gameMenu);
    public static final Scene scene = new Scene(window, WIDTH, HEIGHT);
    public static final InputHandler inputHandler = new InputHandler();
    public static final GameHandler gameHandler = new GameHandler();
    public static final SoundHandler soundHandler = new SoundHandler();
    public static final Colors colors = new Colors();

    @Override
    public void start(final Stage stage) {
        window.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        stage.setScene(scene);
        stage.setTitle(("Tetris"));
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        stage.show();
        gameMenu.init();
        new Thread(game).start();
    }

    public static void main(final String[] args) {
        launch(args);
    }
}
