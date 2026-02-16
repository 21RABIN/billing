package com.rbilling.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbilling.DTO.EmployeeDTO;
import com.rbilling.model.BusinessUnit;
import com.rbilling.model.ERole;
import com.rbilling.model.Employee;
import com.rbilling.model.Role;
import com.rbilling.model.User;
import com.rbilling.repository.BusinessUnitRepository;
import com.rbilling.repository.EmployeeRepository;
import com.rbilling.repository.RoleRepository;
import com.rbilling.repository.UserRepository;
import com.rbilling.responce.MessageResponse;
import com.rbilling.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	BusinessUnitRepository bunitrepo;

	@Autowired
	EmployeeRepository emprepo;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	RoleRepository roleRepository;

	private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	public ResponseEntity<?> createUpdateEmp(EmployeeDTO empdto) {

		// Create Employee
		if (empdto.getId() == null) {

			if (empdto.getBusiness_unit_id() == null) {
				return ResponseEntity.badRequest().body(new MessageResponse("Business Unit is required"));
			}

			BusinessUnit unit = bunitrepo.findById(empdto.getBusiness_unit_id()).orElse(null);

			if (unit == null) {
				return ResponseEntity.badRequest().body(new MessageResponse("Invalid Business Unit"));
			}

			if (emprepo.existsByEmail(empdto.getEmail())) {

				return ResponseEntity.badRequest().body(new MessageResponse("Employee already available!"));

			} else {
				if (emprepo.existsByMobile(empdto.getMobile())) {
					return ResponseEntity.badRequest().body(new MessageResponse("Employee already available!"));
				}

			}

			Employee employee = new Employee();

			// Login Credential Adding
			Set<Role> roles = new HashSet<>();

			Role specific_role = (Role) roleRepository.findByRole(ERole.ROLE_EMPLOYEE)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));

			System.out.println("specific_role :" + specific_role);
			roles.add(specific_role);

			// Create new user's account
			User user = new User(empdto.getEmail(), encoder.encode(empdto.getMobile()), empdto.getEmail(),
					empdto.getMobile());
			user.setRoles(roles);
			userRepository.save(user);

			System.out.println("user.getId() :" + user.getId());

			employee.setUser_id(user.getId());
			employee.setBusiness_unit_id(empdto.getBusiness_unit_id());
			employee.setName(empdto.getName());
			employee.setMobile(empdto.getMobile());
			employee.setEmail(empdto.getEmail());
			employee.setAddress(empdto.getAddress());
			employee.setIsActive(true);

			emprepo.save(employee);

			return ResponseEntity.ok(new MessageResponse("Employee Created Successfully"));
		}

		// Update Employee
		else {

			Employee employee = emprepo.findById(empdto.getId()).orElse(null);

			if (employee == null) {
				return ResponseEntity.badRequest().body(new MessageResponse("Employee not found"));
			}

			if (emprepo.existsByEmailAndIdNot(empdto.getEmail(), empdto.getId())) {

				return ResponseEntity.badRequest().body(new MessageResponse("Employee Email already available!"));

			} else {
				if (emprepo.existsByMobileAndIdNot(empdto.getMobile(), empdto.getId())) {
					return ResponseEntity.badRequest().body(new MessageResponse("Employee Mobile already available!"));
				}

			}

			if (empdto.getName() != null)
				employee.setName(empdto.getName());

			if (empdto.getMobile() != null)
				employee.setMobile(empdto.getMobile());

			if (empdto.getEmail() != null)
				employee.setEmail(empdto.getEmail());

			if (empdto.getBusiness_unit_id() != null)
				employee.setBusiness_unit_id(empdto.getBusiness_unit_id());

			if (empdto.getAddress() != null)
				employee.setAddress(empdto.getAddress());

			if (empdto.getIsActive() != null)
				employee.setIsActive(empdto.getIsActive());

			emprepo.save(employee);

			User user = userRepository.findById(employee.getUser_id())
					.orElseThrow(() -> new RuntimeException("Error: User not found."));

			// Check if email already exists for another user
			if (userRepository.existsByEmailAndIdNot(empdto.getEmail(), empdto.getUser_id())) {
				return ResponseEntity.badRequest().body(new MessageResponse("User email already available!"));
			}

			// Update fields
			user.setEmail(employee.getEmail());
			user.setPassword(encoder.encode(employee.getMobile())); // encoding mobile as password
			user.setRaw_password(employee.getMobile());
			user.setUsername(employee.getEmail());

			// Save changes
			userRepository.save(user);

			return ResponseEntity.ok(new MessageResponse("Employee Updated Successfully"));
		}
	}

}
