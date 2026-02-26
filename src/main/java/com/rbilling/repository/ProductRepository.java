package com.rbilling.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rbilling.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	Product findByIdAndBusinessUnitId(Long id, Long bunit_id);

	boolean existsByName(String name);

	@Query(value = "SELECT prod.*,supl.id as supplier_id,REPLACE(prod.image, '/var/www/html/', 'http://192.168.1.182/') AS image_url,prodbatch.id as batch_id,prodbatch.batch_no,prodbatch.expiry_date,prodbatch.manufacture_date,prodbatch.stock_qty,prodbatch.purchase_price,prodbatch.created_at,supl.supplier_name,bunit.name as business_name FROM products prod left join business_units bunit on bunit.id=prod.business_unit_id left join product_batches prodbatch on prodbatch.product_id=prod.id left join suppliers supl on supl.id=prodbatch.supplier_id  where(:bunitid=0 OR prod.business_unit_id=:bunitid)", nativeQuery = true)
	List<Map<String, Object>> getAllProducts(Long bunitid);

	@Query(value = "SELECT COUNT(*) FROM products", nativeQuery = true)
	Long countAllProducts();

	@Query(value = "SELECT COUNT(*) FROM products WHERE business_unit_id IN (:unitIds)", nativeQuery = true)
	Long countProductsByBusinessUnitIds(@Param("unitIds") List<Long> unitIds);

	@Query(value = "SELECT p.id, p.business_unit_id, p.name, p.sku, p.hsn_code, p.price, p.selling_price, p.discount_percent, p.gst_percent, p.track_batch, p.is_active, REPLACE(p.image, '/var/www/html/', 'http://192.168.1.182/') AS image_url, COALESCE(SUM(pb.stock_qty),0) AS stock_qty, MIN(pb.expiry_date) AS nearest_expiry_date FROM products p JOIN product_batches pb ON pb.product_id = p.id AND COALESCE(pb.is_active, 1) = 1 GROUP BY p.id, p.business_unit_id, p.name, p.sku, p.hsn_code, p.price, p.selling_price, p.discount_percent, p.gst_percent, p.track_batch, p.is_active, p.image HAVING COALESCE(SUM(pb.stock_qty),0) <= :threshold ORDER BY stock_qty ASC, p.id ASC", nativeQuery = true)
	List<Map<String, Object>> getLowStockProductsAll(@Param("threshold") Integer threshold);

	@Query(value = "SELECT p.id, p.business_unit_id, p.name, p.sku, p.hsn_code, p.price, p.selling_price, p.discount_percent, p.gst_percent, p.track_batch, p.is_active, REPLACE(p.image, '/var/www/html/', 'http://192.168.1.182/') AS image_url, COALESCE(SUM(pb.stock_qty),0) AS stock_qty, MIN(pb.expiry_date) AS nearest_expiry_date FROM products p JOIN product_batches pb ON pb.product_id = p.id AND COALESCE(pb.is_active, 1) = 1 WHERE p.business_unit_id IN (:unitIds) GROUP BY p.id, p.business_unit_id, p.name, p.sku, p.hsn_code, p.price, p.selling_price, p.discount_percent, p.gst_percent, p.track_batch, p.is_active, p.image HAVING COALESCE(SUM(pb.stock_qty),0) <= :threshold ORDER BY stock_qty ASC, p.id ASC", nativeQuery = true)
	List<Map<String, Object>> getLowStockProductsByBusinessUnitIds(@Param("unitIds") List<Long> unitIds,
			@Param("threshold") Integer threshold);

}
