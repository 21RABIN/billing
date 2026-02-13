package com.rbilling.DTO;

import lombok.Data;

@Data
public class CustomerDTO {
	
	private Long id; // null = create

    private Long businessUnitId;
    private String name;
    private String mobile;
    private String email;
    private String address;
    private Boolean isActive;
    private Boolean membershipEnabled;
    private Long membershipId;

}
