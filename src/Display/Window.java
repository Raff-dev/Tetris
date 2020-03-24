package Display;

import Mechanics.GameHandler;
import Mechanics.InputHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Window extends Application {
    static int WIDTH = 700, HEIGHT = 800;
    public static final Game game = new Game();
    public static final SideBar sideBar = new SideBar();
    public static final GameMenu gameMenu = new GameMenu();
    public static final Pane window = new Pane(game, sideBar, gameMenu);
    public static final Scene scene = new Scene(window, WIDTH, HEIGHT);
    public static final InputHandler inputHandler = new InputHandler();
    public static final GameHandler gameHandler = new GameHandler();
    public static final SoundHandler soundHandler = new SoundHandler();

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
    }



    public static void main(final String[] args) {
        launch(args);
    }
}
