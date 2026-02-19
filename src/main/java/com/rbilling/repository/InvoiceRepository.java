package com.rbilling.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rbilling.model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
