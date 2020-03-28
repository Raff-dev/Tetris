package DLC;

import Display.BlockTask;
import Display.Colors;
import Display.GameMenu;
import Mechanics.Block;
import Mechanics.Tile;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.*;

import static Display.GameMenu.Mode.PAUSE;
import static Display.Window.*;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;
import static javafx.scene.paint.Color.BLACK;

public class WATIFY {

    Map<Tile, Color> tiles = new Hashtable<>();
    private boolean isWatified = false;

    public WATIFY() {

        colors.getPalette().add("WAT");
        Colors.getColorMap().put("WAT", wat);
        gameHandler.getActiveBlocks().addListener((ListChangeListener<Block>) change -> {
            change.getAddedSubList().forEach(b -> WATify(b));
            System.out.println("watify active");
            System.out.println(change.wasAdded());
            System.out.println(change);
        });
        gameMenu.getFloaties().addListener(
                (ListChangeListener<Block>) change -> change.getAddedSubList().forEach(b -> WATify(b)));
        gameMenu.getColorChoices().addListener(
                (ListChangeListener<String>) change -> {
                    String paletteName = gameMenu.getColorChoices().get(0);
                    if (paletteName.equals("Classic")) game.removeTask("JP2", true);
                    if (paletteName.equals("WAT")) {
                        isWatified = true;
                    } else if (paletteName.equals("Vatican")) {
                        deWATify();
//                    blockTask = (b) -> {
//                        watify.setWatified(false);
//                        b.setColor(colors.getRandom());
//                    };
                    }
                    BlockTask blockTask;
                    blockTask = (b) -> b.setColor(colors.getRandom());

                    new Thread(() -> Platform.runLater(() -> {
                        gameHandler.getOccupied().forEach(b -> blockTask.execute(b));
                        gameMenu.getFloaties().forEach(f -> blockTask.execute(f));
                        if (gameMenu.getMode() == PAUSE) {
                            blockTask.execute(gameHandler.activeBlock());
                            blockTask.execute(gameHandler.nextBlock());
                            blockTask.execute(sideBar.getNextBlock());
                        }
                    })).start();
                });

        scene.addEventFilter(KEY_PRESSED, event -> {
            if (gameMenu.getMode() != GameMenu.Mode.RUNNING) {
                System.out.println("THIS IS WATIFY");

            }
        });
    }

    void WATify(Block block) {
        block.getTiles().forEach(t -> WATinit(t));
    }

    public void deWATify() {
        tiles.keySet().forEach(t -> deWATify(t));
    }


    private void WATinit(Tile tile) {
        VBox textBox = new VBox();
        int index = new Random().nextInt(watBlock.size());
        tiles.put(tile, Colors.getPalette("WAT").get(index));
        for (String info : watBlock.get(index)) {
            Text t = new Text(info);
            t.setFont(Font.font(12));
            t.setTextAlignment(TextAlignment.CENTER);
            t.setFill(BLACK);
            t.setTranslateX(Tile.side * 0.5);
            textBox.getChildren().add(t);
        }
        textBox.setAlignment(Pos.TOP_CENTER);
        tile.relocate(tile.getBlock().getX() + tile.getOffsetX(),
                tile.getBlock().getY() + tile.getOffsetY());
        tile.setOpacity(0);
        tile.getChildren().add(textBox);
        if (isWatified) WATify(tile);
    }

    void WATify(Tile tile) {
        tile.setOpacity(1);
        tile.getBg().setFill(tiles.get(tile));
    }

    void deWATify(Tile tile) {
        tile.setOpacity(0);
    }

    public boolean isWatified() {
        return isWatified;
    }

    public void setWatified(boolean watified) {
        isWatified = watified;
    }

    public static List<List<String>> watBlock = new ArrayList<>(Arrays.asList(
            Arrays.asList("Bpe", "(W)", "Do"),
            Arrays.asList("Bpe", "(L)", "Rd"),
            Arrays.asList("Bzs", "(W)", "Sza"),
            Arrays.asList("IO", "(W)", "Bl"),
            Arrays.asList("IO", "(L)", "BąP"),
            Arrays.asList("Jtp", "(W)", "PK"),
            Arrays.asList("Jtp", "(L)", "PK"),
            Arrays.asList("MM", "(W)", "Ch"),
            Arrays.asList("MM", "(ć)", "MK"),
            Arrays.asList("Oin", "(W)", "RJ"),
            Arrays.asList("Oin", "(ć)", "RJ"),
            Arrays.asList("Pw", "(W)", "Rul"),
            Arrays.asList("Pw", "(L)", "Rul"),
            Arrays.asList("Pz", "(W)", "Mur"),
            Arrays.asList("Pz", "(ć)", "Mur"),
            Arrays.asList("Sck", "(W)", "SkL"),
            Arrays.asList("Sck", "(L)", "Kwoj"),
            Arrays.asList("Swb", "(W)", "Mu"),
            Arrays.asList("Swb", "(L)", "Mi"),
            Arrays.asList("Swk", "(W)", "Ar"),
            Arrays.asList("Swk", "(L)", "Ka"),
            Arrays.asList("Swk", "(ć)", "Tu")
    ));
    private static ArrayList<Color> wat = new ArrayList<>(Arrays.asList(
            Color.rgb(250, 142, 130),
            Color.rgb(250, 142, 130),
            Color.rgb(237, 149, 142),
            Color.rgb(247, 214, 25),
            Color.rgb(247, 214, 25),
            Color.rgb(99, 208, 224),
            Color.rgb(99, 208, 224),
            Color.rgb(230, 154, 195),
            Color.rgb(230, 154, 195),
            Color.rgb(179, 255, 217),
            Color.rgb(179, 255, 217),
            Color.rgb(174, 234, 252),
            Color.rgb(174, 234, 252),
            Color.rgb(113, 245, 223),
            Color.rgb(113, 245, 223),
            Color.rgb(255, 238, 128),
            Color.rgb(255, 238, 128),
            Color.rgb(205, 127, 219),
            Color.rgb(205, 127, 219),
            Color.rgb(39, 196, 81),
            Color.rgb(39, 196, 81),
            Color.rgb(39, 196, 81)
    ));
}
