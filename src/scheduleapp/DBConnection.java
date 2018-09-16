/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Customer;

/**
 *
 * @author alisii
 */
public class DBConnection {
    
    private static Connection conn = null;
    private Statement stmt = null;
    private String dbUser = null;
    private String dbPass = null;
    
    public static void MakeConnection (){
        
        //Connection String
        //Server name:  52.206.157.109
        //Database name:  U03QIu
        //Username:  U03QIu
        //Password:  53688051379
        // JDBC driver name and database URL
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://52.206.157.109/U05mXQ";
        
        //  Database credentials
        final String DBUSER = "U05mXQ";
        final String DBPASS = "53688548906";
        
        boolean res = false;

        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);
        } 
        catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void CloseConnection() throws SQLException {
        conn.close();   
    }
    
    public static  ArrayList<Customer> getAllCustomers() {
        
        ArrayList<Customer> customers = new ArrayList<>();
        
        try {
            // connect
            MakeConnection();
            Statement stmt;
            ResultSet rs = null;
            
            try {

                stmt = conn.createStatement();
                String query = "SELECT * FROM customer LIMIT 5";
                rs = stmt.executeQuery(query);

                while (rs.next()) {
                    
                    // while there is something in the row, look for next
                    int id = rs.getInt("customerId"); 
                    String name = rs.getString("customerName");
                    
                    Customer customer = new Customer();
                    customer.setId(id);
                    customer.setName(name);
                    
                    customers.add(customer);
                }
            } 
            catch (SQLException ex) {
                ex.printStackTrace();

            }
            
            //close
            CloseConnection();
            
        } 
        catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return customers;
    }
}
