package com.rbilling.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.rbilling.model.User;
import java.util.List;



 
public interface UserRepository extends JpaRepository<User, Long> {
	
    Optional<User> findByEmail(String username);
	
    boolean existsByEmail(String email); 

	User getByEmail(String email);
	
	@Query(value = "select id from users where email=:email",nativeQuery = true)
	int getUserIdByEmail(String email);
	
	@Modifying
    @Transactional
	@Query(value = "update users set password=:password,raw_password=:raw_password where email=:email",nativeQuery = true)
	void resetPassword(String password,String raw_password,String email);
	
	@Query(value = "SELECT r.role AS role_name FROM user_roles ur JOIN roles r ON ur.role_id = r.id WHERE ur.user_id = :user_id",nativeQuery = true)
	String getRoleNameByUserId(int user_id);
	
}