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
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    public void loadCustomerScreen (ActionEvent event) throws IOException {
        
        // before loading customer screen try to log in
        if (checkIfValidUser()) {
            
            // this is to keep track current user that is logged in
            UserClass.setCurrentUserID(getUserID(usernameTextField.getText()));
            
            // get the hold of the stage (window of the button) 
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/viewcontroller/FXMLCustomer.fxml"));
            Parent root = loader.load();        
            Scene scene = new Scene(root);
            
            // load customer tableview
            FXMLCustomerController customerController = loader.getController();
            customerController.loadCustomerTableView();
            
            // get the hold of the stage (window of the button) 
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        }
        else {
            // display error message in 2 languages
            errorMessageLabel.setText("wrong user");
        }
        
        
    }
    
    public boolean checkIfValidUser() {
        
        // get values from textfieds
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        
        // compare the values with db
        String usernameDB = "test";
        String passwordDB = "test";
        
        return true;
    }
    
    public int getUserID(String username) {
        
        // get the id from the DB
        // select userId from user where userName = username
        
        return 0;
        
    }
    
}
