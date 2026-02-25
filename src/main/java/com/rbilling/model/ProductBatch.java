package com.rbilling.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_batches")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long product_id;

    private String batch_no;

    private LocalDate expiry_date;

    private LocalDate manufacture_date;

    private Integer stock_qty;

    private BigDecimal purchase_price;
    

    private Long supplier_id;

    private Boolean isActive;
}


