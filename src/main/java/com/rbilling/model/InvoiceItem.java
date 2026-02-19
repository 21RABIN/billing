package com.rbilling.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "invoice_items")
@Data
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_id")
    private Long invoice_id;

    @Column(name = "item_type")
    private String item_type; // SERVICE / PRODUCT

    @Column(name = "item_id")
    private Long item_id;

    private String description;

    private Integer quantity;

    @Column(name = "gst_percent")
    private BigDecimal gst_percent;

    @Column(name = "tax_amount")
    private BigDecimal tax_amount;

    @Column(name = "total_amount")
    private BigDecimal total_amount;

    @Column(name = "performed_by")
    private Long performed_by;

    @Column(name = "rounding_type")
    private String rounding_type;

    @Column(name = "rounding_increment")
    private BigDecimal rounding_increment;

    @Column(name = "gross_amount")
    private BigDecimal gross_amount;

    @Column(name = "round_off")
    private BigDecimal round_off;

    @Column(name = "net_amount")
    private BigDecimal net_amount;
}

