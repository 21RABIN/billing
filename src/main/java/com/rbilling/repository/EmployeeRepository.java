package com.rbilling.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rbilling.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	boolean existsByMobile(String mobile);

	boolean existsByEmail(String email);

	boolean existsByEmailAndIdNot(String email, Long id);

	boolean existsByMobileAndIdNot(String mobile, Long id);

	@Query(value = "select bunit.* from business_units bunit left join employees emp on bunit.id=emp.business_unit_id where emp.id=:empid",nativeQuery = true)
	List<Map<String, Object>> getEmployeeFranchise(long empid);

	@Query(value = "SELECT emp.*,bunit.name as business_name FROM employees emp left join business_units bunit on bunit.id=emp.business_unit_id", nativeQuery = true) 
	List<Map<String, Object>> getAllEmployees();

}
