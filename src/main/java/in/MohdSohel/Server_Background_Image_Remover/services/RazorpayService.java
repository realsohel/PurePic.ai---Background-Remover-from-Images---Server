package in.MohdSohel.Server_Background_Image_Remover.services;


import com.razorpay.Order;
import com.razorpay.RazorpayException;

import java.util.Map;

public interface RazorpayService {

    Order createrOrder(Double amount, String currency) throws RazorpayException;
    Map<String, Object> verifyPayment(String razorPayOrderId) throws RazorpayException;

}
