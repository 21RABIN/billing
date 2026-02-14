package com.rbilling.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rbilling.DTO.CustomerDTO;
import com.rbilling.model.Customer;
import com.rbilling.repository.CustomerRepository;
import com.rbilling.service.CustomerService;

import lombok.Getter;

@CrossOrigin(origins = "*", maxAge = 3600)  // Allowing all origins is risky. Consider restricting to trusted domains in production.
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	

	@Autowired
	 CustomerRepository cusrepo;

    @PostMapping("/create")
    public ResponseEntity<?> createUpdateCustomer(@RequestBody CustomerDTO cusdto) {
        return customerService.createUpdateCustomer(cusdto);
    }
    
    @GetMapping("/all")
	public ResponseEntity<List<Map<String, Object>>> getAllCustomer() {
		List<Map<String, Object>> customer = cusrepo.getAllCustomer();
		return ResponseEntity.ok(customer);
	}
    
 
}
