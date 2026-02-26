package com.rbilling.DTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CustomerHistoryServiceRecordDTO {

	private String id;
	private Long customerId;
	private String customer;
	private String service;
	private String staff;
	private String duration;
	private BigDecimal amount;
	private String status;
	private String date;
	private String time;
}
