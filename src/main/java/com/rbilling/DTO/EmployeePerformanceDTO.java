package com.rbilling.DTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EmployeePerformanceDTO {
	private Long employee_id;
	private Long user_id;
	private String employee_name;
	private Long business_unit_id;
	private String business_unit_name;
	private Long total_invoices;
	private BigDecimal total_service_amount;
	private BigDecimal total_product_amount;
	private BigDecimal total_amount;
}
