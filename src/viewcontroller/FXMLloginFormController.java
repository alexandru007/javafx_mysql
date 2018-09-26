/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewcontroller;

import java.util.Locale;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import scheduleapp.DBConnection;
import scheduleapp.UserClass;


/**
 * FXML Controller class
 *
 * @author alisii
 */
public class FXMLloginFormController implements Initializable {

    // login text fields
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    
    // log in labels
    @FXML private Label errorMessageLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    
    // log in button
    @FXML private Button loginButton;
    
    // locale
    Locale currentLocale;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Locale.setDefault(new Locale("ro")); // uncoment to test the romanian
        
        currentLocale = Locale.getDefault();
        
        if (currentLocale.getLanguage()== "ro") {
            
            // change labels to romanian
            usernameLabel.setText("utilizator");
            passwordLabel.setText("parola");
            loginButton.setText("Logeaza-te");
        }
    }
    
    @FXML
    public void loadCustomerScreen (ActionEvent event) throws IOException {
        
        // before loading customer screen try to log in
        if (checkIfValidUser()) {
            
            // get the hold of the stage (window of the button) 
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/viewcontroller/FXMLCustomer.fxml"));
            Parent root = loader.load();        
            Scene scene = new Scene(root);
            
            // load customer tableview
            FXMLCustomerController customerController = loader.getController();
            customerController.loadCustomerTableView();
            customerController.loadAppointmentsTableView();
            
            // get the hold of the stage (window of the button) 
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        }
        else {
            
            currentLocale = Locale.getDefault();
            
            if (currentLocale.getLanguage() == "ro") {

                // change labels to romanian
                errorMessageLabel.setText("Gresit utilizator, introduceti datele inca odata");
            }
            else{
                errorMessageLabel.setText("The username and password did not match");
            }
            
            // empty the fields
            usernameTextField.setText("");
            passwordTextField.setText("");
        }
        
        
    }
    
    public boolean checkIfValidUser() {
        
        // get values from textfieds
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        
        // compare the values with db
        return true;
        //return DBConnection.isUserValid(username, password);
    }
}
