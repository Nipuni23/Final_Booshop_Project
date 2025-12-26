package lk.ijse.bookshop1.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.bookshop1.dto.SupplierDTO;
import lk.ijse.bookshop1.model.SupplierModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {

    @FXML
    private ComboBox<Integer> cmbSupplierId;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField contactField;

    @FXML
    private TableView<SupplierDTO> supplierTable;
    @FXML
    private TableColumn<SupplierDTO, Integer> colId;
    @FXML
    private TableColumn<SupplierDTO, String> colName;
    @FXML
    private TableColumn<SupplierDTO, String> colEmail;
    @FXML
    private TableColumn<SupplierDTO, String> colContact;

    private ObservableList<SupplierDTO> supplierList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("sup_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("sup_name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("sup_email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact_number"));

        supplierTable.setItems(supplierList);

        try {
            loadSupplierIds();
            loadAllSuppliers();
        } catch (SQLException | ClassNotFoundException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load data: " + ex.getMessage());
        }

        supplierTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        populateFields(newValue);
                    }
                }
        );

        cmbSupplierId.setOnAction(event -> {
            try {
                loadSupplierData();
            } catch (SQLException | ClassNotFoundException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load supplier: " + e.getMessage());
            }
        });
    }

    private void loadSupplierIds() throws SQLException, ClassNotFoundException {
        List<Integer> supplierIds = SupplierModel.getSupplierIds();
        ObservableList<Integer> idList = FXCollections.observableArrayList(supplierIds);
        cmbSupplierId.setItems(idList);
    }

    private void loadAllSuppliers() throws SQLException, ClassNotFoundException {
        supplierList.clear();
        List<SupplierDTO> suppliers = SupplierModel.getAllSuppliers();
        supplierList.addAll(suppliers);
    }

    private void loadSupplierData() throws SQLException, ClassNotFoundException {
        Integer selectedId = cmbSupplierId.getValue();
        if (selectedId != null) {
            SupplierDTO supplier = SupplierModel.getSupplierById(selectedId);
            if (supplier != null) {
                nameField.setText(supplier.getSup_name());
                emailField.setText(supplier.getSup_email());
                contactField.setText(supplier.getContact_number());
            }
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            if (nameField.getText().isEmpty() || contactField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Name and Contact are required!");
                return;
            }

            SupplierDTO supplier = new SupplierDTO();
            supplier.setSup_name(nameField.getText());
            supplier.setSup_email(emailField.getText());
            supplier.setContact_number(contactField.getText());

            SupplierModel supplierModel = new SupplierModel();
            boolean isSaved = supplierModel.saveSupplier(supplier);

            if (isSaved) {
                loadAllSuppliers();
                loadSupplierIds();
                handleReset(null);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier saved successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save supplier!");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save supplier: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        try {
            Integer selectedId = cmbSupplierId.getValue();
            if (selectedId == null) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a supplier to update!");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Update");
            confirmAlert.setHeaderText("Update Supplier");
            confirmAlert.setContentText("Are you sure you want to update this supplier?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                SupplierDTO supplier = new SupplierDTO();
                supplier.setSup_id(selectedId);
                supplier.setSup_name(nameField.getText());
                supplier.setSup_email(emailField.getText());
                supplier.setContact_number(contactField.getText());

                boolean isUpdated = SupplierModel.updateSupplier(supplier);

                if (isUpdated) {
                    loadAllSuppliers();
                    handleReset(null);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier updated successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update supplier!");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update supplier: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        try {
            Integer selectedId = cmbSupplierId.getValue();
            if (selectedId == null) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a supplier to delete!");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Delete Supplier");
            confirmAlert.setContentText("Are you sure you want to delete this supplier?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean isDeleted = SupplierModel.deleteSupplier(selectedId);

                if (isDeleted) {
                    loadAllSuppliers();
                    loadSupplierIds();
                    handleReset(null);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier deleted successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete supplier!");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete supplier: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReset(ActionEvent event) {
        cmbSupplierId.setValue(null);
        nameField.clear();
        emailField.clear();
        contactField.clear();
        supplierTable.getSelectionModel().clearSelection();
    }

    private void populateFields(SupplierDTO supplier) {
        cmbSupplierId.setValue(supplier.getSup_id());
        nameField.setText(supplier.getSup_name());
        emailField.setText(supplier.getSup_email());
        contactField.setText(supplier.getContact_number());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}