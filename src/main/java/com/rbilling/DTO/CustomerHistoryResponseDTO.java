package com.rbilling.DTO;

import java.util.List;

import lombok.Data;

@Data
public class CustomerHistoryResponseDTO {

	private CustomerHistoryCustomerDTO customer;
	private CustomerHistorySummaryDTO summary;
	private List<CustomerHistoryServiceRecordDTO> lastVisits;
	private List<CustomerHistoryServiceRecordDTO> serviceHistory;
	private List<CustomerHistoryPaymentRecordDTO> paymentHistory;
}
