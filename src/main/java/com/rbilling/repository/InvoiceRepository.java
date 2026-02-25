package com.rbilling.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rbilling.model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	
	@Query(value = "SELECT COALESCE(SUM(net_amount),0) FROM invoices WHERE  (:userId IS NULL OR billed_by = :userId)", nativeQuery = true)
	BigDecimal getTotalInvoiceAmount(Long userId);

	@Query(value = "SELECT COUNT(*) FROM invoices WHERE invoice_date = CURDATE()", nativeQuery = true)
	Long countTodayInvoices();

	@Query(value = "SELECT COUNT(*) FROM invoices WHERE billed_by = :userId AND invoice_date = CURDATE()", nativeQuery = true)
	Long countTodayInvoicesByUser(@Param("userId") Long userId);

	@Query(value = "SELECT COUNT(*) FROM invoices WHERE business_unit_id IN (:unitIds) AND invoice_date = CURDATE()", nativeQuery = true)
	Long countTodayInvoicesByBusinessUnitIds(@Param("unitIds") List<Long> unitIds);

	@Query(value = "SELECT COALESCE(SUM(net_amount),0) FROM invoices WHERE business_unit_id IN (:unitIds)", nativeQuery = true)
	BigDecimal getTotalInvoiceAmountByUnits(@Param("unitIds") List<Long> unitIds);

}
