package com.rbilling.DTO;

import com.rbilling.model.BusinessType;

import lombok.Data;

@Data
public class BusinessUnitDTO {
		private Long id;          // Required for update, null for create
	    private String name;
	    private BusinessType type;      // MAIN / FRANCHISE
	    private Long parent_id;    // Required only for FRANCHISE
	    private String address;
	    private String mobile;
	    private String email;
	    private Long user_id;
	    private String gst_in;
	    private Boolean isActive;
}
