package com.rbilling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rbilling.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

//	Product findByIdAndBusinessUnitId(Long id, Long bunit_id);

	Product findByIdAndBusinessUnitId(Long id, Long bunit_id);

	

}
