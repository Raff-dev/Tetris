package Display;

import javafx.application.Platform;

import java.util.function.BooleanSupplier;

public class RepetitiveTask implements Task {
    private Task task;
    private double seconds;
    private long timer = 0;
    private boolean active = true;
    private BooleanSupplier stopCondition = () -> false;

    RepetitiveTask(boolean active, double seconds, Task task) {
        this.active = active;
        this.task = task;
        this.seconds = seconds;
    }

    RepetitiveTask(boolean active, double seconds) {
        this.active = active;
        this.seconds=seconds;
    }

        RepetitiveTask(double seconds, Task task) {
        this.task = task;
        this.seconds = seconds;
    }

    @Override
    public void execute() {
        if (timer == 0) timer = System.currentTimeMillis();
        if (System.currentTimeMillis() - timer > seconds * 1000) {
            if (stopCondition.getAsBoolean()) active = false;
            if (active) Platform.runLater(() -> task.execute());
            timer += 1000 * seconds;
        }
    }

    void setTask(Task task){
        this.task = task;
    }
    void setStopCondition(BooleanSupplier bs) {
        this.stopCondition = bs;
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

