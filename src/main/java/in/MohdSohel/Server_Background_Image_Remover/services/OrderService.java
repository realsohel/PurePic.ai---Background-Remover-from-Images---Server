package in.MohdSohel.Server_Background_Image_Remover.services;

import com.razorpay.Order;
import com.razorpay.RazorpayException;

public interface OrderService {

    Order createOrder(String planId, String clerkId) throws RazorpayException;
}
