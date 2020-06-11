package Mechanics;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

import java.util.Hashtable;

import static Mechanics.RepetitiveTask.perSecond;
import static Display.Window.*;

/**
 * The main Game class that is responsible for running any behaviour
 * associated with with the game.
 * @author Rafal Lazicki
 */
public class Game extends Pane implements Runnable {
    public static int WIDTH = 10 * Tile.side;
    static int HEIGHT = 20 * Tile.side;
    private Hashtable<String, RepetitiveTask> gameTasks = new Hashtable<>();
    private Hashtable<String, RepetitiveTask> menuTasks = new Hashtable<>();
    private boolean initialized = false;

    /**
     * Draws mesh, and initializes the game mechanics.
     */
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

    /**
     * Draws visual separation of places that single blocks can occupy.
     */
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

    /**
     * Adds a task that should be executed repetively
     * @param name identifier of a given task
     * @param seconds time between every repetition
     * @param task block of code to be executed
     * @param game specifies wether the tash should
     *             be executed while game is started\running or not
     */
    public void addTask(String name, double seconds, Task task, boolean game) {
        RepetitiveTask rt = new RepetitiveTask(seconds, task);
        if (game) gameTasks.put(name, rt);
        else menuTasks.put(name, rt);
    }

    /**
     * Removes the task from repetitive execution.
     * @param name identifier of a given task
     * @param game specifies which array to remove the task from
     */
    public void removeTask(String name, boolean game) {
        if (game && gameTasks.containsKey(name)) gameTasks.remove(name);
        else menuTasks.remove(name);
    }

    /**
     * Specifies how fast the game should be running.
     * @param level game difficulty level
     */
    void setLevel(int level) {
        RepetitiveTask rt = gameTasks.get("updateGameHandler");
        rt.setSeconds(perSecond(2 + 1.8 * Math.sqrt(level)));
    }

    public boolean isInitialized() {
        return initialized;
    }
}
