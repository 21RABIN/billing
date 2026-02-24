package com.rbilling.service.impl;

import java.time.LocalDate;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rbilling.DTO.MembershipDTO;
import com.rbilling.DTO.ServiceMappingDTO;
import com.rbilling.model.Customer;
import com.rbilling.model.CustomerMembership;
import com.rbilling.model.Membership;
import com.rbilling.model.ServiceMembershipPrice;
import com.rbilling.repository.CustomerMembershipRepository;
import com.rbilling.repository.CustomerRepository;
import com.rbilling.repository.MembershipRepository;
import com.rbilling.repository.ServiceMembershipPriceRepository;
import com.rbilling.repository.ServicesRepository;
import com.rbilling.responce.MessageResponse;
import com.rbilling.service.MembershipService;

@Service
@Transactional
public class MembershipServiceImpl implements MembershipService {

	@Autowired
	private MembershipRepository memsrepo;

	@Autowired
	private ServicesRepository servrepo;

	@Autowired
	private ServiceMembershipPriceRepository servmemspricerepo;

	@Autowired
	private CustomerRepository cusrepo;

	@Autowired
	private CustomerMembershipRepository cusmemsrepo;

	@Override
	@Transactional
	public ResponseEntity<?> createOrUpdateMembership(MembershipDTO memsdto) {

	    Membership membership;
	    boolean isNewMembership = false;

	    // CREATE
	    if (memsdto.getId() == null) {
	        isNewMembership = true;
	        membership = new Membership();
	        
	        if (memsrepo.existsByName(memsdto.getName())) {
	            return ResponseEntity.badRequest()
	                .body(new MessageResponse("Membership Name already available!"));
	        }
	        membership.setIsActive(true);

	        

	    } else {
	        // UPDATE
	        if (memsrepo.existsByNameAndIdNot(memsdto.getName(), memsdto.getId())) {
	            return ResponseEntity.badRequest()
	                .body(new MessageResponse("Membership Name already available!"));
	        }

	        membership = memsrepo.findById(memsdto.getId())
	            .orElseThrow(() -> new RuntimeException("Membership not found"));
	    }

	    // Set membership details
	    membership.setName(memsdto.getName());
	    membership.setValidityDays(memsdto.getValidity_days());
	    membership.setDiscountType(memsdto.getDiscount_type());
	    membership.setDiscountValue(memsdto.getDiscount_value());
	    membership.setBusinessUnitId(memsdto.getBusiness_unit_id());

	    memsrepo.save(membership);

	    // REMOVE OLD SERVICE MAPPING
	    servmemspricerepo.deleteByMembershipId(membership.getId());

	    // MAP SERVICES
	    if (memsdto.getServices() != null) {
	        for (ServiceMappingDTO serviceDto : memsdto.getServices()) {
	            ServiceMembershipPrice map = new ServiceMembershipPrice();
	            map.setMembershipId(membership.getId());
	            map.setService_id(serviceDto.getService_id());
	            map.setSpecial_price(serviceDto.getSpecial_price());
	            map.setDiscount_percent(serviceDto.getDiscount_percent());
	            servmemspricerepo.save(map);
	        }
	    }

	    // ASSIGN CUSTOMERS
//	    if (memsdto.getCustomer_id() != null) {
//	        for (Long customerId : memsdto.getCustomer_id()) {
//	            Customer customer = cusrepo.findById(customerId)
//	                .orElseThrow(() -> new RuntimeException("Customer not found"));
//
//	            // Expire old ACTIVE membership
//	            cusmemsrepo.findByCustomerIdAndStatus(customerId, CustomerMembership.Status.ACTIVE)
//	                .ifPresent(old -> {
//	                    old.setStatus(CustomerMembership.Status.EXPIRED);
//	                    cusmemsrepo.save(old);
//	                });
//
//	            CustomerMembership cm = new CustomerMembership();
//	            cm.setCustomerId(customer.getId());
//	            cm.setMembership_id(membership.getId());
//	            cm.setStart_date(LocalDate.now());
//	            cm.setEnd_date(LocalDate.now().plusDays(membership.getValidityDays()));
//	            cm.setStatus(CustomerMembership.Status.ACTIVE);
//
//	            cusmemsrepo.save(cm);
//	        }
//	    }

	    // Final response
	    if (isNewMembership) {
	        return ResponseEntity.ok(new MessageResponse("Membership Created Successfully"));
	    } else {
	        return ResponseEntity.ok(new MessageResponse("Membership Updated Successfully"));
	    }
	}



}
