package com.rbilling.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.rbilling.DTO.RetailInvoiceRequestDTO;

public interface RetailBillingService {

	ResponseEntity<?> createRetailInvoice(RetailInvoiceRequestDTO request);

	List<Map<String, Object>> getRoleBasedPayments(Long user_id);

	List<Map<String, Object>> getPaymentData(Long user_id);

	

}
