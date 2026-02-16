package com.rbilling.service;

import org.springframework.http.ResponseEntity;

import com.rbilling.DTO.BusinessUnitDTO;
import com.rbilling.model.BusinessUnit;

public interface BusinessUnitService {

	ResponseEntity<?> createUpdateBunit(BusinessUnitDTO bunitdto);

}
