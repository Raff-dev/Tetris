package Display;

import Mechanics.Block;
import Mechanics.Tile;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Hashtable;

import static Display.RepetitiveTask.perSecond;
import static Display.Window.game;
import static Display.Window.sideBar;

public class SideBar extends VBox {
    private static int side = Tile.side;
    private static int WIDTH = Window.WIDTH - Game.WIDTH;
    private static int HEIGHT = Window.HEIGHT - Game.HEIGHT;
    private Hashtable<String, BarBox> boxes = new Hashtable<>();

    void init() {
        String[] boxesText = new String[]{"Next", "Score", "Lines", "Level"};
        for (String s : boxesText) boxes.put(s, new BarBox(s));
        setTranslateX(Game.WIDTH);
        setTranslateY(100);
        setSpacing(25);
        Rectangle bg = new Rectangle(WIDTH, HEIGHT);
        bg.setFill(Color.RED);
        bg.setOpacity(0.4);
        Window.window.getChildren().add(this);
        getChildren().addAll(boxes.values());
        getChildren().add(bg);
    }

    public void setValues(int score, int lines, int level) {
        boxes.get("Score").content.setVal(score);
        boxes.get("Lines").content.setVal(lines);
        boxes.get("Level").content.setVal(level);
    }

    public void setNextBlock(Block block) {
        boxes.get("Next").content.setNextBlock(block.getBlockType(), block.getColor());
    }

    public void rot() {
        Pane p = boxes.get("Next").content.blockPane;
        p.setRotate(p.getRotate() + 10);
    }

    //----------------------------------------------------------
    public static class BarBox extends VBox {
        Text text = new Text();
        BoxContent content = new BoxContent();

        BarBox(String name) {
            setAlignment(Pos.CENTER);

            this.text.setText(name);
            getChildren().addAll(text, content);
        }

        //----------------------------------------------------------
        class BoxContent extends StackPane {
            Rectangle bg = new Rectangle(WIDTH, side * 3);
            Text val = new Text();
            Pane blockPane = new Pane();
            Block nextBlock;

            BoxContent() {
                bg.setFill(Color.BLACK);
                bg.setOpacity(0.2);
                val.setText("Sample text");
                val.setFont(Font.font(30));
                val.setFill(Color.WHITE);
                setAlignment(Pos.CENTER);
                RepetitiveTask rt = new RepetitiveTask(true, perSecond(60), () -> sideBar.rot());
                game.addTask("rot", rt, true);
                getChildren().addAll(bg, val, blockPane);
            }

            void setVal(int val) {
                this.val.setText(Integer.toString(val));
            }

            void setNextBlock(Block.BlockType blockType, Color color) {
                if (this.nextBlock != null) this.nextBlock.removeFrom(blockPane);
                this.nextBlock = new Block((int) (WIDTH * 0.5 + blockType.width() * 0.5), (int) (side * 1.5 + blockType.height() * 0.5), blockType, color);
                this.nextBlock.showOn(blockPane);
            }

        }
    }

}
