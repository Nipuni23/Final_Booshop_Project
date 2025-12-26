package lk.ijse.bookshop1.dto;

public class SupplierDTO {
    private Integer sup_id;
    private String sup_name;
    private String sup_email;
    private String contact_number;

    public SupplierDTO() {
    }

    public SupplierDTO(Integer sup_id, String sup_name, String sup_email, String contact_number) {
        this.sup_id = sup_id;
        this.sup_name = sup_name;
        this.sup_email = sup_email;
        this.contact_number = contact_number;
    }

    public SupplierDTO(String sup_name, String sup_email, String contact_number) {
        this.sup_name = sup_name;
        this.sup_email = sup_email;
        this.contact_number = contact_number;
    }

    public Integer getSup_id() {
        return sup_id;
    }

    public void setSup_id(Integer sup_id) {
        this.sup_id = sup_id;
    }

    public String getSup_name() {
        return sup_name;
    }

    public void setSup_name(String sup_name) {
        this.sup_name = sup_name;
    }

    public String getSup_email() {
        return sup_email;
    }

    public void setSup_email(String sup_email) {
        this.sup_email = sup_email;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    @Override
    public String toString() {
        return "SupplierDTO{" + "sup_id=" + sup_id + ", sup_name=" + sup_name + ", sup_email=" + sup_email + ", contact_number=" + contact_number + '}';
    }
}