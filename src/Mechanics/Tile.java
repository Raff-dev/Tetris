package Mechanics;

import Display.Game;
import Display.Window;
import javafx.scene.shape.Rectangle;

public class Tile {
    private static GameHandler gameHandler = Window.gameHandler;
    public static final int side = 40;
    private Block block;


    private Rectangle tile = new Rectangle();
    private int offsetX, offsetY;

    Tile copy() {
        Tile newTile = new Tile(offsetX, offsetY, block);
        newTile.tile = tile;
        return newTile;
    }

    Tile(int offsetX, int offsetY, Block block) {
        tile.setX(block.getX() + offsetX);
        tile.setY(block.getY() + offsetY);
        tile.setWidth(side);
        tile.setHeight(side);
        this.block = block;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        tile.setFill(block.color);
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

    void move() {
        tile.relocate(block.getX() + offsetX, block.getY() + offsetY);
    }

    void fall() {
        this.offsetY += side;
        this.move();
    }

    void show() {
        Game.window.getChildren().add(tile);
    }

    void remove() {
        Game.window.getChildren().remove(tile);
        gameHandler.getOccupied().remove(this);
    }

    static boolean isOcuppied(int x, int y) {
        return gameHandler.getOccupied().stream().anyMatch(t -> t.getX() == x && t.getY() == y);
    }

    void setOffset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    int getX() {
        return this.block.getX() + offsetX;
    }

    int getY() {
        return this.block.getY() + offsetY;
    }

    public Rectangle getTile() {
        return tile;
    }
}
