package Mechanics;

import Mechanics.Task;
import javafx.application.Platform;

import java.util.function.BooleanSupplier;

/**
 * This class is used to execute specified blocks of code
 * in a repetitive manner, within given time intervals.
 */
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
        this.seconds = seconds;
    }

    /**
     *
     * @param seconds time between every repetition of task execution
     * @param task block of code to be executed
     */
    RepetitiveTask(double seconds, Task task) {
        this.task = task;
        this.seconds = seconds;
    }

    /**
     * Repetitive execution of the given task.
     */
    @Override
    public void execute() {
        if (timer == 0) timer = System.currentTimeMillis();
        if (System.currentTimeMillis() - timer > seconds * 1000) {
            if (stopCondition.getAsBoolean()) active = false;
            if (active) Platform.runLater(() -> task.execute());
            timer += 1000 * seconds;
        }
    }

    void setTask(Task task) {
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

    /**
     * @param count count of times to execute the task within one second
     * @return frequency of task repetition.
     */
    static double perSecond(double count) {
        return (double) 1 / count;
    }
}

