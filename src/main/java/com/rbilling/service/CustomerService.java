package com.rbilling.service;

import org.springframework.http.ResponseEntity;

import com.rbilling.DTO.CustomerDTO;

public interface CustomerService {

	ResponseEntity<?> createUpdateCustomer(CustomerDTO cusdto);
	
	ResponseEntity<?> deleteCustomer(Long id);

}
