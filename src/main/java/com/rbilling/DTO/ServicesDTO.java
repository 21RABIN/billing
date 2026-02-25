package com.rbilling.DTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ServicesDTO {

	
	private Long id;   // null = create
	private Integer business_unit_id;
    private String name;
    private BigDecimal base_price;
    private String sac_code;
    private BigDecimal gst_percent;
    private Boolean isActive;
    private String image;
}
