package lk.ijse.bookshop1.dto;

public class UserDTO {
    private Integer user_id;
    private String user_name;
    private String user_password;
    private String user_status;

    public UserDTO() {
    }

    public UserDTO(Integer user_id, String user_name, String user_password, String user_status) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_password = user_password;
        this.user_status = user_status;
    }

    public UserDTO(String user_name, String user_password, String user_status) {
        this.user_name = user_name;
        this.user_password = user_password;
        this.user_status = user_status;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "user_id=" + user_id + ", user_name=" + user_name + ", user_password=" + user_password + ", user_status=" + user_status + '}';
    }
}