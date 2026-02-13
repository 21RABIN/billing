package com.rbilling.DTO;

import lombok.Data;

@Data
public class BusinessUnitDTO {
		private Long id;          // Required for update, null for create
	    private String name;
	    private String type;      // MAIN / FRANCHISE
	    private Long parentId;    // Required only for FRANCHISE
	    private String address;
	    private String mobile;
	    private String gstIn;
	    private Boolean isActive;
}
