package com.tetris.main;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.MatrixType;

import java.awt.*;
import java.util.*;

class Block {
    private ArrayList<Tile> tiles = new ArrayList<Tile>();
    private int x, y, topY = Game.HEIGHT;
    private BlockType blockType;
    Color color;
    int colorIndex;
    Line line;


    Block() {
        this.x = Game.WIDTH / 2 + Tile.side;
        this.y = 0;
        this.color = colorAtRandom();
        generateTiles();
        canMoveY();
    }

    private void generateTiles() {
        do blockType = BlockType.atRandom();
        while (blockType == Game.handler.getActiveBlockBlockType());
        int offsetX, offsetY = 0;
        for (int[] row : blockType.layout) {
            offsetY += Tile.side;
            offsetX = 0;
            for (int isPresent : row) {
                offsetX += Tile.side;
                if (isPresent == 1) {
                    Tile newTile = new Tile(-offsetX, -offsetY, this);
                    newTile.show();
                    this.tiles.add(newTile);
                }
            }
        }
        line = new Line(x, y, x, y);
        line.setStroke(Color.RED);
        line.setStrokeWidth(10);
        Game.window.getChildren().add(line);
    }

    public void moveX(int dir) {
        if (canMoveX(dir)) {
            x += dir * Tile.side;
            line.relocate(x, y);
            for (Tile tile : this.tiles) tile.move();
        }
    }

    public void moveY() {
        if (!canMoveY()) landed();
        else {
            y += Tile.side;
            line.relocate(x, y);
            for (Tile tile : this.tiles) tile.move();
        }
    }

    public boolean canMoveY() {
        for (Tile tile : tiles) if (tile.hasLanded()) return false;
        return true;
    }

    public boolean canMoveX(int dir) {
        for (Tile tile : tiles) if (!tile.canMove(dir)) return false;
        return true;
    }

    private void landed() {
        Game.handler.blockLanded(this);
    }

    void rotate() {
        BlockType bt = this.blockType;
        int rows = bt.layout.length - 1;
        int cols = bt.layout[0].length - 1;
        int[][] newlayout = new int[cols + 1][rows + 1];
        for (int row = 0; row <= rows; row++) {
            for (int col = 0; col <= cols; col++) {
                newlayout[col][rows - row] = bt.layout[row][col];
            }
        }

        ArrayList<Tile> backupTiles = new ArrayList<Tile>();
        for (Tile tile : tiles) backupTiles.add(tile.copy());
        Iterator<Tile> titer = tiles.iterator();
        int backupX = x;
        int offsetX, offsetY = 0;
        for (int[] row : newlayout) {
            offsetY += Tile.side;
            offsetX = 0;
            for (int isPresent : row) {
                offsetX += Tile.side;
                if (isPresent == 1) {
                    Tile t = titer.next();
                    t.setOffset(-offsetX, -offsetY);
                    boolean ocuppied = Tile.isOcuppied(t.getX(),t.getY());
                    boolean boundaries = t.getX() == -Tile.side || t.getX() == Game.WIDTH;
                    if (ocuppied || boundaries) {
                        if (canMoveX(1)) moveX(1);
                        else {
                            System.out.println("cant, bruh");
                            tiles = backupTiles;
                            x = backupX;
                            for (Tile tile : this.tiles) tile.move();
                            return;
                        }
                    }
                    t.move();
                }
            }
        }
        bt.layout = newlayout;
    }

    public void tick() {
    }

    public void render() {
    }

    enum BlockType {
        Orange_Ricky(new int[][]{{0, 0, 1}, {1, 1, 1}}),
        Blue_Ricky(new int[][]{{1, 0, 0}, {1, 1, 1}}),
        Cleveland_Z(new int[][]{{1, 1, 0}, {0, 1, 1}}),
        Rhode_Island_Z(new int[][]{{0, 1, 1}, {1, 1, 0}}),
        Hero(new int[][]{{1, 1, 1, 1}}),
        Teewee(new int[][]{{0, 1, 0}, {1, 1, 1}}),
        Smashboy(new int[][]{{1, 1}, {1, 1}});
        int[][] layout;
        int rotation = 0;

        BlockType(int[][] layout) {
            this.layout = layout;
        }

        private static BlockType atRandom() {
            Random random = new Random();
            return BlockType.values()[random.nextInt(BlockType.values().length)];
        }
    }

    public Color colorAtRandom() {
        int prevIndex = Game.handler.getLastColorIndex();
        do colorIndex = new Random().nextInt(Handler.colors.size());
        while (colorIndex == prevIndex);
        return Handler.colors.get(colorIndex);
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
