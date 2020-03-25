package Mechanics;

import Display.RepetitiveTask;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static Display.SoundHandler.Sound.*;
import static Display.Window.*;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameHandler {
    private ArrayList<Tile> occupied = new ArrayList<>();
    private boolean gameOver;
    private Block activeBlock;
    private Block nextBlock;
    private int startLevel;
    private int level;
    private int score;
    private int lines;

    public void start() {
        gameOver = false;
        if (activeBlock != null)
            activeBlock.getTiles().forEach(t -> game.getChildren().remove(t.getTile()));
        occupied.removeIf(t -> game.getChildren().removeAll(t.getTile()));
        activeBlock = new Block();
        activeBlock.showOn(game);
        nextBlock = new Block();
        score = lines = 0;
        level = startLevel;
        game.setLevel(level);
        sideBar.setValues(score, lines, level);
        sideBar.setNextBlock(nextBlock);
    }

    public void update() {
        activeBlock.moveY();
    }

    void move(int dir) {
        if (dir == 0) game.addTask("Move" + dir, 0.05, () -> activeBlock.moveY(), true);
        else game.addTask("Move" + dir, 0.05, () -> activeBlock.moveX(dir), true);
    }

    void unMove(int dir) {
        game.removeTask("Move" + dir, true);
    }

    void rotate() {
        if (activeBlock.rotate()) soundHandler.playSound(blockRotate);
        else soundHandler.playSound(denied);
    }

    void fall() {
        if (activeBlock.getY() > Tile.side) {
            while (activeBlock.canMoveY()) activeBlock.moveY();
            activeBlock.moveY();
            TranslateTransition tr = new TranslateTransition(new Duration(50), game);
            tr.setToY(game.getTranslateY() + 5);
            tr.setInterpolator(Interpolator.EASE_IN);
            tr.setToX(new Random().nextInt(10) - 5);
            System.out.println(game.getTranslateX());
            tr.setOnFinished((e) -> {
                TranslateTransition tr2 = new TranslateTransition(new Duration(50), game);
                tr2.setToY(0);
                tr2.setToX(0);
                tr2.play();
            });
            tr.play();
            System.out.println(game.getTranslateX());
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
        if (linesCleared == 4) soundHandler.playSound(lineClear);
        else if (linesCleared > 0) soundHandler.playSound(lineClear);
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
            for (int i = 0; i < toRemove.size(); i++) {
                FadeTransition ft = new FadeTransition(new Duration(50), toRemove.get(i).getTile());
                ft.setToValue(0);
                if (i+1 == toRemove.size()) ft.setOnFinished((e) -> {
                    toRemove.forEach(t -> t.removeFrom(game));
                    yDelete.forEach(y -> occupied.stream()
                            .filter(t -> t.getY() < y).forEach(Tile::fall));
                });
                ft.play();
            }
            updateSideBar(yDelete.size());
            return yDelete.size();
        }
        return yDelete.size();
    }

    private void updateSideBar(int count) {
        score += 100 * count * (level + count * 0.5);
        for (int i = 0; i < count; i++)
            if (++lines % 5 == 0) game.setLevel(++level);
        sideBar.setValues(score, lines, level);
    }

    public void setStartLevel(int startLevel) {
        this.startLevel = startLevel;
    }

    private void gameOver() {
        gameOver = true;
        System.out.println("GAME OVER");
    }

    public Block getActiveBlock() {
        return activeBlock;
    }

    Color getActiveBlockColor() {
        if (activeBlock == null) return null;
        return activeBlock.getColor();
    }

    Block.BlockType getActiveBlockBlockType() {
        if (activeBlock == null) return null;
        return activeBlock.getBlockType();
    }

    public ArrayList<Tile> getOccupied() {
        return occupied;
    }
}
