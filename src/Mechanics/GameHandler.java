package Mechanics;

import Display.Window;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameHandler {
    private static final ObservableList<Node> window = Window.window.getChildren();
    private ArrayList<Tile> occupied = new ArrayList<>();
    private Block activeBlock;
    private boolean gameOver = false;
    static ArrayList<Color> colors = new ArrayList<>(Arrays.asList(
            Color.rgb(102, 153, 255),
            Color.rgb(153, 255, 102),
            Color.rgb(255, 204, 102),
            Color.rgb(255, 102, 102),
            Color.rgb(255, 102, 204)
    ));

    public GameHandler() {
    }

    public void init() {
        this.activeBlock = new Block();
    }

    public void update() {
        activeBlock.moveY();
    }

    void move(int dir) {
        if (dir == 0) activeBlock.moveY();
        else activeBlock.moveX(dir);
    }

    void rotate() {
        activeBlock.rotate();
    }

    void fall() {
        if (activeBlock.getY() > Tile.side * 2)
            while (activeBlock.canMoveY()) activeBlock.moveY();
    }

    void blockLanded(Block block) {
        if (gameOver) return;
        activeBlock = new Block();
        if (!activeBlock.canMoveY()) gameOver();
        TreeSet<Integer> yLookFor = new TreeSet<>();
        block.getTiles().forEach(t -> {
            occupied.add(t);
            yLookFor.add(t.getY());
        });
        clearLines(yLookFor);
    }

    private void clearLines(TreeSet<Integer> yLookFor) {
        List<Tile> toRemove = new ArrayList<>();
        TreeSet<Integer> yDelete = new TreeSet<>();
        yLookFor.descendingSet().forEach(y -> {
            Stream<Tile> rowStream = occupied.stream().filter(t -> t.getY() == y);
            List<Tile> row = rowStream.collect(Collectors.toList());
            if (row.size()==10) {
                toRemove.addAll(row);
                yDelete.add(y);
            }
        });
        if (toRemove.size() > 0) {
            toRemove.forEach(Tile::remove);
            yDelete.forEach(y -> {
                occupied.stream().filter(t -> t.getY() < y).forEach(Tile::fall);
                System.out.println("ylookfor " + y);
            });
        }
    }

    int getLastColorIndex() {
        if (activeBlock == null) return -1;
        return activeBlock.colorIndex;
    }

    private void gameOver() {
        gameOver = true;
        System.out.println("GAME OVER");
    }

    public void reset() {
        activeBlock.getTiles().forEach(t -> window.remove(t.getTile()));
        occupied.removeIf(t -> window.removeAll(t.getTile()));
        init();
    }

    Block.BlockType getActiveBlockBlockType() {
        if (activeBlock == null) return null;
        return activeBlock.getBlockType();
    }

    ArrayList<Tile> getOccupied() {
        return occupied;
    }
}
