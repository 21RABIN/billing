package com.rbilling.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;

import com.rbilling.DTO.EmployeeDTO;
import com.rbilling.DTO.EmployeePerformanceDTO;
import com.rbilling.repository.BusinessUnitRepository;
import com.rbilling.repository.EmployeeRepository;
import com.rbilling.service.AccessScopeService;
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

	@Autowired
	BusinessUnitRepository bunitrepo;

	@Autowired
	AccessScopeService accessScopeService;

	@PostMapping("/create") // Both Api Create and Update
	@PreAuthorize("hasRole('ADMIN')") // Admin role Only Access this Api
	public ResponseEntity<?> createUpdateEmp(@RequestBody EmployeeDTO empdto) {

		return empservice.createUpdateEmp(empdto);
	}

	@GetMapping("/franchises")
	public ResponseEntity<List<Map<String, Object>>> getEmployeeFranchise(@RequestParam Long user_id) {
		List<Long> unitIds = accessScopeService.getAccessibleBusinessUnitIds(user_id);
		if (unitIds.isEmpty()) {
			return ResponseEntity.ok(Collections.emptyList());
		}

		return ResponseEntity.ok(bunitrepo.getAllBusinessUnitsByIds(unitIds));
	}

	@GetMapping("/mainfranchises")
	public ResponseEntity<List<Map<String, Object>>> getEmployeeMainFranchise(@RequestParam Long user_id) {
		List<Long> unitIds = accessScopeService.getAccessibleBusinessUnitIds(user_id);
		if (unitIds.isEmpty()) {
			return ResponseEntity.ok(Collections.emptyList());
		}

		return ResponseEntity.ok(bunitrepo.getMainBusinessUnitsByIds(unitIds));
	}

	@GetMapping("/all")
	public ResponseEntity<List<Map<String, Object>>> getAllEmployees(@RequestParam Long user_id) {
		List<Map<String, Object>> employees = emprepo.getAllEmployees(0L);
		employees = accessScopeService.filterRowsByBusinessUnits(employees, "business_unit_id", user_id);
		return ResponseEntity.ok(employees);
	}

	@GetMapping("/userdetails")
	public ResponseEntity<?> getUserDetails(@RequestParam Long user_id) {
		return ResponseEntity.ok(empservice.getUserFullDetails(user_id));
	}

	@GetMapping("/performance")
	public ResponseEntity<List<EmployeePerformanceDTO>> getEmployeePerformance(@RequestParam Long user_id,
			@RequestParam(required = false) LocalDate from_date, @RequestParam(required = false) LocalDate to_date) {
		return ResponseEntity.ok(empservice.getEmployeePerformance(user_id, from_date, to_date));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
		return empservice.deleteEmployee(id);
	}

}
