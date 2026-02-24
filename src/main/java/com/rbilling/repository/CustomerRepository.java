package com.rbilling.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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




}


