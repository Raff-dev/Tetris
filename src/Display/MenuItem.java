package Display;

import Mechanics.Task;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static Display.Window.WIDTH;
import static javafx.scene.paint.Color.*;

public class MenuItem extends StackPane implements Task {
    GameMenu.ButtonName name;
    private Task task;
    private Text text = new Text();
    private Rectangle bg = new Rectangle();

    public MenuItem(GameMenu.ButtonName name, Task task) {
        this.name = name;
        this.task = task;
        text.setText(String.valueOf(name).replace("_", " "));
        setDefault();
        setTranslateX(WIDTH);
        getChildren().addAll(bg, text);
    }

    void setDefault() {
        FadeTransition ft = new FadeTransition(new Duration(50), this);
        ScaleTransition st = new ScaleTransition(new Duration(50), this);
        ft.setToValue(0.8);
        ft.play();
        st.setToX(1);
        st.setToY(1);
        st.play();
        bg.setWidth(WIDTH * 0.7);
        bg.setHeight(50);
        bg.setFill(CADETBLUE);
        text.setFont(Font.font(30));
        text.setFill(WHITE);
    }

    void setHovered() {
        FadeTransition ft = new FadeTransition(new Duration(100), this);
        ScaleTransition st = new ScaleTransition(new Duration(100), this);
        ft.setToValue(1);
        st.setToX(1.2);
        st.setToY(1.2);
        st.play();
        ft.play();
    }

    void setText(String text) {
        this.text.setText(text);
    }

    @Override
    public void execute() {
        task.execute();
    }
}
