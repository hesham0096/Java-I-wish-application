
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author iti
 */
class Friend {

    private final String friendEmail;
    private final String friendName;

    public Friend( String friendName, String friendEmail ) {
        this.friendEmail = friendEmail;
        this.friendName = friendName;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public String getFriendName() {
        return friendName;
    }

    public StringProperty friendEmailProperty() {
        return new SimpleStringProperty(friendEmail);
    }

    public StringProperty friendNameProperty() {
        return new SimpleStringProperty(friendName);
    }
}
