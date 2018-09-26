/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewcontroller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import scheduleapp.DBConnection;

/**
 * FXML Controller class
 *
 * @author alisii
 */
public class FXMLEditAppointmentController implements Initializable {

    
    @FXML private Button saveAppointment;
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
    
    Customer customer = new Customer();
    int appointmentId;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        hours.addAll("09", "10", "11", "12", "13", "14", "15", "16");
        minutes.addAll("00", "15", "30", "45");
        comboBoxHour.setItems(hours);
        comboBoxMin.setItems(minutes);
        
    }
    
    public void initData(Appointment appointment) {
        
        custometNameTextField.setText(appointment.getCustomerName());
        typeTextField.setText(appointment.getType());
        descriptionTextField.setText(appointment.getDescription());
        locationTextField.setText(appointment.getLocation());
        
        comboBoxHour.setValue("09");
        comboBoxMin.setValue("00");
        
        this.customer.setId(appointment.getCustomerId());
        this.appointmentId = appointment.getAppointmentId();// was passed from the tableview
    }
    
    public void editAppointment(ActionEvent event) throws IOException {
        
        //check if fields are empty
        if (typeTextField.getText().equals("") ||
            descriptionTextField.getText().equals("") ||
            locationTextField.getText().equals("") ||
            comboBoxHour.getValue() == null || 
            comboBoxMin.getValue() == null || 
            datePicker.getValue() == null){
            
            messageLabel.setText("Enter appointments details");
            return;
        
        }
        
        // add appointment
        Appointment appointment = new Appointment();
        
        appointment.setAppointmentId(this.appointmentId);
        appointment.setCustomerId(this.customer.getId());
        appointment.setCustomerName(this.customer.getName());
        appointment.setType(typeTextField.getText());
        appointment.setDescription(descriptionTextField.getText());
        appointment.setLocation(locationTextField.getText());
        
        LocalDate date = datePicker.getValue();
        LocalDateTime localDateTime = LocalDateTime.of(date.getYear(), date.getMonthValue(),
                date.getDayOfMonth(), Integer.parseInt(comboBoxHour.getValue()), Integer.parseInt(comboBoxMin.getValue()));
        
        appointment.setStartTime(localDateTime.toString());
        
        try {
            DBConnection.editAppointment(appointment);
        } catch (Exception e) {
            messageLabel.setText("Unable to save a appointment");
        }
        
        goToMainScreen(event);
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
