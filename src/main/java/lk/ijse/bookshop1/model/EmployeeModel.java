package lk.ijse.bookshop1.model;

import lk.ijse.bookshop1.dto.EmployeeDTO;
import lk.ijse.bookshop1.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {

    // Save employee - ID auto generates
    public boolean saveEmployee(EmployeeDTO employeeDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO employee (emp_name, password, role, contact_number) VALUES (?,?,?,?)",
                employeeDTO.getEmp_name(),
                employeeDTO.getPassword(),
                employeeDTO.getRole(),
                employeeDTO.getContact_number()
        );
    }

    // Update employee
    public static boolean updateEmployee(EmployeeDTO employeeDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE employee SET emp_name=?, password=?, role=?, contact_number=? WHERE emp_id=?",
                employeeDTO.getEmp_name(),
                employeeDTO.getPassword(),
                employeeDTO.getRole(),
                employeeDTO.getContact_number(),
                employeeDTO.getEmp_id()
        );
    }

    // Delete employee
    public static boolean deleteEmployee(Integer empId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM employee WHERE emp_id=?", empId);
    }

    // Get all employee IDs
    public static List<Integer> getEmployeeIds() throws SQLException, ClassNotFoundException {
        List<Integer> empIds = new ArrayList<>();
        ResultSet rst = null;

        try {
            rst = CrudUtil.execute("SELECT emp_id FROM employee");
            while (rst.next()) {
                empIds.add(rst.getInt("emp_id"));
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return empIds;
    }

    // Get all employees
    public static List<EmployeeDTO> getAllEmployees() throws SQLException, ClassNotFoundException {
        List<EmployeeDTO> employees = new ArrayList<>();
        ResultSet rst = null;

        try {
            rst = CrudUtil.execute("SELECT * FROM employee");
            while (rst.next()) {
                EmployeeDTO employee = new EmployeeDTO(
                        rst.getInt("emp_id"),
                        rst.getString("emp_name"),
                        rst.getString("password"),
                        rst.getString("role"),
                        rst.getString("contact_number")
                );
                employees.add(employee);
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return employees;
    }

    // Get employee by ID
    public static EmployeeDTO getEmployeeById(Integer empId) throws SQLException, ClassNotFoundException {
        ResultSet rst = null;
        EmployeeDTO employee = null;

        try {
            rst = CrudUtil.execute("SELECT * FROM employee WHERE emp_id=?", empId);
            if (rst.next()) {
                employee = new EmployeeDTO(
                        rst.getInt("emp_id"),
                        rst.getString("emp_name"),
                        rst.getString("password"),
                        rst.getString("role"),
                        rst.getString("contact_number")
                );
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return employee;
    }
}