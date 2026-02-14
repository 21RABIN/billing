package com.rbilling.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "customers")
@Data

public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long business_unit_id;
    private String name;
    private String mobile;
    private String email;
    private String address;

    @Column(name="isactive")
    private Boolean isActive = true;
    
    private Boolean isDelete = false;
    
}
