package com.rbilling.model;

import java.math.BigDecimal;

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
@Table(name = "purchase_order_items")
@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "purchase_order_id")
    private Long purchase_order_id;

    @Column(name = "product_id")
    private Long productId;

    
    private Integer quantity;

    
    private BigDecimal purchase_price;
}

