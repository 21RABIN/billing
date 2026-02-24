package com.rbilling.DTO;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class MembershipDTO {
	
		private Long id; // null = create, not null = update

	    private String name;
	    private Integer validity_days;
	    private String discount_type; // PERCENT / FLAT
	    private BigDecimal discount_value;
	    private Boolean isActive;

	    private List<ServiceMappingDTO> services;
	    private List<Long> customer_id;
	    private Long business_unit_id;

}
