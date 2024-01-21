package com.ehasan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ehasan.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	public User findByEmail(String email);

}
