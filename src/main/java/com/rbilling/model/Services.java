package com.rbilling.model;

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
	    
	  
	    private Double base_price;


	    private Double gst_percent;

	    @Column(name="is_active")
	    private Boolean isActive = true;
}
