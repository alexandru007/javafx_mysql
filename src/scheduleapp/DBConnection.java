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
        
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://52.206.157.109/U05mXQ";
        
        //  Database credentials
        final String DBUSER = "U05mXQ";
        final String DBPASS = "53688548906";

        try {
            //STEP 2: Register JDBC driver
            //Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);
        } 
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void CloseConnection() throws SQLException {
        conn.close();   
    }
    
    public static boolean isUserValid(String username, String password) {
        
        try {
            MakeConnection();
            Statement stmt;
            ResultSet rs = null;
            
            try {
                stmt = conn.createStatement();
                String query = "SELECT * FROM user where userName='"+username+"' AND password='"+password+"'";
                rs = stmt.executeQuery(query);

                // if anything was returned
                if(rs.isBeforeFirst()){
                    
                    // get the user id
                    rs.next();
                    UserClass.setCurrentUserID(rs.getInt("userId"));
                    
                    return true;
                }
                    
                
            } catch (Exception e) {
            }
            
            
        } catch (Exception e) {
        }
        
        return false;
    }
    
    public static  ArrayList<Customer> getAllCustomers() {
        
        ArrayList<Customer> customers = new ArrayList<>();
        
        try {

            MakeConnection();

            Statement stmt = conn.createStatement();
            String query = "SELECT customer.customerId, "
                    + "customer.customerName, "
                    + "address.address, "
                    + "address.phone, "
                    + "city.city,"
                    + "country.county "
                    + "FROM customer "
                    + "INNER JOIN address ON customer.addressId = address.addressId "
                    + "INNER JOIN city ON address.cityId = city.addressId "
                    + "INNER JOIN country ON city.countryId = country.countryId "
                    + "AND LIMIT 5";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {

                // while there is something in the row, look for next
                int id = rs.getInt(0);
                String name = rs.getString(1);
                String address = rs.getString(2);
                String phone = rs.getString(3);
                String city = rs.getString(4);
                String country = rs.getString(5);

                Customer customer = new Customer();
                customer.setId(id);
                customer.setName(name);
                customer.setAddress(address);
                customer.setPhone(phone);
                customer.setCity(city);
                customer.setCountry(country);

                customers.add(customer);
            }

            CloseConnection();
        } 
        catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return customers;
    }

    public static void addCustomer(Customer customer) {

        //
        try {
            MakeConnection();
            Statement stmt = conn.createStatement();

            String customerName = customer.getName();
            //String address = customer.getAddress();

            String query
                    = String.format("INSERT INTO customer('customerName') values('%s')",
                            customerName);
            stmt.executeUpdate(query);

            CloseConnection();

        } 
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public static void deleteCustomer(int customerId) {
        
        try {
            MakeConnection();
            Statement stmt;
            
            stmt = conn.createStatement();
            String query = "DELETE FROM customer WHERE customerId='"+customerId+"'";
            stmt.executeUpdate(query);
            
            //close
            CloseConnection();
            
            
        } 
        catch (Exception e) {
            System.out.println(e.toString());
        }  
    }
}
