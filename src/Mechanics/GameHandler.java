package Mechanics;

import javafx.scene.paint.Color;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameHandler {
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
        if (activeBlock.getY()>Tile.side*2)
            while (activeBlock.canMoveY()) activeBlock.moveY();
    }

    void blockLanded(Block block) {
        if (gameOver) return;
        activeBlock = new Block();
        if (!activeBlock.canMoveY()) gameOver();
        HashSet<Integer> yLookFor = new HashSet<>();
        block.getTiles().forEach(t -> {
            occupied.add(t);
            yLookFor.add(t.getY());
        });
        System.out.println("Lookin for: " + yLookFor);
        for (int y : yLookFor) {
            Stream<Tile> rowStream = occupied.stream().filter(t -> t.getY() == y);
            List<Tile> row = rowStream.collect(Collectors.toList());
            if (row.size() == 10) {
                System.out.println("REEEEEE 1000000");
                row.forEach(t -> {
                    occupied.remove(t);
                    t.remove();
                });
                Stream<Tile> higherStream = occupied.stream().filter(t -> t.getY() < y);
                List<Tile> higher = higherStream.collect(Collectors.toList());
                higher.forEach(Tile::fall);
                System.out.println("COUNT: " + higher.size());
            }
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

    Block.BlockType getActiveBlockBlockType() {
        if (activeBlock == null) return null;
        return activeBlock.getBlockType();
    }

    ArrayList<Tile> getOccupied() {
        return occupied;
    }
}
