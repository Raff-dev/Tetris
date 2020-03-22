package Mechanics;

import Display.GameMenu;
import Display.Task;
import Display.Window;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.*;

import static Display.GameMenu.Mode.PLAY;
import static Display.GameMenu.Mode.START;
import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;


public class InputHandler {
    private static final GameMenu gameMenu = Window.gameMenu;
    private Map<Object, Task> gameBindings = new HashMap<>();
    private Map<Object, Task> menuBindings = new HashMap<>();
    private List<KeyCode> codes = new ArrayList<>(
            Arrays.asList(ESCAPE, SPACE, ENTER, UP, DOWN, LEFT, RIGHT));

    public InputHandler(Scene scene, GameHandler gameHandler) {
        assignBindings(gameHandler);

        scene.addEventFilter(KEY_PRESSED, event -> {
            if (codes.contains(event.getCode())) {
                if (Window.gameMenu.getMode() == PLAY)
                    gameBindings.get(event.getCode()).execute();
                else menuBindings.get(event.getCode()).execute();
            }
        });
    }

    private void assignBindings(GameHandler gameHandler) {
        Task[] gameActions = {
                gameMenu::toggle, gameHandler::fall,
                gameHandler::fall, gameHandler::rotate,
                () -> gameHandler.move(0),
                () -> gameHandler.move(-1),
                () -> gameHandler.move(1)
        };
        Task[] menuActions = {
                gameMenu::toggle,
                gameMenu::select, gameMenu::select,
                () -> gameMenu.switchSelection(-1),
                () -> gameMenu.switchSelection(1),
        };
        final Iterator citer1 = codes.iterator(), citer2 = codes.iterator();
        Arrays.stream(gameActions).forEach(action -> gameBindings.put(citer1.next(), action));
        Arrays.stream(menuActions).forEach(action -> menuBindings.put(citer2.next(), action));
    }
}
