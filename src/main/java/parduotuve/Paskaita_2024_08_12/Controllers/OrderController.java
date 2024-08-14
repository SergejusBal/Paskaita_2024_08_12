package parduotuve.Paskaita_2024_08_12.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import parduotuve.Paskaita_2024_08_12.Models.Order;
import parduotuve.Paskaita_2024_08_12.Services.OrderService;

import java.util.HashMap;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:5500","http://localhost:7778/"})
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



    private HttpStatus checkHttpStatus(String response){

        switch (response){
            case "Invalid username or password", "No authorization":
                return HttpStatus.UNAUTHORIZED;
            case "Database connection failed":
                return HttpStatus.INTERNAL_SERVER_ERROR;
            case "Invalid data":
                return HttpStatus.BAD_REQUEST;
            case "Order was successfully added","user authorize":
                return HttpStatus.OK;
            default:
                return HttpStatus.NOT_IMPLEMENTED;
        }

    }

}
