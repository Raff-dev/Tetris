package DLC.WATIFY;

import DLC.DLC;
import Display.Colors;
import Mechanics.Block;
import Mechanics.Tile;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.*;

import static Display.Window.*;
import static javafx.scene.paint.Color.BLACK;

public class WATIFY {
    private List<Tile> watTiles = new ArrayList<>();
    private boolean isWatified = false;

    public WATIFY() {
        colors.getPalette().add("WAT");
        Colors.getColorMap().put("WAT", wat);
        gameHandler.getActiveBlocks().addListener((ListChangeListener<Block>) change -> {
            while (change.next())
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(this::WATBlock);
                }
        });
        gameMenu.getFloaties().addListener(
                (ListChangeListener<Block>) change -> {
                    while (change.next()) {
                        if (change.wasAdded()) change.getAddedSubList().forEach(this::WATBlock);

                        if (change.wasRemoved()) {
                            change.getRemoved().forEach(f -> {
                                watTiles.removeAll(f.getTiles());
                            });
                        }
                    }
                });
        gameMenu.getColorChoices().addListener(
                (ListChangeListener<String>) change -> {
                    String paletteName = gameMenu.getColorChoices().get(0);
                    if (paletteName.equals("WAT")) WATify();
                    else if (isWatified) deWATify();
                });
    }

    private void WATify() {
        isWatified = true;
//        Platform.runLater(()->new Thread(() ->watTiles.forEach(this::WATTile)).start());
//        new Thread(() ->watTiles.forEach(this::WATTile)).start();
        new Thread(() ->watTiles.forEach(this::WATTile)).start();

    }

    private void deWATify() {
        isWatified = false;
        Platform.runLater(()->watTiles.forEach(this::deWATTile));
    }

    private void WATBlock(Block block) {
        block.getTiles().forEach(this::WATinit);
    }

    private void WATTile(Tile tile) {
        Platform.runLater(()->tile.setOpacity(1));
    }

    private void deWATTile(Tile tile) {
        Platform.runLater(()->tile.setOpacity(0));
    }

    private void WATinit(Tile tile) {
        watTiles.add(tile);
        VBox textBox = new VBox();
        Rectangle watGround = new Rectangle(Tile.side, Tile.side);
        int index = new Random().nextInt(watBlock.size());
        watGround.setFill(Colors.getPalette("WAT").get(index));
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
        tile.getChildren().addAll(watGround, textBox);
        if (isWatified) WATTile(tile);
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
