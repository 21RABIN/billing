package com.rbilling.DTO;

import lombok.Data;

@Data
public class ServicesDTO {

	
	private Long id;   // null = create
	private Integer business_unit_id;
    private String name;
    private Double base_price;
    private Double gst_percent;
    private Boolean isActive;
}
