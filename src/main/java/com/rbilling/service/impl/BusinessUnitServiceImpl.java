package com.rbilling.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rbilling.DTO.BusinessUnitDTO;
import com.rbilling.model.BusinessType;
import com.rbilling.model.BusinessUnit;
import com.rbilling.repository.BusinessUnitRepository;
import com.rbilling.responce.MessageResponse;
import com.rbilling.service.BusinessUnitService;

@Service
public class BusinessUnitServiceImpl implements BusinessUnitService {

	@Autowired
	BusinessUnitRepository bunitrepo;

	// Create and Update BusinessUnit
	public ResponseEntity<?> createUpdateBunit(BusinessUnitDTO bunitdto) {

		// CREATE
		if (bunitdto.getId() == null) {

			if (bunitrepo.existsByName(bunitdto.getName())) {

				if (bunitrepo.existsByMobile(bunitdto.getMobile())) {
					return ResponseEntity.badRequest().body(new MessageResponse("business_units already available!"));
				}
			} else {
				if (bunitrepo.existsByMobile(bunitdto.getMobile())) {
					return ResponseEntity.badRequest().body(new MessageResponse("business_units already available!"));
				}

			}

			BusinessType type;

			try {
				type = bunitdto.getType();
			} catch (Exception e) {

				return ResponseEntity.badRequest().body(new MessageResponse("Invalid business type"));

			}

			if (type == BusinessType.FRANCHISE) {

				if (bunitdto.getParent_id() == null) {

					return ResponseEntity.badRequest().body(new MessageResponse("Parent ID required for FRANCHISE"));

				}

				BusinessUnit parent = bunitrepo.findById(bunitdto.getParent_id()).orElse(null);

				if (parent == null || parent.getType() != BusinessType.MAIN) {

					return ResponseEntity.badRequest().body(new MessageResponse("Main FRANCHISE INVALID"));

				}
			}

			BusinessUnit unit = BusinessUnit.builder().name(bunitdto.getName()).type(type)
					.parent_id(bunitdto.getParent_id()).address(bunitdto.getAddress()).mobile(bunitdto.getMobile())
					.gst_in(bunitdto.getGst_in()).isActive(true).build();

			bunitrepo.save(unit);

			return ResponseEntity.ok(new MessageResponse("Business Unit Created Successfully"));

		}

		else {

			if (bunitrepo.existsByMobileAndIdNot(bunitdto.getMobile(), bunitdto.getId())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Business Unit Mobile already available!"));
			}

			BusinessUnit bunit = bunitrepo.findById(bunitdto.getId()).orElse(null);

			if (bunit == null) {
				return ResponseEntity.badRequest().body("Business Unit not found");
			}

			if (bunitdto.getName() != null)
				bunit.setName(bunitdto.getName());

			if (bunitdto.getAddress() != null)
				bunit.setAddress(bunitdto.getAddress());
			
			if (bunitdto.getType() != null)
				bunit.setType(bunitdto.getType());
			
			if(bunitdto.getParent_id()!=null)
				bunit.setParent_id(bunitdto.getParent_id());

			if (bunitdto.getMobile() != null)
				bunit.setMobile(bunitdto.getMobile());

			if (bunitdto.getGst_in() != null)
				bunit.setGst_in(bunitdto.getGst_in());

			if (bunitdto.getIsActive() != null)
				bunit.setIsActive(bunitdto.getIsActive());

			bunitrepo.save(bunit);

			return ResponseEntity.ok(new MessageResponse("Business Unit Updated Successfully"));

		}

	}

}
