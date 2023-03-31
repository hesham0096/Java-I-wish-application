package project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class FriendWishController  {

      @FXML
    private TableView<Wish> friendTable;

    @FXML
    private TableColumn<Wish, String> itemCol;
    
     @FXML
    private TableColumn<Wish, String> contCol;

    @FXML
    private TableColumn<Wish, String> priceCol;

    @FXML
    private TableColumn<Wish, String> desCol;

    @FXML
    private Button HomeFriendWish;
     @FXML
    private TextField friendnameField;
     @FXML
    private Label messageLabel;
        
       @FXML
    private TextField contField;

    @FXML
    private Button contBtn;

    @FXML
    private Button refreshBtn;
 
    @FXML
    private Label Label2;
    
    @FXML
    private Button friendBtn;
    private String balance;
   
    private DataInputStream in;
    private DataOutputStream out;
    private String friendName;
    private String response;

     private ObservableList<Wish> wishList = FXCollections.observableArrayList();
     
     @FXML
    private void handleFriendWish(ActionEvent event) throws IOException {
    friendName = friendnameField.getText(); 
    
     try {
    // Connect to server socket
    in = project.getIn();
    out = project.getOut();

    // Send request to server for friend list
    out.writeUTF("FRIENDWISHLIST");
    out.writeUTF(friendName);

    // Receive friend list data from server
     response = in.readUTF();
    if (response.equals("ERROR")) {
        Platform.runLater(() -> {
            messageLabel.setText("you can only see your Friends' Wishes, not all users " );
        });
    } else {
           balance = in.readUTF();
          System.out.println("habiba" + response);
        
        int rowCount = in.readInt();

    if (rowCount == 0) {
        Platform.runLater(() -> {
               
        messageLabel.setText("No wishlist data found for friend " + friendName);
        });
    } else {
        List<Wish> data = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            String name = in.readUTF();
            String price = in.readUTF(); 
            String desc = in.readUTF();
            String cont = in.readUTF();
            
            data.add(new Wish(name , price , desc , cont));
        }

        // Update UI with friend list data
        Platform.runLater(() -> {
            wishList.clear();
            wishList.addAll(data);
            itemCol.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
            priceCol.setCellValueFactory(cellData -> cellData.getValue().PriceProperty());
            desCol.setCellValueFactory(cellData -> cellData.getValue().DescProperty());
            contCol.setCellValueFactory(cellData -> cellData.getValue().ContProperty());
            friendTable.setItems(wishList);
        });
    }}

} catch (IOException e) {
    e.printStackTrace();
}
    }

  @FXML
private void handlContButton(ActionEvent event) { 
    Wish selectedFriend = friendTable.getSelectionModel().getSelectedItem() ;
    String selectedprice = friendTable.getSelectionModel().getSelectedItem().getPrice();
    String selectedtotal = friendTable.getSelectionModel().getSelectedItem().getCont();
    String contribute  = contField.getText();
    
    

    
    if (!contribute.isEmpty()) {
        try {
            
            // Send request to server to add friend
            
            
           int total =  Integer.valueOf(selectedtotal);
            int cont = Integer.valueOf(contribute);
            int price =Integer.valueOf(selectedprice) ;
            int blnc = Integer.valueOf(response) ;
           
            System.out.println ("b " + blnc );
              System.out.println ("y " + cont );
               System.out.println ("total " + total );
               System.out.println ("price " + price );
           
           if ( blnc >= cont ) {
                          
            if(   ( total + cont ) >  price    )
                    {
                            
                         messageLabel.setText("youe exceeded the price for this wish!");
                                   System.out.println("2");

                    }
            
            else if ( price >= (total + cont)) { 
                    int v = blnc - cont ;
                     messageLabel.setText("your balance has been deducted with " + contribute + " $" + "your current balance is " + v + " $" );
                     
                                       System.out.println("3");
                                       
                                       out.writeUTF("CONTRIBUTE");
  
                                        System.out.println(selectedprice);
                                         out.writeUTF(contribute);
                                       out.writeUTF(friendName);
                                       out.writeUTF(selectedFriend.getitemName());
                                       out.writeUTF(selectedprice);

                 }
            
           }
           
           else { messageLabel.setText("your balance isn't enough to make this contribution" + "your current balance is " + response + " $");
                        System.out.println("1"); }


           
            
            
        }
        catch (IOException e) {
            e.printStackTrace();
        }    
        
    }
}

    @FXML
    public void initialize() {}
        
  @FXML     
  private void Refresh (ActionEvent event) throws IOException {

    Parent table = FXMLLoader.load(getClass().getResource("FriendWish.fxml"));
    Scene scene =new Scene(table);
    
    // this is to get the stage information 
    
    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
}
     
    
    
    @FXML
        private void HomeFriendWish(ActionEvent event) throws IOException {

    Parent table = FXMLLoader.load(getClass().getResource("Mainpage.fxml"));
    Scene scene =new Scene(table);
    
    // this is to get the stage information 
    
    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
}

    }

 




    
    

