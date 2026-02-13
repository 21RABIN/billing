package com.rbilling.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rbilling.model.Customer;
import com.rbilling.model.CustomerMembership;

@Repository
public interface CustomerMembershipRepository extends JpaRepository<CustomerMembership, Long> {

	Optional<CustomerMembership> findByCustomerId(Long id);

//	Optional<Customer> findByCustomerId(Long id);

//    Optional<CustomerMembership> findByCustomerId(Long customerId);
}
