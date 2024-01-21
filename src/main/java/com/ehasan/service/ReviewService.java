package com.ehasan.service;

import java.util.List;

import com.ehasan.exception.ProductException;
import com.ehasan.model.Review;
import com.ehasan.model.User;
import com.ehasan.request.ReviewRequest;

public interface ReviewService {

	public Review createReview(ReviewRequest req,User user) throws ProductException;
	
	public List<Review> getAllReview(Long productId);
	
	
}
