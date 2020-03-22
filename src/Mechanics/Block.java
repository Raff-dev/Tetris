package Mechanics;

import Display.Game;
import Display.Window;
import javafx.scene.paint.Color;

import java.util.*;

public class Block {
    private static int side = Tile.side;
    private ArrayList<Tile> tiles = new ArrayList<>();
    private int x, y;
    private static boolean isShown = false;
    private BlockType blockType;
    private Color color;
    int colorIndex;

    public Block(int x, int y, BlockType blockType, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.blockType = blockType;
        tiles.addAll(generateTiles(this));

    }

    Block() {
        this.x = Game.WIDTH / 2 + side;
        this.y = -side;
        this.color = colorAtRandom();
        do blockType = BlockType.atRandom();
        while (blockType == Window.gameHandler.getActiveBlockBlockType());
        tiles.addAll(generateTiles(this));
        canMoveY();
    }

    private static ArrayList<Tile> generateTiles(Block block) {
        ArrayList<Tile> newTiles = new ArrayList<>();
        for (int row = 0; row < block.blockType.layout.length; row++)
            for (int col = 0; col < block.blockType.layout[0].length; col++)
                if (block.blockType.layout[row][col] == 1)
                    newTiles.add(new Tile(-col * side, -row * side, block));
        return newTiles;
    }

    public void show() {
        tiles.forEach(t -> Window.window.getChildren().add(t.getTile()));
        isShown = true;
    }

    void moveX(int dir) {
        if (canMoveX(dir)) {
            x += dir * side;
            tiles.forEach(Tile::move);
        }
    }

    void moveY() {
        if (!canMoveY()) landed();
        else {
            y += side;
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
            offsetY += side;
            offsetX = 0;
            for (int isPresent : row) {
                offsetX += side;
                if (isPresent == 1) {
                    Tile t = titer.next();
                    t.setOffset(-offsetX, -offsetY);
                    boolean ocuppied = Tile.isOcuppied(t.getX(), t.getY());
                    boolean boundaries = t.getX() == -side || t.getX() == Game.WIDTH;
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

    public enum BlockType {
        Orange_Ricky(new int[][]{{0, 0, 1}, {1, 1, 1}}),
        Blue_Ricky(new int[][]{{1, 0, 0}, {1, 1, 1}}),
        Cleveland_Z(new int[][]{{1, 1, 0}, {0, 1, 1}}),
        Rhode_Island_Z(new int[][]{{0, 1, 1}, {1, 1, 0}}),
        Smashboy(new int[][]{{1, 1}, {1, 1}}),
        Hero(new int[][]{{1}, {1}, {1}, {1}}),
        Teewee(new int[][]{{0, 1, 0}, {1, 1, 1}});
        int[][] layout;

        BlockType(int[][] layout) {
            this.layout = layout;
        }

        private static BlockType atRandom() {
            Random random = new Random();
            return BlockType.values()[random.nextInt(BlockType.values().length)];
        }
    }

    public void remove() {
        tiles.forEach(Tile::remove);
    }

    private Color colorAtRandom() {
        int prevIndex = Window.gameHandler.getLastColorIndex();
        do colorIndex = new Random().nextInt(GameHandler.colors.size());
        while (colorIndex == prevIndex);
        return GameHandler.colors.get(colorIndex);
    }

    public BlockType getBlockType() {
        return this.blockType;
    }

    ArrayList<Tile> getTiles() {
        return this.tiles;
    }

    public Color getColor() {
        return this.color;
    }

    int getX() {
        return this.x;
    }

    int getY() {
        return this.y;
    }
}
