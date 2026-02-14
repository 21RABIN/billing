package com.rbilling.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rbilling.repository.MembershipRepository;

@CrossOrigin(origins = "*", maxAge = 3600) // Allowing all origins is risky. Consider restricting to trusted domains in production.
@RestController
@RequestMapping("/api/v1/membership")
public class MembershipsController {
	
	
	@Autowired
	MembershipRepository memrepo;
	
	
	@GetMapping("/all")
	public ResponseEntity<List<Map<String, Object>>> getAllMemberships() {
		List<Map<String, Object>> employees = memrepo.getAllMemberships();
		return ResponseEntity.ok(employees);
	}

}
