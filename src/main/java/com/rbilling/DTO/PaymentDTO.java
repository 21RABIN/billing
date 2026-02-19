package com.rbilling.DTO;

import java.math.BigDecimal;

import lombok.Data;


@Data
public class PaymentDTO {
	
	 private BigDecimal amount;
     private String mode; // CASH, CARD, UPI, BANK
     private String reference_no;

}
