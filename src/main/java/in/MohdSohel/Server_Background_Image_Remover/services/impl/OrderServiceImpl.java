package in.MohdSohel.Server_Background_Image_Remover.services.impl;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import in.MohdSohel.Server_Background_Image_Remover.entities.OrderEnt;
import in.MohdSohel.Server_Background_Image_Remover.repositories.OrderRepository;
import in.MohdSohel.Server_Background_Image_Remover.services.OrderService;
import in.MohdSohel.Server_Background_Image_Remover.services.RazorpayService;
import in.MohdSohel.Server_Background_Image_Remover.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final RazorpayService razorpayService;
    private final UserService userService;
    private final OrderRepository orderRepository;

    private static final Map<String , PlanDetails> PLAN_DETAILS = Map.of(
            "Basic", new PlanDetails("Basic", 100, 499.00),
            "Premium", new PlanDetails("Premium", 300, 899.00),
            "Ultimate", new PlanDetails("Ultimate", 1000, 1499.00)
    );

    private record PlanDetails(String name, int credits, double amount){
    }

    @Override
    public Order createOrder(String planId, String clerkId) throws RazorpayException {
        log.info("Paying through razorpay!");
        PlanDetails details = PLAN_DETAILS.get(planId);

        if(details==null){
            throw new IllegalArgumentException("Invalid plan details provided " + planId);
        }

        try{
            Order razorpayOrder = razorpayService.createrOrder(details.amount(),"INR");

            OrderEnt orderEnt =  OrderEnt.builder()
                    .clerkId(clerkId)
                    .plan(details.name())
                    .credits(details.credits())
                    .amount(details.amount())
                    .orderId(razorpayOrder.get("id"))
                    .build();

            orderRepository.save(orderEnt);
            log.info("Order created!");
            return razorpayOrder;

        }catch (RazorpayException e){
            log.error("Error while creating order!", e.getMessage());
            throw new RazorpayException("Razor payment failed" + e.getMessage());
        }
    }
}
