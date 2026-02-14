package com.rbilling.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rbilling.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

//	Product findByIdAndBusinessUnitId(Long id, Long bunit_id);

	Product findByIdAndBusinessUnitId(Long id, Long bunit_id);

	boolean existsByName(String name);
	
	
	@Query(value = "SELECT prod.*,bunit.name as business_name FROM products prod left join business_units bunit on bunit.id=prod.business_unit_id", nativeQuery = true)
	List<Map<String, Object>> getAllProducts();

	

}
