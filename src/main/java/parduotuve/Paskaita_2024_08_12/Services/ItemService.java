package parduotuve.Paskaita_2024_08_12.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import parduotuve.Paskaita_2024_08_12.Models.Filter;
import parduotuve.Paskaita_2024_08_12.Models.Item;
import parduotuve.Paskaita_2024_08_12.Repositories.ItemRepository;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserService userService;

    public String registerItem(Item item, String authorizationHeader,  String name){
        if(item == null) return "Invalid data";
        if(userService.userAutoLogIn(authorizationHeader,name)) return itemRepository.registerItem(item);
        else return "No authorization";
    }

    public String updateItem(Item item, String authorizationHeader, String name, int id ){
        if(item == null) return "Invalid data";
        if(userService.userAutoLogIn(authorizationHeader,name))  return itemRepository.updateItem(item,id);
        else return "No authorization";
    }

    public Item getItemByID(int id){
        return itemRepository.getItemByID(id);
    }

    public List<Item> getAllItems(int offset , int limit, Filter filter){
        return itemRepository.getAllItems(offset,limit,filter);
    }

    public double getItemPriceByID(int id){
        return itemRepository.getItemPriceByID(id);
    }
}
