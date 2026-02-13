package com.rbilling.DTO;

import lombok.Data;

@Data
public class ProductDTO {
	
	 private Long id;   // null = create
	    private String name;
	    private Long businessUnitId;
	    private String sku;
	    private Double price;
	    private Double gstPercent;
	    private Boolean isActive;
}
