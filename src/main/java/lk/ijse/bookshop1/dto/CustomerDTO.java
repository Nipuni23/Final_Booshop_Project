package lk.ijse.bookshop1.dto;

public class CustomerDTO {
    private Integer cust_id;
    private String cus_name;
    private String cus_phone;
    private String cus_email;
    private String cus_address;

    public CustomerDTO() {
    }

    public CustomerDTO(Integer cust_id, String cus_name, String cus_phone, String cus_email, String cus_address) {
        this.cust_id = cust_id;
        this.cus_name = cus_name;
        this.cus_phone = cus_phone;
        this.cus_email = cus_email;
        this.cus_address = cus_address;
    }

    public CustomerDTO(String cus_name, String cus_phone, String cus_email, String cus_address) {
        this.cus_name = cus_name;
        this.cus_phone = cus_phone;
        this.cus_email = cus_email;
        this.cus_address = cus_address;
    }

    public Integer getCust_id() {
        return cust_id;
    }

    public void setCust_id(Integer cust_id) {
        this.cust_id = cust_id;
    }

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public String getCus_phone() {
        return cus_phone;
    }

    public void setCus_phone(String cus_phone) {
        this.cus_phone = cus_phone;
    }

    public String getCus_email() {
        return cus_email;
    }

    public void setCus_email(String cus_email) {
        this.cus_email = cus_email;
    }

    public String getCus_address() {
        return cus_address;
    }

    public void setCus_address(String cus_address) {
        this.cus_address = cus_address;
    }

    @Override
    public String toString() {
        return "CustomerDTO{" + "cust_id=" + cust_id + ", cus_name=" + cus_name + ", cus_phone=" + cus_phone + ", cus_email=" + cus_email + ", cus_address=" + cus_address + '}';
    }
}