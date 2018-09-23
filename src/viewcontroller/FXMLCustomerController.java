/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewcontroller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import scheduleapp.DBConnection;


public class FXMLCustomerController implements Initializable {
    
    // customer buttons
    @FXML private Button deleteCustomer;
    @FXML private Button editCustomer;
    @FXML private Button addCustomer;
    
    // appointments buttons
    @FXML private Button addAppointment;
    @FXML private Button editAppointment;
    @FXML private Button deleteAppointment;
    
    // customer tableView
    @FXML private TableView<Customer> tableViewCustomer;
    @FXML private TableColumn<Customer, Integer> idColumn;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, String> addressColumn;
    @FXML private TableColumn<Customer, String> phoneColumn;
    @FXML private TableColumn<Customer, String> cityColumn;
    @FXML private TableColumn<Customer, String> countryColumn;
    
    
    // appointments tableView
    @FXML private TableView<Appointment> tableViewAppointments;
    @FXML private TableColumn<Appointment, String> customerNameColumn;
    @FXML private TableColumn<Appointment, String> titleColumn;
    @FXML private TableColumn<Appointment, String> descriptionColumn;
    @FXML private TableColumn<Appointment, String> locationColumn;
    @FXML private TableColumn<Appointment, String> startTimeColumn;
    
    // error message label
    @FXML private Label message;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // make meesage invisible
        message.setOpacity(0.0);
        // TODO
    }
    
    
    @FXML
    public void loadAddCustomerScene(ActionEvent event) throws IOException{
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/viewcontroller/FXMLAddCustomer.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
    
    public void loadAddAppointmentsScene(ActionEvent event) throws IOException{
        
        // select a customer first
        Customer customer = tableViewCustomer.getSelectionModel().getSelectedItem();
        
        if (customer == null) {
            
            message.setOpacity(1.0);   
            return;
        }
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/viewcontroller/FXMLAddAppointment.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        FXMLAddAppointmentController addAppointmentController = loader.getController();
        addAppointmentController.initData(customer); // // pass the customer id
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
    
    
    // edit customer
    public void loadEditCustomerScene(ActionEvent event) throws IOException{
        
        // check if no customer is selected
        Customer customer = tableViewCustomer.getSelectionModel().getSelectedItem();
        
        if (customer == null)
            return;
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/viewcontroller/FXMLEditCustomer.fxml"));
        Parent root = loader.load();        
        Scene scene = new Scene(root);
        
        // populate the array inventory
        FXMLEditCustomerController editCustomerController = loader.getController();
        editCustomerController.initData(customer); // sent the customer to be edited screen
        
        // get the hold of the stage (window of the button) 
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();  
        stage.setScene(scene);
    }
    
    // delete customer
    public void deleteCustomer() {
        
        // check if an customer is selected from tableview
        Customer customer = tableViewCustomer.getSelectionModel().getSelectedItem();
        
        if(customer == null){

            return;
        }
            
        
        // otherwise delete the customer
        DBConnection.deleteCustomer(customer.getId());
        
        // reload the tableview
        loadCustomerTableView();
        loadAppointmentsTableView();
    }
    
    public void deleteAppointment() {
        
        Appointment appointment = tableViewAppointments.getSelectionModel().getSelectedItem();
        
        if(appointment == null){

            return;
        }
        
        // otherwise delete the customer
        DBConnection.deleteAppointment(appointment.getAppointmentId());
        
        // reload the tableview
        loadAppointmentsTableView();
    }

    
    public void loadAppointmentsTableView() {
        
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        
        tableViewAppointments.setItems(getAppointments());
        
    }
    
    private ObservableList<Appointment> getAppointments() {
        
        return FXCollections.observableArrayList(DBConnection.getAllAppointments());
        
    }
    
    public void loadCustomerTableView() {
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("City"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("Country"));
        
        tableViewCustomer.setItems(getCustomers());
    }

    private ObservableList<Customer> getCustomers() {
        
        // should get this from DB
        return FXCollections.observableArrayList(DBConnection.getAllCustomers());
    }
}
