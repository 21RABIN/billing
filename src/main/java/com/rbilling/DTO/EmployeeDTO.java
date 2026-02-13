package com.rbilling.DTO;

import lombok.Data;

@Data
public class EmployeeDTO {
	
	private Long id;               // null for create, required for update
    private Long userId;
    private Long businessUnitId;
    private String name;
    private String mobile;
    private String email;
    private String address;
    private Boolean isActive;

}
