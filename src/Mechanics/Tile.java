package Mechanics;

import Display.Game;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import static Display.Window.gameHandler;

public class Tile {
    public static int side = 50;
    private Block block;
    private Rectangle tile = new Rectangle(side, side);
    private int offsetX, offsetY;

    Tile(int offsetX, int offsetY, Block block) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        setBlock(block);
    }

    boolean cantMove(int dir) {
        int x = getX(), y = getY();
        boolean xBorder = false, yBorder = false, occupied;
        if (dir == 0) {
            yBorder = y + side == Game.HEIGHT;
            occupied = isOcuppied(x, y + side);
        } else {
            xBorder = (x + side) * dir == Game.WIDTH || (x + side) * dir == -side;
            occupied = isOcuppied(x + dir * side, y);
        }
        return occupied || xBorder || yBorder;
    }

    boolean hasLanded() {
        return cantMove(0);
    }

    static boolean isOcuppied(int x, int y) {
        return gameHandler.getOccupied().stream().anyMatch(t -> t.getX() == x && t.getY() == y);
    }

    public void move() {
        tile.relocate(block.getX() + offsetX, block.getY() + offsetY);
    }

    void fall() {
        this.offsetY += side;
        this.move();
    }

    void removeFrom(Pane pane) {
        pane.getChildren().remove(tile);
        gameHandler.getOccupied().remove(this);
    }

    void setOffset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    private void setBlock(Block block) {
        this.block = block;
        tile.setFill(block.getColor());
        tile.setX(block.getX() + offsetX);
        tile.setY(block.getY() + offsetY);
    }

    Tile copy() {
        Tile newTile = new Tile(offsetX, offsetY, block);
        newTile.tile = tile;
        return newTile;
    }
    public static void setSide(int side) {
        Tile.side = side;
    }

    public int getX() {
        return this.block.getX() + offsetX;
    }

    int getY() {
        return this.block.getY() + offsetY;
    }

    Rectangle getTile() {
        return tile;
    }
}
