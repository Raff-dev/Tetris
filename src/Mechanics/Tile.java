package Mechanics;

import Display.Game;
import Display.Window;
import javafx.scene.shape.Rectangle;

public class Tile {
    private static GameHandler gameHandler = Window.gameHandler;
    public static final int side = 40;
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
        boolean xBorder = false, yBorder = false, ocuppied;
        if (dir == 0) {
            yBorder = y + side == Game.HEIGHT;
            ocuppied = isOcuppied(x, y + side);
        } else {
            xBorder = (x + side) * dir == Game.WIDTH || (x + side) * dir == -side;
            ocuppied = isOcuppied(x + dir * side, y);
        }
        return ocuppied || xBorder || yBorder;
    }

    boolean hasLanded() {
        return cantMove(0);
    }

    static boolean isOcuppied(int x, int y) {
        return gameHandler.getOccupied().stream().anyMatch(t -> t.getX() == x && t.getY() == y);
    }

    void move() {
        tile.relocate(block.getX() + offsetX, block.getY() + offsetY);
    }

    void fall() {
        this.offsetY += side;
        this.move();
    }

    void remove() {
        Game.window.getChildren().remove(tile);
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

    int getX() {
        return this.block.getX() + offsetX;
    }

    int getY() {
        return this.block.getY() + offsetY;
    }

    Rectangle getTile() {
        return tile;
    }
}
