package project;

import com.mysql.cj.conf.StringProperty;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class MyWishListController {

    @FXML
    private TableView<Wish> wishList_table;

    @FXML
    private TableColumn<Wish, String> cName;
   
    @FXML
    private TableColumn<Wish, String> conCol;
    
    @FXML
    private TableColumn<Wish, String> cDesc;
    @FXML
    private TableColumn<Wish, String> cPrice;
    
    @FXML
    private TextField balanceField;
    
    private DataInputStream in;
    private DataOutputStream out;

     private ObservableList<Wish> wishList = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
     
     @FXML
    public void initialize () { 
        
         try {
            // Connect to server socket
            in = project.getIn();
            out = project.getOut();

            // Send request to server for friend list
            out.writeUTF("WISHLIST");

            // Receive friend list data from server
            List<Wish> data = new ArrayList<>();
            int rowCount = in.readInt();
            for (int i = 0; i < rowCount; i++) {
                String name = in.readUTF();
                 String price = in.readUTF(); 
                 String desc = in.readUTF();
                 String cont = in.readUTF();
                 
                
                data.add(new Wish(name , price , desc , cont));
            }

            // Update UI with friend list data
            Platform.runLater(() -> {
                wishList.addAll(data);
                cName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
                cPrice.setCellValueFactory(cellData -> cellData.getValue().PriceProperty());
                cDesc.setCellValueFactory(cellData -> cellData.getValue().DescProperty());
                conCol.setCellValueFactory(cellData -> cellData.getValue().ContProperty());
                wishList_table.setItems(wishList);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }}
     
    
    
    
    
    @FXML
        private void Homewish(ActionEvent event) throws IOException {

        Parent table = FXMLLoader.load(getClass().getResource("Mainpage.fxml"));
        Scene scene =new Scene(table);
    
    // this is to get the stage information 
    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
}
    
}


class Wish {

    private final String itemName;
     private final String Price; 
     private final String Desc; 
     private final String Cont;
     
     

    public Wish(String itemName , String Price , String Desc , String Cont   ) {
        this.itemName = itemName;
        this.Price = Price; 
        this.Desc = Desc;
        this.Cont= Cont;
    }

    public String getitemName() {
        return itemName;
    }
    
      public String getPrice() {
        return Price;
    }
      
       public String getDesc() {
        return Desc;
    }
      
          public String getCont() {
        return Cont;
    }
    

    public SimpleStringProperty itemNameProperty() {
        return new SimpleStringProperty(itemName);
    }

    
    
    public SimpleStringProperty PriceProperty() {
        return new SimpleStringProperty(Price);
    }
    
    
      public SimpleStringProperty DescProperty() {
        return new SimpleStringProperty(Desc);
    }
      
        public SimpleStringProperty ContProperty() {
        return new SimpleStringProperty(Cont);
    }

   
}
