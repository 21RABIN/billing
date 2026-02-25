package com.rbilling.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rbilling.model.Customer;
import com.rbilling.model.CustomerMembership;
import com.rbilling.model.CustomerMembership.Status;
import com.rbilling.model.Membership;

@Repository
public interface CustomerMembershipRepository extends JpaRepository<CustomerMembership, Long> {

	Optional<CustomerMembership> findByCustomerId(Long id);

	Optional<CustomerMembership> findByCustomerIdAndStatus(Long customerId, Status active);

	boolean existsByCustomerIdIn(List<Long> customer_id);

//	boolean existsByCustomerId(List<Long> customer_id);


}
