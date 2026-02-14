package com.rbilling.DTO;

import lombok.Data;

@Data
public class CustomerDTO {
	
	private Long id; // null = create

    private Long business_unit_id;
    private String name;
    private String mobile;
    private String email;
    private String address;
    private Boolean isActive;
    private Boolean membership_enabled;
    private Long membership_id;

}
