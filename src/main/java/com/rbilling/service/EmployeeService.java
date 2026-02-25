package com.rbilling.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.rbilling.DTO.EmployeeDTO;
import com.rbilling.DTO.EmployeePerformanceDTO;

public interface EmployeeService {



	ResponseEntity<?> createUpdateEmp(EmployeeDTO empdto);
	
	ResponseEntity<?> deleteEmployee(Long id);
	
	List<EmployeePerformanceDTO> getEmployeePerformance(Long user_id, LocalDate from_date, LocalDate to_date);

	Map<String, Object> getUserFullDetails(Long user_id);

}
