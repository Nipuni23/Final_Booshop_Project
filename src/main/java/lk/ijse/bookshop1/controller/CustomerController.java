package lk.ijse.bookshop1.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bookshop1.dto.CustomerDTO;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import lk.ijse.bookshop1.model.CustomerModel;
import net.sf.jasperreports.engine.JRException;

public class CustomerController implements Initializable {

    @FXML
    private ComboBox<Integer> cmbCustomerID;

    @FXML
    private AnchorPane Customercontrol;

    @FXML
    private TextField nameField;

    @FXML
    private TextField contactField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField addressField;

    @FXML
    private Button updateBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private TableView<CustomerDTO> customerTable;

    @FXML
    private TableColumn<CustomerDTO, Integer> colId;

    @FXML
    private TableColumn<CustomerDTO, String> colName;

    @FXML
    private TableColumn<CustomerDTO, String> colPhone;

    @FXML
    private TableColumn<CustomerDTO, String> colEmail;

    @FXML
    private TableColumn<CustomerDTO, String> colAddress;

    private ObservableList<CustomerDTO> customerList = FXCollections.observableArrayList();
    private int nextId = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("cust_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("cus_name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("cus_phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("cus_email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("cus_address"));

        customerTable.setItems(customerList);

        // Load customer IDs into ComboBox
        try {
            loadCustomerIds();
            loadAllCustomers();
        } catch (SQLException | ClassNotFoundException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load data: " + ex.getMessage());
        }

        // When a customer is selected from table, populate fields
        customerTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        populateFields(newValue);
                    }
                }
        );

        // When customer ID is selected from ComboBox, load that customer's data
        cmbCustomerID.setOnAction(event -> {
            try {
                loadCustomerData();
            } catch (SQLException | ClassNotFoundException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load customer: " + e.getMessage());
            }
        });
    }

    // Load all customer IDs into the ComboBox
    private void loadCustomerIds() throws SQLException, ClassNotFoundException {
        List<Integer> customerIds = CustomerModel.getCustomerID();
        ObservableList<Integer> idList = FXCollections.observableArrayList(customerIds);
        cmbCustomerID.setItems(idList);
    }

    // Load all customers into the table
    private void loadAllCustomers() throws SQLException, ClassNotFoundException {
        customerList.clear();
        List<CustomerDTO> customers = CustomerModel.getAllCustomers();
        customerList.addAll(customers);
    }

    // Load customer data when ID is selected from ComboBox
    private void loadCustomerData() throws SQLException, ClassNotFoundException {
        Integer selectedId = cmbCustomerID.getValue();
        if (selectedId != null) {
            CustomerDTO customer = CustomerModel.getCustomerById(selectedId);
            if (customer != null) {
                nameField.setText(customer.getCus_name());
                contactField.setText(customer.getCus_phone());
                emailField.setText(customer.getCus_email());
                addressField.setText(customer.getCus_address());
            }
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            if (nameField.getText().isEmpty() || contactField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Name and Phone are required!");
                return;
            }

            // Create CustomerDTO object
            CustomerDTO customer = new CustomerDTO();
            customer.setCus_name(nameField.getText());
            customer.setCus_phone(contactField.getText());
            customer.setCus_email(emailField.getText());
            customer.setCus_address(addressField.getText());

            // Save to database
            CustomerModel customerModel = new CustomerModel();
            boolean isSaved = customerModel.clickSaveCustomer(customer);

            if (isSaved) {
                // Reload data
                loadAllCustomers();
                loadCustomerIds();
                handleReset(null);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer saved successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save customer to database!");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save customer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        try {
            // Check if customer ID is selected
            Integer selectedId = cmbCustomerID.getValue();
            if (selectedId == null) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a customer ID to update!");
                return;
            }

            // Validate input fields
            if (nameField.getText().isEmpty() || contactField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Name and Phone are required!");
                return;
            }

            // Confirmation dialog
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Update");
            confirmAlert.setHeaderText("Update Customer");
            confirmAlert.setContentText("Are you sure you want to update this customer?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                // Create CustomerDTO with updated values
                CustomerDTO customer = new CustomerDTO();
                customer.setCust_id(selectedId);
                customer.setCus_name(nameField.getText());
                customer.setCus_phone(contactField.getText());
                customer.setCus_email(emailField.getText());
                customer.setCus_address(addressField.getText());

                // Update in database
                boolean isUpdated = CustomerModel.clickUpdateCustomer(customer);

                if (isUpdated) {
                    // Reload table data
                    loadAllCustomers();
                    handleReset(null);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Customer updated successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update customer in database!");
                }
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update customer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        try {
            Integer selectedId = cmbCustomerID.getValue();
            if (selectedId == null) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a customer ID to delete!");
                return;
            }

            // Confirmation dialog
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Delete Customer");
            confirmAlert.setContentText("Are you sure you want to delete this customer?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                boolean isDeleted = CustomerModel.deleteCustomer(selectedId);

                if (isDeleted) {
                    loadAllCustomers();
                    loadCustomerIds();
                    handleReset(null);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Customer deleted successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete customer!");
                }
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete customer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReset(ActionEvent event) {
        cmbCustomerID.setValue(null);
        nameField.clear();
        contactField.clear();
        emailField.clear();
        addressField.clear();
        customerTable.getSelectionModel().clearSelection();
    }

    private void populateFields(CustomerDTO customer) {
        cmbCustomerID.setValue(customer.getCust_id());
        nameField.setText(customer.getCus_name());
        contactField.setText(customer.getCus_phone());
        emailField.setText(customer.getCus_email());
        addressField.setText(customer.getCus_address());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadSampleData() {
        // Load sample data if needed
    }

    public void printPDF(ActionEvent actionEvent) {
        CustomerModel cm = new CustomerModel();
        try {
            cm.printPDFReport();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed To Load Data");
        } catch (JRException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed To Load PDF");
        }

    }
}