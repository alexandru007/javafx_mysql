/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewcontroller;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import scheduleapp.DBConnection;


public class FXMLAddAppointmentController implements Initializable {

    @FXML private Button addAppointment;
    @FXML private Button cancelAppointment;
    
    @FXML private TextField custometNameTextField;
    @FXML private TextField typeTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField locationTextField;
    
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> comboBoxHour;
    @FXML private ComboBox<String> comboBoxMin;
    
    ObservableList<String> hours = FXCollections.observableArrayList();
    ObservableList<String> minutes = FXCollections.observableArrayList();
    @FXML private Label messageLabel;
    
    private Customer customer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        hours.addAll("09", "10", "11", "12", "13", "14", "15", "16");
        minutes.addAll("00", "15", "30", "45");
        comboBoxHour.setItems(hours);
        comboBoxMin.setItems(minutes);
        
    }
    
    public void initData(Customer customer) {
        
        this.customer = customer;
        
        // display customer name
        custometNameTextField.setText(this.customer.getName());
    }
    
    public void addAppointment(ActionEvent event) {
        
        //check if fields are empty
        if (typeTextField.getText().equals("") ||
            descriptionTextField.getText().equals("") ||
            locationTextField.getText().equals("") ||
            comboBoxHour.getValue() == null || 
            comboBoxMin.getValue() == null || 
            datePicker.getValue() == null){
            
            dialogBoxAlert("Please enter all required fields");
            return;
        
        }
        
        // add appointment
        Appointment appointment = new Appointment();
        
        appointment.setCustomerId(customer.getId());
        appointment.setCustomerName(customer.getName());
        appointment.setType(typeTextField.getText());
        appointment.setDescription(descriptionTextField.getText());
        appointment.setLocation(locationTextField.getText());
        
        LocalDate date = datePicker.getValue();
        LocalDateTime localDateTime = LocalDateTime.of(date.getYear(), date.getMonthValue(),
                date.getDayOfMonth(), Integer.parseInt(comboBoxHour.getValue()), Integer.parseInt(comboBoxMin.getValue()));
        
        appointment.setStartTime(localDateTime.toString());
        
        try {
            // check if appointment is overlapping
            if(DBConnection.appointmentTimeExists(appointment)) {
                // alert the user
                dialogBoxAlert("This appointment is overlapping with an existing one");
                
                return;
            }
            
            DBConnection.addAppointment(appointment);
            messageLabel.setText("appointment added");
        } catch (Exception e) {
            messageLabel.setText("unable to add a appointment");
        }
        
        // go to main screen
        try {
            goToMainScreen(event);
        } catch (Exception e) {
        }
        
    }
    
    public void dialogBoxAlert(String alertText) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setContentText(alertText);
        alert.showAndWait();
    }
    
    public void goToMainScreen(ActionEvent event) throws IOException {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/viewcontroller/FXMLCustomer.fxml"));
        Parent root = loader.load();        
        Scene scene = new Scene(root);
        
        FXMLCustomerController customerController = loader.getController();
        customerController.loadCustomerTableView();
        customerController.loadAppointmentsTableView();
        
        // get the hold of the stage (window of the button) 
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();  
        stage.setScene(scene);
    }
    
}
