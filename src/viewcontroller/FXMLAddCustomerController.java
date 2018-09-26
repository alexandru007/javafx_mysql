/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewcontroller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.jar.Attributes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;
import scheduleapp.DBConnection;


public class FXMLAddCustomerController implements Initializable {

    @FXML private Button addCustomer;
    @FXML private TextField nameTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField phoneTextField;
    @FXML private TextField cityTextField;
    @FXML private TextField countryTextField;
    @FXML private Label messageLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void dialogBoxAlert(String alertText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setContentText(alertText);
        alert.showAndWait();
    }
    
    public void addCustomer() {
        
        if (nameTextField.getText().equals("")){    
            dialogBoxAlert("Name cannot be empty");
            return;
        }
        
        if (addressTextField.getText().equals("")){    
            dialogBoxAlert("address cannot be empty");
            return;
        }
        
        if (phoneTextField.getText().equals("")){    
            dialogBoxAlert("Phone cannot be empty");
            return;
        }
        
        if (cityTextField.getText().equals("")){    
            dialogBoxAlert("City cannot be empty");
            return;
        }
        
        if (countryTextField.getText().equals("")){    
            dialogBoxAlert("Country cannot be empty");
            return;
        }
        
        // otherwise add a new customer
        Customer customer = new Customer();
        
        customer.setName(nameTextField.getText());
        customer.setAddress(addressTextField.getText());
        customer.setPhone(phoneTextField.getText());
        customer.setCity(cityTextField.getText());
        customer.setCountry(countryTextField.getText());
        
        try {
            DBConnection.addCustomer(customer);
            messageLabel.setText("customer added");
        } catch (Exception e) {
            messageLabel.setText("unable to add a customer");
        }
        
        // clear all fiels
        customer.setName("");
        customer.setAddress("");
        customer.setPhone("");
        customer.setCity("");
        customer.setCountry("");
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
