package ShoppingList;

import java.util.Collections;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.scene.control.Button;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

public class App extends Application{
    
    private long lastClickTime = 0;
    private final long doubleClickTimeThreshold = 300;
    private boolean nameAsc = false;
    private boolean priceAsc = false;
    private boolean qtyAsc = false;

    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        //deklarasi
        boolean selectAll = false;
        Label totalSumLabel = new Label("Total harga");
        Label checkedSumLabel = new Label("Item yang dipilih");
        Label totalSum = new Label("Rp. 0");
        Label checkedSum = new Label("Rp. 0");
        ArrayList<Item> items = new ArrayList<>();
        VBox verticalContainer = new VBox();
        java.io.File save = new java.io.File("history.txt");
        Font labelFont = Font.font("Calibri", FontWeight.BOLD, 16);
        double width = 800, height = 600;
        
        
        //init
        //Algoritma dimulai di sini
        Scanner input = new Scanner(save);
        if (save.exists()) {
            if (save.length() != 0) {
                //Ini adalah algoritma untuk membaca riwayat sebelumnya.
                while (input.hasNext()) {
                    String out = input.nextLine();
                    
                    String[] originalArray = out.split(" ");
                    String itemId = originalArray[0];
                    boolean state = Boolean.parseBoolean(originalArray[1]);
                    int qty = Integer.parseInt(originalArray[originalArray.length-2]);
                    double itemPrice = Double.parseDouble(originalArray[originalArray.length-1]);
                    String itemName = "";
                    
                    
                    for (int i = 2; i < originalArray.length-2;i++) {
                        itemName += originalArray[i]+" ";
                    }
                    
                    insert(items, verticalContainer, checkedSum, totalSum, itemName, qty, itemPrice, state);
                }
                double sums = getTotal(items);
                changeLabel(checkedSum, totalSum,updateCheckedSum(items), sums );
            } 
        }
        
        //src image
        Image iconImage = new Image(getClass().getResourceAsStream("assets/check.png"));

        //fitur cari
        double cariWidth = width/1.5;
        double initX = width/2 - cariWidth / 2;
        double cariHeight = 35;
        Rectangle searchContainer = new Rectangle(cariWidth, cariHeight);
        searchContainer.setArcWidth(10); 
        searchContainer.setArcHeight(10);
        searchContainer.setFill(Color.LIGHTGRAY);
        searchContainer.setLayoutX(width/2 - cariWidth / 2);
        searchContainer.setLayoutY(10);
        SearchIcon searchIcon = new SearchIcon();
        searchIcon.setLayoutY(7);
        searchIcon.setLayoutX(width/2 - cariWidth / 2 +  width/1.5 - searchIcon.size*2);
        searchIcon.setOnMouseEntered(e -> {
            searchIcon.setCursor(Cursor.HAND);
            searchIcon.setColor(Color.rgb(0,0,0,0.1));
        });
        searchIcon.setOnMouseExited(e -> {
            searchIcon.setColor(Color.LIGHTGRAY);
        });
        searchIcon.setOnMouseClicked(e -> {
            searchIcon.requestFocus();
        });
        TextField cariField = new TextField();

        cariField.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: black;" +
            "-fx-prompt-text-fill: gray;" + 
            "-fx-caret-color: black;" //
        );
        
        cariField.setFont(Font.font("Calibri", 16));
        cariField.setLayoutX(width/2 - cariWidth / 2 + 10);
        cariField.setLayoutY(13);
        cariField.setPrefColumnCount(30);
        cariField.setPromptText("Cari item");
        
        //status area
        // bagian kode ini membuat status data saaat ini, tentang total dan item yang dipilih.
        
        Rectangle statusArea = new Rectangle( cariWidth, cariHeight*2);
        statusArea.setLayoutX(initX);
        statusArea.setLayoutY(55);
        statusArea.setArcWidth(7); 
        statusArea.setArcHeight(7);
        statusArea.setFill(Color.WHITE);
        DropShadow statusAreaShadow = new DropShadow();
        statusAreaShadow.setRadius(3.0); // Adjust the shadow radius as needed
        statusAreaShadow.setColor(Color.GRAY);
        statusArea.setEffect(statusAreaShadow);
        
        totalSumLabel.setLayoutX(initX+10);
        totalSumLabel.setLayoutY(63);
        totalSumLabel.setFont(labelFont);
        checkedSumLabel.setLayoutX(initX+10);
        checkedSumLabel.setLayoutY(95);
        checkedSumLabel.setFont(labelFont);
        totalSum.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        checkedSum.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        totalSum.setLayoutY(63);
        totalSum.setFont(labelFont);
        totalSum.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            double labelWidth = newBounds.getWidth();
            totalSum.setLayoutX((width-initX) - labelWidth - 10);
        });
        checkedSum.setLayoutY(95);
        checkedSum.setFont(labelFont);
        checkedSum.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            double labelWidth = newBounds.getWidth();
            checkedSum.setLayoutX((width-initX) - labelWidth - 10);
        });
        
        
        verticalContainer.setSpacing(10);
        verticalContainer.setPadding(new Insets(1,1,30,1));
        
        
        TambahItem tambah = new TambahItem(initX, 135);
        tambah.checkbox.setOnMouseClicked(e -> {
            tambah.select = !tambah.select;
            
            for (Item item : items) {
                item.state = tambah.select;
                item.checkbox.setChecked(item.state);
            }
            
            double sums = updateCheckedSum(items);
            checkedSum.setText("Rp. "+formatNumber(sums, 2));
            try {
                saveHistory(items);
            } catch (IOException ex) {
            }
        });
        
        tambah.checkbox.setOnMouseEntered(e -> tambah.setCursor(Cursor.HAND));
        
        Tooltip.install(tambah.checkbox, new Tooltip("Pilih semua"));
        tambah.tambah.setOnMouseClicked(e -> {
            if (!"".equals(tambah.nameTag.getText()) && tambah.priceTag2.getText() != "" && tambah.priceTag.getText() != "") {
                try {
                    double pt = Double.parseDouble(tambah.priceTag2.getText());
                    int qty = Integer.parseInt(tambah.priceTag.getText());
                    insert(items, verticalContainer, checkedSum, totalSum, tambah.nameTag.getText(), qty, pt, false);

                    tambah.nameTag.clear();tambah.priceTag.clear();tambah.priceTag2.clear();
                } catch (NumberFormatException er) {
                }
            }
            try {
                saveHistory(items);
            } catch (IOException ex) {
            }
        });
        
        ScrollPane scrollPane = new ScrollPane(verticalContainer);
        scrollPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, null)));
        scrollPane.setPrefSize(width/1.35+20, 380);
        scrollPane.setLayoutX(width/2 - (width/1.35+10)/2);
        scrollPane.setLayoutY(180);
        
        cariField.setOnKeyPressed(e -> {
            String key = cariField.getText();
            if (!"".equals(key)) {
                for (Item item : items) {
                    if (!(item.itemName).toUpperCase().contains(key.toUpperCase())) {
                        verticalContainer.getChildren().remove(item);
                    } else {
                        if (!verticalContainer.getChildren().contains(item)) {
                            verticalContainer.getChildren().add(item);
                        }
                    }
                }
            } else {
                for (Item item : items) {
                    if (verticalContainer.getChildren().contains(item)) {
                        verticalContainer.getChildren().remove(item);
                    }
                }
                for (Item item : items) {
                    if (!verticalContainer.getChildren().contains(item)) {
                        verticalContainer.getChildren().add(item);
                    }
                }
            }
        });
        
        Pane creditMaster = new Pane();
        creditMaster.setPrefHeight(100);
        creditMaster.setPrefWidth(100);
        
        Rectangle creditBg = new Rectangle(0,0,300,200);
        creditBg.setArcWidth(7); 
        creditBg.setArcHeight(7);
        creditBg.setFill(Color.WHITE);
        DropShadow shadow = new DropShadow();
        shadow.setRadius(10.0); // Adjust the shadow radius as needed
        shadow.setColor(Color.GRAY);
        creditBg.setEffect(shadow);
        
        VBox creditVertical = new VBox();
        Label dev = new Label("Mukhtada Billah Nasution");
        dev.setFont(labelFont);
        Label nim = new Label("F1E122037");
        nim.setFont(labelFont);
        Label prodi = new Label("Sistem Informasi 2022");
        prodi.setFont(labelFont);
        Button close = new Button("OK");
        close.setFont(labelFont);
        
        creditVertical.getChildren().addAll(dev, nim, prodi, close);
        creditVertical.setAlignment(Pos.CENTER);
        creditVertical.setSpacing(20);
        creditVertical.setPadding(new Insets(20));
        creditVertical.setPrefHeight(200);
        creditVertical.setPrefWidth(300);
        
        creditMaster.getChildren().addAll(creditBg, creditVertical);
        
        creditMaster.setLayoutX(width/2 - 150);
        creditMaster.setLayoutY(width/2 - 200);
        
        ImageView info = new ImageView(new Image(getClass().getResourceAsStream("assets/info.png")));
        info.setOnMouseEntered( e -> {
            info.setCursor(Cursor.HAND);
            info.setOpacity(0.7);
        });
        info.setOnMouseExited( e -> info.setOpacity(1));
        
        info.setFitHeight(35);
        info.setFitWidth(35);
        info.setLayoutX(10);
        info.setLayoutY(height-80);
        info.setEffect(shadow);
        
        ImageView trashbin = new ImageView(new Image(getClass().getResourceAsStream("assets/trashbin.png")));
        Tooltip tooltip = new Tooltip("Reset");
        Tooltip.install(trashbin, tooltip);
        Tooltip.install(info, new Tooltip("Info"));
        
        
        trashbin.setOnMouseEntered( e -> {
            trashbin.setCursor(Cursor.HAND);
            trashbin.setOpacity(0.7);
        });
        trashbin.setOnMouseExited( e -> trashbin.setOpacity(1));
        
        trashbin.setFitHeight(35);
        trashbin.setFitWidth(35);
        trashbin.setLayoutX(10);
        trashbin.setLayoutY(height-120);
        trashbin.setEffect(shadow);
        
        Pane resetConfiguration = new Pane();
        resetConfiguration.setPrefHeight(100);
        resetConfiguration.setPrefWidth(100);
        
        Rectangle resetBg = new Rectangle(0,0,300,150);
        resetBg.setArcWidth(7); 
        resetBg.setArcHeight(7);
        resetBg.setFill(Color.WHITE);
        resetBg.setEffect(shadow);
        
        VBox resetV = new VBox();
        Label resetConf = new Label("Konfirmasi Reset");
        HBox resetH = new HBox();
        Button yes = new Button("Ya");
        yes.setPrefWidth(50);
        Button no = new Button("Tidak");
        no.setPrefWidth(50);
        resetH.getChildren().addAll(no,yes);
        resetH.setSpacing(50);
        resetH.setPrefWidth(200);
        resetH.setAlignment(Pos.CENTER);
        resetV.getChildren().addAll(resetConf, resetH);
        resetV.setAlignment(Pos.CENTER);
        resetV.setPrefWidth(300);
        resetV.setPrefHeight(150);
        resetV.setSpacing(20);
        
        resetConfiguration.getChildren().addAll(resetBg, resetV);
        resetConfiguration.setLayoutX(width/2 - 150);
        resetConfiguration.setLayoutY(width/2 - 180);
        
        MenuButton nameSort = new MenuButton("A-Z", "Sort by name");
        MenuButton priceSort = new MenuButton("Rp. A", "Sort by price");
        MenuButton qtySort = new MenuButton("Qty A", "Sort by qty");
        
        nameSort.setOnAction((e) -> {
            this.nameAsc = !this.nameAsc;
            sortByName(items, verticalContainer, this.nameAsc);
            nameSort.setText(this.nameAsc ? "A-z" : "z-A");
        });
        
        priceSort.setOnAction(e -> {
            this.priceAsc = !this.priceAsc;
            sortByPrice(items, verticalContainer, this.priceAsc);
            priceSort.setText(this.priceAsc ? "Rp. A" : "Rp. D");
        });
        qtySort.setOnAction(e -> {
            this.qtyAsc = !this.qtyAsc;
            sortByQty(items, verticalContainer, this.qtyAsc);
            qtySort.setText(this.qtyAsc ? "Qty A" : "Qty D");
        });
        
        nameSort.setLayoutX(720);
        priceSort.setLayoutX(720);
        qtySort.setLayoutX(720);
        
        nameSort.setLayoutY(180);
        priceSort.setLayoutY(215);
        qtySort.setLayoutY(250);
        
        //controller
        Pane root = new Pane();
        root.requestFocus();
        root.getChildren().addAll(
                searchContainer, 
                searchIcon, 
                cariField, 
                statusArea, 
                totalSumLabel,
                checkedSumLabel, 
                totalSum,
                checkedSum,
                tambah,
                scrollPane,
                info,
                trashbin,
                nameSort, priceSort, qtySort
        );
        
        trashbin.setOnMouseClicked( e -> {
            if (!root.getChildren().contains(resetConfiguration) && !root.getChildren().contains(creditMaster)) 
                root.getChildren().add(resetConfiguration);
        });
        info.setOnMouseClicked( e-> {
            if (!root.getChildren().contains(creditMaster) && !root.getChildren().contains(resetConfiguration)) 
                root.getChildren().add(creditMaster);
        });
        no.setOnMouseClicked( e -> {
            if (root.getChildren().contains(resetConfiguration))
                root.getChildren().remove(resetConfiguration);
        });
        yes.setOnMouseClicked( e -> {
            if (root.getChildren().contains(resetConfiguration)) {
                for (Item item : items) {
                    verticalContainer.getChildren().remove(item);
                    items.set(items.indexOf(item), null);
                    checkedSum.setText("Rp. 0");
                    totalSum.setText("Rp. 0");
                }
                root.getChildren().remove(resetConfiguration);
            }
            items.removeIf(pane -> pane == null);
            try {
                saveHistory(items);
            } catch (IOException ex) {
            }
        });
        
        close.setOnMouseClicked( e -> {
            if (root.getChildren().contains(creditMaster))
                root.getChildren().remove(creditMaster);
        });
        
        root.setOnMouseClicked( e -> root.requestFocus());
        
        Scene scene = new Scene(root, width, height);
        primaryStage.getIcons().add(iconImage);
        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(height);
        primaryStage.setMaxWidth(width);
        primaryStage.setMaxHeight(height);
        primaryStage.setTitle("Daftar Belanja");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private double updateCheckedSum(ArrayList<Item> items) {
        double sum = 0;
        for (Item item : items) {
            if (item.state) {
                sum += (item.itemPrice*item.itemQty);
            }
        }
        
        return sum;
    }
    
    public static void changeLabel(Label checkedSum, Label totalSum, double check, double total) {
        checkedSum.setText("Rp. "+formatNumber(check, 2));
        totalSum.setText("Rp. "+formatNumber(total, 2));
    }
    
    private double getTotal(ArrayList<Item> items) {
        double sum = 0;
        for (Item item : items) {
            sum += (item.itemPrice*item.itemQty);
        }
        
        return sum;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public static void saveHistory(ArrayList<Item> items) throws java.io.IOException {
        String filePath = "history.txt";
        File file = new java.io.File("history.txt");
        if (file.exists()) {
            try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
        PrintWriter output = new PrintWriter(file);
        if (items.size() == 0) {
            output.print("");
            output.close();
            return;
        }
        
        for (Item item : items) {
            output.print(item.id+" ");
            output.print(item.state+" ");
            output.print(item.itemName+" ");
            output.print(item.itemQty+" ");
            output.println(item.itemPrice+" ");
        }
        
        output.close();

    }
    
    public static void changeSave(ArrayList<Item> items) throws IOException {
        File file = new File("history.txt");
        if (file.exists()) {
            file.delete();
        }
    }
    
    public void insert(ArrayList<Item> items, VBox verticalContainer, Label checkedSum, Label totalSum,String itemName, int qty, double itemPrice, boolean state) {
        
        Pattern validEditingState = Pattern.compile("^[0-9]*$");
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (validEditingState.matcher(newText).matches()) {
                return change;
            } else {
                return null;
            }
        };

        StringConverter<Number> converter = new NumberStringConverter();

        TextFormatter<Number> textFormatter = new TextFormatter<>(converter, 0, filter);
        
        
        Item item = new Item(itemName, qty, itemPrice);
        item.state = state;
        item.checkbox.setChecked(item.state);
        items.add(item);
        item.priceTag3.setText("Rp. "+formatNumber(qty*itemPrice,2));
        item.checkbox.setOnMouseClicked(ev -> {
            item.state = !item.state;
            item.checkbox.setChecked(item.state);
            double sums = updateCheckedSum(items);
            checkedSum.setText("Rp. "+formatNumber(sums, 2));
            try {
                saveHistory(items);
            } catch (IOException ex) {
            }
        });

        item.cross.setOnMouseClicked(ev -> {
            verticalContainer.getChildren().remove(item);
            items.set(items.indexOf(item), null);
            items.removeIf(pane -> pane == null);
            double sums = updateCheckedSum(items);
            changeLabel(checkedSum, totalSum, sums, getTotal(items));
            try {
                saveHistory(items);
            } catch (IOException ex) {
            }
        });
        
        item.pen.setOnMouseClicked(ev -> {
            item.container.setFill(!item.editable ? Color.POWDERBLUE : Color.WHITE);
            item.editable = !item.editable;
            item.nameTag.setDisable(!item.editable);
            item.priceTag.setDisable(!item.editable);
            item.priceTag2.setDisable(!item.editable);
            item.pen.setOpacity(item.editable ? 0.5 : 1);
            
            try {
                int cQty = Integer.parseInt(item.priceTag2.getText());
                double cPrice = Double.parseDouble(item.priceTag.getText());
                item.priceTag3.setText("Rp. "+formatNumber(cQty*cPrice, 2));
            } catch (NumberFormatException er) {   
            }
            
            try {
                item.itemPrice = Double.parseDouble(item.priceTag.getText());
            } catch (NumberFormatException ex) {
                item.itemPrice = 0;
                item.priceTag.setText("0");
            }
            
            try {
                item.itemQty = Integer.parseInt(item.priceTag2.getText());
            } catch (NumberFormatException ex) {
                item.itemQty = 0;
                item.priceTag2.setText("0");
                item.priceTag3.setText("Rp. 0");
            }
            
            item.itemName = item.nameTag.getText();  
            changeLabel(checkedSum, totalSum, updateCheckedSum(items), getTotal(items));
            try {
                saveHistory(items);
            } catch (IOException ex) {
            }
            if (item.priceTag2.getText() == "") {
                item.priceTag2.setText("0");
            }
        });
        
        item.setOnMouseClicked(event -> {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastClickTime < doubleClickTimeThreshold) {
                item.container.setFill(!item.editable ? Color.POWDERBLUE : Color.WHITE);
                item.editable = !item.editable;
                item.nameTag.setDisable(!item.editable);
                item.priceTag.setDisable(!item.editable);
                item.priceTag2.setDisable(!item.editable);
                item.pen.setOpacity(item.editable ? 0.5 : 1);

                try {
                    int cQty = Integer.parseInt(item.priceTag2.getText());
                    double cPrice = Double.parseDouble(item.priceTag.getText());
                    item.priceTag3.setText("Rp. "+formatNumber(cQty*cPrice, 2));
                } catch (NumberFormatException er) {   
                }

                try {
                    item.itemPrice = Double.parseDouble(item.priceTag.getText());
                } catch (NumberFormatException ex) {
                    item.itemPrice = 0;
                    item.priceTag.setText("0");
                }

                try {
                    item.itemQty = Integer.parseInt(item.priceTag2.getText());
                } catch (NumberFormatException ex) {
                    item.itemQty = 0;
                    item.priceTag2.setText("0");
                    item.priceTag3.setText("Rp. 0");
                }

                item.itemName = item.nameTag.getText();  
                changeLabel(checkedSum, totalSum, updateCheckedSum(items), getTotal(items));
                try {
                    saveHistory(items);
                } catch (IOException ex) {
                }
                if (item.priceTag2.getText() == "") {
                    item.priceTag2.setText("0");
                }
            }

            lastClickTime = currentTime;
        });

        verticalContainer.getChildren().add(item);
    } 
    
    public static String formatNumber(double number, int decimalPlaces) {
        String pattern = "###,###." + "0".repeat(decimalPlaces);
        DecimalFormat decimalFormat = new DecimalFormat(pattern); 
        return decimalFormat.format(number);
    }
    
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
    
    public static void sortByPrice(ArrayList<Item> items, VBox verticalContainer, boolean priceAsc) {
        if (priceAsc) {
            Collections.sort(items, (item1, item2) -> Double.compare(item1.itemQty*item1.itemPrice, item2.itemQty*item2.itemPrice));
        } else {
            Collections.sort(items, (item1, item2) -> Double.compare(item2.itemQty*item2.itemPrice, item1.itemQty*item1.itemPrice));
        }
        verticalContainer.getChildren().clear();

        for (Item item : items) {
            verticalContainer.getChildren().add(item);
        }
    }
    public static void sortByQty(ArrayList<Item> items, VBox verticalContainer, boolean qtyAsc) {
        if (qtyAsc) {
            Collections.sort(items, (item1, item2) -> Integer.compare(item1.itemQty, item2.itemQty));
        } else {
            Collections.sort(items, (item1, item2) -> Integer.compare(item2.itemQty, item1.itemQty));
        }

        verticalContainer.getChildren().clear();

        for (Item item : items) {
            verticalContainer.getChildren().add(item);
        }
    }
    public static void sortByName(ArrayList<Item> items, VBox verticalContainer, boolean nameAsc) {
        if (nameAsc) {
            Collections.sort(items, (item1, item2) -> item1.itemName.compareTo(item2.itemName));
        } else {
            Collections.sort(items, (item1, item2) -> item2.itemName.compareTo(item1.itemName));
        }

        verticalContainer.getChildren().clear();

        for (Item item : items) {
            verticalContainer.getChildren().add(item);
        }
    }
}

class MenuButton extends Button {
    MenuButton(String name, String tooltip) {
        setText(name);
        setFont(Font.font("Calibri", 8));
        setPrefWidth(30);
        setPrefHeight(30);
        Tooltip.install(this, new Tooltip(tooltip));
    }
}