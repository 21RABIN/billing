package com.rbilling.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rbilling.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
