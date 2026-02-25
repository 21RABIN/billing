package com.rbilling.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "service_membership_price")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMembershipPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "membership_id", nullable = false)
    private Long membershipId;

    @Column(name = "service_id", nullable = false)
    private Long service_id;

    @Column(name = "special_price", precision = 10, scale = 2)
    private BigDecimal special_price;

    @Column(name = "discount_percent", precision = 5, scale = 2)
    private BigDecimal discount_percent;


}

