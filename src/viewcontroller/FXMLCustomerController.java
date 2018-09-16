/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewcontroller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import scheduleapp.DBConnection;

/**
 * FXML Controller class
 *
 * @author alisii
 */
public class FXMLCustomerController implements Initializable {

    
    // customer tableView
    @FXML private TableView<Customer> tableViewCustomer;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, String> addressColumn;
    @FXML private TableColumn<Customer, String> cityColumn;
    @FXML private TableColumn<Customer, String> countryColumn;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void loadCustomerTableView() {
        
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        //addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        //cityColumn.setCellValueFactory(new PropertyValueFactory<>("City"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("Country"));
        
        tableViewCustomer.setItems(getCustomers());
    }

    private ObservableList<Customer> getCustomers() {
        
        // should get this from DB
        return FXCollections.observableArrayList(DBConnection.getAllCustomers());
    }
}
