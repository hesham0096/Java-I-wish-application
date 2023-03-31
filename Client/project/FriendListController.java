/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import com.mysql.cj.conf.StringProperty;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
public class FriendListController  {
     private DataInputStream in;
    private DataOutputStream out;
    @FXML
    private TableView<Friend> friendListTable;
    @FXML
    private TableColumn<Friend,String> friendEmail;

    @FXML
    private TableColumn<Friend, String> friendNameCol;
    
    @FXML
    private TextField friendNameField;

    @FXML
    private Button addButton;
    
    @FXML
    private Label messageLabel;

    @FXML
    private Button removeButton;

    @FXML
    private ObservableList<Friend> friendList = FXCollections.observableArrayList();
    
    
    
public void initialize() {
        try {
            // Connect to server socket
            in = project.getIn();
            out = project.getOut();

            // Send request to server for friend list
            out.writeUTF("FRIENDLIST");

            // Receive friend list data from server
            List<Friend> data = new ArrayList();
            int rowCount = in.readInt();
            for (int i = 0; i < rowCount; i++) {
                String name = in.readUTF();
                 String email = in.readUTF();
                data.add(new Friend(name,email));
            } 
            

            // Update UI with friend list data
            Platform.runLater(() -> {
                friendList.addAll(data);
                friendNameCol.setCellValueFactory(cellData -> cellData.getValue().friendNameProperty());
                friendEmail.setCellValueFactory(cellData -> cellData.getValue().friendEmailProperty());
                friendListTable.setItems(friendList);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }}

        // Add event listeners for add and remove buttons
  @FXML
private void handleAddButton(ActionEvent event) {
    String name = friendNameField.getText();
    if (!name.isEmpty()) {
        try {
            // Send request to server to add friend
            out.writeUTF("ADDFRIEND");
            out.writeUTF(name);

            // Receive friend data from server or error message
            String response = in.readUTF();
            if (!response.equals("ERROR")) {
              
                messageLabel.setText("Friend request sent");
            } else {
                // Display error message to user
                messageLabel.setText("Friend not found. Please enter a valid friend name.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

    
    @FXML
    private void handleRemoveButton(ActionEvent event) {
    Friend selectedFriend = friendListTable.getSelectionModel().getSelectedItem();
    if (selectedFriend != null) {
        try {
            // Send request to server to remove friend
            out.writeUTF("REMOVEFRIEND");
            out.writeUTF(selectedFriend.getFriendName());

            // Wait for response from server
            String response = in.readUTF();
            if (response.equals("SUCCESS")) {
                // Remove friend from UI
                Platform.runLater(() -> friendList.remove(selectedFriend));
                messageLabel.setText("Friend removed successfully");
            } else {
                System.out.println("Error removing friend");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    
    /**
     * Initializes the controller class.
     */
  
    @FXML
    
private void HomeF(ActionEvent event) throws IOException {

    Parent table = FXMLLoader.load(getClass().getResource("Mainpage.fxml"));
    Scene scene =new Scene(table);
    
    // this is to get the stage information 
    
    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
}



  public void stop() {
        try {
            // Close socket and streams
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


class Friend {

   
    private final String friendName;
     private final String friendEmail;

    public Friend( String friendName,String friendEmail) {
        this.friendEmail = friendEmail;
        this.friendName = friendName;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public String getFriendName() {
        return friendName;
    }
 public SimpleStringProperty friendEmailProperty() {
        return new SimpleStringProperty(friendEmail);
    }
    

    public SimpleStringProperty friendNameProperty() {
        return new SimpleStringProperty(friendName);
    }
}














