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
                    .businessUnitId(servdto.getBusiness_unit_id())
                    .name(servdto.getName())
                    .base_price(servdto.getBase_price())
                    .gst_percent(servdto.getGst_percent())
                    .sac_code(servdto.getSac_code())
                    .isActive(true)
                    .build();

			servrepo.save(service);

			return ResponseEntity.ok(new MessageResponse("Service Created Successfully"));
		}

		else {

			Services service = servrepo.findByIdAndBusinessUnitId(servdto.getId(),servdto.getBusiness_unit_id());

			if (service == null) {
				return ResponseEntity.badRequest().body(new MessageResponse("Services not found or unauthorized"));
			}

			if (servdto.getName() != null)
				service.setName(servdto.getName());

			if (servdto.getBase_price() != null)
				service.setBase_price(servdto.getBase_price());

			if (servdto.getGst_percent() != null)
				service.setGst_percent(servdto.getGst_percent());
			
			if (servdto.getSac_code() != null)
				service.setSac_code(servdto.getSac_code());

			if (servdto.getIsActive() != null)
				service.setIsActive(true);

			servrepo.save(service);

			return ResponseEntity.ok(new MessageResponse("Services Updated Successfully"));
		}
	}

}
