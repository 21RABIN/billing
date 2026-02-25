package com.rbilling.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "memberships")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Membership {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(name = "validity_days")
	private Integer validityDays;

	@Column(name = "discount_type")
	private String discountType;
	
	@Column(name = "discount_value")
	private BigDecimal discountValue;
	
	@Column(name="business_unit_id")
	private Long businessUnitId;
	
	@Column(name = "is_active")
	private Boolean isActive;



	
}

