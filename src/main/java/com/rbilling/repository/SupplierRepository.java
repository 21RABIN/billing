package com.rbilling.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rbilling.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);

	boolean existsByEmailAndIdNot(String email, Long id);

	boolean existsByPhoneAndIdNot(String phone, Long id);

	@Query(value = "SELECT supl.*,bunit.name as business_name FROM suppliers supl left join business_units bunit on bunit.id=supl.business_unit_id where(:bunitid=0 OR supl.business_unit_id=:bunitid)", nativeQuery = true)
	List<Map<String, Object>> getAllSuppliers(Long bunitid);

}
