package com.rbilling.DTO;

import lombok.Data;

@Data
public class CustomerHistoryCustomerDTO {

	private Long id;
	private String name;
	private String mobile;
	private String email;
	private String business_name;
	private String business_unit_name;
	private Boolean membership_enabled;
	private Boolean membershipEnabled;
	private String membership_name;
}
