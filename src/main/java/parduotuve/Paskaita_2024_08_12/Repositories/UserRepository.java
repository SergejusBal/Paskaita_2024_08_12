package parduotuve.Paskaita_2024_08_12.Repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import parduotuve.Paskaita_2024_08_12.Models.User;
import parduotuve.Paskaita_2024_08_12.Security.JwtGenerator;

import java.sql.*;

@Repository
public class UserRepository {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;


    public String registerUser(User user){

        if(user.getName() == null || user.getPassword() == null) return "Invalid data";

        String sql = "INSERT INTO user (name,password)\n" +
                "VALUES (?,?);";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getPassword());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

        }catch (SQLException e) {

            System.out.println(e.getMessage());

            if (e.getErrorCode() == 1062) return "User already exists";

            return "Database connection failed";
        }

        return "User was successfully added";

    }

    public User getUserById(int id){


        User user = new User();
        String sql = "SELECT * FROM user WHERE id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet resultSet =  preparedStatement.executeQuery();

            if(!resultSet.next()) return new User();

            user.setId(resultSet.getInt("id"));
            user.setName(resultSet.getString("name"));
            user.setPassword(resultSet.getString("password"));

        }catch (SQLException e) {

            System.out.println(e.getMessage());
            return new User();
        }

        return user;
    }

    public String login(User user){

        String sql = "SELECT * FROM user WHERE name = ? AND password = ?";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getPassword());
            ResultSet resultSet =  preparedStatement.executeQuery();

            if(!resultSet.next()) return "Invalid username or password";
            user.setId(resultSet.getInt("id"));

        }catch (SQLException e) {

            System.out.println(e.getMessage());

            return "Database connection failed";
        }

        return "user authorize " + user.getId();
    }

    public boolean checkIfNameMatchID(int id,String name){

        String sql = "SELECT * FROM user WHERE id = ? and name = ?";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.setString(2,name);
            ResultSet resultSet =  preparedStatement.executeQuery();

            if(!resultSet.next()) return false;

        }catch (SQLException e) {

            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }



}
