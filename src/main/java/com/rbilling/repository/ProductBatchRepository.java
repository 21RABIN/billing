package com.rbilling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rbilling.model.ProductBatch;

public interface ProductBatchRepository  extends JpaRepository<ProductBatch, Long> {

	@Query(value = "select id from product_batches where product_id=:prodid",nativeQuery = true)
	Long getprodbatchid(Long prodid);

}
