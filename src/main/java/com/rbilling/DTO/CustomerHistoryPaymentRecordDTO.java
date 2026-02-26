package com.rbilling.DTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CustomerHistoryPaymentRecordDTO {

	private String invoice;
	private Long customerId;
	private String customer;
	private String method;
	private BigDecimal amount;
	private String status;
	private String date;
}
