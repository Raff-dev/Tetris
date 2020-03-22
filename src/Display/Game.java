package Display;

import Mechanics.Tile;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static Display.RepetitiveTask.perSecond;

public class Game implements Runnable {
    private double clockU = 3.0;
    public static int WIDTH = 10 * Tile.side, HEIGHT = 20 * Tile.side;
    private ArrayList<RepetitiveTask> tasks = new ArrayList<>();
    private boolean running = false;
    private Thread thread;
    private Task updateGameHandler;

    public static final Pane window = Window.window;

    Game() {
        drawMesh();
    }

    @Override
    public void run() {
        updateGameHandler = Window.gameHandler::update;
        addTask(perSecond(clockU), updateGameHandler);
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

    void pause() {
        tasks.stream().filter(t -> t.getTask() == updateGameHandler)
                .forEach(RepetitiveTask::Pause);
    }

    void resume() {
        tasks.stream().filter(t -> t.getTask() == updateGameHandler)
                .forEach(RepetitiveTask::Resume);
    }

    private void addTask(double seconds, Task task) {
        tasks.add(new RepetitiveTask(seconds, task));
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

    void difficulty(int level) {
        tasks.stream().filter(t -> t.getTask() == updateGameHandler)
                .forEach(t -> t.setSeconds(perSecond(clockU + level * 1.5)));
        System.out.println("Diff: " + (clockU + level * 1.5));
    }
}
