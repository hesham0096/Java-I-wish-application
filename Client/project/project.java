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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author DELL
 */
public class project extends Application {
    

    private static Socket socket;
    private static DataInputStream in;
    private static DataOutputStream out;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

        // Connect to server
        connectToServer();
    }

    /**
     * Connects to the server.
     */
    private static void connectToServer() {
        try {
            socket = new Socket("localhost", 5005);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }

    /**
     * Returns the DataInputStream object used for communication with the server.
     */
    public static DataInputStream getIn() {
        return in;
    }

    /**
     * Returns the DataOutputStream object used for communication with the server.
     */
    public static DataOutputStream getOut() {
        return out;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

