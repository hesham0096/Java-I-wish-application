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
 * FXML Controller class
 *
 * @author DELL
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField regUsernameField;
    
    @FXML
    private TextField regEmail;
    @FXML
    private Button registerBackButton;
    @FXML
    private Button registerConfirmButton;
    @FXML
    private Label registerMessageLabel;
    @FXML
    private PasswordField regPasswordField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    public void gotologin(ActionEvent event) throws IOException {
       
    Parent table = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
    Scene scene =new Scene(table);
    
    // this is to get the stage information 
    
    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = regUsernameField.getText();
    String password = regPasswordField.getText();
    String email = regEmail.getText();
    if (username.isEmpty() || password.isEmpty()|| email.isEmpty()) {
        registerMessageLabel.setText("Please enter all of the fields.");
        return;
    }
    // Get DataInputStream and DataOutputStream objects from Client class
        DataInputStream in = project.getIn();
        DataOutputStream out = project.getOut();
    // Connect to server
    try {

        // Send register request to server
        out.writeUTF("REGISTER");
        out.writeUTF(username);
        out.writeUTF(password);
         out.writeUTF(email);
        
        out.flush();

        String response = in.readUTF();

        if (response.equals("REGISTER_SUCCESS")) {
            registerMessageLabel.setText("Registration successful. Please log in.");
        } else {
            registerMessageLabel.setText("Username or email are already taken. Please choose a different ones.");
        }
    } catch (IOException e) {
        System.out.println("Error sending register request: " + e.getMessage());
    }
    }
    
}
