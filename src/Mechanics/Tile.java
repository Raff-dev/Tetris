package Mechanics;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import static Display.Window.gameHandler;

/**
 * Represents single square of a Tetromino block.
 * This class is responsible for mechanics
 * connected to the behaviour of a single Tile
 */
public class Tile extends BorderPane {
    public static int side = 50;
    private Rectangle bg = new Rectangle(side, side);
    private Block block;
    private int offsetX, offsetY;

    Tile(int offsetX, int offsetY, Block block) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        setBlock(block);
    }

    /**
     * checks wether the tile could be moved in a given direction
     * @param dir direction whitch should be checked
     * @return boolean value representing possibility of
     *          movement in specified direcction
     */
    boolean cantMove(int dir) {
        int x = getX(), y = getY();
        boolean xBorder = false, yBorder = false, occupied;
        if (dir == 0) {
            yBorder = y + side == Game.HEIGHT;
            occupied = isOccuppied(x, y + side);
        } else {
            xBorder = (x + side) * dir == Game.WIDTH || (x + side) * dir == -side;
            occupied = isOccuppied(x + dir * side, y);
        }
        return occupied || xBorder || yBorder;
    }

    /**
     * @return boolean value indicating whether the block has landed
     */
    boolean hasLanded() {
        return cantMove(0);
    }
    /**
     * @return boolean value indicating whether given position is occupied by another tile
     */
    static boolean isOccuppied(int x, int y) {
        return gameHandler.getOccupied().stream().anyMatch(b->b.getTiles()
                .stream().anyMatch(t->t.getY()==y&&t.getX()==x));
    }

    /**
     * Relocates the graphical representation of this Tile.
     */
    void move() {
        bg.relocate(block.getX() + offsetX, block.getY() + offsetY);
        this.relocate(block.getX() + offsetX, block.getY() + offsetY);
    }

    void fall() {
        this.offsetY += side;
        this.move();
    }

    void removeFrom(Pane pane) {
        pane.getChildren().removeAll(bg, this);
    }

    void setOffset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * Connects the tile to a block
     * @param block block that the tile should be assigned to
     */
    private void setBlock(Block block) {
        this.block = block;
        bg.setFill(block.getColor());
        bg.setX(block.getX() + offsetX);
        bg.setY(block.getY() + offsetY);
    }

    Tile copy() {
        Tile newTile = new Tile(offsetX, offsetY, block);
        newTile.bg = bg;
        return newTile;
    }

    int getX() {
        return this.block.getX() + offsetX;
    }

    int getY() {
        return this.block.getY() + offsetY;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    Rectangle getBg() {
        return bg;
    }

    public Block getBlock() {
        return block;
    }
}
