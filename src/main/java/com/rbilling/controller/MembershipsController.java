package com.rbilling.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rbilling.DTO.MembershipDTO;
import com.rbilling.DTO.ServiceMappingDTO;
import com.rbilling.repository.MembershipRepository;
import com.rbilling.repository.ServicesRepository;
import com.rbilling.service.MembershipService;

@CrossOrigin(origins = "*", maxAge = 3600) // Allowing all origins is risky. Consider restricting to trusted domains in
											// production.
@RestController
@RequestMapping("/api/v1/membership")
public class MembershipsController {

	@Autowired
	MembershipRepository memrepo;
	
	@Autowired
	MembershipService memsserv;
	
	@Autowired
	ServicesRepository servicerepo;

	  @PostMapping("/create")
	    public ResponseEntity<?> createOrUpdateMembership(@RequestBody MembershipDTO memsdto) {
	        return memsserv.createOrUpdateMembership(memsdto);
	    }
	  
	  @PostMapping("/memsmap")
	    public ResponseEntity<?> MapMembership(@RequestBody MembershipDTO memsdto) {
	        return memsserv.MapMembership(memsdto);
	    }
	  
	  //This api changes Needed 
	  @GetMapping("/all")
	  public ResponseEntity<List<MembershipDTO>> getAllMemberships() {
	      List<Object[]> results = memrepo.getAllMemberships();
	      
	      Map<Long, MembershipDTO> membershipMap = new LinkedHashMap<>();
	       
	      for (Object[] row : results) {
	    	    if (row[0] == null) continue; // skip invalid rows

	    	    Long membershipId = ((Number) row[0]).longValue();

	    	    MembershipDTO membership = membershipMap.computeIfAbsent(membershipId, id -> {
	    	        MembershipDTO dto = new MembershipDTO();
	    	        dto.setId(id);
	    	        dto.setName((String) row[1]);
	    	        dto.setValidity_days(row[2] != null ? ((Number) row[2]).intValue() : null);
	    	        dto.setDiscount_type((String) row[3]);
	    	        dto.setDiscount_value((BigDecimal) row[4]);
	    	        dto.setIsActive(row[15] != null ? ((Number) row[15]).intValue() == 1 : true);

	    	        dto.setServices(new ArrayList<>());
	    	        return dto;
	    	    });

	    	    if (row[5] != null) { // only add service if present
	    	        ServiceMappingDTO service = new ServiceMappingDTO();
	    	        service.setService_id(((Number) row[5]).longValue());
	    	        service.setBusiness_unit_id(row[6] != null ? ((Number) row[6]).longValue() : null);
	    	        service.setBusiness_name((String) row[7]);
	    	        service.setName((String) row[8]);
	    	        service.setBase_Price((BigDecimal) row[9]);
	    	        service.setGst_percent((BigDecimal) row[10]);
	    	        service.setSac_code((String) row[11]);
	    	        service.setIsActive(row[12] != null ? (Boolean) row[12] : true);
	    	        service.setSpecial_price((BigDecimal) row[13]);
	    	        service.setDiscount_percent((BigDecimal) row[14]);

	    	        membership.getServices().add(service);
	    	    }
	    	}

	      
	    
	      return ResponseEntity.ok(new ArrayList<>(membershipMap.values()));
	  }

	
	@GetMapping("/servceprice")
	public ResponseEntity<List<Map<String, Object>>> getAllServMemsPrice() {
		List<Map<String, Object>> servmemsprice = servicerepo.getAllServMemsPrice();
		return ResponseEntity.ok(servmemsprice);
	}

}
