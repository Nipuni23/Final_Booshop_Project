package lk.ijse.bookshop1.model;

import lk.ijse.bookshop1.dto.UserDTO;
import lk.ijse.bookshop1.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserModel {

    // Save user - ID auto generates
    public boolean saveUser(UserDTO userDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO users (user_name, user_password, user_status) VALUES (?,?,?)",
                userDTO.getUser_name(),
                userDTO.getUser_password(),
                userDTO.getUser_status()
        );
    }

    // Update user
    public static boolean updateUser(UserDTO userDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE users SET user_name=?, user_password=?, user_status=? WHERE user_id=?",
                userDTO.getUser_name(),
                userDTO.getUser_password(),
                userDTO.getUser_status(),
                userDTO.getUser_id()
        );
    }

    // Delete user
    public static boolean deleteUser(Integer userId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM users WHERE user_id=?", userId);
    }

    // Get all user IDs
    public static List<Integer> getUserIds() throws SQLException, ClassNotFoundException {
        List<Integer> userIds = new ArrayList<>();
        ResultSet rst = null;

        try {
            rst = CrudUtil.execute("SELECT user_id FROM users");
            while (rst.next()) {
                userIds.add(rst.getInt("user_id"));
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return userIds;
    }

    // Get all users
    public static List<UserDTO> getAllUsers() throws SQLException, ClassNotFoundException {
        List<UserDTO> users = new ArrayList<>();
        ResultSet rst = null;

        try {
            rst = CrudUtil.execute("SELECT * FROM users");
            while (rst.next()) {
                UserDTO user = new UserDTO(
                        rst.getInt("user_id"),
                        rst.getString("user_name"),
                        rst.getString("user_password"),
                        rst.getString("user_status")
                );
                users.add(user);
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return users;
    }

    // Get user by ID
    public static UserDTO getUserById(Integer userId) throws SQLException, ClassNotFoundException {
        ResultSet rst = null;
        UserDTO user = null;

        try {
            rst = CrudUtil.execute("SELECT * FROM users WHERE user_id=?", userId);
            if (rst.next()) {
                user = new UserDTO(
                        rst.getInt("user_id"),
                        rst.getString("user_name"),
                        rst.getString("user_password"),
                        rst.getString("user_status")
                );
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return user;
    }

    // Validate user for login
    public static UserDTO validateUser(String username, String password) throws SQLException, ClassNotFoundException {
        ResultSet rst = null;
        UserDTO user = null;

        try {
            rst = CrudUtil.execute("SELECT * FROM users WHERE user_name=? AND user_password=?", username, password);
            if (rst.next()) {
                user = new UserDTO(
                        rst.getInt("user_id"),
                        rst.getString("user_name"),
                        rst.getString("user_password"),
                        rst.getString("user_status")
                );
            }
        } finally {
            if (rst != null) {
                rst.getStatement().close();
            }
        }
        return user;
    }
}