package com.tetris.main;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Game implements Runnable {

    private static float RATIO = 14f / 9;
    static int WIDTH = 10 * Tile.side, HEIGHT = 20 * Tile.side;
    static Rectangle gameArea = new Rectangle(0, 0, WIDTH, HEIGHT);
    static BorderPane window;
    static Handler handler;

    private Thread thread;
    public boolean running = false;
    private static double clockU = 3.0, clockF = 60.0;

    Game(BorderPane window, Scene scene) {
        Game.window = window;
        Game.handler = new Handler();
        drawMesh();
        handler.init();
        new KeyInput(scene, handler);
    }

    synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double ns = Math.pow(10, -9);
        double deltaU = 0, deltaF = 0;
        int ticks = 0, frames = 0;
        double timer = System.currentTimeMillis();

        while (running) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - lastTime) * ns * clockU;
            deltaF += (currentTime - lastTime) * ns * clockF;
            lastTime = currentTime;
            if (deltaU >= 1) {
                tick();
                ticks++;
                deltaU--;
            }
            if (deltaF >= 1) {
                render();
                frames++;
                deltaF--;
            }
            if (System.currentTimeMillis() - timer > 1000) {
                //System.out.println("UPS: " + ticks + " FPS: " + frames);
                timer += 1000;
                ticks = 0;
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                handler.tick();
            }
        });
    }

    private void render() {

    }

    private void drawMesh() {
        for (int y = Tile.side; y < HEIGHT; y += Tile.side) {
            Line line = new Line(0, y, WIDTH, y);
            line.setFill(Color.GREEN);
            line.setStroke(Color.GREEN);
            window.getChildren().add(new Line(0, y, WIDTH, y));
        }
        for (int x = Tile.side; x <= WIDTH; x += Tile.side) {
            window.getChildren().add(new Line(x, 0, x, HEIGHT));
        }
    }

    public BorderPane getWindow() {
        return window;
    }
}
