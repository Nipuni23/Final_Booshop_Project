package lk.ijse.bookshop1.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.bookshop1.dto.ItemDTO;
import lk.ijse.bookshop1.dto.OrderDTO;
import lk.ijse.bookshop1.dto.OrderItemDTO;
import lk.ijse.bookshop1.model.ItemModel;
import lk.ijse.bookshop1.model.OrderModel;
import lk.ijse.bookshop1.model.OrderItemModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField totalAmountField;
    @FXML
    private ComboBox<String> statusComboBox;

    // Item selection fields
    @FXML
    private ComboBox<Integer> cmbItemId;
    @FXML
    private TextField txtItemName;
    @FXML
    private TextField txtUnitPrice;
    @FXML
    private TextField txtAvailableQty;
    @FXML
    private TextField txtQty;
    @FXML
    private TextField txtTotal;
    @FXML
    private Button btnAddToCart;

    @FXML
    private TableView<OrderDTO> orderTable;
    @FXML
    private TableColumn<OrderDTO, Integer> colId;
    @FXML
    private TableColumn<OrderDTO, String> colDate;
    @FXML
    private TableColumn<OrderDTO, Double> colTotalAmount;
    @FXML
    private TableColumn<OrderDTO, String> colStatus;

    // Order items table
    @FXML
    private TableView<OrderItemDTO> orderItemTable;
    @FXML
    private TableColumn<OrderItemDTO, Integer> colItemId;
    @FXML
    private TableColumn<OrderItemDTO, String> colItemName;
    @FXML
    private TableColumn<OrderItemDTO, Integer> colQty;
    @FXML
    private TableColumn<OrderItemDTO, Double> colUnitPrice;
    @FXML
    private TableColumn<OrderItemDTO, Double> colItemTotal;

    private ObservableList<OrderDTO> orderList = FXCollections.observableArrayList();
    private ObservableList<OrderItemDTO> orderItemList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize status combo box
        ObservableList<String> statusList = FXCollections.observableArrayList("Pending", "Completed", "Cancelled");
        statusComboBox.setItems(statusList);
        statusComboBox.setValue("Pending");

        // Set date picker to today
        datePicker.setValue(LocalDate.now());

        // Setup order table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("order_date"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        orderTable.setItems(orderList);

        // Setup order items table columns
        colItemId.setCellValueFactory(new PropertyValueFactory<>("item_id"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        colItemTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        orderItemTable.setItems(orderItemList);

        try {
            loadAllOrders();
            loadItemIds();
        } catch (SQLException | ClassNotFoundException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load data: " + ex.getMessage());
        }

        orderTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        populateFields(newValue);
                    }
                }
        );

        // Item selection listener
        cmbItemId.setOnAction(event -> {
            try {
                loadItemData();
            } catch (SQLException | ClassNotFoundException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load item: " + e.getMessage());
            }
        });

        // Quantity field listener
        txtQty.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateItemTotal();
        });
    }

    private void loadAllOrders() throws SQLException, ClassNotFoundException {
        orderList.clear();
        List<OrderDTO> orders = OrderModel.getAllOrders();
        orderList.addAll(orders);
    }

    private void loadItemIds() throws SQLException, ClassNotFoundException {
        List<Integer> itemIds = ItemModel.getItemIds();
        ObservableList<Integer> idList = FXCollections.observableArrayList(itemIds);
        cmbItemId.setItems(idList);
    }

    private void loadItemData() throws SQLException, ClassNotFoundException {
        Integer selectedId = cmbItemId.getValue();
        if (selectedId != null) {
            ItemDTO item = ItemModel.getItemById(selectedId);
            if (item != null) {
                txtItemName.setText(item.getItem_name());
                txtUnitPrice.setText(String.valueOf(item.getUnit_price()));
                txtAvailableQty.setText(String.valueOf(item.getQuantity_in_stock()));
                txtQty.clear();
                txtTotal.clear();
            }
        }
    }

    private void calculateItemTotal() {
        try {
            if (!txtUnitPrice.getText().isEmpty() && !txtQty.getText().isEmpty()) {
                double unitPrice = Double.parseDouble(txtUnitPrice.getText());
                int qty = Integer.parseInt(txtQty.getText());
                double total = unitPrice * qty;
                txtTotal.setText(String.format("%.2f", total));
            }
        } catch (NumberFormatException e) {
            txtTotal.clear();
        }
    }

    @FXML
    private void handleAddToCart(ActionEvent event) {
        try {
            if (cmbItemId.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Please select an item!");
                return;
            }

            if (txtQty.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Please enter quantity!");
                return;
            }

            int requestedQty = Integer.parseInt(txtQty.getText());
            int availableQty = Integer.parseInt(txtAvailableQty.getText());

            if (requestedQty <= 0) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Quantity must be greater than 0!");
                return;
            }

            if (requestedQty > availableQty) {
                showAlert(Alert.AlertType.WARNING, "Insufficient Stock",
                        "Available quantity: " + availableQty);
                return;
            }

            // Check if item already in cart
            Integer itemId = cmbItemId.getValue();
            for (OrderItemDTO existingItem : orderItemList) {
                if (existingItem.getItem_id().equals(itemId)) {
                    showAlert(Alert.AlertType.WARNING, "Duplicate Item",
                            "Item already in cart! Remove it first to add again.");
                    return;
                }
            }

            // Add to cart
            OrderItemDTO orderItem = new OrderItemDTO(
                    itemId,
                    txtItemName.getText(),
                    requestedQty,
                    Double.parseDouble(txtUnitPrice.getText())
            );

            orderItemList.add(orderItem);
            calculateOrderTotal();
            clearItemFields();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation", "Please enter valid numbers!");
        }
    }

    @FXML
    private void handleRemoveFromCart(ActionEvent event) {
        OrderItemDTO selectedItem = orderItemTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            orderItemList.remove(selectedItem);
            calculateOrderTotal();
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item to remove!");
        }
    }

    private void calculateOrderTotal() {
        double total = 0;
        for (OrderItemDTO item : orderItemList) {
            total += item.getTotal();
        }
        totalAmountField.setText(String.format("%.2f", total));
    }

    private void clearItemFields() {
        cmbItemId.setValue(null);
        txtItemName.clear();
        txtUnitPrice.clear();
        txtAvailableQty.clear();
        txtQty.clear();
        txtTotal.clear();
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            if (orderItemList.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Please add at least one item to the order!");
                return;
            }

            if (totalAmountField.getText().isEmpty() || datePicker.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Date and Total Amount are required!");
                return;
            }

            // Confirmation dialog
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Order");
            confirmAlert.setHeaderText("Place Order");
            confirmAlert.setContentText("Are you sure you want to place this order?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                OrderDTO order = new OrderDTO();
                LocalDate selectedDate = datePicker.getValue();
                LocalDateTime dateTime = LocalDateTime.of(selectedDate, LocalTime.now());
                order.setOrder_date(dateTime);
                order.setTotal_amount(Double.parseDouble(totalAmountField.getText()));
                order.setStatus(statusComboBox.getValue());

                // Save order with items using transaction
                Integer orderId = OrderModel.saveOrderWithItems(order, orderItemList);

                if (orderId != null) {
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "Order placed successfully! Order ID: " + orderId);
                    loadAllOrders();
                    loadItemIds(); // Refresh item list to show updated stock
                    handleReset(null);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to place order!");
                }
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("Insufficient stock")) {
                showAlert(Alert.AlertType.ERROR, "Stock Error", e.getMessage());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save order: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        try {
            if (idField.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an order to update!");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Update");
            confirmAlert.setHeaderText("Update Order");
            confirmAlert.setContentText("Are you sure you want to update this order status?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                OrderDTO order = new OrderDTO();
                order.setOrder_id(Integer.parseInt(idField.getText()));
                LocalDate selectedDate = datePicker.getValue();
                LocalDateTime dateTime = LocalDateTime.of(selectedDate, LocalTime.now());
                order.setOrder_date(dateTime);
                order.setTotal_amount(Double.parseDouble(totalAmountField.getText()));
                order.setStatus(statusComboBox.getValue());

                boolean isUpdated = OrderModel.updateOrder(order);

                if (isUpdated) {
                    loadAllOrders();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Order status updated successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update order!");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        try {
            if (idField.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an order to delete!");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Delete Order");
            confirmAlert.setContentText("Are you sure you want to delete this order? Stock will be restored.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean isDeleted = OrderModel.deleteOrder(Integer.parseInt(idField.getText()));

                if (isDeleted) {
                    loadAllOrders();
                    loadItemIds(); // Refresh to show restored stock
                    handleReset(null);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Order deleted and stock restored!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete order!");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReset(ActionEvent event) {
        idField.clear();
        datePicker.setValue(LocalDate.now());
        totalAmountField.clear();
        statusComboBox.setValue("Pending");
        orderTable.getSelectionModel().clearSelection();
        orderItemList.clear();
        clearItemFields();
    }

    @FXML
    private void handleViewOrderDetails(ActionEvent event) {
        OrderDTO selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            try {
                orderItemList.clear();
                List<OrderItemDTO> items = OrderItemModel.getOrderItemsByOrderId(selectedOrder.getOrder_id());
                orderItemList.addAll(items);

                if (items.isEmpty()) {
                    showAlert(Alert.AlertType.INFORMATION, "No Items", "This order has no items.");
                }
            } catch (SQLException | ClassNotFoundException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load order details: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an order to view details!");
        }
    }

    private void populateFields(OrderDTO order) {
        idField.setText(String.valueOf(order.getOrder_id()));
        datePicker.setValue(order.getOrder_date().toLocalDate());
        totalAmountField.setText(String.valueOf(order.getTotal_amount()));
        statusComboBox.setValue(order.getStatus());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}