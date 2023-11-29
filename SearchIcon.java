package ShoppingList;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class SearchIcon extends Pane {
    public double size = 20;
    private Circle bg;
    
    public SearchIcon() {
        double d = 3;
        bg = new Circle(size,size,size/1.2);
        bg.setFill(Color.LIGHTGRAY);
        Circle c = new Circle(size/1.1,size/1.1,size/d);
        c.setStrokeWidth(2);
        c.setFill(null);
        c.setStroke(Color.BLACK);
        
        Line l = new Line(size+size/2/d, size+size/2/d, size+7, size+7);
        l.setStroke(Color.BLACK);
        l.setStrokeWidth(2);
        
        getChildren().addAll(bg,c, l);
    }
    
    public void setColor(Color c) {
        bg.setFill(c);
    }
    
    
}
