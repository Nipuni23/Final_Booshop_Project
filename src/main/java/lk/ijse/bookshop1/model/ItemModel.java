package lk.ijse.bookshop1.model;

import lk.ijse.bookshop1.dto.ItemDTO;
import lk.ijse.bookshop1.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemModel {

    // Save item - ID auto generates
    public boolean saveItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO item (item_name, category, unit_price, quantity_in_stock) VALUES (?,?,?,?)",
                itemDTO.getItem_name(),
                itemDTO.getCategory(),
                itemDTO.getUnit_price(),
                itemDTO.getQuantity_in_stock()
        );
    }

    // Update item
    public static boolean updateItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE item SET item_name=?, category=?, unit_price=?, quantity_in_stock=? WHERE item_id=?",
                itemDTO.getItem_name(),
                itemDTO.getCategory(),
                itemDTO.getUnit_price(),
                itemDTO.getQuantity_in_stock(),
                itemDTO.getItem_id()
        );
    }

    // Delete item
    public static boolean deleteItem(Integer itemId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM item WHERE item_id=?", itemId);
    }

    // Get all item IDs
    public static List<Integer> getItemIds() throws SQLException, ClassNotFoundException {
        List<Integer> itemIds = new ArrayList<>();
        ResultSet rst = null;

        try {
            rst = CrudUtil.execute("SELECT item_id FROM item");
            while (rst.next()) {
                itemIds.add(rst.getInt("item_id"));
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return itemIds;
    }

    // Get all items
    public static List<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException {
        List<ItemDTO> items = new ArrayList<>();
        ResultSet rst = null;

        try {
            rst = CrudUtil.execute("SELECT * FROM item");
            while (rst.next()) {
                ItemDTO item = new ItemDTO(
                        rst.getInt("item_id"),
                        rst.getString("item_name"),
                        rst.getString("category"),
                        rst.getDouble("unit_price"),
                        rst.getInt("quantity_in_stock")
                );
                items.add(item);
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return items;
    }

    // Get item by ID
    public static ItemDTO getItemById(Integer itemId) throws SQLException, ClassNotFoundException {
        ResultSet rst = null;
        ItemDTO item = null;

        try {
            rst = CrudUtil.execute("SELECT * FROM item WHERE item_id=?", itemId);
            if (rst.next()) {
                item = new ItemDTO(
                        rst.getInt("item_id"),
                        rst.getString("item_name"),
                        rst.getString("category"),
                        rst.getDouble("unit_price"),
                        rst.getInt("quantity_in_stock")
                );
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return item;
    }
}