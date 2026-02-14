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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rbilling.DTO.EmployeeDTO;
import com.rbilling.repository.EmployeeRepository;
import com.rbilling.service.EmployeeService;

@CrossOrigin(origins = "*", maxAge = 3600) // Allowing all origins is risky. Consider restricting to trusted domains in
											// production.
@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

	@Autowired
	EmployeeService empservice;

	@Autowired
	EmployeeRepository emprepo;

	@PostMapping("/create") // Both Api Create and Update
	 @PreAuthorize("hasRole('ADMIN')")  //Admin role Only Access this Api
	public ResponseEntity<?> createUpdateEmp(@RequestBody EmployeeDTO empdto) {

		System.out.println("nsfajkn");
		return empservice.createUpdateEmp(empdto);
	}

	@GetMapping("/franchises")
	public ResponseEntity<List<Map<String, Object>>> getEmployeeFranchise(@RequestParam Long empid) {

		List<Map<String, Object>> units = emprepo.getEmployeeFranchise(empid);

		return ResponseEntity.ok(units);
	}

	@GetMapping("/all")
	public ResponseEntity<List<Map<String, Object>>> getAllEmployees() {
		List<Map<String, Object>> employees = emprepo.getAllEmployees();
		return ResponseEntity.ok(employees);
	}

}
