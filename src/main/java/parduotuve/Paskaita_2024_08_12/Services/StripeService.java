package parduotuve.Paskaita_2024_08_12.Services;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import parduotuve.Paskaita_2024_08_12.Models.Item;
import parduotuve.Paskaita_2024_08_12.Repositories.OrderRepository;
import parduotuve.Paskaita_2024_08_12.Repositories.StripeRepository;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;
    @Autowired
    private StripeRepository stripeRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ItemService itemService;

    private final String URL ="localhost";

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public PaymentIntent createPaymentIntent(Long amount, String currency) throws StripeException {
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency(currency)
                        .build();

        return PaymentIntent.create(params);
    }

    public Session createCheckoutSession(Integer orderID) throws StripeException {

        UUID uuid = generateUID();
        UUID uuidSecret = generateUID();
        int payment_id = createPayment(uuid,uuidSecret,orderID);

        Long orderPrice = calculateOrderPrice(orderID);
        String OrderCurrency = "EUR";

        String successUrl = "http://"+URL+":8080/stripe/" + uuid + "/" + uuidSecret + "/" + payment_id;
        String cancelUrl = "http://"+URL+":8080/stripe/" + uuid + "/" + payment_id;

        Map<String, String> metadata = new HashMap<>();
        metadata.put("uuid", uuid.toString());
        metadata.put("id", payment_id + "");
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(OrderCurrency)
                                .setUnitAmount(orderPrice)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Moketi")
                                        .build())
                                .build())
                        .build())
                .putAllMetadata(metadata)
                .build();

        return Session.create(params);
    }

    public void setPaymentStatus(int id, String uuid, String uuidSecret){
        int orderID = stripeRepository.getOrderID(id,uuid);

        if(orderID == 0) return;
        if(orderService.isOrderPaid(orderID)) return;

        if (uuidSecret != null && stripeRepository.checkIfPaymentValid(id,uuid,uuidSecret)) orderService.setPaymentStatus(orderID,"Paid");
        else orderService.setPaymentStatus(orderID,"Cancelled");
    }

    private int createPayment(UUID uuid,UUID uuidSecret, Integer orderID){
        return stripeRepository.createPayment(uuid, uuidSecret, orderID);
    }


    private UUID generateUID(){
        return UUID.randomUUID();
    }

    private Item[] getOrderItemArrayByOrderID(int orderID){
        String jsonItemString = orderService.getProductJsonByOrderID(orderID);

        Gson gson = new Gson();

        return gson.fromJson(jsonItemString, Item[].class);
    }

    private Long calculateOrderPrice(int orderID){
        Item[] items = getOrderItemArrayByOrderID(orderID);
        long totalPrice = 0;

        for(int i = 0 ; i < items.length;i++){
           double price = itemService.getItemPriceByID(items[i].getId()) * 100;
           totalPrice += (long) price;

        }
        return totalPrice;
    }

}