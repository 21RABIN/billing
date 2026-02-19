package com.rbilling.service;

import org.springframework.http.ResponseEntity;

import com.rbilling.DTO.MembershipDTO;

public interface MembershipService {

	ResponseEntity<?>  createOrUpdateMembership(MembershipDTO memsdto);

	ResponseEntity<?> MapMembership(MembershipDTO memsdto);

}
