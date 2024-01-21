package com.ehasan.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ehasan.exception.OrderException;
import com.ehasan.exception.UserException;
import com.ehasan.model.Order;
import com.ehasan.repository.OrderRepository;
import com.ehasan.response.ApiResponse;
import com.ehasan.response.PaymentLinkResponse;
import com.ehasan.service.OrderService;
import com.ehasan.service.UserService;
import com.ehasan.user.domain.OrderStatus;
import com.ehasan.user.domain.PaymentStatus;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/api")
public class PaymentController {
	
	private OrderService orderService;
	private UserService userService;
	private OrderRepository orderRepository;
	
	@Value("${razorpay.api.key}")
	String apiKey;
	
	@Value("${razorpay.api.secret}")
	String apiSecret;
	
	public PaymentController(OrderService orderService,UserService userService,OrderRepository orderRepository) {
		this.orderService=orderService;
		this.userService=userService;
		this.orderRepository=orderRepository;
	}
	
	@PostMapping("/payments/{orderId}")
	public ResponseEntity<PaymentLinkResponse>createPaymentLink(@PathVariable Long orderId,
			@RequestHeader("Authorization")String jwt) 
					throws UserException, OrderException, RazorpayException{
		
		Order order=orderService.findOrderById(orderId);
		 try {
		      // Instantiate a Razorpay client with your key ID and secret
		      RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

		      // Create a JSON object with the payment link request parameters
		      JSONObject paymentLinkRequest = new JSONObject();
		      paymentLinkRequest.put("amount",order.getTotalPrice()* 100);
		      paymentLinkRequest.put("currency","INR");    
//		      paymentLinkRequest.put("expire_by",1691097057);
//		      paymentLinkRequest.put("reference_id",order.getId().toString());
		     

		      // Create a JSON object with the customer details
		      JSONObject customer = new JSONObject();
		      customer.put("name",order.getUser().getFirstName());
//		      customer.put("contact",order.getUser().getMobile());
		      customer.put("email",order.getUser().getEmail());
		      paymentLinkRequest.put("customer",customer);

		      // Create a JSON object with the notification settings
		      JSONObject notify = new JSONObject();
		      notify.put("sms",true);
		      notify.put("email",true);
		      paymentLinkRequest.put("notify",notify);

		      // Set the reminder settings
//		      paymentLinkRequest.put("reminder_enable",true);

		      // Set the callback URL and method
		      paymentLinkRequest.put("callback_url","http://localhost:3000/payment/"+orderId);
		      paymentLinkRequest.put("callback_method","get");

		      // Create the payment link using the paymentLink.create() method
		      PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);
		      
		      String paymentLinkId = payment.get("id");
		      String paymentLinkUrl = payment.get("short_url");
		      
		      PaymentLinkResponse res=new PaymentLinkResponse();
		      res.setPayment_link_id(paymentLinkUrl);
		      res.setPayment_link_url(paymentLinkUrl);
		      
//		      PaymentLink fetchedPayment = razorpay.paymentLink.fetch(paymentLinkId);
//		      
//		      order.setOrderId(fetchedPayment.get("order_id"));
//		      orderRepository.save(order);
		      
		   // Print the payment link ID and URL
//		      System.out.println("Payment link ID: " + paymentLinkId);
//		      System.out.println("Payment link URL: " + paymentLinkUrl);
//		      System.out.println("Order Id : "+fetchedPayment.get("order_id")+fetchedPayment);
		      
		      return new ResponseEntity<PaymentLinkResponse>(res,HttpStatus.CREATED);
		      
		    } catch (RazorpayException e) {
		    	throw new RazorpayException(e.getMessage());
		    }
		
		
//		order_id
	}
	
  @GetMapping("/payments")
  public ResponseEntity<ApiResponse> redirect(@RequestParam(name="payment_id") String paymentId,@RequestParam("order_id")Long orderId) throws RazorpayException, OrderException {
	  RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
	  Order order =orderService.findOrderById(orderId);
	
	  try {
		
		
		Payment payment = razorpay.payments.fetch(paymentId);
		
		if(payment.get("status").equals("captured")) {
		  
			order.getPaymentDetails().setPaymentId(paymentId);
			order.getPaymentDetails().setStatus(PaymentStatus.COMPLETED);
			order.setOrderStatus(OrderStatus.PLACED);
//			order.setOrderItems(order.getOrderItems());
//			System.out.println(order.getPaymentDetails().getStatus()+"payment status ");
			orderRepository.save(order);
		}
		ApiResponse res=new ApiResponse();
		res.setMessage("your order get placed");
		res.setStatus(true);
		return new ResponseEntity<ApiResponse>(res,HttpStatus.ACCEPTED);
	      
	} catch (Exception e) {
		throw new RazorpayException(e.getMessage());
	}

  }

}
