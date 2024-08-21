package parduotuve.Paskaita_2024_08_12.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import parduotuve.Paskaita_2024_08_12.Models.Item;
import parduotuve.Paskaita_2024_08_12.Models.Order;
import parduotuve.Paskaita_2024_08_12.Services.OrderService;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:5500","http://localhost:7778/","http://127.0.0.1:7778/"})
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @PostMapping("/new")
    public ResponseEntity<Integer> registerOrder(@RequestBody Order order) {

        HashMap<String,String> responseMap = orderService.registerOrder(order);
        HttpStatus status = checkHttpStatus(responseMap.get("response"));

        if(status == HttpStatus.OK) return new ResponseEntity<>(Integer.parseInt(responseMap.get("orderID")), status);
        else return new ResponseEntity<>(0, status);
    }

    @PostMapping("/new/{name}/{id}")
    public ResponseEntity<Boolean> updateOrder(@RequestBody(required = false) Order order, @RequestHeader("Authorization") String authorizationHeader, @PathVariable String name, @PathVariable int id) {

        String response = orderService.updateOrder(order,authorizationHeader,name,id);
        HttpStatus status = checkHttpStatus(response);

        if(status == HttpStatus.OK) return new ResponseEntity<>(true, status);
        else return new ResponseEntity<>(false, status);
    }


    @GetMapping("{name}/{id}")
    public ResponseEntity<Order> getItemByID(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String name, @PathVariable int id) {

        Order order = orderService.getOrderByID(authorizationHeader, name, id);

        if(order.getId() == 0)  return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/all/{name}")
    public ResponseEntity<List<Order>> getAllOrders(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String name, @RequestParam int offset , @RequestParam int limit, @RequestParam String paymentStatus) {

        List<Order> orderList = orderService.getAllOrders(offset,limit,paymentStatus,authorizationHeader,name);

        if(orderList.isEmpty())  return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else return new ResponseEntity<>(orderList, HttpStatus.OK);
    }




    private HttpStatus checkHttpStatus(String response){

        switch (response){
            case "Invalid username or password", "No authorization":
                return HttpStatus.UNAUTHORIZED;
            case "Database connection failed":
                return HttpStatus.INTERNAL_SERVER_ERROR;
            case "Invalid data":
                return HttpStatus.BAD_REQUEST;
            case "Order was successfully added","user authorize","Order was successfully updated":
                return HttpStatus.OK;
            default:
                return HttpStatus.NOT_IMPLEMENTED;
        }

    }

}
