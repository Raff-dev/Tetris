package Display;

import javafx.application.Platform;

public class RepetitiveTask implements Task {
    private Task task;
    private double seconds;
    private long timer = 0;

    RepetitiveTask(double seconds, Task task) {
        this.task = task;
        this.seconds = seconds;
    }

    @Override
    public void execute() {
        if (timer == 0) timer = System.currentTimeMillis();
        if (System.currentTimeMillis() - timer > seconds * 1000) {
            timer += 1000 * seconds;
            Platform.runLater(() -> task.execute());
        }
    }
     static double perSecond(double count){
        return (double)1/count;
    }
}

