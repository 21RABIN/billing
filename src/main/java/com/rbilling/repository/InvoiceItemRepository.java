package com.rbilling.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rbilling.model.InvoiceItem;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

	@Query(value = "SELECT ii.id AS service_record_id, i.customer_id, cus.name AS customer_name, COALESCE(ii.description, serv.name) AS service_name, COALESCE(emp.name, billed_emp.name) AS staff_name, ii.quantity, ii.total_amount AS amount, i.status AS invoice_status, i.invoice_date FROM invoice_items ii JOIN invoices i ON i.id = ii.invoice_id JOIN customers cus ON cus.id = i.customer_id LEFT JOIN services serv ON serv.id = ii.item_id LEFT JOIN employees emp ON emp.user_id = ii.performed_by LEFT JOIN employees billed_emp ON billed_emp.user_id = i.billed_by WHERE i.customer_id = :customerId AND UPPER(ii.item_type) = 'SERVICE' ORDER BY i.invoice_date DESC, i.id DESC, ii.id DESC", nativeQuery = true)
	List<Map<String, Object>> getServiceHistoryByCustomerId(@Param("customerId") Long customerId);

}
