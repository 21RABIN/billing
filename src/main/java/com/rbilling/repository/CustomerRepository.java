package com.rbilling.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rbilling.model.Customer;
import com.rbilling.model.Membership;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	boolean existsByEmail(String email);

	boolean existsByMobile(String mobile);

	boolean existsByEmailAndIdNot(String email, Long id);

	boolean existsByMobileAndIdNot(String mobile, Long id);

	@Query(value = "SELECT cus.*,mems.name as membership_name,cusmems.membership_id,CASE WHEN cusmems.end_date < CURRENT_DATE THEN false WHEN cusmems.end_date >= CURRENT_DATE THEN true ELSE false END AS membership_enabled,cusmems.start_date,cusmems.end_date,bunit.name as business_name FROM customers cus left join business_units bunit on bunit.id=cus.business_unit_id left join customer_membership cusmems on cusmems.customer_id=cus.id left join memberships mems on mems.id=cusmems.membership_id WHERE (:bunitid=0 OR cus.business_unit_id = :bunitid)", nativeQuery = true)
	List<Map<String, Object>> getAllCustomer(Long bunitid);

	@Query(value = "SELECT cus.id, cus.business_unit_id, cus.name, cus.mobile, cus.email, bunit.name AS business_name, bunit.name AS business_unit_name, mems.name AS membership_name, CASE WHEN latest_cm.status = 'ACTIVE' AND latest_cm.end_date >= CURRENT_DATE THEN true ELSE false END AS membership_enabled FROM customers cus LEFT JOIN business_units bunit ON bunit.id = cus.business_unit_id LEFT JOIN customer_membership latest_cm ON latest_cm.id = (SELECT cm2.id FROM customer_membership cm2 WHERE cm2.customer_id = cus.id ORDER BY cm2.end_date DESC, cm2.id DESC LIMIT 1) LEFT JOIN memberships mems ON mems.id = latest_cm.membership_id WHERE cus.id = :customerId LIMIT 1", nativeQuery = true)
	Map<String, Object> getCustomerProfileById(@Param("customerId") Long customerId);

	@Query(value = "SELECT COUNT(*) FROM customers WHERE COALESCE(is_delete, 0) = 0 AND COALESCE(is_active, 1) = 1", nativeQuery = true)
	Long countActiveCustomers();

	@Query(value = "SELECT COUNT(*) FROM customers WHERE business_unit_id IN (:unitIds) AND COALESCE(is_delete, 0) = 0 AND COALESCE(is_active, 1) = 1", nativeQuery = true)
	Long countActiveCustomersByBusinessUnitIds(@Param("unitIds") List<Long> unitIds);


}
