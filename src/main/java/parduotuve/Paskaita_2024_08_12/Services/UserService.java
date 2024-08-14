package parduotuve.Paskaita_2024_08_12.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parduotuve.Paskaita_2024_08_12.Models.User;
import parduotuve.Paskaita_2024_08_12.Repositories.UserRepository;
import parduotuve.Paskaita_2024_08_12.Security.JwtDecoder;
import parduotuve.Paskaita_2024_08_12.Security.JwtGenerator;

import java.util.HashMap;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public String registerUser(User user){
        return userRepository.registerUser(user);
    }
    public User getUserById(int id){
        return userRepository.getUserById(id);
    }

    public HashMap<String,String> login(User user){

        String response = userRepository.login(user);

        HashMap<String,String> responseMap = new HashMap<>();

        if (response.substring(0,14).equals("user authorize")){
            int endIndex = response.length();
            responseMap.put("JWT",JwtGenerator.generateJwt(Integer.parseInt(response.substring(15,endIndex))));
            responseMap.put("response", response.substring(0,14));
        }
        else responseMap.put("response", response);

        return responseMap;
    }

    public boolean userAutoLogIn(String authorizationHeader, String name){
        if(autorize(authorizationHeader)){
            int id = userIDFromJWT(authorizationHeader);
            if(userRepository.checkIfNameMatchID(id,name)) return true;
            else return false;
        }
        else return false;
    }


    private boolean autorize(String authorizationHeader){

        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            return false;
        }
        try {
            JwtDecoder.decodeJwt(authorizationHeader);
        } catch (JwtException e){
            return false;
        }
        return true;
    }

    private int userIDFromJWT(String authorizationHeader){
        return Integer.parseInt((String) JwtDecoder.decodeJwt(authorizationHeader).get("UserId"));
    }



}
