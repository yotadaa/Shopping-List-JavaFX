
package ShoppingList;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;


public final class Checkbox extends Pane{
    private double size = 15;
    private boolean state = true;
    private final Rectangle square;
    private final Line line1;
    private final Line line2;
    
    public Checkbox() {
        square = new Rectangle(0,0,size,size);
        
        line1 = new Line(size/20, size / 1.8, size/2.5, size-size/10);
        line2 = new Line(size/2.5, size-size/10, size, 0);
        line1.setStrokeWidth(4);
        line2.setStrokeWidth(4);
        setCheckColor(Color.rgb(56,215,38));
        setSquareColor(Color.rgb(255,255,255));
        
        square.setOnMouseClicked( e -> setChecked());
        line1.setOnMouseClicked( e -> setChecked());
        line2.setOnMouseClicked( e -> setChecked());
        DropShadow shadow = new DropShadow();
        shadow.setRadius(3.0); // Adjust the shadow radius as needed
        shadow.setColor(Color.GRAY);
        this.setEffect(shadow);
        
        setChecked();
        
        getChildren().addAll(square,line1,line2);
    }
    
    public void changeState() {
        this.state = !this.state;
    }
    
    public void setChecked(boolean value) {
        line1.setVisible(value);
        line2.setVisible(value);
    }
    
    public void setChecked() {
        changeState();
        line1.setVisible(state);
        line2.setVisible(state);
    }
    
    public void changeSize(double s) {
        square.setWidth(s);
        square.setHeight(s);
    }
    
    public void setCheckColor(Color c) {
        line1.setStroke(c);
        line2.setStroke(c);
    }
    
    public void setSquareColor(Color c) {
        square.setFill(c);
        square.setStroke(Color.BLACK);
    }
    
    public void setSize(double s) {
        this.size = s;
        changeSize(s);
        
    }
    
    public double getSize() {
        return this.size;
    }
    
}
