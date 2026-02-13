package com.rbilling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rbilling.DTO.ServicesDTO;
import com.rbilling.service.ServicesService;

@CrossOrigin(origins = "*", maxAge = 3600)  // Allowing all origins is risky. Consider restricting to trusted domains in production.
@RestController
@RequestMapping("/api/v1/service")
public class ServiceController {
	
	@Autowired
	ServicesService servicesserv;
	
	@PostMapping("/create")//Both Api Create and Update
//    @PreAuthorize("hasRole('FRANCHISE')")
    public ResponseEntity<?> createUpdateServices(@RequestBody ServicesDTO dto) {

       
        return servicesserv.createUpdateServices(dto);
    }

}
