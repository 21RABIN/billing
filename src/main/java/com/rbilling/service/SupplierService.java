package com.rbilling.service;

import org.springframework.http.ResponseEntity;

import com.rbilling.DTO.SupplierDTO;

public interface SupplierService {

	ResponseEntity<?> createUpdateSupplier(SupplierDTO supdto);

	
}
