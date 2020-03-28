package DLC;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DLC {
    private enum dlcs {WATIFY,Vaticancheek}

    public DLC() throws IOException, ClassNotFoundException {
        Arrays.asList(dlcs.values()).forEach(dlc -> {
            try {
                Class.forName("DLC." +dlc+"."+ dlc).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException |
                    InvocationTargetException | NoSuchMethodException |
                    ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

    }
    }
