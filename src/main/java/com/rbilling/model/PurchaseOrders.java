package com.rbilling.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long supplier_id;

	private Long business_unit_id;

	private LocalDate order_date;

	private String status;

	private BigDecimal totalAmount;
}
