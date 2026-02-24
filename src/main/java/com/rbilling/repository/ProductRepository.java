package com.rbilling.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rbilling.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	Product findByIdAndBusinessUnitId(Long id, Long bunit_id);

	boolean existsByName(String name);

	@Query(value = "SELECT prod.*,supl.id as supplier_id,REPLACE(prod.image, '/var/www/html/', 'http://192.168.1.182/') AS image_url,prodbatch.id as batch_id,prodbatch.batch_no,prodbatch.expiry_date,prodbatch.manufacture_date,prodbatch.stock_qty,prodbatch.purchase_price,prodbatch.created_at,supl.supplier_name,bunit.name as business_name FROM products prod left join business_units bunit on bunit.id=prod.business_unit_id left join product_batches prodbatch on prodbatch.product_id=prod.id left join suppliers supl on supl.id=prodbatch.supplier_id  where(:bunitid=0 OR prod.business_unit_id=:bunitid)", nativeQuery = true)
	List<Map<String, Object>> getAllProducts(Long bunitid);

}
