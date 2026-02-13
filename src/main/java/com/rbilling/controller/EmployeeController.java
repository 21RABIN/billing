package com.rbilling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rbilling.DTO.EmployeeDTO;
import com.rbilling.service.EmployeeService;

@CrossOrigin(origins = "*", maxAge = 3600)  // Allowing all origins is risky. Consider restricting to trusted domains in production.
@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
	
	@Autowired
	EmployeeService empservice;
	
	 @PostMapping("/create")//Both Api Create and Update
	 @PreAuthorize("hasRole('ADMIN')")  //Admin role Only Access this Api
	    public ResponseEntity<?> createUpdateEmp(@RequestBody EmployeeDTO empdto) {
		 
	        return empservice.createUpdateEmp(empdto);
	    }
	
	

}
