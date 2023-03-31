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
 * @author Enter Computer
 */

class Wish {

    private final String itemName;
     private final String Price; 
     private final String Desc; 
     private final String Cont;
     
     

    public Wish(String itemName , String Price , String Desc , String Cont   ) {
        this.itemName = itemName;
        this.Price = Price; 
        this.Desc = Desc;
        this.Cont= Cont;
    }

    public String getitemName() {
        return itemName;
    }
    
      public String getPrice() {
        return Price;
    }
      
       public String getDesc() {
        return Desc;
    }
      
          public String getCont() {
        return Cont;
    }
    

    public SimpleStringProperty itemNameProperty() {
        return new SimpleStringProperty(itemName);
    }

    
    
    public SimpleStringProperty PriceProperty() {
        return new SimpleStringProperty(Price);
    }
    
    
      public SimpleStringProperty DescProperty() {
        return new SimpleStringProperty(Desc);
    }
      
        public SimpleStringProperty ContProperty() {
        return new SimpleStringProperty(Cont);
    }

   
}

