package com.rbilling.DTO;

import lombok.Data;

@Data
public class TopServiceDTO {
	private Long id;
	private String name;
	private Long count;
	private String revenue;
	private Integer percent;
}
