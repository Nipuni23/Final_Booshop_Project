package lk.ijse.bookshop1.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.bookshop1.dto.UserDTO;
import lk.ijse.bookshop1.model.UserModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    private ComboBox<Integer> cmbUserId;
    @FXML
    private TextField nameField;
    @FXML
    private TextField passwordField;
    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private TableView<UserDTO> userTable;
    @FXML
    private TableColumn<UserDTO, Integer> colId;
    @FXML
    private TableColumn<UserDTO, String> colName;
    @FXML
    private TableColumn<UserDTO, String> colPassword;
    @FXML
    private TableColumn<UserDTO, String> colStatus;

    private ObservableList<UserDTO> userList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("User FXML is loaded!");

        // Initialize status combo box
        ObservableList<String> statusList = FXCollections.observableArrayList("active", "inactive");
        statusComboBox.setItems(statusList);
        statusComboBox.setValue("active");

        colId.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("user_name"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("user_password"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("user_status"));

        userTable.setItems(userList);

        try {
            loadUserIds();
            loadAllUsers();
        } catch (SQLException | ClassNotFoundException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load data: " + ex.getMessage());
        }

        userTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        populateFields(newValue);
                    }
                }
        );

        cmbUserId.setOnAction(event -> {
            try {
                loadUserData();
            } catch (SQLException | ClassNotFoundException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load user: " + e.getMessage());
            }
        });
    }

    private void loadUserIds() throws SQLException, ClassNotFoundException {
        List<Integer> userIds = UserModel.getUserIds();
        ObservableList<Integer> idList = FXCollections.observableArrayList(userIds);
        cmbUserId.setItems(idList);
    }

    private void loadAllUsers() throws SQLException, ClassNotFoundException {
        userList.clear();
        List<UserDTO> users = UserModel.getAllUsers();
        userList.addAll(users);
    }

    private void loadUserData() throws SQLException, ClassNotFoundException {
        Integer selectedId = cmbUserId.getValue();
        if (selectedId != null) {
            UserDTO user = UserModel.getUserById(selectedId);
            if (user != null) {
                nameField.setText(user.getUser_name());
                passwordField.setText(user.getUser_password());
                statusComboBox.setValue(user.getUser_status());
            }
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            if (nameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Name and Password are required!");
                return;
            }

            UserDTO user = new UserDTO();
            user.setUser_name(nameField.getText());
            user.setUser_password(passwordField.getText());
            user.setUser_status(statusComboBox.getValue());

            UserModel userModel = new UserModel();
            boolean isSaved = userModel.saveUser(user);

            if (isSaved) {
                loadAllUsers();
                loadUserIds();
                handleReset(null);
                showAlert(Alert.AlertType.INFORMATION, "Success", "User saved successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save user!");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        try {
            Integer selectedId = cmbUserId.getValue();
            if (selectedId == null) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to update!");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Update");
            confirmAlert.setHeaderText("Update User");
            confirmAlert.setContentText("Are you sure you want to update this user?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                UserDTO user = new UserDTO();
                user.setUser_id(selectedId);
                user.setUser_name(nameField.getText());
                user.setUser_password(passwordField.getText());
                user.setUser_status(statusComboBox.getValue());

                boolean isUpdated = UserModel.updateUser(user);

                if (isUpdated) {
                    loadAllUsers();
                    handleReset(null);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user!");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        try {
            Integer selectedId = cmbUserId.getValue();
            if (selectedId == null) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to delete!");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Delete User");
            confirmAlert.setContentText("Are you sure you want to delete this user?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean isDeleted = UserModel.deleteUser(selectedId);

                if (isDeleted) {
                    loadAllUsers();
                    loadUserIds();
                    handleReset(null);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete user!");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReset(ActionEvent event) {
        cmbUserId.setValue(null);
        nameField.clear();
        passwordField.clear();
        statusComboBox.setValue("active");
        userTable.getSelectionModel().clearSelection();
    }

    private void populateFields(UserDTO user) {
        cmbUserId.setValue(user.getUser_id());
        nameField.setText(user.getUser_name());
        passwordField.setText(user.getUser_password());
        statusComboBox.setValue(user.getUser_status());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}