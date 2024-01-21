package com.ehasan.service;

import java.util.List;

import com.ehasan.exception.ProductException;
import com.ehasan.model.Rating;
import com.ehasan.model.User;
import com.ehasan.request.RatingRequest;

public interface RatingServices {
	
	public Rating createRating(RatingRequest req,User user) throws ProductException;
	
	public List<Rating> getProductsRating(Long productId);

}
