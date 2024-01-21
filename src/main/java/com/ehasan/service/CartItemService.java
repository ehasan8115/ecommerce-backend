package com.ehasan.service;

import com.ehasan.exception.CartItemException;
import com.ehasan.exception.UserException;
import com.ehasan.model.Cart;
import com.ehasan.model.CartItem;
import com.ehasan.model.Product;

public interface CartItemService {
	
	public CartItem createCartItem(CartItem cartItem);
	
	public CartItem updateCartItem(Long userId, Long id,CartItem cartItem) throws CartItemException, UserException;
	
	public CartItem isCartItemExist(Cart cart,Product product,String size, Long userId);
	
	public void removeCartItem(Long userId,Long cartItemId) throws CartItemException, UserException;
	
	public CartItem findCartItemById(Long cartItemId) throws CartItemException;
	
}
