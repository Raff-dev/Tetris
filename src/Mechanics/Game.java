package Mechanics;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

import java.util.Hashtable;

import static Mechanics.RepetitiveTask.perSecond;
import static Display.Window.*;

public class Game extends Pane implements Runnable {
    public static int WIDTH = 10 * Tile.side;
    static int HEIGHT = 20 * Tile.side;
    private Hashtable<String, RepetitiveTask> gameTasks = new Hashtable<>();
    private Hashtable<String, RepetitiveTask> menuTasks = new Hashtable<>();
    private boolean initialized = false;

    public void init() {
        initialized = true;
        drawMesh();
        addTask("updateGameHandler", perSecond(3.0), gameHandler::update, true);
        gameTasks.forEach((n, t) -> t.Start());
    }

    @Override
    public void run() {
        while (true) {
            gameTasks.forEach((n, t) -> t.execute());
            menuTasks.forEach((n, t) -> t.execute());
        }
    }

    public void pause() {
        gameTasks.forEach((n, t) -> t.Stop());
        menuTasks.forEach((n, t) -> t.Start());
    }

    public void resume() {
        menuTasks.forEach((n, t) -> t.Stop());
        gameTasks.forEach((n, t) -> t.Start());
    }

    private void drawMesh() {
        for (int y = Tile.side; y < HEIGHT; y += Tile.side) {
            Line line = new Line(0, y, WIDTH, y);
            line.setStroke(Color.GRAY);
            getChildren().add(line);
        }
        for (int x = Tile.side; x <= WIDTH; x += Tile.side) {
            Line line = new Line(x, 0, x, HEIGHT);
            line.setStroke(Color.GRAY);
            getChildren().add(line);
        }
    }

    public void addTask(String name, double seconds, Task task, boolean game) {
        RepetitiveTask rt = new RepetitiveTask(seconds, task);
        if (game) gameTasks.put(name, rt);
        else menuTasks.put(name, rt);
    }

    public void removeTask(String name, boolean game) {
        if (game && gameTasks.containsKey(name)) gameTasks.remove(name);
        else menuTasks.remove(name);
    }

    void setLevel(int level) {
        RepetitiveTask rt = gameTasks.get("updateGameHandler");
        rt.setSeconds(perSecond(2 + 1.8 * Math.sqrt(level)));
    }

    public boolean isInitialized() {
        return initialized;
    }
}