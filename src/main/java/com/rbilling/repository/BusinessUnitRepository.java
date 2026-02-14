package com.rbilling.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rbilling.model.BusinessUnit;

@Repository
public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, Long> {

	boolean existsByName(String name);

	boolean existsByMobile(String mobile);

	
	@Query(value = "SELECT bu.*,COALESCE(parent.name, 'N/A') AS parent_name FROM business_units bu LEFT JOIN business_units parent ON bu.parent_id = parent.id",nativeQuery = true)
	List<Map<String, Object>> getAllBusinessUnits();

	boolean existsByMobileAndIdNot(String mobile, Long id);
	
}
