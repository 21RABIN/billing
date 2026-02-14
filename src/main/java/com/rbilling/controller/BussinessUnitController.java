package com.rbilling.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rbilling.DTO.BusinessUnitDTO;
import com.rbilling.repository.BusinessUnitRepository;
import com.rbilling.service.BusinessUnitService;

@CrossOrigin(origins = "*", maxAge = 3600) // Allowing all origins is risky. Consider restricting to trusted domains in
											// production.
@RestController
@RequestMapping("/api/v1/bunit")
public class BussinessUnitController {

	@Autowired
	BusinessUnitService bunitservice;

	@Autowired
	BusinessUnitRepository bunitrepo;

	// Create Business Unit
	@PostMapping("/create") // Both Api Create and Update
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createUpdateBunit(@RequestBody BusinessUnitDTO bunitdto) {
		
		System.out.println("bunitdto :"+bunitdto);

		return bunitservice.createUpdateBunit(bunitdto);
	}

//	@GetMapping("/getbusinessunit")
//	public List<Map<String, Object>> getbusinessunit() {
//
//		try {
//
//			return bunitrepo.findAll();
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//
//		return null;
//	}

	@GetMapping("/all")
	public ResponseEntity<List<Map<String, Object>>> getAllBusinessUnits() {
		
		List<Map<String, Object>> units = bunitrepo.getAllBusinessUnits();
		
		return ResponseEntity.ok(units);
	}

}
