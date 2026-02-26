package com.rbilling.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rbilling.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	boolean existsByMobile(String mobile);

	boolean existsByEmail(String email);

	@Query(value = "select * from  employees where user_id=:id", nativeQuery = true)
	Employee findByUserId(Long id);

	boolean existsByEmailAndIdNot(String email, Long id);

	boolean existsByMobileAndIdNot(String mobile, Long id);

	@Query(value = "select bunit.* from business_units bunit left join employees emp on bunit.id=emp.business_unit_id where(:empid=0 or emp.id=:empid)", nativeQuery = true)
	List<Map<String, Object>> getEmployeeFranchise(long empid);

//	@Query(value = "SELECT emp.*,bunit.name as business_name FROM employees emp left join business_units bunit on bunit.id=emp.business_unit_id  where (:bunitid=0 OR emp.business_unit_id=:bunitid)", nativeQuery = true) 
//	List<Map<String, Object>> getAllEmployees(Long bunitid);

	@Query(value = "SELECT emp.*, bunit.name AS business_name, r.role AS role FROM employees emp LEFT JOIN business_units bunit ON bunit.id = emp.business_unit_id LEFT JOIN user_roles u ON u.user_id = emp.user_id LEFT JOIN roles r ON r.id = u.role_id WHERE (:bunitid = 0 OR emp.business_unit_id = :bunitid)", nativeQuery = true)
	List<Map<String, Object>> getAllEmployees(Long bunitid);

	@Query(value = "select bunit.* from business_units bunit left join employees emp on bunit.id=emp.business_unit_id where(:empid=0 or emp.id=:empid) and bunit.type='MAIN'", nativeQuery = true)
	List<Map<String, Object>> getEmployeeMainFranchise(long empid);

	@Query(value = "SELECT e.id AS emp_id, e.name AS emp_name, e.mobile, bu.id AS bu_id, bu.name AS bu_name, bu.type AS bu_type, parent.id AS parent_id, parent.name AS parent_name, parent.type AS parent_type FROM employees e LEFT JOIN business_units bu ON e.business_unit_id = bu.id LEFT JOIN business_units parent ON bu.parent_id = parent.id WHERE e.user_id = :userId", nativeQuery = true)
	Map<String,Object> getEmployeeFullDetails(@Param("userId") Long userId);

	@Query(value = "SELECT e.id AS employee_id, e.user_id, e.name AS employee_name, e.business_unit_id, bu.name AS business_unit_name, COALESCE(COUNT(DISTINCT i.id),0) AS total_invoices, COALESCE(SUM(CASE WHEN ii.item_type = 'SERVICE' THEN ii.total_amount ELSE 0 END),0) AS total_service_amount, COALESCE(SUM(CASE WHEN ii.item_type = 'PRODUCT' THEN ii.total_amount ELSE 0 END),0) AS total_product_amount, COALESCE(SUM(ii.total_amount),0) AS total_amount FROM employees e LEFT JOIN business_units bu ON bu.id = e.business_unit_id LEFT JOIN invoices i ON i.billed_by = e.user_id AND (:fromDate IS NULL OR i.invoice_date >= :fromDate) AND (:toDate IS NULL OR i.invoice_date <= :toDate) LEFT JOIN invoice_items ii ON ii.invoice_id = i.id WHERE COALESCE(e.is_active, 1) = 1 GROUP BY e.id, e.user_id, e.name, e.business_unit_id, bu.name ORDER BY total_amount DESC, e.id ASC", nativeQuery = true)
	List<Map<String, Object>> getEmployeePerformanceAll(@Param("fromDate") LocalDate fromDate,
			@Param("toDate") LocalDate toDate);

	@Query(value = "SELECT e.id AS employee_id, e.user_id, e.name AS employee_name, e.business_unit_id, bu.name AS business_unit_name, COALESCE(COUNT(DISTINCT i.id),0) AS total_invoices, COALESCE(SUM(CASE WHEN ii.item_type = 'SERVICE' THEN ii.total_amount ELSE 0 END),0) AS total_service_amount, COALESCE(SUM(CASE WHEN ii.item_type = 'PRODUCT' THEN ii.total_amount ELSE 0 END),0) AS total_product_amount, COALESCE(SUM(ii.total_amount),0) AS total_amount FROM employees e LEFT JOIN business_units bu ON bu.id = e.business_unit_id LEFT JOIN invoices i ON i.billed_by = e.user_id AND (:fromDate IS NULL OR i.invoice_date >= :fromDate) AND (:toDate IS NULL OR i.invoice_date <= :toDate) LEFT JOIN invoice_items ii ON ii.invoice_id = i.id WHERE COALESCE(e.is_active, 1) = 1 AND e.business_unit_id IN (:unitIds) GROUP BY e.id, e.user_id, e.name, e.business_unit_id, bu.name ORDER BY total_amount DESC, e.id ASC", nativeQuery = true)
	List<Map<String, Object>> getEmployeePerformanceByUnits(@Param("unitIds") List<Long> unitIds,
			@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

	@Query(value = "SELECT e.id AS employee_id, e.user_id, e.name AS employee_name, e.business_unit_id, bu.name AS business_unit_name, COALESCE(COUNT(DISTINCT i.id),0) AS total_invoices, COALESCE(SUM(CASE WHEN ii.item_type = 'SERVICE' THEN ii.total_amount ELSE 0 END),0) AS total_service_amount, COALESCE(SUM(CASE WHEN ii.item_type = 'PRODUCT' THEN ii.total_amount ELSE 0 END),0) AS total_product_amount, COALESCE(SUM(ii.total_amount),0) AS total_amount FROM employees e LEFT JOIN business_units bu ON bu.id = e.business_unit_id LEFT JOIN invoices i ON i.billed_by = e.user_id AND (:fromDate IS NULL OR i.invoice_date >= :fromDate) AND (:toDate IS NULL OR i.invoice_date <= :toDate) LEFT JOIN invoice_items ii ON ii.invoice_id = i.id WHERE COALESCE(e.is_active, 1) = 1 AND e.user_id = :userId GROUP BY e.id, e.user_id, e.name, e.business_unit_id, bu.name ORDER BY total_amount DESC, e.id ASC", nativeQuery = true)
	List<Map<String, Object>> getEmployeePerformanceByUserId(@Param("userId") Long userId,
			@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);


}
