package com.rbilling.DTO;

import com.rbilling.model.ERole;

import lombok.Data;

@Data
public class EmployeeDTO {
	
	private Long id;               // null for create, required for update
    private Long user_id;
    private Long business_unit_id;
    private String name;
    private String mobile;
    private ERole role;
    private String email;
    private String address;
    private Boolean isActive;

}
