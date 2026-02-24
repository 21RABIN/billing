package com.rbilling.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rbilling.model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	
	@Query(value = "SELECT COALESCE(SUM(net_amount),0) FROM invoices WHERE  (:userId IS NULL OR billed_by = :userId)", nativeQuery = true)
	BigDecimal getTotalInvoiceAmount(Long userId);

}
