package com.rbilling.DTO;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class RetailInvoiceRequestDTO {
	
	 	private Long business_unit_id;
	    private Long customer_id;
	    private Long billed_by;
	    private String status;
	    private List<ProductItemDTO> productitems;
	    private PaymentDTO payment;
	    

	  
	    
	    @Data
	    public static class ProductItemDTO {
	        private Long product_id;
	        private Long batch_id;   // required if track_batch = 1
	        private Integer quantity;
	        private BigDecimal discount;
	        private String item_type;
	       
	    }
	 

}
