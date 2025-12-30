package in.MohdSohel.Server_Background_Image_Remover.services.impl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import in.MohdSohel.Server_Background_Image_Remover.dto.UserDto;
import in.MohdSohel.Server_Background_Image_Remover.entities.OrderEnt;
import in.MohdSohel.Server_Background_Image_Remover.repositories.OrderRepository;
import in.MohdSohel.Server_Background_Image_Remover.services.RazorpayService;
import in.MohdSohel.Server_Background_Image_Remover.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RazorpayServiceImpl implements RazorpayService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    private final OrderRepository orderRepository;
    private final UserService userService;


    @Override
    public Order createrOrder(Double amount, String currency) throws RazorpayException {
        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount*100);
            orderRequest.put("currency", currency);
            orderRequest.put("receipt", "order_rcptid_"+System.currentTimeMillis());
            orderRequest.put("payment_capture",1);

            return razorpayClient.orders.create(orderRequest);
        }catch (RazorpayException ex){
            ex.printStackTrace();
            throw new RazorpayException("Razor payment failed" + ex.getMessage());
        }
    }

    @Override
    public Map<String, Object> verifyPayment(String razorPayOrderId) throws RazorpayException {
        log.info("Verifying the payment");
        Map<String, Object> response = new HashMap<>();

        try{
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId,razorpayKeySecret);
            Order orderInfo = razorpayClient.orders.fetch(razorPayOrderId);

            if(orderInfo.get("status").toString().equalsIgnoreCase("paid")){
                OrderEnt existingOrder = orderRepository.findByOrderId(razorPayOrderId)
                        .orElseThrow(()-> new RuntimeException("Order not found with Id: " + razorPayOrderId));

                if(existingOrder.getPayment()){
                    response.put("success",false);
                    response.put("message", "Payment failed.");
                    return  response;
                }

                UserDto userDto = userService.getUserByClerkId(existingOrder.getClerkId());
                userDto.setCredits(userDto.getCredits() + existingOrder.getCredits());

                userService.saveUser(userDto);
                existingOrder.setPayment(true);
                orderRepository.save(existingOrder);
                response.put("success",true);
                response.put("message", "Payment successful & Credits added successfully.");
                return response;
            }

        }catch (RazorpayException e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while verifying the payment. Please try again");
        }

        return response;
    }
}
