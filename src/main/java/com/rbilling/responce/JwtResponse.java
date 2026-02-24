package com.rbilling.responce;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
	private String accessToken;
	private String type = "Bearer";
	private Long id;
	private String username;
	private String email;
	private List<String> roles;
}
