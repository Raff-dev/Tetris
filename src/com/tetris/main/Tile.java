package com.tetris.main;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.util.Random;

public class Tile {
    static final int side = 40;
    private Block block;
    private Rectangle tile;
    private int offsetX, offsetY;
    private Rectangle point;

    public Tile copy(){
        Tile newTile = new Tile(offsetX,offsetY,block);
        newTile.tile =tile;
        newTile.point=point;
        return newTile;
    }

    Tile(int offsetX, int offsetY, Block block) {
        tile = new Rectangle(
                block.getX() + offsetX,
                block.getY() + offsetY,
                side, side);
        this.block = block;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        tile.setFill(block.color);
    }
    public void show(){
        Game.window.getChildren().add(tile);
        //---------------------------
        point = new Rectangle(block.getX() + offsetX,
                block.getY() + offsetY,
                0, 0);

        Color color = Handler.colors.get(new Random().nextInt(Handler.colors.size()));
        point.setStroke(color);
        point.setStrokeWidth(10);
        Game.window.getChildren().add(point);
    }


    public boolean canMove(int dir) {
        int x = getX(), y = getY();
        boolean xBorder = false, yBorder = false, ocuppied;
        if (dir == 0){
            yBorder = y + side == Game.HEIGHT;
            ocuppied = Game.handler.getOccupied().contains(new Point(x,y+side));
        }
        else{
            xBorder = (x + side) * dir == Game.WIDTH || (x + side) * dir == -side;
            ocuppied = Game.handler.getOccupied().contains(new Point(x+side*dir,y));
        }
        return !(ocuppied || xBorder || yBorder);
    }

    public boolean hasLanded() {
        return !canMove(0);
    }

    public void move() {
        point.relocate(block.getX() + offsetX, block.getY() + offsetY);
        tile.relocate(block.getX() + offsetX,block.getY() + offsetY);
    }

    public int getX() {
        return this.block.getX() + offsetX;
    }

    public int getY() {
        return this.block.getY() + offsetY;
    }

    public Node getRect() {
        return this.tile;
    }

    public void setOffset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
}
