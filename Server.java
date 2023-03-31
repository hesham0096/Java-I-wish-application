import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
//import proj.Client ;
//import proj.friends_table;
//import proj.Friend_listController;

public class Server {
    private  final int SERVER_PORT = 5005;
    private  final String DB_URL = "jdbc:mysql://localhost:3306/iwish";
    private  final String DB_USERNAME = "root";
    private  final String DB_PASSWORD = "root";
    private Vector<ChatHandler> clientsVector = new Vector<ChatHandler>();
    

    public Server () { 
    
         ServerSocket serverSocket = null;
        Connection conn = null;

        try {
            // Start server socket
            serverSocket = new ServerSocket(5005);
            System.out.println("Server started on port " + 5005);

            // Connect to database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/iwish", "root", "1234");
            System.out.println("Connected to database");

            while (true) {
                // Wait for client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress());

                // Create input and output streams for client
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                // Create a new handler for the client
                ChatHandler handler = new ChatHandler(clientSocket, in, out, conn);
               
            }
        } catch (IOException e) {
            System.out.println("Error processing client request: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
                if (serverSocket != null) serverSocket.close();
            } catch (IOException | SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    
    }
    public static void main(String[] args) {
        new Server() ;
       
    }
    

        class ChatHandler extends Thread {
        private Socket clientSocket;
        private DataInputStream in;
        private DataOutputStream out;
        private Connection conn;
        private int userid;
        private String username;
        private String useremail;
        private String friend_name;
        private int balance;
        private Integer totalAmount;
          private  String  balancest ;
        PreparedStatement stmt ;
        ResultSet rs ;
        
        
        
        

        public ChatHandler(Socket clientSocket, DataInputStream in, DataOutputStream out, Connection conn) {
            this.clientSocket = clientSocket;
            this.in = in;
            this.out = out;
            this.conn = conn;
            
            start();
        }

        public void run() {
            try {
                while (true) {
                    // Process client requests
                   
                        String request = in.readUTF();
                        
                        if (request.equals("WISHLIST")) {
                // Retrieve friend list data from database
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM wishlist where user_id = (select id from users where username=?)" );
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
//
                // Convert result set to list of friends
                List<Wish> wishList = new ArrayList<>();
                while (rs.next()) {
 
                  
                    String itemName=rs.getString("item_name");
                    String Price=rs.getString("price");
                     String Desc=rs.getString("description"); 
                 String Cont=rs.getString("contribution");

                    wishList.add(new Wish(itemName,Price,Desc, Cont));
                }

                // Send friend list data to client
                out.writeInt(wishList.size());
                for (Wish wish : wishList) {
                    out.writeUTF(wish.getitemName());   
                     out.writeUTF(wish.getPrice());
                     out.writeUTF(wish.getDesc());
                     out.writeUTF(wish.getCont());
                }

                // Flush output stream
                out.flush();
                    } 
                    
                    if (request.equals("FRIENDWISHLIST")) {
                // Retrieve friend list data from database
                try{
                    
                    String friendname = in.readUTF();
                    
                PreparedStatement stmt2 = conn.prepareStatement("SELECT balance FROM users where id = (select id from users where username=?)");
                stmt2.setString(1,username);
                ResultSet rs2 = stmt2.executeQuery();
                System.out.println("USERRRRR" + username);
                 
                     if (rs2.next()) {
             balance = rs2.getInt(1);
           
            out.writeUTF(String.valueOf(balance));
                System.out.println("BALANCE  HHH" + balance);
        }
                
                PreparedStatement stmt = conn.prepareStatement("select friend_id from friendlist where friendname=? and user_id=?" );
                stmt.setString(1, friendname);
                stmt.setInt(2, userid);
                ResultSet ms = stmt.executeQuery();
                
                
                if (ms.next()){
                    
                    out.writeUTF("SUCCESS");
                stmt = conn.prepareStatement("SELECT * FROM wishlist where user_id = (select friend_id from friendlist where friendname=? and user_id=?)" );
                stmt.setString(1, friendname);
                stmt.setInt(2, userid);
                ResultSet rs = stmt.executeQuery();
                
          
                   
       
//
                // Convert result set to list of friends
                List<Wish> wishList = new ArrayList<>();
                while (rs.next()) {
 
                  
                    String itemName=rs.getString("item_name");
                    String Price=rs.getString("price");
                    String Desc=rs.getString("description"); 
                    String Cont=rs.getString("contribution");
                    wishList.add(new Wish(itemName,Price,Desc,Cont));
                }
                    if (wishList.isEmpty()) {
                        // Send error message to client
                        out.writeInt(0);
                    } else {
                        // Send friend list data to client
                        out.writeInt(wishList.size());
                        for (Wish wish : wishList) {
                            out.writeUTF(wish.getitemName());
                            out.writeUTF(wish.getPrice());
                            out.writeUTF(wish.getDesc());
                            out.writeUTF(wish.getCont());
                        }
                    }
                 out.flush();
                }else{
                
                
                         out.writeUTF("ERROR");
                    }}
                catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // Flush output stream
               
                    } 
                        
                        
                         
                        
                         if (request.equals("DECLINEFRIEND")) {
                try {
                    String emailToDecline = in.readUTF();
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM friend_requests WHERE sender_email = ?");
                    stmt.setString(1, emailToDecline);
                    int rowsAffected = stmt.executeUpdate();
                    
                    if (rowsAffected >= 1) {
                        out.writeUTF("SUCCESS");
                    } else {
                        out.writeUTF("ERROR");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
                        
                         if (request.equals("ACCEPTFRIEND")) {
                try {
                    String emailToAccept = in.readUTF();
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM friend_requests WHERE sender_email= ?");
                    stmt.setString(1, emailToAccept);
                    stmt.executeUpdate();
                    
                    stmt = conn.prepareStatement("SELECT * FROM users where email= ?");
        stmt.setString(1, emailToAccept);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            String friendname = rs.getString("username");
            out.writeUTF("Success");

            int friend_id = rs.getInt("id");

            stmt = conn.prepareStatement("INSERT INTO friendlist (user_id, friend_id, friendname, email) VALUES (?,?, ?, ?)");
            stmt.setInt(1, userid);
            stmt.setInt(2, friend_id);
            stmt.setString(3, friendname);
            stmt.setString(4, emailToAccept);
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement("INSERT INTO friendlist (user_id, friend_id, friendname, email) VALUES (?,?, ?, ?)");
            stmt.setInt(1, friend_id);
            stmt.setInt(2, userid);
            stmt.setString(3, username);
            stmt.setString(4, useremail);
            stmt.executeUpdate();
            

        } else {
                        out.writeUTF("ERROR");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
                        
                if (request.equals("FRIENDREQUESTLIST")) {
                // Retrieve friend list data from database
                PreparedStatement stmt = conn.prepareStatement("SELECT sender_email,status FROM friend_requests where receiver_id= (select id from users where username=?)");
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery(); 

                // Convert result set to list of friends
                List<friendRequest> friendRequestList = new ArrayList<>();
                while (rs.next()) {
                   
                    String email = rs.getString("sender_email");
                    String status = rs.getString("status");
                    friendRequestList.add(new friendRequest(email,status));
                }

                // Send friend list data to client
                out.writeInt(friendRequestList.size());
                for (friendRequest friend : friendRequestList) {
                    out.writeUTF(friend.getFriendEmail());
                    out.writeUTF(friend.getFriendStatus());
                    
                }

                // Flush output stream
                out.flush();
                    }
                   if (request.equals("FRIENDLIST")) {
                // Retrieve friend list data from database
                PreparedStatement stmt = conn.prepareStatement("SELECT friendname,email FROM friendlist where user_id= (select id from users where username=?)");
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery(); 

                // Convert result set to list of friends
                List<Friend> friendList = new ArrayList<>();
                while (rs.next()) {
                   
                    String name = rs.getString("friendname");
                    String email = rs.getString("email");
                    friendList.add(new Friend(name,email));
                }

                // Send friend list data to client
                out.writeInt(friendList.size());
                for (Friend friend : friendList) {
                    out.writeUTF(friend.getFriendName());
                    out.writeUTF(friend.getFriendEmail());
                    
                }

                // Flush output stream
                out.flush();
                    }
                   
                   if (request.equals("REMOVEFRIEND")) {
                try {
                    String friendToRemove = in.readUTF();
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM friendlist WHERE friendname = ?");
                    stmt.setString(1, friendToRemove);
                    int rowsAffected = stmt.executeUpdate();
                    
                    if (rowsAffected >= 1) {
                        out.writeUTF("SUCCESS");
                    } else {
                        out.writeUTF("ERROR");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

                        if (request.equals("ADDFRIEND")) {
                            try {
                                String friendName = in.readUTF();

                                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users where username= ?");
                                stmt.setString(1, friendName);
                                ResultSet rs = stmt.executeQuery();

                                if (rs.next()) {
                                    String email = rs.getString("email");
                                    out.writeUTF(email);

                                    int receiver_id = rs.getInt("id");

                                    stmt = conn.prepareStatement("INSERT INTO friend_requests (sender_id, receiver_id, sender_email, status) VALUES (?,?, ?, CONCAT(?, ' sends a friend request'))");
                                    stmt.setInt(1, userid);
                                    stmt.setInt(2, receiver_id);
                                    stmt.setString(3, useremail);
                                    stmt.setString(4, username);
                                    stmt.executeUpdate();

                                } else {
                                    // Friend not found, send error message to client
                                    out.writeUTF("ERROR");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } if (request.equals("CONTRIBUTE")) {
                            
                             try {
                                   String contribute = in.readUTF();
                                   String friendName = in.readUTF();
                                   String itemName = in.readUTF();
                                   String price     = in.readUTF();
           
                  System.out.println("frindName " + friendName);
                         System.out.println("contribute " + contribute);
              System.out.println("itemName " + itemName);
                          //  System.out.println("request " + request);
                          

                          System.out.println("BALANCE CONT   " + balance); 
                          
                           int new_balance = balance - Integer.valueOf(contribute) ;
                           int int_price = Integer.valueOf(price);
                           int int_cont = Integer.valueOf(contribute) ;
                          
                         //  if (new_balance >= 0 && int_price >= int_cont && (int_price + int_cont) <= totalAmount    ) {
                           PreparedStatement stmt4 = conn.prepareStatement("UPDATE users SET balance=? WHERE username=?" );      
                            stmt4.setInt(1, new_balance);
                            stmt4.setString(2, username);
                            stmt4.executeUpdate(); 
                           
             
                           
                                    
              PreparedStatement stmt = conn.prepareStatement("insert into contribution (username ,friendname , item_name , amount ) values (? , ? , ? , ? ) ");
                stmt.setString(1, username);
                 stmt.setString(2, friendName);
                stmt.setString(3, itemName);
   
               stmt.setString(4, contribute);
                stmt.executeUpdate(); 
               // System.out.println("user1" + username);
                  //System.out.println("friend1" + friendName);

            
         PreparedStatement stmt2 = conn.prepareStatement("SELECT SUM(amount) AS total_amount FROM contribution WHERE item_name=? AND username=? AND friendname=?");
         
                      //   System.out.println("user2" + username);
                    //   System.out.println("friend2" + friendName);

          stmt2.setString(1, itemName);
         stmt2.setString(2, username);
         stmt2.setString(3, friendName);
         ResultSet rs = stmt2.executeQuery();
            
         System.out.println("itemNam2  " + itemName);
             System.out.println("username2  " + username);
             System.out.println("friendname2  " + friendName);


         
         if (rs.next()) {
              totalAmount = rs.getInt(1);
             System.out.println("totalAMoint    " + totalAmount);
             
              
             PreparedStatement stmt3 = conn.prepareStatement("UPDATE wishlist SET contribution=? WHERE user_id=(SELECT id FROM users WHERE username=?) AND item_name=?");      
             stmt3.setInt(1, totalAmount);
             stmt3.setString(2, friendName);
             stmt3.setString(3, itemName);
             stmt3.executeUpdate();
             
             
             
         } 
                             // }
                    

               // 
                                   

                            
                             }
                             catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                        }
                        
                        if (request.equals("CHARGE")) { 
                        String charged = in.readUTF ();
                        
                    
                        
                         PreparedStatement stmt2 = conn.prepareStatement("SELECT balance FROM users WHERE username = ? ");
                        stmt2.setString(1, username);
                        ResultSet rs2 = stmt2.executeQuery();
                        

                        if (rs2.next()) {
                 balancest= rs2.getString(1);
                System.out.println("balance" + balancest);
                //out.writeUTF(balancest);
                
            } 
                        System.out.println ("charged " + charged );
                        System.out.println ("charged " + username );
             
                        int new_balance = Integer.valueOf(charged) + Integer.valueOf(balancest) ; 
                        String new_balancest = String.valueOf(new_balance);
                        
                        stmt = conn.prepareStatement("update users set balance =? where username= ? ");
                        stmt.setString(1, String.valueOf(new_balance));
                        stmt.setString(2, username);
                        stmt.executeUpdate();
                        
                        System.out.println("balance String sent" + new_balancest ) ;
                        out.writeUTF(new_balancest);  
                        
                        }
                         if (request.equals("CURRENT BALANCE")) { 
                             
                                        PreparedStatement stmt2 = conn.prepareStatement("SELECT balance FROM users WHERE username = ? ");
                        stmt2.setString(1, username);
                        ResultSet rs2 = stmt2.executeQuery();
                        

                        if (rs2.next()) {
                 balancest= rs2.getString(1);
                System.out.println("current balance " + balancest);
                
                out.writeUTF(balancest); 
                //out.writeUTF(balancest);
                
            } 
                         }
                        
                        
                        
                        if (request.equals("LOGIN")) {
                        String username = in.readUTF();
                        
                        String password = in.readUTF();
                        

                        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
                        stmt.setString(1, username);
                        stmt.setString(2, password);
                        ResultSet rs = stmt.executeQuery();
                        

                        if (rs.next()) {
                            out.writeUTF("LOGIN_SUCCESS");
                            this.username = username;
                            System.out.println("User " + username + " logged in");
                            this.userid = rs.getInt("id");
                            this.useremail = rs.getString("email");
                          
                           // System.out.println("Your"  + respond );
                            
                           
                        } else {
                            out.writeUTF("LOGIN_FAILURE");
                        }

                        rs.close();
                        stmt.close();
                    } else if (request.equals("REGISTER")) {
                        String username = in.readUTF();
                        String password = in.readUTF();
                        String email=in.readUTF();

                        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? or email=?");
                        stmt.setString(1, username);
                        stmt.setString(2, email);
                        ResultSet rs = stmt.executeQuery();

                        if (rs.next()) {
                out.writeUTF("REGISTER_FAILURE");
            } else  {
                stmt = conn.prepareStatement("INSERT INTO users (username, password,email) VALUES (?, ?,?)");
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, email);
                stmt.executeUpdate();
                out.writeUTF("REGISTER_SUCCESS");
            }
               
        }
            
    }
                
} catch (IOException e) {
    System.out.println("Error processing client request: " + e.getMessage());
} catch (SQLException e) {
    System.out.println("Error connecting to database: " + e.getMessage());
} 
 
        }       
    
    
    
    
 }}