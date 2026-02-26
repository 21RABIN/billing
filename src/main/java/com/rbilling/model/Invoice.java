package com.rbilling.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "invoices")
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_no")
    private String invoice_no;

    @Column(name = "business_unit_id")
    private Long business_unit_id;

    @Column(name = "customer_id")
    private Long customer_id;

    @Column(name = "invoice_date")
    private LocalDate invoice_date;

    @Column(name = "tax_amount")
    private BigDecimal tax_amount;

    @Column(name = "total_amount")
    private BigDecimal total_amount;

    private String status; // ESTIMATE, PAID, PARTIAL, CANCELLED

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "billed_by")
    private Long billed_by;

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
