package com.rbilling.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InventoryAlertDTO {
	private Long id;
	private String item;

	private Long business_unit_id;

	private Integer stock;
	private String status;
}
