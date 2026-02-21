package com.rbilling.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Services {

		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

		@Column(name="business_unit_id")
	    private Integer businessUnitId;

	    private String name;
	    
	  
	    private BigDecimal base_price;


	    private BigDecimal gst_percent;
	    
	    private String sac_code;
	    
	    private String image;
	    
	    @Transient
	    private BigDecimal discount_percent;

	    @Column(name="is_active")
	    private Boolean isActive = true;
}
