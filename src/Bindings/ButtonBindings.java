package Bindings;

import Display.MenuItem;

import static Display.GameMenu.ButtonName.*;

import static Display.GameMenu.*;
import static Display.Window.gameMenu;

/**
 * Class containing all the button creations.
 * @author Rafal Lazicki
 */
public class ButtonBindings {

    /**
     * Creates MenuItem buttons with specified names and functions,
     * that they represented after being interacted with and adds them
     * to the Menu's buttons array list.
     */
    public static void bind() {
        buttons.add(new MenuItem(Play,
                () -> gameMenu.extendWith(gameMenu.getButtons(Easy, Medium, Hard))));
        buttons.add(new MenuItem(Resume,
                () -> resume()));
        buttons.add(new MenuItem(Settings,
                () -> gameMenu.openSettings()));
        buttons.add(new MenuItem(Change_level,
                () -> gameMenu.extendWith(gameMenu.getButtons(Easy, Medium, Hard))));
        buttons.add(new MenuItem(Restart,
                () -> restart()));
        buttons.add(new MenuItem(Quit,
                () -> gameMenu.quit()));

        buttons.add(new MenuItem(Easy,
                () -> gameMenu.startGame(1)));
        buttons.add(new MenuItem(Medium,
                () -> gameMenu.startGame(5)));
        buttons.add(new MenuItem(Hard,
                () -> gameMenu.startGame(10)));

        buttons.add(new MenuItem(Volume,
                () -> gameMenu.changeVolume()));
        buttons.add(new MenuItem(Color_palette,
                () -> gameMenu.changeColorPalette()));
    }
}
