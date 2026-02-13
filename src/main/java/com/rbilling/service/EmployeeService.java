package com.rbilling.service;

import org.springframework.http.ResponseEntity;

import com.rbilling.DTO.EmployeeDTO;

public interface EmployeeService {



	ResponseEntity<?> createUpdateEmp(EmployeeDTO empdto);

}
