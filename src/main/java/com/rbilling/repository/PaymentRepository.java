package com.rbilling.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rbilling.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	
	@Query(value = "SELECT p.id AS payment_id, p.amount, p.payment_date, p.mode, p.reference_no, i.invoice_no, i.invoice_date, i.tax_amount, i.net_amount AS invoice_net_amount,i.customer_id,i.status,e.name AS employee_name,e.id as empid,cus.name as customer_name  FROM payments p JOIN invoices i ON p.invoice_id = i.id LEFT JOIN employees e ON e.user_id = i.billed_by LEFT JOIN customers cus on cus.id=i.customer_id WHERE i.business_unit_id IN (:unitIds) ORDER BY p.payment_date DESC, p.id DESC", nativeQuery = true)
	List<Map<String, Object>> getPaymentsByUnits(@Param("unitIds") List<Long> unitIds);

	@Query(value = "SELECT p.id AS payment_id, p.amount, p.payment_date, p.mode, p.reference_no, i.invoice_no, i.invoice_date, i.tax_amount, i.net_amount AS invoice_net_amount,i.customer_id,i.status,e.name AS employee_name,e.id as empid,cus.name as customer_name  FROM payments p JOIN invoices i ON p.invoice_id = i.id LEFT JOIN employees e ON e.user_id = i.billed_by LEFT JOIN customers cus on cus.id=i.customer_id WHERE i.billed_by = :userId ORDER BY p.payment_date DESC, p.id DESC", nativeQuery = true)
	List<Map<String, Object>> getPaymentsByUser(@Param("userId") Long userId);

	@Query(value = "SELECT p.id AS payment_id, p.amount, p.payment_date, p.mode, p.reference_no, i.invoice_no, i.invoice_date, i.tax_amount, i.net_amount AS invoice_net_amount,i.customer_id,i.status, e.name AS employee_name,e.id as empid,cus.name as customer_name FROM payments p JOIN invoices i ON p.invoice_id = i.id  LEFT JOIN employees e ON e.user_id = i.billed_by LEFT JOIN customers cus on cus.id=i.customer_id  ORDER BY p.payment_date DESC, p.id DESC", nativeQuery = true)
	List<Map<String, Object>> getAllPayments();

	@Query(value = "SELECT p.id AS payment_id, i.invoice_no, i.customer_id, cus.name AS customer_name, p.mode, p.amount, i.status AS invoice_status, p.payment_date FROM payments p JOIN invoices i ON i.id = p.invoice_id JOIN customers cus ON cus.id = i.customer_id WHERE i.customer_id = :customerId ORDER BY p.payment_date DESC, p.id DESC", nativeQuery = true)
	List<Map<String, Object>> getPaymentHistoryByCustomerId(@Param("customerId") Long customerId);
		
	
	@Query(value = "SELECT COALESCE(SUM(p.amount),0) FROM payments p JOIN invoices i ON p.invoice_id = i.id WHERE (:userId IS NULL OR i.billed_by = :userId)", nativeQuery = true)
	BigDecimal getTotalReceivedByUser(@Param("userId") Long userId);


	@Query(value = "SELECT COALESCE(SUM(p.amount),0) FROM payments p JOIN invoices i ON p.invoice_id = i.id WHERE (:userId IS NULL OR i.billed_by = :userId) AND p.payment_date = CURDATE()", nativeQuery = true)
	BigDecimal getTodayReceivedByUser(@Param("userId") Long userId);

	@Query(value = "SELECT COALESCE(SUM(p.amount),0) FROM payments p JOIN invoices i ON p.invoice_id = i.id WHERE i.business_unit_id IN (:unitIds)", nativeQuery = true)
	BigDecimal getTotalReceivedByUnits(@Param("unitIds") List<Long> unitIds);

	@Query(value = "SELECT COALESCE(SUM(p.amount),0) FROM payments p JOIN invoices i ON p.invoice_id = i.id WHERE i.business_unit_id IN (:unitIds) AND p.payment_date = CURDATE()", nativeQuery = true)
	BigDecimal getTodayReceivedByUnits(@Param("unitIds") List<Long> unitIds);





	
	

}
