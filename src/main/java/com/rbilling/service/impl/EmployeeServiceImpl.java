package com.rbilling.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rbilling.DTO.EmployeeDTO;
import com.rbilling.DTO.EmployeePerformanceDTO;
import com.rbilling.model.BusinessUnit;
import com.rbilling.model.Employee;
import com.rbilling.model.Role;
import com.rbilling.model.User;
import com.rbilling.repository.BusinessUnitRepository;
import com.rbilling.repository.EmployeeRepository;
import com.rbilling.repository.RoleRepository;
import com.rbilling.repository.UserRepository;
import com.rbilling.responce.MessageResponse;
import com.rbilling.service.AccessScopeService;
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

	@Autowired
	AccessScopeService accessScopeService;

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

			Role specific_role = (Role) roleRepository.findByRole(empdto.getRole())
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
			
			Set<Role> roles = new HashSet<>();

			Role specific_role = (Role) roleRepository.findByRole(empdto.getRole())
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(specific_role);
			user.setRoles(roles);


			// Save changes
			userRepository.save(user);
			
			
			
			
			
			return ResponseEntity.ok(new MessageResponse("Employee Updated Successfully"));
		}
	}
	
	
	public Map<String, Object> getUserFullDetails(Long userId) {

	    Map<String, Object> response = new LinkedHashMap<>();

	    //  Get Basic User
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    //Get Role
	    String role = roleRepository.getRoleuserid(userId);

	    response.put("userId", user.getId());
	    response.put("username", user.getUsername());
	    response.put("email", user.getEmail());
	    response.put("role", role);

	    // If Employee Role
	    if ("ROLE_EMPLOYEE".equalsIgnoreCase(role)) {

	        Map<String, Object> empData =
	        		emprepo.getEmployeeFullDetails(userId);

	        if (empData != null) {

	            Map<String, Object> businessUnit = new LinkedHashMap<>();
	            businessUnit.put("id", empData.get("bu_id"));
	            businessUnit.put("name", empData.get("bu_name"));
	            businessUnit.put("type", empData.get("bu_type"));
	            

	            Map<String, Object> parent = new LinkedHashMap<>();
	            parent.put("id", empData.get("parent_id"));
	            parent.put("name", empData.get("parent_name"));
	            parent.put("type", empData.get("parent_type"));

//	            businessUnit.put("parent", parent);

	            Map<String, Object> employee = new LinkedHashMap<>();
	            employee.put("id", empData.get("emp_id"));
	            employee.put("name", empData.get("emp_name"));
	            employee.put("mobile", empData.get("mobile"));
//	            employee.put("businessUnit", businessUnit);
	            

	            response.put("employee", employee);
	            response.put("franchese", businessUnit);
	            response.put("main", parent);
	        }
	    }

	    return response;
	}

	@Override
	public ResponseEntity<?> deleteEmployee(Long id) {
		Employee employee = emprepo.findById(id).orElse(null);
		if (employee == null) {
			return ResponseEntity.badRequest().body(new MessageResponse("Employee not found"));
		}

		employee.setIsActive(false);
		emprepo.save(employee);
		return ResponseEntity.ok(new MessageResponse("Employee Deleted Successfully"));
	}

	@Override
	public List<EmployeePerformanceDTO> getEmployeePerformance(Long user_id, LocalDate from_date, LocalDate to_date) {
		List<Map<String, Object>> rows = new ArrayList<>();
		String role = accessScopeService.getRole(user_id);

		if ("ROLE_ADMIN".equals(role)) {
			rows = emprepo.getEmployeePerformanceAll(from_date, to_date);
		} else if ("ROLE_EMPLOYEE".equals(role)) {
			rows = emprepo.getEmployeePerformanceByUserId(user_id, from_date, to_date);
		} else {
			List<Long> unitIds = accessScopeService.getAccessibleBusinessUnitIds(user_id);
			if (!unitIds.isEmpty()) {
				rows = emprepo.getEmployeePerformanceByUnits(unitIds, from_date, to_date);
			}
		}

		List<EmployeePerformanceDTO> response = new ArrayList<>();
			for (Map<String, Object> row : rows) {
				EmployeePerformanceDTO dto = new EmployeePerformanceDTO();
				dto.setEmployee_id(toLong(row.get("employee_id")));
				dto.setUser_id(toLong(row.get("user_id")));
				dto.setEmployee_name(toStringValue(row.get("employee_name")));
				dto.setBusiness_unit_id(toLong(row.get("business_unit_id")));
				dto.setBusiness_unit_name(toStringValue(row.get("business_unit_name")));
				dto.setTotal_invoices(toLong(row.get("total_invoices")));
				dto.setTotal_service_amount(toBigDecimal(row.get("total_service_amount")));
				dto.setTotal_product_amount(toBigDecimal(row.get("total_product_amount")));
				dto.setTotal_amount(toBigDecimal(row.get("total_amount")));
				response.add(dto);
		}

		return response;
	}

	private Long toLong(Object value) {
		if (value == null) {
			return 0L;
		}
		if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		try {
			return Long.parseLong(value.toString());
		} catch (Exception ex) {
			return 0L;
		}
	}

	private BigDecimal toBigDecimal(Object value) {
		if (value == null) {
			return BigDecimal.ZERO;
		}
		if (value instanceof BigDecimal) {
			return (BigDecimal) value;
		}
		if (value instanceof Number) {
			return BigDecimal.valueOf(((Number) value).doubleValue());
		}
		try {
			return new BigDecimal(value.toString());
		} catch (Exception ex) {
			return BigDecimal.ZERO;
		}
	}

	private String toStringValue(Object value) {
		return value == null ? null : value.toString();
	}

}
