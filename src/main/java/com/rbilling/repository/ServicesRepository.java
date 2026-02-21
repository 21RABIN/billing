package com.rbilling.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rbilling.model.Services;

public interface ServicesRepository extends JpaRepository<Services, Long> {

	Services findByIdAndBusinessUnitId(Long id, Integer businessUnitId);
	
	@Query(value = "SELECT serv.*,serv.base_price as price,serv.base_price as selling_price,serv.base_price as discount_percent,REPLACE(serv.image, '/var/www/html/', 'http://192.168.1.182/') AS image_url,bunit.name as business_name FROM services serv left join business_units bunit on bunit.id=serv.business_unit_id where (:bunitid=0 OR serv.business_unit_id=:bunitid)", nativeQuery = true)
	List<Map<String, Object>> getAllServices(Long bunitid);

	 @Query(value = "select * from service_membership_price ",nativeQuery = true)
	List<Map<String, Object>> getAllServMemsPrice();



}
