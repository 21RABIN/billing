package com.rbilling.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rbilling.DTO.RetailInvoiceRequestDTO;
import com.rbilling.service.RetailBillingService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600) 
@RestController
@RequestMapping("/api/v1/retail")
@RequiredArgsConstructor
public class RetailBillingController {

	@Autowired
	RetailBillingService billingService;

	@PostMapping("/billing")
	public ResponseEntity<?> createInvoice(@RequestBody RetailInvoiceRequestDTO request) {
		
		return billingService.createRetailInvoice(request);

	}

}
