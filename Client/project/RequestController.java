/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class RequestController implements Initializable {
     private DataInputStream in;
    private DataOutputStream out;
    
    @FXML
    private TableView<friendRequest> friendRequestTable;
    @FXML
    private TableColumn<friendRequest,String> friendEmailCol;

    @FXML
    private TableColumn<friendRequest, String> friendStatusCol;
    @FXML
    private ObservableList<friendRequest> friendRequestList = FXCollections.observableArrayList();
    
    @FXML
    private Button acceptbtn;
    
    @FXML
    private Label messageLabel;

    @FXML
    private Button declinebtn;
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Connect to server socket
            in = project.getIn();
            out = project.getOut();

            // Send request to server for friend list
            out.writeUTF("FRIENDREQUESTLIST");

            // Receive friend list data from server
            List<friendRequest> data = new ArrayList();
            int rowCount = in.readInt();
            for (int i = 0; i < rowCount; i++) {
                String email = in.readUTF();
                 String status = in.readUTF();
                data.add(new friendRequest(email,status));
            } 
            

            // Update UI with friend list data
            Platform.runLater(() -> {
                friendRequestList.addAll(data);
                friendEmailCol.setCellValueFactory(cellData -> cellData.getValue().friendEmailProperty());
                friendStatusCol.setCellValueFactory(cellData -> cellData.getValue().friendStatusProperty());
                friendRequestTable.setItems(friendRequestList);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }}
    
    @FXML
    private void handleAcceptButton(ActionEvent event) {
    friendRequest selectedFriend = friendRequestTable.getSelectionModel().getSelectedItem();
    if (selectedFriend != null) {
        try {
            // Send request to server to remove friend
            out.writeUTF("ACCEPTFRIEND");
            out.writeUTF(selectedFriend.getFriendEmail());

            // Wait for response from server
            String response = in.readUTF();
            if (!response.equals("ERROR")) {
                // Remove friend from UI
                Platform.runLater(() -> friendRequestList.remove(selectedFriend));
                messageLabel.setText("Friend Request Accepted, user added to your frientlist");
            } else {
                System.out.println("Error Accepting friend Request");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    
    @FXML
    private void handleDeclineButton(ActionEvent event) {
    friendRequest selectedFriend = friendRequestTable.getSelectionModel().getSelectedItem();
    if (selectedFriend != null) {
        try {
            // Send request to server to remove friend
            out.writeUTF("DECLINEFRIEND");
            out.writeUTF(selectedFriend.getFriendEmail());

            // Wait for response from server
            String response = in.readUTF();
            if (response.equals("SUCCESS")) {
                // Remove friend from UI
                Platform.runLater(() -> friendRequestList.remove(selectedFriend));
                messageLabel.setText("Friend Request Declined successfully");
            } else {
                System.out.println("Error Declining friend request");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

     
    
    
    
    @FXML
        private void HomeRequest(ActionEvent event) throws IOException {

    Parent table = FXMLLoader.load(getClass().getResource("Mainpage.fxml"));
    Scene scene =new Scene(table);
    
    // this is to get the stage information 
    
    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
}

        
class friendRequest {

   
    
     private final String friendEmail;
     private final String friendStatus;

    public friendRequest( String friendEmail,String friendStatus) {
        this.friendEmail = friendEmail;
        this.friendStatus = friendStatus;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public String getFriendStatus() {
        return friendStatus;
    }
 public SimpleStringProperty friendEmailProperty() {
        return new SimpleStringProperty(friendEmail);
    }
    

    public SimpleStringProperty friendStatusProperty() {
        return new SimpleStringProperty(friendStatus);
    }
}

    
}
