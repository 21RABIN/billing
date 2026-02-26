//package com.rbilling.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.rbilling.model.ServiceConsumptionMap;
//import com.rbilling.service.ServiceConsumptionService;
//
//import lombok.RequiredArgsConstructor;
//
//@CrossOrigin(origins = "*", maxAge = 3600)
//@RestController
//@RequestMapping("/api/v1/retail")
//@RequiredArgsConstructor
//public class ServiceConsumptionController {
//	
//	
//	@Autowired
//	ServiceConsumptionService service;
//
//	@PostMapping("/mapping")
//	public ResponseEntity<?> createMapping(@RequestBody ServiceConsumptionMap map, @RequestHeader("Role") String role) {
//		return ResponseEntity.ok(service.createMapping(map, role));
//	}
//
//}
