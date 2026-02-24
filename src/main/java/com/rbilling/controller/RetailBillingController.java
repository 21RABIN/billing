package com.rbilling.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rbilling.DTO.RetailInvoiceRequestDTO;
import com.rbilling.model.ERole;
import com.rbilling.model.Employee;
import com.rbilling.repository.EmployeeRepository;
import com.rbilling.repository.PaymentRepository;
import com.rbilling.repository.RoleRepository;
import com.rbilling.service.RetailBillingService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600) 
@RestController
@RequestMapping("/api/v1/retail")
@RequiredArgsConstructor
public class RetailBillingController {

	@Autowired
	RetailBillingService billingService;
	
	@Autowired
	PaymentRepository paymentrepo;
	
	@Autowired
	EmployeeRepository emprepo;
	
	@Autowired
	RoleRepository roleRepository;
	
	


	@PostMapping("/billing")
	public ResponseEntity<?> createInvoice(@RequestBody RetailInvoiceRequestDTO request) {
		
		return billingService.createRetailInvoice(request);

	}
	
	 @GetMapping("/all")
	    public ResponseEntity<List<Map<String, Object>>> getBillingPayment(
	            @RequestParam Long user_id) {

	        List<Map<String, Object>> data =
	        		billingService.getRoleBasedPayments(user_id);

	        return ResponseEntity.ok(data);
	    }
	 
	 

	    @GetMapping("/paymentdata")
	    public ResponseEntity<List<Map<String, Object>>> getPaymentData(@RequestParam Long user_id) {
	        return ResponseEntity.ok(billingService.getPaymentData(user_id));
	    }
	
	
	

}
