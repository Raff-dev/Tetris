package Display;

import Mechanics.Game;
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

/**
 * SideBar class is responsible for the display of game state,
 * which includes graphical display of score, lines that had been cleared
 * by the player, as well as the current game difficulty.
 */
public class SideBar extends VBox {
    private static int side = Tile.side;
    private static int WIDTH = Window.WIDTH - Game.WIDTH;
    private Hashtable<String, BarBox> boxes = new Hashtable<>();

    /**
     * Initializes the sidebar and adds all the needed items.
     */
    void init() {
        getChildren().clear();
        setProperties();
        String[] boxesText = new String[]{"Next", "Score", "Lines", "Level"};
        for (String s : boxesText) boxes.put(s, new BarBox(s));
        getChildren().addAll(boxes.values());
    }

    private void setProperties() {
        setTranslateX(Game.WIDTH);
        setTranslateY(50);
        setSpacing(25);
    }

    /**
     * updates text values
     * @param score count of points that the player had obtained
     * @param lines count of lines that had been cleaned
     * @param level game difficulty level to be set on label
     */
    public void setValues(int score, int lines, int level) {
        boxes.get("Score").content.setVal(score);
        boxes.get("Lines").content.setVal(lines);
        boxes.get("Level").content.setVal(level);
    }

    public void setNextBlock(Block block) {
        boxes.get("Next").content.setNextBlock(block);
    }

    Block getNextBlock() {
        return boxes.get("Next").content.nextBlock;
    }

    public void setLevelText(int level) {
        if (boxes.containsKey("Level")) boxes.get("Level").content.setVal(level);
    }

    /**
     * BarBox helps organise the sidebar contents.
     * It consists of background and content label.
     */
    public static class BarBox extends VBox {
        Text text = new Text();
        BoxContent content = new BoxContent();

        BarBox(String name) {
            setAlignment(Pos.CENTER);

            text.setText(name);
            text.setFont(Font.font(20));
            getChildren().addAll(text, content);
        }

        /**
         * BoxContent helps organise the sidebar contents.
         * It contains the value of a given sidebar object.
         */
        static class BoxContent extends StackPane {
            Rectangle bg = new Rectangle(WIDTH, side * 3);
            Text val = new Text();
            Pane blockPane = new Pane();
            Block nextBlock;

            BoxContent() {
                bg.setFill(Color.BLACK);
                bg.setOpacity(0.2);
                val.setFont(Font.font(30));
                val.setFill(Color.WHITE);
                setAlignment(Pos.CENTER);
                getChildren().addAll(bg, val, blockPane);
            }

            void setVal(int val) {
                this.val.setText(Integer.toString(val));
            }

            /**
             * Changes the display of next Tetromino (the Tetris block)
             * that has yet to come.
             * @param nextBlock the block that should be displayed on the sidebar.
             */
            void setNextBlock(Block nextBlock) {
                while (nextBlock.getBlockType().getRotation() != 0) nextBlock.rotate();
                Block.BlockType blockType = nextBlock.getBlockType();
                int width = blockType.width();
                int height = blockType.height();
                if (this.nextBlock != null) this.nextBlock.removeFrom(blockPane);
                this.nextBlock = nextBlock;
                nextBlock.setX((int) (WIDTH * 0.5 + width * 0.5));
                nextBlock.setY((int) (side * 1.5 + height * 0.5));
                this.nextBlock.showOn(blockPane);
            }
        }
    }
}
