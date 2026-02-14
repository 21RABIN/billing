package com.rbilling.DTO;

import lombok.Data;

@Data
public class ProductDTO {
	
	 private Long id;   // null = create
	    private String name;
	    private Long business_unit_id;
	    private String sku;
	    private Double price;
	    private Double gst_percent;
	    private Boolean isActive;
}
