package com.rbilling.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rbilling.model.BusinessUnit;

@Repository
public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, Long> {

	boolean existsByName(String name);

	boolean existsByMobile(String mobile);

	@Query(value = "SELECT bu.*,COALESCE(parent.name, 'N/A') AS parent_name FROM business_units bu LEFT JOIN business_units parent ON bu.parent_id = parent.id where (:bunitid=0 OR parent.id=:bunitid)", nativeQuery = true)
	List<Map<String, Object>> getAllBusinessUnits(Long bunitid);

	@Query(value = "SELECT bu.*,COALESCE(parent.name, 'N/A') AS parent_name FROM business_units bu LEFT JOIN business_units parent ON bu.parent_id = parent.id WHERE bu.id IN (:unitIds)", nativeQuery = true)
	List<Map<String, Object>> getAllBusinessUnitsByIds(@Param("unitIds") List<Long> unitIds);

	@Query(value = "SELECT bu.*,COALESCE(parent.name, 'N/A') AS parent_name FROM business_units bu LEFT JOIN business_units parent ON bu.parent_id = parent.id WHERE bu.id IN (:unitIds) AND bu.type='MAIN'", nativeQuery = true)
	List<Map<String, Object>> getMainBusinessUnitsByIds(@Param("unitIds") List<Long> unitIds);

	boolean existsByMobileAndIdNot(String mobile, Long id);

	@Query(value = " select bunit.name,bunit.id,bunitparent.id as parentid,bunitparent.name as parentname from business_units bunit left join users u on u.id=bunit.user_id left join business_units bunitparent on bunitparent.parent_id=bunit.id  where bunit.user_id=7", nativeQuery = true)
	String findbybunitname(String id);

	@Query(value = "select bunit.name, bunit.id, bunitparent.id as parentid, bunitparent.name as parentname from business_units bunit left join business_units bunitparent on bunit.parent_id = bunitparent.id where bunit.user_id = :id limit 1", nativeQuery = true)
	BusinessUnit findBusinessUnitDetails(Long id);

	@Query(value = "select * from  business_units where user_id=:id", nativeQuery = true)
	BusinessUnit findByUserId(Long id);
	
	@Query(value = "WITH RECURSIVE sub_units AS ( SELECT id FROM business_units WHERE id = :unitId UNION ALL SELECT bu.id FROM business_units bu INNER JOIN sub_units su ON bu.parent_id = su.id)SELECT id FROM sub_units ", nativeQuery = true)
	List<Long> getAllSubUnitIds(@Param("unitId") Long unitId);
	
	@Query(value = "WITH RECURSIVE unit_tree AS (SELECT id FROM business_units WHERE id = :unitId UNION ALL SELECT bu.id FROM business_units bu INNER JOIN unit_tree ut ON bu.parent_id = ut.id) SELECT id FROM unit_tree", nativeQuery = true)
	List<Long> getAllChildUnitIds(@Param("unitId") Long unitId);

}
