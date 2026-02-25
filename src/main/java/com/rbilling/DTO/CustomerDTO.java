package com.rbilling.DTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.rbilling.model.CustomerMembership.Status;

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
    private boolean membership_enabled;
    private Long membership_id;
    
   
    
   

}
