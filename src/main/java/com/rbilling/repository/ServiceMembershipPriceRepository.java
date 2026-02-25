package com.rbilling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rbilling.model.ServiceMembershipPrice;

@Repository
public interface ServiceMembershipPriceRepository  extends JpaRepository<ServiceMembershipPrice, Long>  {

	void deleteByMembershipId(Long id);

}
