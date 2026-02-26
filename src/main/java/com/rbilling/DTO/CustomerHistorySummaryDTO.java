package com.rbilling.DTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CustomerHistorySummaryDTO {

	private Long totalVisits;
	private String lastVisit;
	private BigDecimal totalSpend;
	private BigDecimal outstanding;
}
