/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class MainpageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

 @FXML
     private void gotoWishlist(ActionEvent event) throws IOException {

    Parent table = FXMLLoader.load(getClass().getResource("MyWishList.fxml"));
    Scene scene =new Scene(table);
    
    // this is to get the stage information 
    
    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
}
     @FXML
        private void gotoItems(ActionEvent event) throws IOException {

    Parent table = FXMLLoader.load(getClass().getResource("Items.fxml"));
    Scene scene =new Scene(table);
    
    // this is to get the stage information 
    
    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
}
        
        @FXML
      private void gotoFriends(ActionEvent event) throws IOException {

    Parent table = FXMLLoader.load(getClass().getResource("FriendList.fxml"));
    Scene scene =new Scene(table);
    
    // this is to get the stage information 
    
    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
}
        
    ////Go to request 
        @FXML
        private void logout(ActionEvent event) throws IOException {

    Parent table = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
    Scene scene =new Scene(table);
    
    // this is to get the stage information 
    
    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
}
        
       @FXML
        private void btnRequests(ActionEvent event) throws IOException {

    Parent table = FXMLLoader.load(getClass().getResource("Request.fxml"));
    Scene scene =new Scene(table);
    
    // this is to get the stage information 
    
    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
}


 @FXML
        private void btnFriendwish(ActionEvent event) throws IOException {

    Parent table = FXMLLoader.load(getClass().getResource("FriendWish.fxml"));
    Scene scene =new Scene(table);
    
    // this is to get the stage information 
    
    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
}


 @FXML
        private void ChargeBalance (ActionEvent event) throws IOException {

    Parent table = FXMLLoader.load(getClass().getResource("Balance.fxml"));
    Scene scene =new Scene(table);
    
    // this is to get the stage information 
    
    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
}


    
    
}
