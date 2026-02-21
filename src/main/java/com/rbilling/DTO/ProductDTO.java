package com.rbilling.DTO;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProductDTO {
	
	 private Long id;   // null = create
	    private String name;
	    private Long business_unit_id;
	    private String sku;
	    private Double price;
	    private BigDecimal selling_price;
	    private BigDecimal discount_percent;
	    private BigDecimal gst_percent;
	    private Boolean track_batch;
	    private String image;
	    private Boolean isActive;
	   private ProductBatchDTO batch;
	   
	   
	   
	   
	   
	   
}
