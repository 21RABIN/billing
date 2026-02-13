package com.rbilling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rbilling.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	boolean existsByMobile(String mobile);

	boolean existsByEmail(String email);

	boolean existsByEmailAndIdNot(String email, Long id);

	boolean existsByMobileAndIdNot(String mobile, Long id);

}
