package lk.ijse.bookshop1.model;

import lk.ijse.bookshop1.db.DBConnection;
import lk.ijse.bookshop1.dto.OrderDTO;
import lk.ijse.bookshop1.dto.OrderItemDTO;
import lk.ijse.bookshop1.util.CrudUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderModel {

    // Save order with transaction - returns order ID if successful
    public static Integer saveOrderWithItems(OrderDTO orderDTO, List<OrderItemDTO> orderItems) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            // Start transaction
            connection.setAutoCommit(false);

            // 1. Save order and get generated ID
            PreparedStatement orderStmt = connection.prepareStatement(
                    "INSERT INTO orders (order_date, total_amount, status) VALUES (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            orderStmt.setTimestamp(1, Timestamp.valueOf(orderDTO.getOrder_date()));
            orderStmt.setDouble(2, orderDTO.getTotal_amount());
            orderStmt.setString(3, orderDTO.getStatus());

            int affectedRows = orderStmt.executeUpdate();

            if (affectedRows == 0) {
                connection.rollback();
                return null;
            }

            // Get generated order ID
            ResultSet generatedKeys = orderStmt.getGeneratedKeys();
            Integer orderId = null;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            } else {
                connection.rollback();
                return null;
            }

            // 2. Save order items
            for (OrderItemDTO item : orderItems) {
                // Check stock availability
                if (!OrderItemModel.checkStock(item.getItem_id(), item.getQty())) {
                    connection.rollback();
                    throw new SQLException("Insufficient stock for item ID: " + item.getItem_id());
                }

                item.setOrder_id(orderId);
                boolean itemSaved = OrderItemModel.saveOrderItem(item);

                if (!itemSaved) {
                    connection.rollback();
                    return null;
                }

                // 3. Update item stock
                boolean stockUpdated = OrderItemModel.updateItemStock(item.getItem_id(), item.getQty());

                if (!stockUpdated) {
                    connection.rollback();
                    return null;
                }
            }

            // Commit transaction
            connection.commit();
            return orderId;

        } catch (SQLException e) {
            // Rollback on error
            connection.rollback();
            throw e;
        } finally {
            // Reset auto-commit
            connection.setAutoCommit(true);
        }
    }

    // Update order - simple update without items
    public static boolean updateOrder(OrderDTO orderDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE orders SET order_date=?, total_amount=?, status=? WHERE order_id=?",
                Timestamp.valueOf(orderDTO.getOrder_date()),
                orderDTO.getTotal_amount(),
                orderDTO.getStatus(),
                orderDTO.getOrder_id()
        );
    }

    // Delete order with transaction
    public static boolean deleteOrder(Integer orderId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);

            // 1. Get order items to restore stock
            List<OrderItemDTO> orderItems = OrderItemModel.getOrderItemsByOrderId(orderId);

            // 2. Restore stock for each item
            for (OrderItemDTO item : orderItems) {
                PreparedStatement stmt = connection.prepareStatement(
                        "UPDATE item SET quantity_in_stock = quantity_in_stock + ? WHERE item_id = ?"
                );
                stmt.setInt(1, item.getQty());
                stmt.setInt(2, item.getItem_id());
                stmt.executeUpdate();
            }

            // 3. Delete order items
            OrderItemModel.deleteOrderItemsByOrderId(orderId);

            // 4. Delete order
            boolean deleted = CrudUtil.execute("DELETE FROM orders WHERE order_id=?", orderId);

            if (deleted) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    // Get all order IDs
    public static List<Integer> getOrderIds() throws SQLException, ClassNotFoundException {
        List<Integer> orderIds = new ArrayList<>();
        ResultSet rst = null;

        try {
            rst = CrudUtil.execute("SELECT order_id FROM orders");
            while (rst.next()) {
                orderIds.add(rst.getInt("order_id"));
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return orderIds;
    }

    // Get all orders
    public static List<OrderDTO> getAllOrders() throws SQLException, ClassNotFoundException {
        List<OrderDTO> orders = new ArrayList<>();
        ResultSet rst = null;

        try {
            rst = CrudUtil.execute("SELECT * FROM orders ORDER BY order_id DESC");
            while (rst.next()) {
                OrderDTO order = new OrderDTO(
                        rst.getInt("order_id"),
                        rst.getTimestamp("order_date").toLocalDateTime(),
                        rst.getDouble("total_amount"),
                        rst.getString("status")
                );
                orders.add(order);
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return orders;
    }

    // Get order by ID
    public static OrderDTO getOrderById(Integer orderId) throws SQLException, ClassNotFoundException {
        ResultSet rst = null;
        OrderDTO order = null;

        try {
            rst = CrudUtil.execute("SELECT * FROM orders WHERE order_id=?", orderId);
            if (rst.next()) {
                order = new OrderDTO(
                        rst.getInt("order_id"),
                        rst.getTimestamp("order_date").toLocalDateTime(),
                        rst.getDouble("total_amount"),
                        rst.getString("status")
                );
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return order;
    }
}