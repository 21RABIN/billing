package com.rbilling.controller;

import java.util.Collections;
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

import com.rbilling.DTO.BusinessUnitDTO;
import com.rbilling.repository.BusinessUnitRepository;
import com.rbilling.service.AccessScopeService;
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

	@Autowired
	AccessScopeService accessScopeService;

	// Create Business Unit
	@PostMapping("/create") // Both Api Create and Update
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createUpdateBunit(@RequestBody BusinessUnitDTO bunitdto) {
		
		System.out.println("bunitdto :"+bunitdto);

		return bunitservice.createUpdateBunit(bunitdto);
	}



	@GetMapping("/all")
	public ResponseEntity<List<Map<String, Object>>> getAllBusinessUnits(@RequestParam Long user_id) {
		if (accessScopeService.isAdmin(user_id)) {
			return ResponseEntity.ok(bunitrepo.getAllBusinessUnits(0L));
		}

		List<Long> unitIds = accessScopeService.getAccessibleBusinessUnitIds(user_id);
		if (unitIds.isEmpty()) {
			return ResponseEntity.ok(Collections.emptyList());
		}

		return ResponseEntity.ok(bunitrepo.getAllBusinessUnitsByIds(unitIds));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteBusinessUnit(@PathVariable Long id) {
		return bunitservice.deleteBusinessUnit(id);
	}
	

	
	
	
	
	

}
