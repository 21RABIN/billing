package com.rbilling.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rbilling.DTO.CustomerDTO;
import com.rbilling.repository.CustomerRepository;
import com.rbilling.service.AccessScopeService;
import com.rbilling.service.CustomerService;

@CrossOrigin(origins = "*", maxAge = 3600)  // Allowing all origins is risky. Consider restricting to trusted domains in production.
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	

	@Autowired
	 CustomerRepository cusrepo;

	@Autowired
	AccessScopeService accessScopeService;

    @PostMapping("/create")
    public ResponseEntity<?> createUpdateCustomer(@RequestBody CustomerDTO cusdto) {
        return customerService.createUpdateCustomer(cusdto);
    }
    
    @GetMapping("/all")
	public ResponseEntity<List<Map<String, Object>>> getAllCustomer(@RequestParam Long user_id) {
		List<Map<String, Object>> customer = cusrepo.getAllCustomer(0L);
		customer = accessScopeService.filterRowsByBusinessUnits(customer, "business_unit_id", user_id);
		return ResponseEntity.ok(customer);
	}
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
    	return customerService.deleteCustomer(id);
    }
    

 
}
