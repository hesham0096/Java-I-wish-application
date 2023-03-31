/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Text;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Enter Computer
 */
public class BalanceController  {

    @FXML
    private Button btnCharge;

    @FXML
    private TextField txtBalance;

    @FXML
    private Text currentBalanceField;

    @FXML
    private Button btnHomeB;

    private DataInputStream in;
    private DataOutputStream out;  
    private String charged ;
    private String  balance ;
    private String currentbalance;
    
    /**
     *
     */
    
           public void initialize () {
               
         try {         
           in = project.getIn();
             out = project.getOut();
           
            out.writeUTF("CURRENT BALANCE");
            
            currentbalance = in.readUTF();
             System.out.println("current balance is " + currentbalance );
             currentBalanceField.setText(currentbalance);
             
             
        } catch (IOException ex) {
            Logger.getLogger(BalanceController.class.getName()).log(Level.SEVERE, null, ex);
        }
              
                 
      
        
            //currentBalanceField.setText(balance);

        // TODO
    }  
    
    
        
 @FXML
        private void Charge (ActionEvent event) throws IOException {
            
            out.writeUTF("CHARGE");
             charged= txtBalance.getText();

              out.writeUTF(charged) ;
               System.out.println("charged" + charged);
              balance = in.readUTF();
              
              System.out.println("balance" + balance);
              
              currentBalanceField.setText(balance);
            
            
}
        
        
  
    
    
 @FXML
        private void HomeB (ActionEvent event) throws IOException {

    Parent table = FXMLLoader.load(getClass().getResource("Mainpage.fxml"));
    Scene scene =new Scene(table);
    
    // this is to get the stage information 
    
    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
}

    
}
