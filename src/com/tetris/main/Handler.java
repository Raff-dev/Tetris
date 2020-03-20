package com.tetris.main;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Handler {
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Point> occupied = new ArrayList<>();
    private Block activeBlock;
    private boolean gameOver = false;
    static ArrayList<Color> colors = new ArrayList<>(Arrays.asList(
            Color.rgb(102, 153, 255),
            Color.rgb(153, 255, 102),
            Color.rgb(255, 204, 102),
            //olor.rgb(255, 102, 102),
            Color.rgb(255, 102, 204)
    ));

    Handler() {
    }

    void init() {
        this.activeBlock = new Block();
    }

    public void tick() {
        activeBlock.moveY();
    }

    public void render() {
        activeBlock.render();
    }

    public void addBlock(Block block) {
        this.blocks.add(block);
    }

    public void removeBlock(Block block) {
        this.blocks.remove(block);
    }


    public void blockLanded(Block block) {
        if (!gameOver) {
            activeBlock = new Block();
            if (!activeBlock.canMoveY()) gameOver();
            blocks.add(block);
            for (Tile tile : block.getTiles()) {
                occupied.add(new Point(tile.getX(), tile.getY()));
                Line line = new Line(tile.getX(), tile.getY(), tile.getX(), tile.getY());
                line.setStroke(Color.BLUEVIOLET);
                line.setStrokeWidth(10);
                Game.window.getChildren().add(line);
            }
        }
    }

    public Block getActiveBlock() {
        return activeBlock;
    }

    public int getLastColorIndex() {
        if (activeBlock == null) return -1;
        return activeBlock.colorIndex;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    private void gameOver() {
        gameOver = true;
        System.out.println("GAME OVER");
    }

    public Block.BlockType getActiveBlockBlockType() {
        if (activeBlock == null) return null;
        return activeBlock.getBlockType();
    }

    public ArrayList<Point> getOccupied() {
        return occupied;
    }

}
