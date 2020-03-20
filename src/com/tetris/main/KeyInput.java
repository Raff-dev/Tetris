package com.tetris.main;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

public class KeyInput {
    private Handler handler;
    private Scene scene;
    private Boolean movex =false, movey=false;

    public KeyInput(Scene scene, Handler handler) {
        this.scene = scene;
        this.handler = handler;

        scene.addEventFilter(KeyEvent.KEY_PRESSED,event -> {
            switch (event.getCode()) {
                case UP:    handler.getActiveBlock().rotate(); break;
                case DOWN:  handler.getActiveBlock().moveY(); break;
                case LEFT:  handler.getActiveBlock().moveX(-1); break;
                case RIGHT: handler.getActiveBlock().moveX(1); break;
            }
        });
    }
}
