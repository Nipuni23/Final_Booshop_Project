package lk.ijse.bookshop1.model;

import lk.ijse.bookshop1.dto.SupplierDTO;
import lk.ijse.bookshop1.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierModel {

    // Save supplier - ID auto generates
    public boolean saveSupplier(SupplierDTO supplierDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO supplier (sup_name, sup_email, contact_number) VALUES (?,?,?)",
                supplierDTO.getSup_name(),
                supplierDTO.getSup_email(),
                supplierDTO.getContact_number()
        );
    }

    // Update supplier
    public static boolean updateSupplier(SupplierDTO supplierDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE supplier SET sup_name=?, sup_email=?, contact_number=? WHERE sup_id=?",
                supplierDTO.getSup_name(),
                supplierDTO.getSup_email(),
                supplierDTO.getContact_number(),
                supplierDTO.getSup_id()
        );
    }

    // Delete supplier
    public static boolean deleteSupplier(Integer supId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM supplier WHERE sup_id=?", supId);
    }

    // Get all supplier IDs
    public static List<Integer> getSupplierIds() throws SQLException, ClassNotFoundException {
        List<Integer> supIds = new ArrayList<>();
        ResultSet rst = null;

        try {
            rst = CrudUtil.execute("SELECT sup_id FROM supplier");
            while (rst.next()) {
                supIds.add(rst.getInt("sup_id"));
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return supIds;
    }

    // Get all suppliers
    public static List<SupplierDTO> getAllSuppliers() throws SQLException, ClassNotFoundException {
        List<SupplierDTO> suppliers = new ArrayList<>();
        ResultSet rst = null;

        try {
            rst = CrudUtil.execute("SELECT * FROM supplier");
            while (rst.next()) {
                SupplierDTO supplier = new SupplierDTO(
                        rst.getInt("sup_id"),
                        rst.getString("sup_name"),
                        rst.getString("sup_email"),
                        rst.getString("contact_number")
                );
                suppliers.add(supplier);
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return suppliers;
    }

    // Get supplier by ID
    public static SupplierDTO getSupplierById(Integer supId) throws SQLException, ClassNotFoundException {
        ResultSet rst = null;
        SupplierDTO supplier = null;

        try {
            rst = CrudUtil.execute("SELECT * FROM supplier WHERE sup_id=?", supId);
            if (rst.next()) {
                supplier = new SupplierDTO(
                        rst.getInt("sup_id"),
                        rst.getString("sup_name"),
                        rst.getString("sup_email"),
                        rst.getString("contact_number")
                );
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return supplier;
    }
}