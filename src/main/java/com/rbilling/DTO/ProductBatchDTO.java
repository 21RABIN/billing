package com.rbilling.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ProductBatchDTO {
	
	 private Long id; 
	private Long product_id;
    private String batch_no;
    private LocalDate expiry_date;
    private BigDecimal purchase_price;
    private LocalDate manufacture_date;
    private Integer stock_qty;
    private Long supplier_id;
    private Boolean isActive;

}
