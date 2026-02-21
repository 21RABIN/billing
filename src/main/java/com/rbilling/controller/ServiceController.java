package com.rbilling.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rbilling.DTO.ServicesDTO;
import com.rbilling.repository.ServicesRepository;
import com.rbilling.service.ServicesService;

@CrossOrigin(origins = "*", maxAge = 3600)  // Allowing all origins is risky. Consider restricting to trusted domains in production.
@RestController
@RequestMapping("/api/v1/service")
public class ServiceController {
	
	@Autowired
	ServicesService servicesserv;
	
	@Autowired
	ServicesRepository servicerepo;
	
	@PostMapping("/create")//Both Api Create and Update
//    @PreAuthorize("hasRole('ADMIN','FRANCHISE')")
    public ResponseEntity<?> createUpdateServices(@RequestBody ServicesDTO dto) {

       
        return servicesserv.createUpdateServices(dto);
    }

	@GetMapping("/all")
	public ResponseEntity<List<Map<String, Object>>> getAllServices(@RequestParam Long bunitid) {
		List<Map<String, Object>> service = servicerepo.getAllServices(bunitid);
		

		List<Map<String, Object>> enrichedProducts = service.stream().map(p -> {
			Map<String, Object> mutable = new HashMap<>(p);
			mutable.put("type", "SERVICE");
			return mutable;
		}).collect(Collectors.toList());
		return ResponseEntity.ok(enrichedProducts);
	}
	
	
	
	
	


}
