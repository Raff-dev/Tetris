package Mechanics;

import static Display.Window.*;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.*;

public class Block {
    private ArrayList<Tile> tiles = new ArrayList<>();
    private BlockType blockType;
    private Color color;
    private int x, y;
    private static boolean isShown = false;

    public Block(int x, int y, BlockType blockType, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.blockType = blockType;
        tiles.addAll(generateTiles(this));
    }

    Block() {
        this.x = Game.WIDTH / 2 + Tile.side;
        this.y = 0;
        do color = colors.getRandom();
        while (color == gameHandler.getActiveBlockColor());
        do blockType = BlockType.atRandom();
        while (blockType == gameHandler.getActiveBlockBlockType());
        tiles.addAll(generateTiles(this));
        canMoveY();
    }

    private static ArrayList<Tile> generateTiles(Block block) {
        ArrayList<Tile> newTiles = new ArrayList<>();
        for (int row = 0; row < block.blockType.layout.length; row++)
            for (int col = 0; col < block.blockType.layout[0].length; col++)
                if (block.blockType.layout[row][col] == 1)
                    newTiles.add(new Tile(-(col + 1) * Tile.side, -(row + 1) * Tile.side, block));
        return newTiles;
    }

    public void showOn(Pane pane) {
        tiles.forEach(t -> pane.getChildren().addAll(t.getBg(), t));
        isShown = true;
    }

    public void removeFrom(Pane pane) {
        tiles.forEach(t -> t.removeFrom(pane));
    }

    void moveX(int dir) {
        if (canMoveX(dir)) {
            x += dir * Tile.side;
            tiles.forEach(Tile::move);
        }
    }

    void moveY() {
        if (!canMoveY()) landed();
        else {
            y += Tile.side;
            tiles.forEach(Tile::move);
        }
    }

    boolean canMoveY() {
        for (Tile tile : tiles) if (tile.hasLanded()) return false;
        return isShown;
    }

    private boolean canMoveX(int dir) {
        for (Tile tile : tiles) if (tile.cantMove(dir)) return false;
        return isShown;
    }

    private void landed() {
        gameHandler.blockLanded(this);
    }

    public boolean rotate() {
        BlockType bt = this.blockType;
        int rows = bt.layout.length - 1;
        int cols = bt.layout[0].length - 1;
        int[][] newlayout = new int[cols + 1][rows + 1];
        for (int row = 0; row <= rows; row++)
            for (int col = 0; col <= cols; col++)
                newlayout[col][rows - row] = bt.layout[row][col];

        ArrayList<Tile> backupTiles = new ArrayList<>();
        tiles.forEach(t-> backupTiles.add(t.copy()));
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
                            tiles = backupTiles;
                            x = backupX;
                            for (Tile tile : this.tiles) tile.move();
                            return false;
                        }
                    }
                    t.move();
                }
            }
        }
        this.blockType.layout = newlayout;
        this.blockType.rotate();
        return true;
    }

    public enum BlockType {
        Orange_Ricky(new int[][]{{0, 0, 1}, {1, 1, 1}}),
        Blue_Ricky(new int[][]{{1, 0, 0}, {1, 1, 1}}),
        Cleveland_Z(new int[][]{{1, 1, 0}, {0, 1, 1}}),
        Rhode_Island_Z(new int[][]{{0, 1, 1}, {1, 1, 0}}),
        Smashboy(new int[][]{{1, 1}, {1, 1}}),
        Hero(new int[][]{{1, 1, 1, 1}}),
        Teewee(new int[][]{{0, 1, 0}, {1, 1, 1}});
        int[][] layout;
        int rotation = 0;

        BlockType(int[][] layout) {
            this.layout = layout;
        }

        public static BlockType atRandom() {
            Random random = new Random();
            return BlockType.values()[random.nextInt(BlockType.values().length)];
        }

        public int width() {
            return this.layout[0].length * Tile.side;
        }

        public int height() {
            return this.layout.length * Tile.side;
        }

        public void rotate() {
            this.rotation = (this.rotation + 1) % 4;
        }

        public int getRotation() {
            return this.rotation;
        }
    }

    public BlockType getBlockType() {
        return this.blockType;
    }

    public ArrayList<Tile> getTiles() {
        return this.tiles;
    }

    Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
        tiles.forEach(t -> t.getBg().setFill(color));
    }

    public void setX(int x) {
        this.x = x;
        tiles.forEach(t -> t.move());
    }

    public void setY(int y) {
        this.y = y;
        tiles.forEach(t -> t.move());
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
