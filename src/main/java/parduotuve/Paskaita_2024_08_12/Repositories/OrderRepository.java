package parduotuve.Paskaita_2024_08_12.Repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import parduotuve.Paskaita_2024_08_12.Models.Order;

import java.sql.*;
import java.util.HashMap;

@Repository
public class OrderRepository {
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public HashMap<String,String> registerOrder(Order order){

        HashMap<String,String> response = new HashMap<>();
        int orderID = 0;


        if(order.getProducts() == null || order.getTotalPrices() == 0){
            response.put("response","Invalid data");
            return response;
        }

        String sql = "INSERT INTO shop.order (products, totalPrice, customerName, customerAddress, customerEmail, paymentStatus)\n" +
                "VALUES (?,?,?,?,?,?);";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1,order.getProducts());
            preparedStatement.setDouble(2,order.getTotalPrices());
            preparedStatement.setString(3,order.getCustomerName());
            preparedStatement.setString(4,order.getCustomerAddress());
            preparedStatement.setString(5,order.getCustomerEmail());
            preparedStatement.setString(6,"pending");


            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            orderID = resultSet.getInt(1);

            preparedStatement.close();
            connection.close();

        }catch (SQLException e) {

            System.out.println(e.getMessage());
            response.put("response","Database connection failed");
            return response;

        }

        response.put("response","Order was successfully added");
        response.put("orderID","" + orderID);
        return response;
     }


    public void setPaymentStatus(int orderID, String paymentStatus){
        String sql = "UPDATE shop.order SET paymentStatus = ? WHERE id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, paymentStatus);
            preparedStatement.setInt(2, orderID);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

        }catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }
    public boolean isOrderPaid(int id){

        boolean isOrderPaid = false;

        String sql = "SELECT paymentStatus FROM shop.order WHERE id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet resultSet =  preparedStatement.executeQuery();

            if(!resultSet.next()) return isOrderPaid;

            isOrderPaid = resultSet.getString(1).equals("Paid");

        }catch (SQLException e) {

            System.out.println(e.getMessage());
            return isOrderPaid;
        }

        return isOrderPaid;

    }

    public String getProductJsonByOrderID(int id){

        String productsStringJSON = "[]";

        String sql = "SELECT products FROM shop.order WHERE id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet resultSet =  preparedStatement.executeQuery();

            if(!resultSet.next()) return productsStringJSON;

            productsStringJSON = resultSet.getString(1);

        }catch (SQLException e) {

            System.out.println(e.getMessage());
            return productsStringJSON;
        }

        return productsStringJSON;
    }


}
