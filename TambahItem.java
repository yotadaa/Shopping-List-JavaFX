package ShoppingList;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public final class TambahItem extends Pane {
    private final double width = 800/1.5;
    private final double height = 40;
    private String itemName;
    private double itemPrice;
    public final TextField nameTag, priceTag, priceTag2;
    public Button tambah;
    public Checkbox checkbox;
    public boolean select = false;
    
    public TambahItem(double x, double y) {
        Rectangle container = new Rectangle(width, height);
        container.setFill(Color.WHITE);
        container.setArcWidth(7); 
        container.setArcHeight(7);
        
        checkbox = new Checkbox();
        checkbox.setLayoutX(10);
        checkbox.setLayoutY(13);
        
        DropShadow shadow = new DropShadow();
        shadow.setRadius(3.0); // Adjust the shadow radius as needed
        shadow.setColor(Color.GRAY);
        this.setEffect(shadow);
        
        checkbox.setEffect(shadow);
        
        nameTag = new TextField();
        setTag(nameTag, "Nama Barang",  50,6, 10);
        
        priceTag = new TextField();
        priceTag2 = new TextField();
        setTag(priceTag, "Qty", 220,6, 3);
        setTag(priceTag2, "Harga", 286,6, 7);
        
        tambah = new Button("Tambah");
        tambah.setLayoutX(800/1.8+23);
        tambah.setLayoutY(8);
        
        
        getChildren().addAll(container, nameTag, priceTag, priceTag2, tambah, checkbox);
        
        this.setLayoutX(x);
        this.setLayoutY(y);
    }
    
    public void setTag(TextField tag, String txt, double xx, double yy, int width) {
        tag.setFont(Font.font("Calibri", 16));
        tag.setPromptText(txt);
        tag.setLayoutY(yy);
        tag.setLayoutX(xx);
        tag.setPrefColumnCount(width);
    }
    
}
