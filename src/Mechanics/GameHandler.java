package Mechanics;

import javafx.scene.paint.Color;

import static Display.SoundHandler.Sound.*;
import static Display.Window.*;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameHandler {
    private ArrayList<Tile> occupied = new ArrayList<>();
    private boolean gameOver = false;
    private Block activeBlock;
    private Block nextBlock;
    private int score = 0, level = 0, lines = 0;


    public void init() {
        activeBlock = new Block();
        activeBlock.showOn(game);
        nextBlock = new Block();
        sideBar.setNextBlock(nextBlock);
        sideBar.setValues(score, level, lines);
    }

    public void update() {
        activeBlock.moveY();
    }

    void move(int dir) {
        if (dir == 0) activeBlock.moveY();
        else activeBlock.moveX(dir);
    }

    void rotate() {
        if (activeBlock.rotate())soundHandler.playSound(blockRotate);
        else soundHandler.playSound(denied);
    }

    void fall() {
        if (activeBlock.getY() > Tile.side){
            while (activeBlock.canMoveY()) activeBlock.moveY();
            activeBlock.moveY();
        }
    }

    void blockLanded(Block block) {
        if (gameOver) return;
        activeBlock = nextBlock;
        activeBlock.showOn(game);
        nextBlock = new Block();
        sideBar.setNextBlock(nextBlock);
        if (!activeBlock.canMoveY()) gameOver();
        TreeSet<Integer> yLookFor = new TreeSet<>();
        block.getTiles().forEach(t -> {
            occupied.add(t);
            yLookFor.add(t.getY());
        });
        int linesCleared = clearLines(yLookFor);
        soundHandler.playSound(blockLanded);
        if (linesCleared==4) soundHandler.playSound(lineClear);
        else if (linesCleared>0)soundHandler.playSound(lineClear);
    }

    private int clearLines(TreeSet<Integer> yLookFor) {
        List<Tile> toRemove = new ArrayList<>();
        TreeSet<Integer> yDelete = new TreeSet<>();
        yLookFor.descendingSet().forEach(y -> {
            Stream<Tile> rowStream = occupied.stream().filter(t -> t.getY() == y);
            List<Tile> row = rowStream.collect(Collectors.toList());
            if (row.size() == 10) {
                toRemove.addAll(row);
                yDelete.add(y);
            }
        });
        if (toRemove.size() > 0) {
            toRemove.forEach(t -> t.removeFrom(game));
            yDelete.forEach(y -> {
                occupied.stream().filter(t -> t.getY() < y).forEach(Tile::fall);
                System.out.println("ylookfor " + y);
            });
            updateSideBar(yDelete.size());
            return yDelete.size();
        }
        return yDelete.size();
    }

    private void updateSideBar(int count) {
        lines += count;
        score += 100 * count * (level + count * 0.5);
        if (lines % 10 == 0) game.increaseLevel(++level);
        sideBar.setValues(score, lines, level);
    }

    private void gameOver() {
        gameOver = true;
        System.out.println("GAME OVER");
    }

    public void reset() {
        activeBlock.getTiles().forEach(t -> game.getChildren().remove(t.getTile()));
        occupied.removeIf(t -> game.getChildren().removeAll(t.getTile()));
        init();
    }

    Color getActiveBlockColor(){
        if (activeBlock == null) return null;
        return activeBlock.getColor();
    }

    Block.BlockType getActiveBlockBlockType() {
        if (activeBlock == null) return null;
        return activeBlock.getBlockType();
    }

    ArrayList<Tile> getOccupied() {
        return occupied;
    }

    public void setLevel(int level) {
        this.level = level;
        sideBar.setValues(score, lines, level);
        System.out.println("level:" + level);
    }
}
