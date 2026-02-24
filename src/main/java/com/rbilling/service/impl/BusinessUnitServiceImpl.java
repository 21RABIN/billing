package com.rbilling.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rbilling.DTO.BusinessUnitDTO;
import com.rbilling.model.BusinessType;
import com.rbilling.model.BusinessUnit;
import com.rbilling.model.ERole;
import com.rbilling.model.Role;
import com.rbilling.model.User;
import com.rbilling.repository.BusinessUnitRepository;
import com.rbilling.repository.RoleRepository;
import com.rbilling.repository.UserRepository;
import com.rbilling.responce.MessageResponse;
import com.rbilling.service.BusinessUnitService;

@Service
public class BusinessUnitServiceImpl implements BusinessUnitService {

	@Autowired
	BusinessUnitRepository bunitrepo;
	

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	RoleRepository roleRepository;

	// Create and Update BusinessUnit
	public ResponseEntity<?> createUpdateBunit(BusinessUnitDTO bunitdto) {
		
		BusinessType type;
		ERole role = null;

		// CREATE
		if (bunitdto.getId() == null) {

			if (bunitrepo.existsByName(bunitdto.getName())) {

				if (bunitrepo.existsByMobile(bunitdto.getMobile())) {
					return ResponseEntity.badRequest().body(new MessageResponse("business_units already available!"));
				}
			} else {
				if (bunitrepo.existsByMobile(bunitdto.getMobile())) {
					return ResponseEntity.badRequest().body(new MessageResponse("business_units already available!"));
				}

			}

			

			try {
				type = bunitdto.getType();
			} catch (Exception e) {

				return ResponseEntity.badRequest().body(new MessageResponse("Invalid business type"));

			}

			if (type == BusinessType.FRANCHISE) {

				if (bunitdto.getParent_id() == null) {

					return ResponseEntity.badRequest().body(new MessageResponse("Parent ID required for FRANCHISE"));
					
					
				}

				BusinessUnit parent = bunitrepo.findById(bunitdto.getParent_id()).orElse(null);

				if (parent == null || parent.getType() != BusinessType.MAIN) {

					return ResponseEntity.badRequest().body(new MessageResponse("Main FRANCHISE INVALID"));

				}
				role=ERole.ROLE_FRANCHESE;
			}
			else {
				role=ERole.ROLE_MAIN;
			}
			
			// Login Credential Adding
			Set<Role> roles = new HashSet<>();

			Role specific_role = (Role) roleRepository.findByRole(role)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));

			System.out.println("specific_role :" + specific_role);
			roles.add(specific_role);

			// Create new user's account
			User user = new User(bunitdto.getEmail(), encoder.encode(bunitdto.getMobile()), bunitdto.getEmail(),
					bunitdto.getMobile());
			user.setRoles(roles);
			
			userRepository.save(user);

			BusinessUnit unit = BusinessUnit.builder().name(bunitdto.getName()).type(type)
					.parent_id(bunitdto.getParent_id()).address(bunitdto.getAddress()).mobile(bunitdto.getMobile()).email(bunitdto.getEmail()).user_id(user.getId())
					.gst_in(bunitdto.getGst_in()).isActive(true).build();

			bunitrepo.save(unit);
			
			
			
			

			return ResponseEntity.ok(new MessageResponse("Business Unit Created Successfully"));

		}

		else {
	

			if (bunitrepo.existsByMobileAndIdNot(bunitdto.getMobile(), bunitdto.getId())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Business Unit Mobile already available!"));
			}
			
			if(bunitdto.getType()==BusinessType.FRANCHISE) {
				role=ERole.ROLE_FRANCHESE;
			}
			else if(bunitdto.getType()==BusinessType.MAIN) {
				role=ERole.ROLE_MAIN;
			}

			BusinessUnit bunit = bunitrepo.findById(bunitdto.getId()).orElse(null);

			if (bunit == null) {
				return ResponseEntity.badRequest().body("Business Unit not found");
			}

			if (bunitdto.getName() != null)
				bunit.setName(bunitdto.getName());

			if (bunitdto.getAddress() != null)
				bunit.setAddress(bunitdto.getAddress());
			
			if (bunitdto.getType() != null)
				bunit.setType(bunitdto.getType());
			
			if(bunitdto.getParent_id()!=null)
				bunit.setParent_id(bunitdto.getParent_id());

			if (bunitdto.getMobile() != null)
				bunit.setMobile(bunitdto.getMobile());

			if (bunitdto.getGst_in() != null)
				bunit.setGst_in(bunitdto.getGst_in());

			if (bunitdto.getIsActive() != null)
				bunit.setIsActive(bunitdto.getIsActive());
			
			if(bunitdto.getEmail() != null)
				bunit.setEmail(bunitdto.getEmail());

			bunitrepo.save(bunit);
			
			User user = userRepository.findById(bunit.getUser_id())
					.orElseThrow(() -> new RuntimeException("Error: User not found."));

			// Check if email already exists for another user
			if (userRepository.existsByEmailAndIdNot(bunitdto.getEmail(), bunitdto.getUser_id())) {
				return ResponseEntity.badRequest().body(new MessageResponse("User email already available!"));
			}

			// Update fields
			user.setEmail(bunit.getEmail());
			user.setPassword(encoder.encode(bunit.getMobile())); // encoding mobile as password
			user.setRaw_password(bunit.getMobile());
			user.setUsername(bunit.getEmail());
			
			Set<Role> roles = new HashSet<>();

			Role specific_role = (Role) roleRepository.findByRole(role)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(specific_role);
			user.setRoles(roles);


			// Save changes
			userRepository.save(user);

			return ResponseEntity.ok(new MessageResponse("Business Unit Updated Successfully"));

		}

	}

}
