/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author DELL
 */
public class LoginSceneController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private PasswordField passwordField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleLogin(ActionEvent event) {
    String username = usernameField.getText();
    String password = passwordField.getText();

    if (username.isEmpty() || password.isEmpty()) {
        loginMessageLabel.setText("Please enter both username and password.");
        return;
    }
    // Get DataInputStream and DataOutputStream objects from Client class
    DataInputStream in = project.getIn();
    DataOutputStream out = project.getOut();

    // Connect to server
    try {

        // Send login request to server
        out.writeUTF("LOGIN");
        out.writeUTF(username);
        out.writeUTF(password);
        out.flush();

        String response = in.readUTF();

        if (response.equals("LOGIN_SUCCESS")) {
            // Login successful, show main window
         Parent table = FXMLLoader.load(getClass().getResource("Mainpage.fxml"));
       Scene scene =new Scene(table);
    
    // this is to get the stage information 
    
    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
                
        } else {
            loginMessageLabel.setText("Invalid username or password. Please try again.");
        }
    } catch (IOException e) {
        System.out.println("Error sending login request: " + e.getMessage());
    }
}
    

    @FXML
    public void change(ActionEvent event) throws IOException {
       
    Parent table = FXMLLoader.load(getClass().getResource("Register.fxml"));
    Scene scene =new Scene(table);
    
    // this is to get the stage information 
    
    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
}
