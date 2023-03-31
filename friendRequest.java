
import javafx.beans.property.SimpleStringProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author iti
 */
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
