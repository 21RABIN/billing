package com.rbilling.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rbilling.model.Services;

public interface ServicesRepository extends JpaRepository<Services, Long> {

	Services findByIdAndBusinessUnitId(Long id, Integer businessUnitId);

}
