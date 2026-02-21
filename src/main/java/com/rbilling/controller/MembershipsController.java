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
	  
	  @GetMapping("/all")
	  public ResponseEntity<List<Map<String,Object>>> getAllMemberships() {
	      List<Map<String,Object>> memberships = memrepo.getMembershipsOnly();
	      List<Map<String,Object>> services = memrepo.getMembershipServices();

	      // Build a map keyed by membership_id
	      Map<Long, Map<String,Object>> membershipMap = new LinkedHashMap<>();

	      // Put memberships into the map
	      for (Map<String,Object> row : memberships) {
	          Long id = ((Number) row.get("id")).longValue();
	          Map<String,Object> mem = new LinkedHashMap<>(row);
	          mem.put("services", new ArrayList<Map<String,Object>>());
	          membershipMap.put(id, mem);
	      }

	      // Attach services to memberships
	      for (Map<String,Object> row : services) {
	          Long membershipId = ((Number) row.get("membership_id")).longValue();
	          Map<String,Object> membership = membershipMap.get(membershipId);
	          if (membership != null) {
	              @SuppressWarnings("unchecked")
	              List<Map<String,Object>> serviceList = (List<Map<String,Object>>) membership.get("services");
	              serviceList.add(row);
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
