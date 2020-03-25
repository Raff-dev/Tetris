package Mechanics;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;
import static Display.GameMenu.Mode.RUNNING;
import static Display.Window.*;
import Display.Task;

import java.util.*;

public class InputHandler {
    private Map<Object, Task> gameBindings = new HashMap<>();
    private Map<Object, Task> menuBindings = new HashMap<>();

    public InputHandler() {
        assignBindings();
        listen();
    }

    private void assignBindings() {
        gameBindings.put(ESCAPE, gameMenu::toggleMenu);
        gameBindings.put(SPACE, () -> gameHandler.fall());
        gameBindings.put(ENTER, () -> gameHandler.fall());
        gameBindings.put(UP, () -> gameHandler.rotate());
        gameBindings.put(DOWN, () -> gameHandler.move(0));
        gameBindings.put(LEFT, () -> gameHandler.move(-1));
        gameBindings.put(RIGHT, () -> gameHandler.move(1));

        menuBindings.put(ESCAPE, gameMenu::toggleMenu);
        menuBindings.put(SPACE, gameMenu::toggleMenu);
        menuBindings.put(ENTER, () -> gameMenu.select());
        menuBindings.put(UP, () -> gameMenu.switchSelection(-1));
        menuBindings.put(DOWN, () -> gameMenu.switchSelection(1));
        menuBindings.put(LEFT, () -> gameMenu.leftArrow());
        menuBindings.put(RIGHT, () -> gameMenu.rightArrow());

    }

    private void listen() {
        scene.addEventFilter(KEY_PRESSED, event -> {
            if (gameMenu.getMode() == RUNNING && gameBindings.containsKey(event.getCode()))
                gameBindings.get(event.getCode()).execute();
            else if (menuBindings.containsKey(event.getCode()))
                menuBindings.get(event.getCode()).execute();
        });
    }
}
