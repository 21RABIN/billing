package com.rbilling.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.rbilling.DTO.EmployeeDTO;

public interface EmployeeService {



	ResponseEntity<?> createUpdateEmp(EmployeeDTO empdto);

	Map<String, Object> getUserFullDetails(Long user_id);

}
