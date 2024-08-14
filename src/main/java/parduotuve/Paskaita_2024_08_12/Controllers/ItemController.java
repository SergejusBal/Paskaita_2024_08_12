package parduotuve.Paskaita_2024_08_12.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parduotuve.Paskaita_2024_08_12.Models.Filter;
import parduotuve.Paskaita_2024_08_12.Models.Item;
import parduotuve.Paskaita_2024_08_12.Services.ItemService;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:5500","http://localhost:7778/"})
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/new/{name}")
    public ResponseEntity<Boolean> registerItem(@RequestBody(required = false) Item item, @RequestHeader("Authorization") String authorizationHeader, @PathVariable String name) {


        String response = itemService.registerItem(item,authorizationHeader,name);
        HttpStatus status = checkHttpStatus(response);

        if(status == HttpStatus.OK) return new ResponseEntity<>(true, status);
        else return new ResponseEntity<>(false, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemByID(@PathVariable int id) {

        Item item = itemService.getItemByID(id);

        if(item.getId() == 0)  return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PostMapping("/getAll")
    public ResponseEntity<List<Item>> getAllItems(@RequestParam int offset , @RequestParam  int limit, @RequestBody(required = false) Filter filter) {

        List<Item> itemList = itemService.getAllItems(offset,limit,filter);

        if(itemList.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(itemList, HttpStatus.OK);
    }



    private HttpStatus checkHttpStatus(String response){

        switch (response){
            case "Invalid username or password", "No authorization":
                return HttpStatus.UNAUTHORIZED;
            case "Database connection failed":
                return HttpStatus.INTERNAL_SERVER_ERROR;
            case "Invalid data":
                return HttpStatus.BAD_REQUEST;
            case "Item was successfully added","user authorize":
                return HttpStatus.OK;
            default:
                return HttpStatus.NOT_IMPLEMENTED;
        }

    }



}
