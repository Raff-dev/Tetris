package com.tetris.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;


public class Window extends Application {

    @Override
    public void start(final Stage stage) throws Exception {
        try {
            final BorderPane window = new BorderPane();
            final Scene scene = new Scene(window, 700, 800);
            scene.getStylesheets().add("com/tetris/main/style.css");

            final Game game = new Game(window, scene);

            stage.setScene(scene);
            stage.setTitle(("Tetris"));
            stage.setOnCloseRequest(e -> {
                game.running=false;
                Platform.exit();
                System.exit(0);
            });
            stage.show();

            game.start();
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(final String[] args) {
        launch(args);
    }

    private void asd() {
        final ToggleButton toggle = new ToggleButton("Toggle color");
        final HBox hb = new HBox(5, toggle);
        hb.setAlignment(Pos.CENTER);
        final Rectangle rect = new Rectangle(200f, 100f, 200f, 200f);
        rect.setFill(Color.RED);
        rect.setY(0);
        //window.getChildren().add(rect);
        rect.setId("chuj");
        rect.getStyleClass().add("chuj");
        hb.getStyleClass().add("hbox");
        hb.setId("hbox-custom");
    }
}
