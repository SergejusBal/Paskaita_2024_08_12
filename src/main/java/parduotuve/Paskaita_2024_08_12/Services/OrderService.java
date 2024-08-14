package parduotuve.Paskaita_2024_08_12.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import parduotuve.Paskaita_2024_08_12.Models.Order;
import parduotuve.Paskaita_2024_08_12.Repositories.OrderRepository;

import java.util.HashMap;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public HashMap<String,String> registerOrder(Order order){
        return orderRepository.registerOrder(order);
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
