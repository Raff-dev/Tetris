package Mechanics;

import Display.Game;
import Display.Window;
import javafx.scene.paint.Color;

import java.util.*;

class Block {
    private ArrayList<Tile> tiles = new ArrayList<>();
    private int x, y;
    private BlockType blockType;
    boolean gtg = false;
    Color color;
    int colorIndex;

    Block() {
        this.x = Game.WIDTH / 2 + Tile.side;
        this.y = 0;
        this.color = colorAtRandom();
        generateTiles();
        canMoveY();
    }

    private void generateTiles() {
        do blockType = BlockType.atRandom();
        while (blockType == Window.gameHandler.getActiveBlockBlockType());
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
        } gtg=true;
    }

     void moveX(int dir) {
        if (canMoveX(dir) && gtg) {
            x += dir * Tile.side;
            tiles.forEach(Tile::move);
        }
    }

     void moveY() {
        if (!canMoveY() && gtg) landed();
        else {
            y += Tile.side;
            tiles.forEach(Tile::move);
        }
    }

     boolean canMoveY() {
        for (Tile tile : tiles) if (tile.hasLanded()) return false;
        return true;
    }

     private boolean canMoveX(int dir) {
        for (Tile tile : tiles) if (tile.cantMove(dir)) return false;
        return true;
    }

    private void landed() {
        Window.gameHandler.blockLanded(this);
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

        ArrayList<Tile> backupTiles = new ArrayList<>();
        for (Tile tile : tiles) backupTiles.add(tile.copy());
        Iterator<Tile> titer = tiles.iterator();
        int offsetX, offsetY = 0, backupX = x;
        for (int[] row : newlayout) {
            offsetY += Tile.side;
            offsetX = 0;
            for (int isPresent : row) {
                offsetX += Tile.side;
                if (isPresent == 1) {
                    Tile t = titer.next();
                    t.setOffset(-offsetX, -offsetY);
                    boolean ocuppied = Tile.isOcuppied(t.getX(), t.getY());
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

    enum BlockType {
        Orange_Ricky(new int[][]{{0, 0, 1}, {1, 1, 1}}),
        Blue_Ricky(new int[][]{{1, 0, 0}, {1, 1, 1}}),
        Cleveland_Z(new int[][]{{1, 1, 0}, {0, 1, 1}}),
        Rhode_Island_Z(new int[][]{{0, 1, 1}, {1, 1, 0}}),
        Hero(new int[][]{{1, 1, 1, 1}}),
        Teewee(new int[][]{{0, 1, 0}, {1, 1, 1}}),
        Smashboy(new int[][]{{1, 1}, {1, 1}});
        int[][] layout;

        BlockType(int[][] layout) {
            this.layout = layout;
        }

        private static BlockType atRandom() {
            Random random = new Random();
            return BlockType.values()[random.nextInt(BlockType.values().length)];
        }
    }

    private Color colorAtRandom() {
        int prevIndex = Window.gameHandler.getLastColorIndex();
        do colorIndex = new Random().nextInt(GameHandler.colors.size());
        while (colorIndex == prevIndex);
        return GameHandler.colors.get(colorIndex);
    }

    BlockType getBlockType() {
        return blockType;
    }

    ArrayList<Tile> getTiles() {
        return tiles;
    }

    int getX() {
        return this.x;
    }

    int getY() {
        return this.y;
    }
}
