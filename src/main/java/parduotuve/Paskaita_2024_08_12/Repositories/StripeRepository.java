package parduotuve.Paskaita_2024_08_12.Repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.UUID;

@Repository
public class StripeRepository {
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;


    public int createPayment(UUID uuid,UUID uuidSecret,Integer orderID){

        int payment_id = 0;

        String sql = "INSERT INTO payment (order_id, payment_uuid, payment_uuid_secret)\n" +
                "VALUES (?,?,?);";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,orderID);
            preparedStatement.setString(2,uuid.toString());
            preparedStatement.setString(3,uuidSecret.toString());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            payment_id = resultSet.getInt(1);

            preparedStatement.close();
            connection.close();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
            return payment_id;
        }
        return payment_id;
    }


    public int getOrderID(int paymentID, String uuid){
        int orderID = 0;
        String sql = "SELECT * FROM payment WHERE id = ? AND payment_uuid = ?";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,paymentID);
            preparedStatement.setString(2,uuid);

            ResultSet resultSet =  preparedStatement.executeQuery();

            if(!resultSet.next()) return orderID;

            orderID = resultSet.getInt("order_id");

            preparedStatement.close();
            connection.close();

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return orderID;
        }

    return orderID;
    }


    public boolean checkIfPaymentValid(int paymentID, String uuid, String uuidSecret){
        boolean isOrderValid = false;
        String sql = "SELECT * FROM payment WHERE id = ? AND payment_uuid = ? AND payment_uuid_secret = ?";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,paymentID);
            preparedStatement.setString(2,uuid);
            preparedStatement.setString(3,uuidSecret);

            ResultSet resultSet =  preparedStatement.executeQuery();

            if(!resultSet.next()) return isOrderValid;

            isOrderValid = true;

            preparedStatement.close();
            connection.close();

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return isOrderValid;
        }

        return isOrderValid;

    }




}
