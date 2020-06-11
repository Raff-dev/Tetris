package Mechanics;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static Display.SoundHandler.Sound.*;
import static Display.Window.*;

import java.util.*;
import java.util.List;

/**
 * Handles all the game logic and connects single objects' behaviour with Tetris rules.
 * @author Rafal Lazicki
 */
public class GameHandler {
    private ArrayList<Block> occupied = new ArrayList<>();
    private boolean gameOver;
    private ObservableList<Block> activeBlocks = FXCollections.observableArrayList();
    private int startLevel;
    private int level;
    private int score;
    private int lines;

    /**
     * Clears the game board.
     * Resets game status information.
     * Creates a new steerable Tetris block.
     */
    public void start() {
        gameOver = false;
        if (!activeBlocks.isEmpty())
            activeBlock().getTiles().forEach(t -> game.getChildren().removeAll(t, t.getBg()));
        occupied.forEach(b -> {
            b.removeFrom(game);
            b.getTiles().clear();
        });
        occupied.clear();
        activeBlocks.clear();
        activeBlocks.addAll(new Block(), new Block());
        activeBlock().showOn(game);
        score = lines = 0;
        level = startLevel;
        game.setLevel(level);
        sideBar.setValues(score, lines, level);
        sideBar.setNextBlock(nextBlock());
    }

    /**
     * Single tick of game logic.
     * Runs with frequency specified by the current game difficulty level.
     */
    void update() {
        if (!gameOver) activeBlock().moveY();
    }

    /**
     * Changes position of the active block, that is beeing steered by the player.
     * @param dir direction according to which the active block shall be moved.
     */
    void move(int dir) {
        if (dir == 0) game.addTask("Move" + dir, 0.05, () -> activeBlock().moveY(), true);
        else game.addTask("Move" + dir, 0.05, () -> activeBlock().moveX(dir), true);
    }

    /**
     * Stops the active block from moving.
     * @param dir direction which identifies the repetitive task
     *           responsible for active block movement
     */
    void unMove(int dir) {
        game.removeTask("Move" + dir, true);
    }


    /**
     * Rotates active block 90 degrees. Plays appropriate sound.
     */
    void rotate() {
        if (activeBlock().rotate()) soundHandler.playSound(blockRotate);
        else soundHandler.playSound(denied);
    }

    /**
     * Makes the active block fall quickly until it has landed.
     */
    void fall() {
        if (activeBlock().getY() > Tile.side) {
            while (activeBlock().canMoveY()) activeBlock().moveY();
            activeBlock().moveY();
            TranslateTransition tr = new TranslateTransition(new Duration(50), game);
            tr.setToY(game.getTranslateY() + 5);
            tr.setInterpolator(Interpolator.EASE_IN);
            tr.setToX(new Random().nextInt(10) - 5);
            tr.setOnFinished((e) -> {
                TranslateTransition tr2 = new TranslateTransition(new Duration(50), game);
                tr2.setToY(0);
                tr2.setToX(0);
                tr2.play();
            });
            tr.play();
        }
    }

    /**
     * Checks for the game over.
     * Clears the lines if appropriate conditions are met.
     * Plays block landing sound.
     * Generates next steerable block.
     * @param block block that has hit the very bottom
     *              of game board or another bloc from up top.
     */
    void blockLanded(Block block) {
        if (gameOver) return;
        occupied.add(block);
        soundHandler.playSound(blockLanded);
        activeBlocks.remove(activeBlock());
        activeBlocks.add(new Block());
        sideBar.setNextBlock(nextBlock());
        activeBlock().setY(0);
        activeBlock().setX(Game.WIDTH / 2 + Tile.side);
        activeBlock().showOn(game);

        if (!activeBlock().canMoveY()) gameOver();
        TreeSet<Integer> yLookFor = new TreeSet<>();
        block.getTiles().forEach(t -> yLookFor.add(t.getY()));
        int linesCleared = clearLines(yLookFor);
        if (linesCleared == 4) soundHandler.playSound(lineClear);
        else if (linesCleared > 0) soundHandler.playSound(lineClear);
    }

    /**
     *
     * @param yLookFor Set of y coordinates, on which the program should
     *                check whether the conditions of clearing line are met.
     * @return count of lines that had been cleared.
     */
    private int clearLines(TreeSet<Integer> yLookFor) {
        List<Tile> toRemove = new ArrayList<>();
        TreeSet<Integer> yDelete = new TreeSet<>();
        yLookFor.descendingSet().forEach(y -> {
            List<Tile> row = new ArrayList<>();
            occupied.forEach(b -> b.getTiles().stream()
                    .filter(t -> t.getY() == y).forEach(row::add));
            if (row.size() == 10) {
                toRemove.addAll(row);
                yDelete.add(y);
            }
        });
        if (toRemove.size() == 0) return 0;
        for (int i = 0; i < toRemove.size(); i++) {
            FadeTransition ft = new FadeTransition(new Duration(50), toRemove.get(i).getBg());
            ft.setToValue(0);
            if (i + 1 == toRemove.size()) ft.setOnFinished((e) -> {
                toRemove.forEach(t -> {
                    t.removeFrom(game);
                    t.getBlock().getTiles().remove(t);
                });
                yDelete.forEach(y -> occupied.forEach(b -> b.getTiles().stream()
                        .filter(t -> t.getY() < y).forEach(Tile::fall)));
            });
            ft.play();
        }
        updateSideBar(yDelete.size());
        return yDelete.size();
    }

    /**
     * updates sidebar with current game status informations.
     * Calculates count of points, with which the player should be awarded with.
     * @param count of cleared lines.
     */
    private void updateSideBar(int count) {
        score += 100 * count * (level + count * 0.5);
        for (int i = 0; i < count; i++)
            if (++lines % 5 == 0) game.setLevel(++level);
        sideBar.setValues(score, lines, level);
    }

    /**
     * Sets the state of the game to game over.
     */
    private void gameOver() {
        System.out.println("GAME OVER");
        gameOver = true;
    }

    public void setStartLevel(int startLevel) {
        this.startLevel = startLevel;
    }

    public Block activeBlock() {
        if (activeBlocks.isEmpty()) return null;
        return activeBlocks.get(0);
    }

    public Block nextBlock() {
        return activeBlocks.get(1);
    }

    Color getActiveBlockColor() {
        if (activeBlocks.isEmpty()) return null;
        return activeBlock().getColor();
    }

    Block.BlockType getActiveBlockBlockType() {
        if (activeBlock() == null) return null;
        return activeBlock().getBlockType();
    }

    public ObservableList<Block> getActiveBlocks() {
        return activeBlocks;
    }

    public ArrayList<Block> getOccupied() {
        return occupied;
    }
}
