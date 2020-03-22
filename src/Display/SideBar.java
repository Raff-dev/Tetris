package Display;

import Mechanics.Block;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Hashtable;

public class SideBar extends VBox {
    private int WIDTH = Window.WIDTH - Game.WIDTH;
    private int HEIGHT = Window.HEIGHT - Game.HEIGHT;
    private Hashtable<String, BarBox> boxes = new Hashtable<>();

    SideBar() {
        String[] boxesText = new String[]{"Next", "Score", "Lines", "Level"};
        for (String s : boxesText) boxes.put(s, new BarBox(s));
        getChildren().addAll(boxes.values());
        setTranslateX(Game.WIDTH + WIDTH * 0.5);
        Window.window.getChildren().add(this);
    }

    public void setVal(String name, int val) {
        boxes.get(name).setVal(val);
    }

    public void setNextBlock(Block block) {
        boxes.get("Next").setNextBlock(block);
    }

    public class BarBox extends VBox {
        Text text = new Text();
        BoxContent content = new BoxContent();
        Block nextBlock;

        BarBox(String name) {
            this.text.setText(name);
            setSpacing(5);
            getChildren().addAll(text, content);
        }

        void setVal(int val) {
            this.content.val.setText(Integer.toString(val));
        }

        void setNextBlock(Block nextBlock) {
            if (this.nextBlock != null) this.nextBlock.remove();
            this.nextBlock = new Block(
                    (int) (Game.WIDTH + WIDTH * 0.5), 50, nextBlock.getBlockType(), nextBlock.getColor());
            this.nextBlock.show();
        }

        class BoxContent extends StackPane {
            Rectangle bg = new Rectangle(WIDTH, HEIGHT);
            Text val = new Text();

            BoxContent() {
                bg.setFill(Color.BLACK);
                bg.setOpacity(0.2);
            }

        }
    }

}
