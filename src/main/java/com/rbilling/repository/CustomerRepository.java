package com.rbilling.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rbilling.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	boolean existsByEmail(String email);

	boolean existsByMobile(String mobile);

	boolean existsByEmailAndIdNot(String email, Long id);

	boolean existsByMobileAndIdNot(String mobile, Long id);

	@Query(value = "SELECT cus.*,mems.name as membership_name,cusmems.membership_id,cusmems.status,cusmems.start_date,cusmems.end_date,bunit.name as business_name FROM customers cus left join business_units bunit on bunit.id=cus.business_unit_id left join customer_membership cusmems on cusmems.customer_id=cus.id left join memberships mems on mems.id=cusmems.membership_id", nativeQuery = true)
	List<Map<String, Object>> getAllCustomer();
}