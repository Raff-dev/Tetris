package Display;

import javafx.application.Platform;

public class RepetitiveTask implements Task {
    private Task task;
    private double seconds;
    private long timer = 0;
    private boolean active = true;

    RepetitiveTask(boolean active, double seconds, Task task) {
        this.active = active;
        this.task = task;
        this.seconds = seconds;
    }

    RepetitiveTask(double seconds, Task task) {
        this.task = task;
        this.seconds = seconds;
    }

    @Override
    public void execute() {
        if (timer == 0) timer = System.currentTimeMillis();
        if (System.currentTimeMillis() - timer > seconds * 1000) {
            Platform.runLater(() -> {
                if (active) task.execute();
            });
            timer += 1000 * seconds;
        }
    }

    void Stop() {
        this.active = false;
    }

    void Start() {
        this.active = true;
    }

    Task getTask() {
        return task;
    }

    void setSeconds(double seconds) {
        this.seconds = seconds;
    }

    double getSeconds() {
        return seconds;
    }

    static double perSecond(double count) {
        return (double) 1 / count;
    }
}

