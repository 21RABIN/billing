package com.rbilling.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rbilling.model.ERole;
import com.rbilling.model.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByRole(ERole name); 
	Boolean existsByRole(ERole name);
	
	@Query(value = "select id,role from roles",nativeQuery = true)
	List<Map<String, Object>> getRoles();
	
	@Query(value = "select id,role from roles where role IN ('ROLE_ADMIN','ROLE_CLIENT')",nativeQuery = true)
	List<Map<String, Object>> getStaffRoles();
	
	@Query(value = "select role from roles where id=:id",nativeQuery = true)
	String getRoleById(int id);
	
	@Query(value = "SELECT r.role FROM user_roles ur JOIN roles r ON ur.role_id = r.id WHERE ur.user_id = :userId", nativeQuery = true) 
	String getRoleuserid(@Param("userId") Long userId);
	
}
