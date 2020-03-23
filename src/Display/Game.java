package Display;

import Mechanics.GameHandler;
import Mechanics.Tile;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

import java.util.Hashtable;

import static Display.RepetitiveTask.perSecond;
import static Display.Window.*;

public class Game implements Runnable {
    public static int WIDTH = 10 * Tile.side, HEIGHT = 20 * Tile.side;
    private Hashtable<String, RepetitiveTask> gameTasks = new Hashtable<>();
    private Hashtable<String, RepetitiveTask> menuTasks = new Hashtable<>();

    void init() {
        drawMesh();
        sideBar.init();
        gameHandler.init();
        addTask("updateGameHandler", perSecond(3.0), gameHandler::update, true);
    }

    @Override
    public void run() {
        while (true) {
            gameTasks.forEach((n, t) -> t.execute());
            menuTasks.forEach((n, t) -> t.execute());
        }
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

    void pause() {
        gameTasks.forEach((n, t) -> t.Stop());
        menuTasks.forEach((n, t) -> t.Start());
    }

    void resume() {
        menuTasks.forEach((n, t) -> t.Stop());
        gameTasks.forEach((n, t) -> t.Start());
    }

    public void addTask(String name, RepetitiveTask rt, boolean game) {
        if (game) gameTasks.put(name, rt);
        else menuTasks.put(name, rt);
    }

    public void addTask(String name, double seconds, Task task, boolean game) {
        RepetitiveTask rt = new RepetitiveTask(seconds, task);
        if (game) gameTasks.put(name, rt);
        else menuTasks.put(name, rt);
    }

    public void increaseLevel(int level) {
        RepetitiveTask rt = gameTasks.get("updateGameHandler");
        rt.setSeconds(perSecond(2 + level));
        gameHandler.setLevel(level);
    }
}
