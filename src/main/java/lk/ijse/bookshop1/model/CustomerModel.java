package lk.ijse.bookshop1.model;

import lk.ijse.bookshop1.db.DBConnection;
import lk.ijse.bookshop1.dto.CustomerDTO;
import lk.ijse.bookshop1.util.CrudUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {

    // Save customer - ID auto generates
    public boolean clickSaveCustomer(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {
        boolean result = CrudUtil.execute(
                "INSERT INTO customer (cus_name, cus_address, cus_phone, cus_email) VALUES (?,?,?,?)",
                customerDTO.getCus_name(),
                customerDTO.getCus_address(),
                customerDTO.getCus_phone(),
                customerDTO.getCus_email()
        );
        return result;
    }

    // Update customer
    public static boolean clickUpdateCustomer(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {
        boolean result = CrudUtil.execute(
                "UPDATE customer SET cus_name=?, cus_address=?, cus_phone=?, cus_email=? WHERE cust_id=?",
                customerDTO.getCus_name(),
                customerDTO.getCus_address(),
                customerDTO.getCus_phone(),
                customerDTO.getCus_email(),
                customerDTO.getCust_id()
        );
        return result;
    }

    // Delete customer
    public static boolean deleteCustomer(int custId) throws SQLException, ClassNotFoundException {
        boolean result = CrudUtil.execute(
                "DELETE FROM customer WHERE cust_id=?",
                custId
        );
        return result;
    }

    // Get all customer IDs
    public static List<Integer> getCustomerID() throws SQLException, ClassNotFoundException {
        List<Integer> cusIds = new ArrayList<>();
        ResultSet rst = null;

        try {
            rst = CrudUtil.execute("SELECT cust_id FROM customer");
            while (rst.next()) {
                cusIds.add(rst.getInt("cust_id"));
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return cusIds;
    }

    // Get all customers
    public static List<CustomerDTO> getAllCustomers() throws SQLException, ClassNotFoundException {
        List<CustomerDTO> customers = new ArrayList<>();
        ResultSet rst = null;

        try {
            rst = CrudUtil.execute("SELECT * FROM customer");
            while (rst.next()) {
                CustomerDTO customer = new CustomerDTO(
                        rst.getInt("cust_id"),
                        rst.getString("cus_name"),
                        rst.getString("cus_phone"),
                        rst.getString("cus_email"),
                        rst.getString("cus_address")
                );
                customers.add(customer);
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return customers;
    }

    // Get customer by ID
    public static CustomerDTO getCustomerById(int custId) throws SQLException, ClassNotFoundException {
        ResultSet rst = null;
        CustomerDTO customer = null;

        try {
            rst = CrudUtil.execute("SELECT * FROM customer WHERE cust_id=?", custId);
            if (rst.next()) {
                customer = new CustomerDTO(
                        rst.getInt("cust_id"),
                        rst.getString("cus_name"),
                        rst.getString("cus_phone"),
                        rst.getString("cus_email"),
                        rst.getString("cus_address")
                );
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return customer;
    }

    public void printPDFReport() throws SQLException, JRException {
        Connection connection = DBConnection.getInstance().getConnection();
        InputStream resourceAsStream = getClass().getResourceAsStream("/lk/ijse/bookshop1/reports/CustomerReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(resourceAsStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);
        JasperViewer.viewReport(jasperPrint, false);
    }
}