package lk.ijse.bookshop1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.bookshop1.App;
import lk.ijse.bookshop1.dto.UserDTO;
import lk.ijse.bookshop1.model.UserModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblMessage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set default focus
        txtUsername.requestFocus();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Please enter username and password!");
            lblMessage.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            UserDTO user = UserModel.validateUser(username, password);

            if (user != null) {
                if ("active".equals(user.getUser_status())) {
                    lblMessage.setText("Login successful!");
                    lblMessage.setStyle("-fx-text-fill: green;");

                    // Load main layout
                    Parent root = App.loadFXML("Layout");
                    Scene scene = new Scene(root, 1000, 600);
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setTitle("Bookshop Management System");
                    stage.show();
                } else {
                    lblMessage.setText("User account is inactive!");
                    lblMessage.setStyle("-fx-text-fill: red;");
                }
            } else {
                lblMessage.setText("Invalid username or password!");
                lblMessage.setStyle("-fx-text-fill: red;");
            }
        } catch (SQLException | ClassNotFoundException e) {
            lblMessage.setText("Database error: " + e.getMessage());
            lblMessage.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        } catch (IOException e) {
            lblMessage.setText("Failed to load main window!");
            lblMessage.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }
}