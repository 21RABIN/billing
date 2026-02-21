package com.rbilling.service;

import org.springframework.http.ResponseEntity;

import com.rbilling.DTO.RetailInvoiceRequestDTO;

public interface RetailBillingService {

	ResponseEntity<?> createRetailInvoice(RetailInvoiceRequestDTO request);

}
