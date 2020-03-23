package Bindings;

import Display.GameMenu.*;
import static Display.GameMenu.ButtonName.*;

import static Display.GameMenu.*;
import static Display.Window.gameMenu;

public class ButtonBindings {
    public static void bind(){
        buttons.add(new MenuItem(Play,
                ()->gameMenu.extendWith(Easy, Medium, Hard)));
        buttons.add(new MenuItem(Easy,
                ()->gameMenu.setDifficulty(1)));
        buttons.add(new MenuItem(Medium,
                ()->gameMenu. setDifficulty(5)));
        buttons.add(new MenuItem(Hard,
                ()-> gameMenu.setDifficulty(10)));
        buttons.add(new MenuItem(Resume,
                ()-> resume()));
        buttons.add(new MenuItem(Settings,
                ()-> openSettings()));
        buttons.add(new MenuItem(Restart,
                ()-> restart()));
        buttons.add(new MenuItem(Quit,
                ()-> quit()));
    }
}
