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

import com.rbilling.DTO.SupplierDTO;
import com.rbilling.repository.SupplierRepository;
import com.rbilling.service.AccessScopeService;
import com.rbilling.service.SupplierService;

@CrossOrigin(origins = "*", maxAge = 3600)  // Allowing all origins is risky. Consider restricting to trusted domains in production.
@RestController
@RequestMapping("/api/v1/supplier")
public class SupplierController {

	
	@Autowired
	SupplierService supplierService;
	
	@Autowired
	SupplierRepository suplrepo;

	@Autowired
	AccessScopeService accessScopeService;
	
	 @PostMapping("/create")
	    public ResponseEntity<?> saveSupplier(@RequestBody SupplierDTO supdto) {
	        return supplierService.createUpdateSupplier(supdto);
	    }
	 
	 @GetMapping("/all")
		public ResponseEntity<List<Map<String, Object>>> getAllSuppliers(@RequestParam Long user_id) {
			List<Map<String, Object>> suplier = suplrepo.getAllSuppliers(0L);
			suplier = accessScopeService.filterRowsByBusinessUnits(suplier, "business_unit_id", user_id);
			return ResponseEntity.ok(suplier);
		}
	 
	 @DeleteMapping("/delete/{id}")
	 public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
	 	return supplierService.deleteSupplier(id);
	 }

	

}
