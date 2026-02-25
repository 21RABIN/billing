package com.rbilling.service;

import org.springframework.http.ResponseEntity;

import com.rbilling.DTO.ServicesDTO;

public interface ServicesService  {

	ResponseEntity<?> createUpdateServices(ServicesDTO servdto);
	
	ResponseEntity<?> deleteService(Long id);

}
