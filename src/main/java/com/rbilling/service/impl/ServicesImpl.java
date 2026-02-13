package com.rbilling.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rbilling.DTO.ServicesDTO;
import com.rbilling.model.Services;
import com.rbilling.repository.ServicesRepository;
import com.rbilling.responce.MessageResponse;
import com.rbilling.service.ServicesService;

@Service
public class ServicesImpl implements ServicesService {

	@Autowired
	ServicesRepository servrepo;

	public ResponseEntity<?> createUpdateServices(ServicesDTO servdto) {

		if (servdto.getId() == null) {

			
			Services service = Services.builder()
                    .businessUnitId(servdto.getBusinessUnitId())
                    .name(servdto.getName())
                    .basePrice(servdto.getBasePrice())
                    .gstPercent(servdto.getGstPercent())
                    .isActive(true)
                    .build();

			servrepo.save(service);

			return ResponseEntity.ok(new MessageResponse("Service Created Successfully"));
		}

		else {

			Services service = servrepo.findByIdAndBusinessUnitId(servdto.getId(),servdto.getBusinessUnitId());

			if (service == null) {
				return ResponseEntity.badRequest().body(new MessageResponse("Services not found or unauthorized"));
			}

			if (servdto.getName() != null)
				service.setName(servdto.getName());

			if (servdto.getBasePrice() != null)
				service.setBasePrice(servdto.getBasePrice());

			if (servdto.getGstPercent() != null)
				service.setGstPercent(servdto.getGstPercent());

			if (servdto.getIsActive() != null)
				service.setIsActive(true);

			servrepo.save(service);

			return ResponseEntity.ok(new MessageResponse("Services Updated Successfully"));
		}
	}

}
