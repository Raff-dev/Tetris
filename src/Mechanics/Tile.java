package Mechanics;

import Display.Colors;
import Display.Game;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.*;

import static Display.Colors.watBlock;
import static Display.Window.gameHandler;
import static javafx.scene.paint.Color.*;

public class Tile extends BorderPane {
    public static int side = 50;
    private Block block;
    private Rectangle bg = new Rectangle(side, side);
    private int offsetX, offsetY;
    private static boolean isWatified = false;
    private Color watColor;

    Tile(int offsetX, int offsetY, Block block) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        setBlock(block);
        WATinit();
    }

    private HashMap<List<String>, Color> watBlocks = new HashMap<>();


    private void WATinit(){
        VBox textBox = new VBox();
        int index = new Random().nextInt(watBlock.size());
        watColor = Colors.getPalette(Colors.Palette.WAT).get(index);
        for (String info:watBlock.get(index)){
            Text t = new Text(info);
            t.setFont(Font.font(12));
            t.setTextAlignment(TextAlignment.CENTER);
            t.setFill(BLACK);
            t.setTranslateX(Tile.side * 0.5);
            textBox.getChildren().add(t);
        }
        textBox.setAlignment(Pos.TOP_CENTER);
        relocate(block.getX() + offsetX, block.getY() + offsetY);
        setOpacity(0);
        getChildren().add(textBox);
        if (isWatified) WATify();

    }
    public void WATify() {
        isWatified=true;
        FadeTransition ft = new FadeTransition(new Duration(100),this);
        ft.setToValue(1);
        ft.play();
        FillTransition fit = new FillTransition(new Duration(100),bg);
        fit.setToValue(watColor);
        fit.play();

    }
    public void deWATify(){
        isWatified=false;
        FadeTransition ft = new FadeTransition(new Duration(100),this);
        ft.setToValue(0);
        ft.play();
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

    void move() {
        bg.relocate(block.getX() + offsetX, block.getY() + offsetY);
        this.relocate(block.getX() + offsetX, block.getY() + offsetY);
    }

    void fall() {
        this.offsetY += side;
        this.move();
    }

    void removeFrom(Pane pane) {
        pane.getChildren().remove(bg);
        pane.getChildren().remove(this);
        gameHandler.getOccupied().remove(this);
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

    public Rectangle getBg() {
        return bg;
    }

    public Block getBlock() {
        return block;
    }
}
