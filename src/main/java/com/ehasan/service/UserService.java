package com.ehasan.service;

import com.ehasan.exception.UserException;
import com.ehasan.model.User;

public interface UserService {
	
	public User findUserById(Long userId) throws UserException;
	
	public User findUserProfileByJwt(String jwt) throws UserException;

}