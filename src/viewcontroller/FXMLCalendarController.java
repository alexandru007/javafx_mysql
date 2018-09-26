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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Appointment;
import scheduleapp.DBConnection;

/**
 * FXML Controller class
 *
 * @author alisii
 */
public class FXMLCalendarController implements Initializable {
    
    @FXML private TextArea textAreaMonth;
    @FXML private TextArea textAreaWeek;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        loadAppointmentsByMonth();
        loadAppointmentsByWeek();

    }
    
    // get all appointments
    public void loadAppointmentsByMonth() {
        
        ArrayList<Appointment> appointmentsPerMonth = DBConnection.getAppointmentsPerMonth();
        // display the calendar for the current month
        String appointments ="";
        
        for(Appointment app : appointmentsPerMonth) {
            appointments += app.getCustomerName() + " ";
            appointments += app.getType()+ " ";
            appointments += app.getDescription()+ " ";
            appointments += app.getLocation()+ " ";
            appointments += app.getStartTime()+ "\n";
        }
        
        textAreaMonth.setText(appointments);
    }
    
    // get all appointments
    public void loadAppointmentsByWeek() {
        
        ArrayList<Appointment> appointmentsPerWeek = DBConnection.getAppointmentsPerWeek();
        // display the calendar for the current month
        String appointments ="";
        
        for(Appointment app : appointmentsPerWeek) {
            appointments += app.getCustomerName() + " ";
            appointments += app.getType()+ " ";
            appointments += app.getDescription()+ " ";
            appointments += app.getLocation()+ " ";
            appointments += app.getStartTime()+ "\n";
        }
        
        textAreaWeek.setText(appointments);
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
