package com.ehasan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ehasan.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
