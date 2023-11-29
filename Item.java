package ShoppingList;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public final class Item extends Pane {
    public String id = getCurrentTimeMillis()+"";
    private final double width = 800/1.35;
    private final double height = 40;
    public String itemName;
    public double itemPrice;
    public final TextField nameTag, priceTag, priceTag2;
    public final Label priceTag3;
    public boolean state = false;
    public ImageView cross = new ImageView(new Image(getClass().getResourceAsStream("assets/cross.png")));
    public ImageView pen = new ImageView(new Image(getClass().getResourceAsStream("assets/pen.png")));
    public boolean editable = false;
    public Checkbox checkbox;
    public Rectangle container;
    public int itemQty = 0;
    
    public Item(String name, int qty, double price) {
        this(0, 0, name, qty, price);
    }
    
    
    public long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    
    public Item(double x, double y, String name, int qty, double price) {
        
        this.itemName = name;
        this.itemPrice = price;
        this.itemQty = qty;
        
        container = new Rectangle(width, height);
        container.setFill(Color.WHITE);
        container.setArcWidth(7); 
        container.setArcHeight(7);
        DropShadow shadow = new DropShadow();
        shadow.setRadius(3.0); // Adjust the shadow radius as needed
        shadow.setColor(Color.GRAY);
        this.setEffect(shadow);
        
        checkbox = new Checkbox();
        checkbox.setLayoutX(10);
        checkbox.setLayoutY(12);
        
        nameTag = new TextField();
        priceTag = new TextField();
        priceTag2 = new TextField();
        priceTag3 = new Label("Rp. Harga total");
        
        setTag(nameTag, name,  50,6, 10);
        setTag(priceTag, price+"", 286,6, 6);
        setTag(priceTag2, qty+"", 220,6, 3);
        priceTag3.setFont(Font.font("Calibri", 16));
        priceTag3.setLayoutX(396);
        priceTag3.setLayoutY(10);
        
        
        nameTag.setDisable(!editable);
        priceTag.setDisable(!editable);
        priceTag2.setDisable(!editable);
        
        nameTag.setStyle("-fx-opacity: 1; -fx-text-fill: black;");
        priceTag.setStyle("-fx-opacity: 1; -fx-text-fill: black;");
        priceTag2.setStyle("-fx-opacity: 1; -fx-text-fill: black;");
        priceTag3.setStyle("-fx-opacity: 1; -fx-text-fill: black;");
        
        cross.setFitWidth(20);
        cross.setFitHeight(20);
        cross.setLayoutX(width-30);
        cross.setLayoutY(10);
        cross.setEffect(shadow);
        cross.setOnMouseEntered(e ->  cross.setCursor(Cursor.HAND));
        
        
        pen.setFitWidth(20);
        pen.setFitHeight(20);
        pen.setLayoutX(width-60);
        pen.setLayoutY(10);
        pen.setEffect(shadow);
        pen.setOnMouseEntered(e ->  pen.setCursor(Cursor.HAND));
        pen.setOnMouseClicked(e -> {
            this.editable = !this.editable;
            changeEdit(this.editable);
            pen.setOpacity(!this.editable ? 0.5 : 1);
            this.itemName = nameTag.getText();
            this.itemPrice = Double.parseDouble(priceTag.getText());
        });
        
        checkbox.setOnMouseClicked(e -> this.state = !this.state);
        
        getChildren().addAll(container, checkbox, nameTag, priceTag,priceTag2,priceTag3, pen, cross);
        
        this.setLayoutX(x);
        this.setLayoutY(y);
    }
    
    public void changeEdit(boolean state) {
        nameTag.setDisable(state);
        priceTag.setDisable(state);
    }
    
    public void setTag(TextField tag, String txt, double xx, double yy, int width) {
        tag.setFont(Font.font("Calibri", 16));
        tag.setText(txt);
        tag.setLayoutY(yy);
        tag.setLayoutX(xx);
        tag.setPrefColumnCount(width);
    }
    
}
