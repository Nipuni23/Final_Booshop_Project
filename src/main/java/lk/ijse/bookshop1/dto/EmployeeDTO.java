package lk.ijse.bookshop1.dto;

public class EmployeeDTO {
    private Integer emp_id;
    private String emp_name;
    private String password;
    private String role;
    private String contact_number;

    public EmployeeDTO() {
    }

    public EmployeeDTO(Integer emp_id, String emp_name, String password, String role, String contact_number) {
        this.emp_id = emp_id;
        this.emp_name = emp_name;
        this.password = password;
        this.role = role;
        this.contact_number = contact_number;
    }

    public EmployeeDTO(String emp_name, String password, String role, String contact_number) {
        this.emp_name = emp_name;
        this.password = password;
        this.role = role;
        this.contact_number = contact_number;
    }

    public Integer getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(Integer emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" + "emp_id=" + emp_id + ", emp_name=" + emp_name + ", password=" + password + ", role=" + role + ", contact_number=" + contact_number + '}';
    }
}