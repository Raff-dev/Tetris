package Mechanics;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;
import static Display.GameMenu.Mode.RUNNING;
import static Display.Window.*;
import static javafx.scene.input.KeyEvent.KEY_RELEASED;

import java.util.*;

/**
 * Handles all the behaviour of user interactions at the basic level with the program.
 * @author Rafal Lazicki
 */
public class InputHandler {
    private Map<Object, Task> gamePressBindings = new HashMap<>();
    private Map<Object, Task> releaseBindings = new HashMap<>();
    private Map<Object, Task> menuBindings = new HashMap<>();

    public InputHandler() {
        assignBindings();
        listen();
    }

    /**
     * Maps all the specified behaviour assigned to certain keyboard buttons.
     */
    private void assignBindings() {
        gamePressBindings.put(ESCAPE, gameMenu::toggleMenu);
        gamePressBindings.put(SPACE, () -> gameHandler.fall());
        gamePressBindings.put(ENTER, () -> gameHandler.fall());
        gamePressBindings.put(UP, () -> gameHandler.rotate());
        gamePressBindings.put(DOWN, () -> gameHandler.move(0));
        gamePressBindings.put(LEFT, () -> gameHandler.move(-1));
        gamePressBindings.put(RIGHT, () -> gameHandler.move(1));

        releaseBindings.put(DOWN,()->gameHandler.unMove(0));
        releaseBindings.put(LEFT,()->gameHandler.unMove(-1));
        releaseBindings.put(RIGHT,()->gameHandler.unMove(1));

        menuBindings.put(ESCAPE, gameMenu::toggleMenu);
        menuBindings.put(SPACE, gameMenu::toggleMenu);
        menuBindings.put(ENTER, () -> gameMenu.select());
        menuBindings.put(UP, () -> gameMenu.switchSelection(-1));
        menuBindings.put(DOWN, () -> gameMenu.switchSelection(1));
        menuBindings.put(LEFT, () -> gameMenu.leftArrow());
        menuBindings.put(RIGHT, () -> gameMenu.rightArrow());
    }

    /**
     * Adds event listeners to the program, witch fire whenever
     * specified actions are taken by the user.
     */
    private void listen() {
        scene.addEventFilter(KEY_PRESSED, event -> {
            if (gameMenu.getMode() == RUNNING && gamePressBindings.containsKey(event.getCode()))
                gamePressBindings.get(event.getCode()).execute();
            else if (menuBindings.containsKey(event.getCode()))
                menuBindings.get(event.getCode()).execute();
        });
        scene.addEventFilter(KEY_RELEASED, event -> {
            if (releaseBindings.containsKey(event.getCode()))
                releaseBindings.get(event.getCode()).execute();
        });
    }
}
