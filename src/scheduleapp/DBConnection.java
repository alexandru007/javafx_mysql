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
import model.Appointment;
import model.Customer;

/**
 *
 * @author alisii
 */
public class DBConnection {
    
    private static Connection conn = null;
    private static Statement stmt = null;
    //private static ResultSet rs = null;
    
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
            stmt = conn.createStatement();
            String query = "SELECT * FROM user where userName='" + username + "' AND password='" + password + "'";
            ResultSet rs = stmt.executeQuery(query);

            // if anything was returned
            if (rs.isBeforeFirst()) {

                // get the user id
                rs.next();
                UserClass.setCurrentUserID(rs.getInt("userId")); // set the global user id

                return true;
            }

        } catch (Exception e) {
        }

        return false;
    }
    
    public static  ArrayList<Customer> getAllCustomers() {
        
        ArrayList<Customer> customers = new ArrayList<>();
        
        try {

            MakeConnection();

            stmt = conn.createStatement();
            String query = "SELECT customer.customerId, "
                    + "customer.customerName, "
                    + "address.address, "
                    + "address.phone, "
                    + "city.city,"
                    + "country.country "
                    + "FROM customer "
                    + "INNER JOIN address ON customer.addressId = address.addressId "
                    + "INNER JOIN city ON address.cityId = city.cityId "
                    + "INNER JOIN country ON city.countryId = country.countryId "
                    + "LIMIT 10";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {

                // while there is something in the row, look for next
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String address = rs.getString(3);
                String phone = rs.getString(4);
                String city = rs.getString(5);
                String country = rs.getString(6);

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
    
    public static ArrayList<Appointment> getAllAppointments() {
        
        ArrayList<Appointment> appointments = new ArrayList<>();
        
        try {
            MakeConnection();
            stmt = conn.createStatement();
            String query = "SELECT appointment.appointmentId, "
                    + "customer.customerName, "
                    + "appointment.title, "
                    + "appointment.description, "
                    + "appointment.location, "
                    + "appointment.start "
                    + "FROM appointment, customer "
                    + "WHERE appointment.customerId=customer.customerId "
                    + "LIMIT 10";
            
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                
                Appointment appointment = new Appointment();
                
                appointment.setAppointmentId(rs.getInt(1));
                appointment.setCustomerName(rs.getString(2));
                appointment.setTitle(rs.getString(3));
                appointment.setDescription(rs.getString(4));
                appointment.setLocation(rs.getString(5));
                appointment.setStartTime(rs.getString(6));
                
                appointments.add(appointment);
            }  
        } catch (Exception e) {
        }
        
        return appointments;
        
    }

    public static void addCustomer(Customer customer) {

        // need to update all tables
        try {
            MakeConnection();
            stmt = conn.createStatement();
            
            // ********* add country
            String addCountryQuery = 
                    String.format("INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + "VALUES ('%s' , now(), 'test', now(), 'test')", customer.getCountry());
            stmt.executeUpdate(addCountryQuery);
            
            // ********* get the last contryid
            ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() FROM country");
            rs.next();
            String countryId = rs.getString(1); // should return the last id
            
            // ********* add city
            String addCityQuery = String.format("INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + "VALUES ('%s', '%d', now(), 'test', now(), 'test')", customer.getCity(), Integer.parseInt(countryId));
            stmt.executeUpdate(addCityQuery);
            
            // ********* get the last cityid
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID() FROM city");
            rs.next();
            String cityId = rs.getString(1); // should return the last id. to be used in the next query
            
            
            // ********* add address
            String addAddressQuery = String.format("INSERT INTO address (address, cityId, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + "VALUES ('%s', '%d', '%s', now(), 'test', now(), 'test')", customer.getAddress(), Integer.parseInt(cityId), customer.getPhone());
            stmt.executeUpdate(addAddressQuery);
            
            // ********* get the last addressid
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID() FROM address");
            rs.next();
            String addressId = rs.getString(1); // should return the last id. to be used in the next query
            
            // ********* add customer
            String addCustomerQuery = String.format("INSERT INTO customer (customerName, addressId, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + "VALUES ('%s', '%d', now(), 'test', now(), 'test')", customer.getName(), Integer.parseInt(addressId));
            stmt.executeUpdate(addCustomerQuery);

            CloseConnection();
        } 
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public static void addAppointment(Appointment appointment) {
        
        try {
            
            MakeConnection();
            stmt = conn.createStatement();
            
            // start date needs to be date format
            // database image is different from the actual db
            
            String query = String.format("INSERT INTO "
                    + "appointment (customerId, title, description, location, contact, start, createDate, createdBy, lastUpdateBy )"
                    + " VALUES ('%d', '%s', '%s', '%s', '%s', 'test', now(), 'test', 'test')", 
                    appointment.getCustomerId(),
                    appointment.getTitle(), 
                    appointment.getDescription(), 
                    appointment.getLocation(),
                    appointment.getStartTime());
            
            try {
                stmt.executeUpdate(query);    
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
            
            
            CloseConnection();
            
        } catch (Exception e) {
            
        }
        
    }
    
    public static void editCustomer (Customer customer) {
        
        // what is currently in the db needs to be updated with new
        //
        try {
            MakeConnection();
            stmt = conn.createStatement();

            // update customer name
            String query = String.format("UPDATE customer SET customerName='%s' WHERE customerId='%d'", customer.getName(), customer.getId());
            stmt.executeUpdate(query);
            
            // get the address id
            String addressIdQuery = String.format("SELECT addressId FROM customer WHERE customerId='%d'", customer.getId());
            ResultSet rs = stmt.executeQuery(addressIdQuery);
            rs.next();
            String addressId = rs.getString(1);
            
            // update address
            String updateAddressQuery = String.format("UPDATE address SET address='%s' WHERE addressId='%d'", customer.getAddress(), Integer.parseInt(addressId));
            stmt.executeUpdate(updateAddressQuery);
            
            // get city id
            String cityIdQuery = String.format("SELECT cityId FROM address WHERE addressId='%d'", Integer.parseInt(addressId));
            rs = stmt.executeQuery(cityIdQuery);
            rs.next();
            String cityId = rs.getString(1);
            
            // update city
            String updateCityQuery = String.format("UPDATE city SET city='%s' WHERE cityId='%d'", customer.getCity(), Integer.parseInt(cityId));
            stmt.executeUpdate(updateCityQuery);
            
            // get country id
            String countryIdQuery = String.format("SELECT countryId FROM city WHERE cityId='%d'", Integer.parseInt(cityId));
            rs = stmt.executeQuery(countryIdQuery);
            rs.next();
            String countryId = rs.getString(1);
            
            // upadate country
            //String updateCountryQuery = String.format("UPDATE country SET country='%s' WHERE countryId='%d'", customer.getCountry(), Integer.parseInt(countryId));
            //stmt.executeUpdate(updateCountryQuery);
            

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
    
    public static void deleteAppointment(int appointmentId) {
        
        try {
            MakeConnection();
            Statement stmt;
            
            stmt = conn.createStatement();
            String query = "DELETE FROM appointment WHERE appointmentId='"+appointmentId+"'";
            stmt.executeUpdate(query);
            
            //close
            CloseConnection();
            
            
        } 
        catch (Exception e) {
            System.out.println(e.toString());
        }  
    }
    
}
