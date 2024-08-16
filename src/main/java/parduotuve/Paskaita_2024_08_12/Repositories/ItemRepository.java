package parduotuve.Paskaita_2024_08_12.Repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import parduotuve.Paskaita_2024_08_12.Models.Filter;
import parduotuve.Paskaita_2024_08_12.Models.Item;
import parduotuve.Paskaita_2024_08_12.Models.Order;
import parduotuve.Paskaita_2024_08_12.Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class ItemRepository {
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public String registerItem(Item item){

        if(item.getName() == null || item.getPrice() == 0) return "Invalid data";

        String sql = "INSERT INTO item (name,description,price,category,imageUrl)\n" +
                "VALUES (?,?,?,?,?);";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,item.getName());
            preparedStatement.setString(2,item.getDescription());
            preparedStatement.setDouble(3,item.getPrice());
            preparedStatement.setString(4,item.getCategory());
            preparedStatement.setString(5,item.getImageUrl());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

        }catch (SQLException e) {

            System.out.println(e.getMessage());

            return "Database connection failed";
        }

        return "Item was successfully added";

    }

    public Item getItemByID(int id){

        Item item = new Item();
        String sql = "SELECT * FROM item WHERE id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet resultSet =  preparedStatement.executeQuery();

            if(!resultSet.next()) return new Item();

            item.setId(resultSet.getInt("id"));
            item.setName(resultSet.getString("name"));
            item.setDescription(resultSet.getString("description"));
            item.setPrice(resultSet.getDouble("price"));
            item.setCategory(resultSet.getString("category"));
            item.setImageUrl(resultSet.getString("imageUrl"));

        }catch (SQLException e) {

            System.out.println(e.getMessage());
            return new Item();
        }

        return item;
    }

    public String updateItem(Item item, int id){

        if(item.getName() == null || item.getPrice() == 0) return "Invalid data";

        String sql = "UPDATE item SET name = ?, description = ?, price = ?, category = ?, imageUrl = ? WHERE id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1,item.getName());
            preparedStatement.setString(2,item.getDescription());
            preparedStatement.setDouble(3,item.getPrice());
            preparedStatement.setString(4,item.getCategory());
            preparedStatement.setString(5,item.getImageUrl());
            preparedStatement.setInt(6,id);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

        }catch (SQLException e) {

            System.out.println(e.getMessage());
            return "Database connection failed";

        }

        return "Item was successfully updated";
    }

    public List<Item> getAllItems(int offset , int limit, Filter filter){

        List<Item> itemList = new ArrayList<>();
        String sql = "SELECT * FROM item ORDER BY id DESC LIMIT ? OFFSET ?";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,limit);
            preparedStatement.setInt(2,offset);
            ResultSet resultSet =  preparedStatement.executeQuery();

            while(resultSet.next()) {

                Item item = new Item();

                item.setId(resultSet.getInt("id"));
                item.setName(resultSet.getString("name"));
                item.setDescription(resultSet.getString("description"));
                item.setPrice(resultSet.getDouble("price"));
                item.setCategory(resultSet.getString("category"));
                item.setImageUrl(resultSet.getString("imageUrl"));

                itemList.add(item);
            }

        }catch (SQLException e) {

            System.out.println(e.getMessage());
            return new ArrayList<>();
        }

        return itemList;

    }

    public double getItemPriceByID(int id){
        double price = 0;

        String sql = "SELECT price FROM item WHERE id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet resultSet =  preparedStatement.executeQuery();

            if(!resultSet.next()) return price;

            price = resultSet.getDouble(1);


        }catch (SQLException e) {

            System.out.println(e.getMessage());
            return price;
        }

        return price;
    }

}
