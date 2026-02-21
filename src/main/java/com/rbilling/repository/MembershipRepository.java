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
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Optional<Membership> findByIdAndIsActive(Long id, Boolean isActive);

//    @Query(value = "SELECT m.id AS membership_id, m.name AS membership_name, m.validity_days, m.discount_type, m.discount_value, s.id AS service_id, s.business_unit_id, bu.name AS business_name, s.name AS service_name, s.base_price, s.gst_percent, s.sac_code, s.is_active as service_active, smp.special_price, smp.discount_percent,m.is_active as mems_active \n"
//    		+ "FROM memberships m \n"
//    		+ "LEFT JOIN service_membership_price smp ON m.id = smp.membership_id \n"
//    		+ "LEFT JOIN services s ON smp.service_id = s.id \n"
//    		+ "LEFT JOIN business_units bu ON s.business_unit_id = bu.id \n"
//    		+ "WHERE m.is_active = 1 \n"
//    		+ "ORDER BY m.id; ",nativeQuery = true)
//	List<Object[]> getAllMemberships();
	
	
	@Query(value = "SELECT mems.* FROM memberships mems WHERE mems.is_active = 1 ORDER BY mems.id", nativeQuery = true)
	List<Map<String,Object>> getMembershipsOnly();

	@Query(value = "SELECT smp.membership_id, s.id AS service_id, s.business_unit_id, bu.name AS business_name, s.name AS service_name, s.base_price, s.gst_percent, s.sac_code, s.is_active AS service_active, smp.special_price, smp.discount_percent FROM service_membership_price smp LEFT JOIN services s ON smp.service_id = s.id LEFT JOIN business_units bu ON s.business_unit_id = bu.id", nativeQuery = true)
	List<Map<String,Object>> getMembershipServices();


	boolean existsByName(String name);

	boolean existsByNameAndIdNot(String name, Long memsid);


}

