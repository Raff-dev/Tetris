package Mechanics;

import Display.Window;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import static Display.Window.*;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameHandler {
    private ArrayList<Tile> occupied = new ArrayList<>();
    private boolean gameOver = false;
    private Block activeBlock;
    private Block nextblock;
    private int score,level,lines;
    static ArrayList<Color> colors = new ArrayList<>(Arrays.asList(
            Color.rgb(102, 153, 255),
            Color.rgb(153, 255, 102),
            Color.rgb(255, 204, 102),
            Color.rgb(255, 102, 102),
            Color.rgb(255, 102, 204)
    ));

    public void init() {
        activeBlock = new Block();
        activeBlock.showOn(window);
        nextblock = new Block();
        Window.sideBar.setNextBlock(nextblock);
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
        activeBlock = nextblock;
        activeBlock.showOn(window);
        nextblock = new Block();
        Window.sideBar.setNextBlock(nextblock);
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
            toRemove.forEach(t->t.removeFrom(window));
            yDelete.forEach(y -> {
                occupied.stream().filter(t -> t.getY() < y).forEach(Tile::fall);
                System.out.println("ylookfor " + y);
            });
            updateSideBar(yDelete.size());


        }
    }
    private void updateSideBar(int count){
        lines+=count;
        score += 100*count*(level + count*0.5);
        if (lines%10==0) game.increaseLevel(++level);
        sideBar.setValues(score,lines,level);
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
        activeBlock.getTiles().forEach(t -> window.getChildren().remove(t.getTile()));
        occupied.removeIf(t -> window.getChildren().removeAll(t.getTile()));
        init();
    }

    Block.BlockType getActiveBlockBlockType() {
        if (activeBlock == null) return null;
        return activeBlock.getBlockType();
    }

    ArrayList<Tile> getOccupied() {
        return occupied;
    }

    public void setLevel(int level){
        this.level = level;
        System.out.println("level:" +level);
    }
}
