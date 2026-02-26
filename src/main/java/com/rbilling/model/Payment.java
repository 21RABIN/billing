package com.rbilling.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long invoice_id;
	 private BigDecimal amount;
    private String mode; // CASH, CARD, UPI, BANK
    private String reference_no;
    
    private LocalDateTime createdAt;
    
    private LocalDate payment_date;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        
    }

}
