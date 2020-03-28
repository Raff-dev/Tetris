package Mechanics;

import Display.Colors;
import Display.Game;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.*;

import static DLC.WATIFY.watBlock;
import static Display.Window.gameHandler;
import static javafx.scene.paint.Color.*;

public class Tile extends BorderPane {
    public static int side = 50;
    private Rectangle bg = new Rectangle(side, side);
    private Block block;
    private int offsetX, offsetY;
    private Color watColor;

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
        return gameHandler.getOccupied().stream().anyMatch(b->b.getTiles()
                .stream().anyMatch(t->t.getY()==y&&t.getX()==x));
    }

    void move() {
        bg.relocate(block.getX() + offsetX, block.getY() + offsetY);
        this.relocate(block.getX() + offsetX, block.getY() + offsetY);
    }

    void fall() {
        this.offsetY += side;
        this.move();
    }

    boolean removeFrom(Pane pane) {
        pane.getChildren().removeAll(bg, this);
        return true;
    }

    void setOffset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

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

    public static void setSide(int side) {
        Tile.side = side;
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

    public Rectangle getBg() {
        return bg;
    }

    public Block getBlock() {
        return block;
    }
}
