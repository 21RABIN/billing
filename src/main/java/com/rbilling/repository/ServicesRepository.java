package com.rbilling.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rbilling.model.Services;

public interface ServicesRepository extends JpaRepository<Services, Long> {

	Services findByIdAndBusinessUnitId(Long id, Integer businessUnitId);
	
	@Query(value = "SELECT serv.*,serv.base_price as price,serv.base_price as selling_price,serv.base_price as discount_percent,REPLACE(serv.image, '/var/www/html/', 'http://192.168.1.182/') AS image_url,bunit.name as business_name FROM services serv left join business_units bunit on bunit.id=serv.business_unit_id where (:bunitid=0 OR serv.business_unit_id=:bunitid)", nativeQuery = true)
	List<Map<String, Object>> getAllServices(Long bunitid);

	 @Query(value = "select * from service_membership_price ",nativeQuery = true)
	List<Map<String, Object>> getAllServMemsPrice();

	@Query(value = "SELECT COUNT(*) FROM services", nativeQuery = true)
	Long countAllServices();

	@Query(value = "SELECT COUNT(*) FROM services WHERE business_unit_id IN (:unitIds)", nativeQuery = true)
	Long countServicesByBusinessUnitIds(@Param("unitIds") List<Long> unitIds);

	@Query(value = "SELECT s.id, s.business_unit_id, s.name, COALESCE(SUM(ii.quantity),0) AS total_qty, COALESCE(SUM(ii.total_amount),0) AS total_revenue FROM invoice_items ii JOIN invoices i ON i.id = ii.invoice_id JOIN services s ON s.id = ii.item_id WHERE ii.item_type = 'SERVICE' GROUP BY s.id, s.business_unit_id, s.name ORDER BY total_qty DESC, s.id ASC", nativeQuery = true)
	List<Map<String, Object>> getTopServicesAll();

	@Query(value = "SELECT s.id, s.business_unit_id, s.name, COALESCE(SUM(ii.quantity),0) AS total_qty, COALESCE(SUM(ii.total_amount),0) AS total_revenue FROM invoice_items ii JOIN invoices i ON i.id = ii.invoice_id JOIN services s ON s.id = ii.item_id WHERE ii.item_type = 'SERVICE' AND i.billed_by = :userId GROUP BY s.id, s.business_unit_id, s.name ORDER BY total_qty DESC, s.id ASC", nativeQuery = true)
	List<Map<String, Object>> getTopServicesByUser(@Param("userId") Long userId);

	@Query(value = "SELECT s.id, s.business_unit_id, s.name, COALESCE(SUM(ii.quantity),0) AS total_qty, COALESCE(SUM(ii.total_amount),0) AS total_revenue FROM invoice_items ii JOIN invoices i ON i.id = ii.invoice_id JOIN services s ON s.id = ii.item_id WHERE ii.item_type = 'SERVICE' AND i.business_unit_id IN (:unitIds) GROUP BY s.id, s.business_unit_id, s.name ORDER BY total_qty DESC, s.id ASC", nativeQuery = true)
	List<Map<String, Object>> getTopServicesByBusinessUnitIds(@Param("unitIds") List<Long> unitIds);


}
