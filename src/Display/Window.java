package Display;

import Mechanics.GameHandler;
import Mechanics.InputHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Window extends Application {
    public static int WIDTH =700, HEIGHT = 800;
    public static final Pane window = new Pane();
    public static final Game game = new Game();
    public static final GameMenu gameMenu = new GameMenu();
    public static final GameHandler gameHandler = new GameHandler();

    @Override
    public void start(final Stage stage) throws Exception {
        try {
            final Scene scene = new Scene(window, WIDTH, HEIGHT);
            scene.getStylesheets().add("Display/style.css");
            new InputHandler(scene,gameHandler);

            stage.setScene(scene);
            stage.setTitle(("Tetris"));
            stage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });
            stage.show();
            gameHandler.init();
            game.start();
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(final String[] args) {
        launch(args);
    }
}
