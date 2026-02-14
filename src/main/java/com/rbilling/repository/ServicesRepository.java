package com.rbilling.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rbilling.model.Services;

public interface ServicesRepository extends JpaRepository<Services, Long> {

	Services findByIdAndBusinessUnitId(Long id, Integer businessUnitId);
	
	@Query(value = "SELECT serv.*,bunit.name as business_name FROM services serv left join business_units bunit on bunit.id=serv.business_unit_id", nativeQuery = true)
	List<Map<String, Object>> getAllServices();

}
