package com.rbilling.DTO;

import lombok.Data;

@Data
public class ServicesDTO {

	
	private Long id;   // null = create
	private Integer businessUnitId;
    private String name;
    private Double basePrice;
    private Double gstPercent;
    private Boolean isActive;
}
