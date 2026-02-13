package com.rbilling.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rbilling.model.Customer;
import com.rbilling.model.Membership;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

//	Membership findByIdAndIsActive(Long membershipId, int i);

    Optional<Membership> findByIdAndIsActive(Long id, Integer isActive);
}

