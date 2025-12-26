package lk.ijse.bookshop1.model;

import lk.ijse.bookshop1.dto.OrderItemDTO;
import lk.ijse.bookshop1.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemModel {

    // Save order item
    public static boolean saveOrderItem(OrderItemDTO orderItemDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO order_item (order_id, item_id, qty, unit_price) VALUES (?,?,?,?)",
                orderItemDTO.getOrder_id(),
                orderItemDTO.getItem_id(),
                orderItemDTO.getQty(),
                orderItemDTO.getUnit_price()
        );
    }

    // Get all order items for a specific order
    public static List<OrderItemDTO> getOrderItemsByOrderId(Integer orderId) throws SQLException, ClassNotFoundException {
        List<OrderItemDTO> orderItems = new ArrayList<>();
        ResultSet rst = null;

        try {
            rst = CrudUtil.execute(
                    "SELECT oi.*, i.item_name FROM order_item oi " +
                            "INNER JOIN item i ON oi.item_id = i.item_id " +
                            "WHERE oi.order_id = ?",
                    orderId
            );

            while (rst.next()) {
                OrderItemDTO item = new OrderItemDTO(
                        rst.getInt("item_id"),
                        rst.getString("item_name"),
                        rst.getInt("qty"),
                        rst.getDouble("unit_price")
                );
                item.setOrder_id(orderId);
                orderItems.add(item);
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return orderItems;
    }

    // Delete all order items for a specific order
    public static boolean deleteOrderItemsByOrderId(Integer orderId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM order_item WHERE order_id=?", orderId);
    }

    // Update item stock after order
    public static boolean updateItemStock(Integer itemId, int qty) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE item SET quantity_in_stock = quantity_in_stock - ? WHERE item_id = ?",
                qty,
                itemId
        );
    }

    // Check if item has enough stock
    public static boolean checkStock(Integer itemId, int requestedQty) throws SQLException, ClassNotFoundException {
        ResultSet rst = null;
        try {
            rst = CrudUtil.execute(
                    "SELECT quantity_in_stock FROM item WHERE item_id = ?",
                    itemId
            );

            if (rst.next()) {
                int availableStock = rst.getInt("quantity_in_stock");
                return availableStock >= requestedQty;
            }
            return false;
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
    }
}