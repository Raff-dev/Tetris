package Mechanics;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;
import static Display.GameMenu.Mode.PLAY;
import static Display.Window.*;

import javafx.scene.input.KeyCode;
import Display.Task;

import java.util.*;

public class InputHandler {
    private Map<Object, Task> gameBindings = new HashMap<>();
    private Map<Object, Task> menuBindings = new HashMap<>();
    private List<KeyCode> codes;

    public InputHandler() {
        assignBindings();
        scene.addEventFilter(KEY_PRESSED, event -> {
            if (gameMenu.getMode() == PLAY && gameBindings.containsKey(event.getCode()))
                gameBindings.get(event.getCode()).execute();
            else if (menuBindings.containsKey(event.getCode()))
                menuBindings.get(event.getCode()).execute();
        });
    }

    private void assignBindings() {
        codes = new ArrayList<>(Arrays.asList(
                ESCAPE, SPACE, ENTER, UP, DOWN, LEFT, RIGHT));

        List<Task> gameActions = new ArrayList<>(Arrays.asList(
                gameMenu::toggle,
                () -> gameHandler.fall(),
                () -> gameHandler.fall(),
                () -> gameHandler.rotate(),
                () -> gameHandler.move(0),
                () -> gameHandler.move(-1),
                () -> gameHandler.move(1)
        ));
        List<Task> menuActions = new ArrayList<>(Arrays.asList(
                gameMenu::toggle, gameMenu::select, gameMenu::select,
                () -> gameMenu.switchSelection(-1),
                () -> gameMenu.switchSelection(1)
        ));
        final Iterator citer1 = codes.iterator(), citer2 = codes.iterator();
        gameActions.forEach(action -> gameBindings.put(citer1.next(), action));
        menuActions.forEach(action -> menuBindings.put(citer2.next(), action));
    }
}
