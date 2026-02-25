package com.rbilling.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rbilling.model.BusinessUnit;
import com.rbilling.model.ERole;
import com.rbilling.model.Employee;
import com.rbilling.model.Role;
import com.rbilling.model.User;
import com.rbilling.repository.BusinessUnitRepository;
import com.rbilling.repository.EmployeeRepository;
import com.rbilling.repository.RoleRepository;
import com.rbilling.repository.UserRepository;
import com.rbilling.request.LoginRequest;
import com.rbilling.request.SignupRequest;
import com.rbilling.responce.JwtResponse;
import com.rbilling.responce.MessageResponse;
import com.rbilling.security.jwt.JwtUtils;
import com.rbilling.security.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600) // Allowing all origins is risky. Consider restricting to trusted domains in
										   // production.
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	BusinessUnitRepository businessUnitRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	BusinessUnitRepository bunitrepo;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
			HttpServletRequest servrequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		System.out.println("roles :" + roles);

		JwtResponse response = new JwtResponse();
//		response.setToken(jwt);
		response.setAccessToken(jwt);
		response.setId(userDetails.getId());
		response.setUsername(userDetails.getUsername());
		response.setEmail(userDetails.getEmail());
		response.setRoles(roles);
		if (roles.size() > 0) {

			return ResponseEntity.ok(response);
		}

		return ResponseEntity.badRequest().body(new MessageResponse("Login failed! User Not Exist"));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest,
			HttpServletRequest servrequest) {

		// Check if email already exists
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		// Map roles from request
		Set<String> strRoles = signUpRequest.getRole();

		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			// Default role USER
			Role userRole = (Role) roleRepository.findByRole(ERole.ROLE_CLIENT)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			// Assign roles based on request

			strRoles.forEach(role -> {
				System.out.println("role:" + role);

				switch (role) {

				case "ROLE_ADMIN":
					Role specific_role = (Role) roleRepository.findByRole(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(specific_role);
					break;
				case "ROLE_FRANCHESE":
					specific_role = (Role) roleRepository.findByRole(ERole.ROLE_FRANCHESE)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(specific_role);
					break;
				case "ROLE_MANAGER":
					specific_role = (Role) roleRepository.findByRole(ERole.ROLE_MANAGER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(specific_role);
					break;
				case "ROLE_EMPLOYEE":
					specific_role = (Role) roleRepository.findByRole(ERole.ROLE_EMPLOYEE)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(specific_role);
					break;
				case "ROLE_CLIENT":
					specific_role = (Role) roleRepository.findByRole(ERole.ROLE_CLIENT)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(specific_role);
					break;
				default:
					specific_role = (Role) roleRepository.findByRole(ERole.ROLE_CLIENT)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(specific_role);
				}
			});
		}

//		String nome=bunitrepo.findbybunitname();

		// Create new user's account
		User user = new User(signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()),
				signUpRequest.getUsername(), signUpRequest.getPassword());
		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String password) {

		if (!userRepository.existsByEmail(email)) {
			return ResponseEntity.badRequest().body(new MessageResponse("User Not found!"));
		}

		userRepository.resetPassword(encoder.encode(password), password, email);

		return ResponseEntity.ok(new MessageResponse("Password Updated successfully!"));
	}

}
