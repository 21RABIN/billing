package com.rbilling.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rbilling.DTO.MembershipDTO;
import com.rbilling.DTO.ServiceMappingDTO;
import com.rbilling.repository.MembershipRepository;
import com.rbilling.repository.ServicesRepository;
import com.rbilling.service.AccessScopeService;
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

	@Autowired
	AccessScopeService accessScopeService;

	  @PostMapping("/create")
	    public ResponseEntity<?> createOrUpdateMembership(@RequestBody MembershipDTO memsdto) {
	        return memsserv.createOrUpdateMembership(memsdto);
	    }
	  
	  @GetMapping("/all")
	  public ResponseEntity<List<Map<String,Object>>> getAllMemberships(@RequestParam Long user_id) {
	      List<Map<String,Object>> memberships = memrepo.getMembershipsOnly();
	      List<Map<String,Object>> services = memrepo.getMembershipServices();

	      if (!accessScopeService.isAdmin(user_id)) {
	          memberships = accessScopeService.filterRowsByBusinessUnits(memberships, "business_unit_id", user_id);
	          services = accessScopeService.filterRowsByBusinessUnits(services, "business_unit_id", user_id);
	      }

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
	public ResponseEntity<List<Map<String, Object>>> getAllServMemsPrice(@RequestParam Long user_id) {
		List<Map<String, Object>> servmemsprice = servicerepo.getAllServMemsPrice();

		if (accessScopeService.isAdmin(user_id)) {
			return ResponseEntity.ok(servmemsprice);
		}

		List<Map<String, Object>> services = servicerepo.getAllServices(0L);
		services = accessScopeService.filterRowsByBusinessUnits(services, "business_unit_id", user_id);
		if (services.isEmpty()) {
			return ResponseEntity.ok(Collections.emptyList());
		}

		Set<Long> serviceIds = new HashSet<>();
		for (Map<String, Object> row : services) {
			Object idValue = row.get("id");
			if (idValue instanceof Number) {
				serviceIds.add(((Number) idValue).longValue());
			} else if (idValue != null) {
				try {
					serviceIds.add(Long.parseLong(idValue.toString()));
				} catch (Exception ignore) {
				}
			}
		}

		List<Map<String, Object>> filtered = new ArrayList<>();
		for (Map<String, Object> row : servmemsprice) {
			Object serviceIdValue = row.get("service_id");
			Long serviceId = null;
			if (serviceIdValue instanceof Number) {
				serviceId = ((Number) serviceIdValue).longValue();
			} else if (serviceIdValue != null) {
				try {
					serviceId = Long.parseLong(serviceIdValue.toString());
				} catch (Exception ignore) {
				}
			}
			if (serviceId != null && serviceIds.contains(serviceId)) {
				filtered.add(row);
			}
		}
		return ResponseEntity.ok(filtered);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteMembership(@PathVariable Long id) {
		return memsserv.deleteMembership(id);
	}

}
