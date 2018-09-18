/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewcontroller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;
import scheduleapp.DBConnection;

/**
 * FXML Controller class
 *
 * @author alisii
 */
public class FXMLEditCustomerController implements Initializable {

    @FXML private Button addCustomer;
    @FXML private TextField nameTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField phoneTextField;
    @FXML private TextField cityTextField;
    @FXML private TextField countryTextField;
    @FXML private Label errorMessageLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    // populate textFields
    public void initData(Customer customer) {
        
        nameTextField.setText(customer.getName());
        addressTextField.setText(customer.getAddress());
        phoneTextField.setText(customer.getPhone());
        cityTextField.setText(customer.getCity());
        countryTextField.setText(customer.getCountry());
        
    }
    
    public void editCustomer(ActionEvent event) {
        
        if (    nameTextField.getText().equals("") || 
                addressTextField.getText().equals("") ||
                phoneTextField.getText().equals("") || 
                cityTextField.getText().equals("") || 
                countryTextField.getText().equals("")){
            
            errorMessageLabel.setText("Please enter all required fields");
            
            return;
        }
        
        Customer customer = new Customer();
        
        customer.setName(nameTextField.getText());
        customer.setAddress(addressTextField.getText());
        customer.setPhone(phoneTextField.getText());
        customer.setCity(cityTextField.getText());
        customer.setCountry(countryTextField.getText());
        
        try {
            DBConnection.addCustomer(customer); // needs refactor, edit instead of add
            errorMessageLabel.setText("Customer saved");
        } catch (Exception e) {
            errorMessageLabel.setText("unable to save changes");
        }
        
        try {
            goToMainScreen(event);
        } catch (Exception e) {
        }
    }
    
    public void goToMainScreen(ActionEvent event) throws IOException {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View_Controller/FXMLCustomer.fxml"));
        Parent root = loader.load();        
        Scene scene = new Scene(root);
        
        FXMLCustomerController customerController = loader.getController();
        customerController.loadCustomerTableView();
        
        // get the hold of the stage (window of the button) 
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();  
        stage.setScene(scene);
    }
    
}
