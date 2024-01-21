package com.ehasan.service;
import com.ehasan.exception.ProductException;
import com.ehasan.model.Cart;
import com.ehasan.model.User;
import com.ehasan.request.AddItemRequest;

public interface CartService {
	
	public Cart createCart(User user);
	
	public String addCartItem(Long userId,AddItemRequest req) throws ProductException;
	
	public Cart findUserCart(Long userId);

}
