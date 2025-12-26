package lk.ijse.bookshop1.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.bookshop1.dto.ItemDTO;
import lk.ijse.bookshop1.model.ItemModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemController implements Initializable {

    @FXML
    private TextField idfield;
    @FXML
    private TextField namefield;
    @FXML
    private TextField categoryfield;
    @FXML
    private TextField unitpricefield;
    @FXML
    private TextField stockfield;
    @FXML
    private TextField supplierpricefield;
    @FXML
    private TextField orderlevelfield;
    @FXML
    private TextField descriptionfield;

    @FXML
    private TableView<ItemDTO> tableItem;
    @FXML
    private TableColumn<ItemDTO, Integer> colItemID;
    @FXML
    private TableColumn<ItemDTO, String> colItemName;
    @FXML
    private TableColumn<ItemDTO, String> colCategory;
    @FXML
    private TableColumn<ItemDTO, Double> colUnitPirce;
    @FXML
    private TableColumn<ItemDTO, Integer> colStock;
    @FXML
    private TableColumn<ItemDTO, Double> colSupplierPrice;
    @FXML
    private TableColumn<ItemDTO, String> colOrderLevel;
    @FXML
    private TableColumn<ItemDTO, String> colDescription;

    private ObservableList<ItemDTO> itemList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colItemID.setCellValueFactory(new PropertyValueFactory<>("item_id"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colUnitPirce.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("quantity_in_stock"));

        tableItem.setItems(itemList);

        try {
            loadAllItems();
        } catch (SQLException | ClassNotFoundException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load data: " + ex.getMessage());
        }

        tableItem.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        populateFields(newValue);
                    }
                }
        );
    }

    private void loadAllItems() throws SQLException, ClassNotFoundException {
        itemList.clear();
        List<ItemDTO> items = ItemModel.getAllItems();
        itemList.addAll(items);
    }

    @FXML
    void handleSaveItem(ActionEvent event) {
        try {
            if (namefield.getText().isEmpty() || categoryfield.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Name and Category are required!");
                return;
            }

            ItemDTO item = new ItemDTO();
            item.setItem_name(namefield.getText());
            item.setCategory(categoryfield.getText());
            item.setUnit_price(Double.parseDouble(unitpricefield.getText()));
            item.setQuantity_in_stock(Integer.parseInt(stockfield.getText()));

            ItemModel itemModel = new ItemModel();
            boolean isSaved = itemModel.saveItem(item);

            if (isSaved) {
                loadAllItems();
                handleReset(null);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Item saved successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save item!");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter valid numbers for price and stock!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleUpdateItem(ActionEvent event) {
        try {
            if (idfield.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item to update!");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Update");
            confirmAlert.setHeaderText("Update Item");
            confirmAlert.setContentText("Are you sure you want to update this item?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                ItemDTO item = new ItemDTO();
                item.setItem_id(Integer.parseInt(idfield.getText()));
                item.setItem_name(namefield.getText());
                item.setCategory(categoryfield.getText());
                item.setUnit_price(Double.parseDouble(unitpricefield.getText()));
                item.setQuantity_in_stock(Integer.parseInt(stockfield.getText()));

                boolean isUpdated = ItemModel.updateItem(item);

                if (isUpdated) {
                    loadAllItems();
                    handleReset(null);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Item updated successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update item!");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteItem(ActionEvent event) {
        try {
            if (idfield.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item to delete!");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Delete Item");
            confirmAlert.setContentText("Are you sure you want to delete this item?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean isDeleted = ItemModel.deleteItem(Integer.parseInt(idfield.getText()));

                if (isDeleted) {
                    loadAllItems();
                    handleReset(null);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Item deleted successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete item!");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleReset(ActionEvent event) {
        idfield.clear();
        namefield.clear();
        categoryfield.clear();
        unitpricefield.clear();
        stockfield.clear();
        supplierpricefield.clear();
        orderlevelfield.clear();
        descriptionfield.clear();
        tableItem.getSelectionModel().clearSelection();
    }

    private void populateFields(ItemDTO item) {
        idfield.setText(String.valueOf(item.getItem_id()));
        namefield.setText(item.getItem_name());
        categoryfield.setText(item.getCategory());
        unitpricefield.setText(String.valueOf(item.getUnit_price()));
        stockfield.setText(String.valueOf(item.getQuantity_in_stock()));
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}