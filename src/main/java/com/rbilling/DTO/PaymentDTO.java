package com.rbilling.DTO;

import java.math.BigDecimal;

import lombok.Data;


@Data
public class PaymentDTO {
	
	 private BigDecimal payment_amount;
     private String mode; // CASH, CARD, UPI, BANK
     private BigDecimal tax_amount;
     private BigDecimal total_amount;
    
//     private BigDecimal net_amount;
    
}
