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

public class SideBar extends VBox {
    private static int side = Tile.side;
    private static int WIDTH = Window.WIDTH - Game.WIDTH;
    private static int HEIGHT = Window.HEIGHT - Game.HEIGHT;
    private Hashtable<String, BarBox> boxes = new Hashtable<>();

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

    public void setValues(int score, int lines, int level) {
        boxes.get("Score").content.setVal(score);
        boxes.get("Lines").content.setVal(lines);
        boxes.get("Level").content.setVal(level);
    }

    public void setNextBlock(Block block) {
        boxes.get("Next").content.setNextBlock(block);
    }

    public Block getNextBlock() {
        return boxes.get("Next").content.nextBlock;
    }

    public void setLevelText(int level){
       if(boxes.containsKey("Level")) boxes.get("Level").content.setVal(level);
    }

    //----------------------------------------------------------
    public static class BarBox extends VBox {
        Text text = new Text();
        BoxContent content = new BoxContent();

        BarBox(String name) {
            setAlignment(Pos.CENTER);

            text.setText(name);
            text.setFont(Font.font(20));
            getChildren().addAll(text, content);
        }

        //----------------------------------------------------------
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

            void setNextBlock(Block nextBlock) {
                while (nextBlock.getBlockType().getRotation() != 0) nextBlock.rotate();
                Block.BlockType blockType = nextBlock.getBlockType();
                int width = blockType.width();
                int height = blockType.height();
                Color color = nextBlock.getColor();
                if (this.nextBlock != null) this.nextBlock.removeFrom(blockPane);
                this.nextBlock =  new Block((int) (WIDTH * 0.5 + width * 0.5), (int) (side * 1.5 +height * 0.5), blockType, color);
                this.nextBlock.showOn(blockPane);
            }
        }
    }

}
