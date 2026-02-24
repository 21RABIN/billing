package com.rbilling.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="business_unit_id")
    private Long businessUnitId;

    private String name;

    private String sku;

    private Double price;
    
    private String hsn_code;
    
    private BigDecimal selling_price;
    
    private BigDecimal discount_percent;
    
    private String image;

    @Column(name="gst_percent")
    private BigDecimal gst_percent;
    
    private Boolean track_batch=true;

    @Column(name="is_active")
    private Boolean isActive = true;
    
    
}
