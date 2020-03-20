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

    public Tile copy() {
        Tile newTile = new Tile(offsetX, offsetY, block);
        newTile.tile = tile;
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

    public boolean canMove(int dir) {
        int x = getX(), y = getY();
        boolean xBorder = false, yBorder = false, ocuppied;
        if (dir == 0) {
            yBorder = y + side == Game.HEIGHT;
            ocuppied =isOcuppied(x,y+side);
        } else {
            xBorder = (x + side) * dir == Game.WIDTH || (x + side) * dir == -side;
            ocuppied = isOcuppied(x + dir * side, y);
        }
        return !(ocuppied || xBorder || yBorder);
    }

    public boolean hasLanded() {
        return !canMove(0);
    }

    public void move() {
        tile.relocate(block.getX() + offsetX, block.getY() + offsetY);
    }

    public void show() {
        Game.window.getChildren().add(tile);
    }

    public void remove() {
        Game.window.getChildren().remove(tile);
    }

    public static boolean isOcuppied(int x, int y) {
        return Game.handler.getOccupied().stream()
                .filter(t -> t.getX() == x && t.getY() == y).count() > 0;
    }

    public int getX() {
        return this.block.getX() + offsetX;
    }

    public int getY() {
        return this.block.getY() + offsetY;
    }

    public Rectangle getRect() {
        return this.tile;
    }

    public void setOffset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void fall() {
        System.out.println(tile.getY()+" "+getY());
        this.offsetY += side;
        this.move();
        System.out.println(tile.getY()+" "+getY());
    }
}
