
package lk.ijse.bookshop1.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bookshop1.App;

public class LayoutController implements Initializable {
    @FXML
    private AnchorPane Maincontroller;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("Layout is loaded");
        
    }  
    @FXML
    private void clickCustomerNav() throws IOException {
      Parent customerFXML =  App.loadFXML("Customer");
      Maincontroller.getChildren().setAll(customerFXML);
    }
    
    @FXML
    private void clickItemNav() throws IOException {
      Parent customerFXML =  App.loadFXML("Item");
      Maincontroller.getChildren().setAll(customerFXML);  
    
    }
    
    @FXML
    private void clickOrderNav() throws IOException {
      Parent customerFXML =  App.loadFXML("Order");
      Maincontroller.getChildren().setAll(customerFXML);  
    }
    
    @FXML
    private void clickSupplierNav() throws IOException {
      Parent customerFXML =  App.loadFXML("Supplier");
      Maincontroller.getChildren().setAll(customerFXML);  
    }
    
    @FXML
    private void clickUserNav() throws IOException {
      Parent customerFXML =  App.loadFXML("User");
      Maincontroller.getChildren().setAll(customerFXML);  
    }
    
    @FXML
    private void clickEmployeeNav() throws IOException{
      Parent customerFXML =  App.loadFXML("Employee");
      Maincontroller.getChildren().setAll(customerFXML);  
    }

    @FXML
    private void logout(ActionEvent event) throws Exception {
        App.setRoot("login");
        System.out.println("Logout");
    }
    
    
}
