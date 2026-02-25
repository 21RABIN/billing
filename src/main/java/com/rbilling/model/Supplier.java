package com.rbilling.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "suppliers")
@Data
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="business_unit_id")
    private Long businessUnitId;

    
    private String supplier_name;

    private String contact_person;
    private String phone;
    private String email;
    private String address;
    private String gst_number;

    private Boolean isActive = true;

    private LocalDateTime createdAt = LocalDateTime.now();
}

