package Mechanics;

import Display.GameMenu;
import Display.Task;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.*;

import static javafx.scene.input.KeyCode.*;


public class KeyInput {
    private boolean menu = false;
    private List<KeyCode> codes = new ArrayList<>(
            Arrays.asList(ESCAPE, SPACE, ENTER, UP, DOWN, LEFT, RIGHT));

    public KeyInput(Scene scene, Handler handler) {
        Map<Object, Task> gameBindings = new HashMap<>();
        Map<Object, Task> menuBindings = new HashMap<>();

        Task[] gameActions = {
                () -> { GameMenu.toggle(); menu = !menu;},
                handler::fall, handler::fall,
                handler::rotate, () -> handler.move(0),
                () -> handler.move(-1),
                () -> handler.move(1)
        };
        Task[] menuActions = {
                ()-> { GameMenu.toggle(); menu = !menu;},
                GameMenu::select, GameMenu::select,
                () -> GameMenu.switchOption(true),
                () -> GameMenu.switchOption(false)
        };
        final Iterator citer1 = codes.iterator(), citer2 = codes.iterator();
        Arrays.stream(gameActions).forEach(action -> gameBindings.put(citer1.next(), action));
        Arrays.stream(menuActions).forEach(action -> menuBindings.put(citer2.next(), action));


        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (codes.contains(event.getCode())) {
                if (menu) menuBindings.get(event.getCode()).execute();
                else gameBindings.get(event.getCode()).execute();
            } else System.out.println(event.getCode());

        });
    }


}
