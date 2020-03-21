package Mechanics;

import Display.GameMenu;
import Display.Task;
import Display.Window;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.*;

import static javafx.scene.input.KeyCode.*;


public class InputHandler {
    private static final GameMenu gameMenu = Window.gameMenu;
    private Map<Object, Task> gameBindings = new HashMap<>();
    private Map<Object, Task> menuBindings = new HashMap<>();
    private List<KeyCode> codes = new ArrayList<>(
            Arrays.asList(ESCAPE, SPACE, ENTER, UP, DOWN, LEFT, RIGHT));

    public InputHandler(Scene scene, GameHandler gameHandler) {
        assignBindings(gameHandler);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (codes.contains(event.getCode())) {
                if (Window.gameMenu.isIsopen())
                    menuBindings.get(
                            event.getCode())
                            .execute();
                else gameBindings.get(event.getCode()).execute();
            } else System.out.println(event.getCode());
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
                GameMenu::select, GameMenu::select,
                () -> GameMenu.switchSelection(true),
                () -> GameMenu.switchSelection(false),
                ()->{},()->{}
        };
        final Iterator citer1 = codes.iterator(), citer2 = codes.iterator();
        Arrays.stream(gameActions).forEach(action -> gameBindings.put(citer1.next(), action));
        Arrays.stream(menuActions).forEach(action -> menuBindings.put(citer2.next(), action));
    }
}
