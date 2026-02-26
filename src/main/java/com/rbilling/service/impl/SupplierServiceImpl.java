package com.rbilling.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rbilling.DTO.SupplierDTO;
import com.rbilling.model.Supplier;
import com.rbilling.repository.SupplierRepository;
import com.rbilling.responce.MessageResponse;
import com.rbilling.service.SupplierService;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public ResponseEntity<?> createUpdateSupplier(SupplierDTO supldto) {

        Supplier supplier;
        String message;

     
        // CREATE
        if (supldto.getId() == null) {

            if (supldto.getSupplier_name() == null || supldto.getSupplier_name().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Supplier Name is required"));
            }

            if (supldto.getEmail() != null && supplierRepository.existsByEmail(supldto.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Email already exists"));
            }

            if (supldto.getPhone() != null && supplierRepository.existsByPhone(supldto.getPhone())) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Phone already exists"));
            }

            supplier = new Supplier();
            message = "Supplier Created Successfully";

        } 
      
        // UPDATE
   
        else {

            supplier = supplierRepository.findById(supldto.getId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));

            if (supldto.getEmail() != null &&
                supplierRepository.existsByEmailAndIdNot(supldto.getEmail(), supldto.getId())) {

                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Email already exists"));
            }

            if (supldto.getPhone() != null &&
                supplierRepository.existsByPhoneAndIdNot(supldto.getPhone(), supldto.getId())) {

                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Phone already exists"));
            }

            message = "Supplier Updated Successfully";
        }
       
        // SET VALUES

        supplier.setBusinessUnitId(supldto.getBusiness_unit_id());
        supplier.setSupplier_name(supldto.getSupplier_name());
        supplier.setContact_person(supldto.getContact_person());
        supplier.setPhone(supldto.getPhone());
        supplier.setEmail(supldto.getEmail());
        supplier.setAddress(supldto.getAddress());
        supplier.setGst_number(supldto.getGst_number());
        supplier.setIsActive(supldto.getIsActive());

        supplierRepository.save(supplier);

        return ResponseEntity.ok(new MessageResponse(message));
    }

	@Override
	public ResponseEntity<?> deleteSupplier(Long id) {
		Supplier supplier = supplierRepository.findById(id).orElse(null);
		if (supplier == null) {
			return ResponseEntity.badRequest().body(new MessageResponse("Supplier not found"));
		}

		supplier.setIsActive(false);
		supplierRepository.save(supplier);
		return ResponseEntity.ok(new MessageResponse("Supplier Deleted Successfully"));
	}
}
