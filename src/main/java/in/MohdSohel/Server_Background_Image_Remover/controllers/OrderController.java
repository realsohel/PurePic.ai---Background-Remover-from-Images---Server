package in.MohdSohel.Server_Background_Image_Remover.controllers;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import in.MohdSohel.Server_Background_Image_Remover.dto.RazorpayOrderDto;
import in.MohdSohel.Server_Background_Image_Remover.dto.UserDto;
import in.MohdSohel.Server_Background_Image_Remover.response.RemoveBgResponse;
import in.MohdSohel.Server_Background_Image_Remover.services.OrderService;
import in.MohdSohel.Server_Background_Image_Remover.services.RazorpayService;
import in.MohdSohel.Server_Background_Image_Remover.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final RazorpayService razorpayService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestParam String planId, Authentication authentication) throws RazorpayException {
        Map<String,Object> map = new HashMap<>();
        RemoveBgResponse response = null;

        try{
            if (authentication == null || authentication.getName() == null || authentication.getName().isEmpty()) {
                log.error("Username is empty");
                response = RemoveBgResponse.builder()
                        .StatusCode(HttpStatus.FORBIDDEN)
                        .data("User does not have permission to access the resource.")
                        .success(false)
                        .build();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            Order order = orderService.createOrder(planId,authentication.getName());
            RazorpayOrderDto orderDto = convertEntityToRazorpayOrderDtoDTO(order);

            response = RemoveBgResponse.builder()
                    .success(true)
                    .data(orderDto)
                    .StatusCode(HttpStatus.OK)
                    .build();

            return ResponseEntity.ok(response);

        }catch (RazorpayException e){
            log.error("Failed in Order Controller");
            response = RemoveBgResponse.builder()
                    .success(false)
                    .data(e.getMessage())
                    .StatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @PostMapping("/verify-order")
    public ResponseEntity<?> verifyOrder(@RequestBody Map<String,Object> request, Authentication authentication) throws RazorpayException {
        log.info("Verify Payment in Order Controller");
        RemoveBgResponse response = null;
        try{
            if (authentication == null || authentication.getName() == null || authentication.getName().isEmpty()) {
                log.error("Username is empty");
                response = RemoveBgResponse.builder()
                        .StatusCode(HttpStatus.FORBIDDEN)
                        .data("User does not have permission to access the resource.")
                        .success(false)
                        .build();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            String razorpayOrderId = request.get("razorpay_order_id").toString();
            Map<String,Object> returnVal = razorpayService.verifyPayment(razorpayOrderId);

            return ResponseEntity.ok(returnVal);
        }catch (RazorpayException e){
            Map<String,Object> returnVal = new HashMap<>();
            returnVal.put("message",e.getMessage());
            returnVal.put("success",false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(returnVal);
        }
    }

    private RazorpayOrderDto convertEntityToRazorpayOrderDtoDTO(Order order) {
        return RazorpayOrderDto.builder()
                .id(order.get("id"))
                .entity(order.get("entity"))
                .amount(order.get("amount"))
                .currency(order.get("currency"))
                .status(order.get("status"))
                .created_at(order.get("created_at"))
                .receipt(order.get("receipt"))
                .build();
    }
}
