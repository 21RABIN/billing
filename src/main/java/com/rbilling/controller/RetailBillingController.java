package com.rbilling.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rbilling.DTO.RetailInvoiceRequestDTO;
import com.rbilling.service.RetailBillingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/retail")
@RequiredArgsConstructor
public class RetailBillingController {
	
	@Autowired
	 RetailBillingService billingService;
	
	  @PostMapping("/invoice")
	    public ResponseEntity<?> createInvoice(@RequestBody RetailInvoiceRequestDTO request) {

	        String message = billingService.createRetailInvoice(request);

	        Map<String, String> response = new HashMap<>(); 
	        response.put("status", "SUCCESS"); 
	        response.put("message", message);
	        return ResponseEntity.ok(response);
	        
//	        return ResponseEntity.ok(Collections.singletonMap("message", message));

	    }

}
