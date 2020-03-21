package Display;

import Mechanics.Handler;
import Mechanics.KeyInput;
import Mechanics.Tile;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import static Display.RepetitiveTask.perSecond;

public class Game implements Runnable {

    public static int WIDTH = 10 * Tile.side, HEIGHT = 20 * Tile.side;
    public static final GameMenu menu = new GameMenu();
    public static final Handler handler = new Handler();
    public static BorderPane window;
    private static Rectangle gameArea = new Rectangle(0, 0, WIDTH, HEIGHT);

    private ArrayList<RepetitiveTask> tasks = new ArrayList<>();

    private Thread thread;
    private boolean running = false;

    Game(BorderPane window, Scene scene) {
        Game.window = window;
        Group g = new Group();
        gameArea.setFill(Color.BLUEVIOLET);
        g.getChildren().add(gameArea);
        window.getChildren().add(g);
        drawMesh();
        handler.init();
        new KeyInput(scene, handler);
    }

    synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    private synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        double clockU = 3.0;
        addTask(perSecond(clockU), handler::update);
        while (running) tasks.forEach(RepetitiveTask::execute);
        stop();
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

    private void addTask(double seconds, Task task) {
        tasks.add(new RepetitiveTask(seconds, task));
    }

    public void removeTask(RepetitiveTask task) {
        tasks.remove(task);
    }
}
