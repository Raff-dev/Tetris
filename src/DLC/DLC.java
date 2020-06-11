package DLC;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * DLC class runs every additional content that was placed within DLC package.
 * Allows to make additions to the game without interfering with the main program.
 * Every additional DLC shall be added to dlcs enum below.
 * @author Rafal Lazicki
 */
public class DLC {
    private enum dlcs {WATIFY, Vaticancheek}

    /**
     * Searches and launches the dlcs that it found.
     */
    public DLC() {
        Arrays.asList(dlcs.values()).forEach(dlc -> {
            try {
                Class.forName("DLC." + dlc + "." + dlc).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException |
                    InvocationTargetException | NoSuchMethodException |
                    ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
}
