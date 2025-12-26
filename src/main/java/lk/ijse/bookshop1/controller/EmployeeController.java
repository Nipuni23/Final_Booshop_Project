package lk.ijse.bookshop1.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.bookshop1.dto.EmployeeDTO;
import lk.ijse.bookshop1.model.EmployeeModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private TextField idfild;
    @FXML
    private TextField namefild;
    @FXML
    private TextField usernamefild;
    @FXML
    private TextField passwordfild;
    @FXML
    private TextField rolefild;

    @FXML
    private TableView<EmployeeDTO> employeeTable;
    @FXML
    private TableColumn<EmployeeDTO, Integer> colId;
    @FXML
    private TableColumn<EmployeeDTO, String> colName;
    @FXML
    private TableColumn<EmployeeDTO, String> colUsername;
    @FXML
    private TableColumn<EmployeeDTO, String> colPassword;
    @FXML
    private TableColumn<EmployeeDTO, String> colRole;

    private ObservableList<EmployeeDTO> employeeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("emp_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("emp_name"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("contact_number"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        employeeTable.setItems(employeeList);

        try {
            loadAllEmployees();
        } catch (SQLException | ClassNotFoundException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load data: " + ex.getMessage());
        }

        employeeTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        populateFields(newValue);
                    }
                }
        );
    }

    private void loadAllEmployees() throws SQLException, ClassNotFoundException {
        employeeList.clear();
        List<EmployeeDTO> employees = EmployeeModel.getAllEmployees();
        employeeList.addAll(employees);
    }

    @FXML
    private void handleSaveEmployee(ActionEvent event) {
        try {
            if (namefild.getText().isEmpty() || passwordfild.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Name and Password are required!");
                return;
            }

            EmployeeDTO employee = new EmployeeDTO();
            employee.setEmp_name(namefild.getText());
            employee.setPassword(passwordfild.getText());
            employee.setRole(rolefild.getText());
            employee.setContact_number(usernamefild.getText());

            EmployeeModel employeeModel = new EmployeeModel();
            boolean isSaved = employeeModel.saveEmployee(employee);

            if (isSaved) {
                loadAllEmployees();
                handleReset(null);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee saved successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save employee!");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save employee: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateEmployee(ActionEvent event) {
        try {
            if (idfild.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee to update!");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Update");
            confirmAlert.setHeaderText("Update Employee");
            confirmAlert.setContentText("Are you sure you want to update this employee?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                EmployeeDTO employee = new EmployeeDTO();
                employee.setEmp_id(Integer.parseInt(idfild.getText()));
                employee.setEmp_name(namefild.getText());
                employee.setPassword(passwordfild.getText());
                employee.setRole(rolefild.getText());
                employee.setContact_number(usernamefild.getText());

                boolean isUpdated = EmployeeModel.updateEmployee(employee);

                if (isUpdated) {
                    loadAllEmployees();
                    handleReset(null);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Employee updated successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update employee!");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update employee: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteEmployee(ActionEvent event) {
        try {
            if (idfild.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee to delete!");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Delete Employee");
            confirmAlert.setContentText("Are you sure you want to delete this employee?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean isDeleted = EmployeeModel.deleteEmployee(Integer.parseInt(idfild.getText()));

                if (isDeleted) {
                    loadAllEmployees();
                    handleReset(null);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Employee deleted successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete employee!");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete employee: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReset(ActionEvent event) {
        idfild.clear();
        namefild.clear();
        usernamefild.clear();
        passwordfild.clear();
        rolefild.clear();
        employeeTable.getSelectionModel().clearSelection();
    }

    private void populateFields(EmployeeDTO employee) {
        idfild.setText(String.valueOf(employee.getEmp_id()));
        namefild.setText(employee.getEmp_name());
        usernamefild.setText(employee.getContact_number());
        passwordfild.setText(employee.getPassword());
        rolefild.setText(employee.getRole());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}