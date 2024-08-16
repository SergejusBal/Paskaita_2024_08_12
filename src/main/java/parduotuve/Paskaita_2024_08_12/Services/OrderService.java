package parduotuve.Paskaita_2024_08_12.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import parduotuve.Paskaita_2024_08_12.Models.Item;
import parduotuve.Paskaita_2024_08_12.Models.Order;
import parduotuve.Paskaita_2024_08_12.Repositories.OrderRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;

    public HashMap<String,String> registerOrder(Order order){
        return orderRepository.registerOrder(order);
    }


    public Order getOrderByID(String authorizationHeader, String name, int id){
        if(userService.userAutoLogIn(authorizationHeader,name)) return orderRepository.getOrderByID(id);
        else return new Order();
    }

    public List<Order> getAllOrders(int offset, int limit, String paymentStatus, String authorizationHeader, String name){
        if(userService.userAutoLogIn(authorizationHeader,name)) return orderRepository.getAllOrders(offset, limit, paymentStatus);
        else return new ArrayList<>();
    }

    public String updateOrder(Order order, String authorizationHeader, String name, int id ){
        if(order == null) return "Invalid data";
        if(userService.userAutoLogIn(authorizationHeader,name))  return orderRepository.updateOrder(order,id);
        else return "No authorization";
    }


    public String getProductJsonByOrderID(int id){
        return orderRepository.getProductJsonByOrderID(id);
    }

    public void setPaymentStatus(int orderID, String paymentStatus){
        orderRepository.setPaymentStatus(orderID,paymentStatus);
    }

    public boolean isOrderPaid(int orderID){
        return orderRepository.isOrderPaid(orderID);
    }


}
