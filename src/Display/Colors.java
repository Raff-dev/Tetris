package Display;

import javafx.scene.paint.Color;

import java.util.*;

public class Colors {

    enum Palette {Classic, Tritanopia, Tropical, Garden, Sunset}

    private Map<Palette, ArrayList<Color>> colorMap = new HashMap<>();
    private static ArrayList<Color> active = new ArrayList<>();
    private static ArrayList<Color> classic = new ArrayList<>(Arrays.asList(
            Color.rgb(102, 153, 255),
            Color.rgb(153, 255, 102),
            Color.rgb(255, 204, 102),
            Color.rgb(255, 102, 102),
            Color.rgb(255, 102, 204)
    ));
    private static ArrayList<Color> tritanopia = new ArrayList<>(Arrays.asList(
            Color.rgb(214, 90, 142),
            Color.rgb(173, 68, 105),
            Color.rgb(120, 100, 156),
            Color.rgb(127, 164, 219),
            Color.rgb(165, 210, 232)
    ));
    private static ArrayList<Color> tropical = new ArrayList<>(Arrays.asList(
            Color.rgb(255, 154, 114),
            Color.rgb(255, 199, 76),
            Color.rgb(78, 201, 255),
            Color.rgb(62, 160, 204),
            Color.rgb(54, 140, 178)
    ));
    private static ArrayList<Color> garden = new ArrayList<>(Arrays.asList(
            Color.rgb(109, 132, 104),
            Color.rgb(149, 195, 159),
            Color.rgb(195, 229, 167),
            Color.rgb(234, 255, 181),
            Color.rgb(76, 143, 68)
    ));
    private static ArrayList<Color> sunset = new ArrayList<>(Arrays.asList(
            Color.rgb(63, 81, 113),
            Color.rgb(98, 85, 129),
            Color.rgb(141, 84, 132),
            Color.rgb(180, 84, 118),
            Color.rgb(205, 92, 92)
    ));

    Colors() {
        active.addAll(classic);
        colorMap.put(Palette.Classic, classic);
        colorMap.put(Palette.Tritanopia, tritanopia);
        colorMap.put(Palette.Tropical, tropical);
        colorMap.put(Palette.Garden, garden);
        colorMap.put(Palette.Sunset, sunset);
    }

    public Color getRandom() {
        return active.get(new Random().nextInt(active.size()));
    }

    ArrayList<Color> getPalette(Colors.Palette palette) {
        return colorMap.get(palette);
    }

    void setActive(Palette palette) {
        Colors.active.clear();
        active.addAll(colorMap.get(palette));
    }

    ArrayList<Color> getActive() {
        return active;
    }

}
