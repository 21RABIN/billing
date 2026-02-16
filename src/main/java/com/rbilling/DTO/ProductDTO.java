package com.rbilling.DTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductDTO {
	
	 private Long id;   // null = create
	    private String name;
	    private Long business_unit_id;
	    private String sku;
	    private Double price;
	    private BigDecimal selling_price;
	    private Double gst_percent;
	    private Boolean isActive;
	   private ProductBatchDTO batch;
	   
	   
	   
	   
	   
	   
}
