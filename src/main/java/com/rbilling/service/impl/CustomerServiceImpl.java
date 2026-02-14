package com.rbilling.service.impl;

import java.time.LocalDate;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rbilling.DTO.CustomerDTO;
import com.rbilling.model.BusinessUnit;
import com.rbilling.model.Customer;
import com.rbilling.model.CustomerMembership;
import com.rbilling.model.Membership;
import com.rbilling.repository.BusinessUnitRepository;
import com.rbilling.repository.CustomerMembershipRepository;
import com.rbilling.repository.CustomerRepository;
import com.rbilling.repository.MembershipRepository;
import com.rbilling.responce.MessageResponse;
import com.rbilling.service.CustomerService;

@Service
@Transactional  //During Data insert time errors find Revort the All data  
public class CustomerServiceImpl implements CustomerService  {

    @Autowired
    private CustomerRepository cusrepo;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private CustomerMembershipRepository customerMembershipRepository;
    
    @Autowired
	BusinessUnitRepository bunitrepo;



    public ResponseEntity<?> createUpdateCustomer(CustomerDTO cusdto) {

        Customer customer;
        
        boolean isNewCustomer=true; //Use For Message Throw Create or Update msg
      
        // Create Customer
       
        if (cusdto.getId() == null) {
        	
        	  customer = new Customer();
        	  
        	  
            
            if (cusdto.getBusiness_unit_id() == null) {
				return ResponseEntity.badRequest().body(new MessageResponse("Business Unit is required"));
			}

			BusinessUnit unit = bunitrepo.findById(cusdto.getBusiness_unit_id()).orElse(null);

			if (unit == null) {
				return ResponseEntity.badRequest().body(new MessageResponse("Invalid Business Unit"));
			}

			if (cusrepo.existsByEmail(cusdto.getEmail())) {

				return ResponseEntity.badRequest().body(new MessageResponse("Customer already available!"));

			} else {
				if (cusrepo.existsByMobile(cusdto.getMobile())) {
					return ResponseEntity.badRequest().body(new MessageResponse("Customer already available!"));
				}

			}
			
					               
           
        }
        
        //Update Customer
        else {
        	
        	  customer = cusrepo.findById(cusdto.getId()).orElse(null);
        	  isNewCustomer=false;
        	
        	if (customer == null) {
				return ResponseEntity.badRequest().body(new MessageResponse("Customer not found"));
			}

			if (cusrepo.existsByEmailAndIdNot(cusdto.getEmail(), cusdto.getId())) {

				return ResponseEntity.badRequest().body(new MessageResponse("Customer Email already available!"));

			} else {
				if (cusrepo.existsByMobileAndIdNot(cusdto.getMobile(), cusdto.getId())) {
					return ResponseEntity.badRequest().body(new MessageResponse("Customer Mobile already available!"));
				}

			}
			
                    
        }


        try {
     
        // Membership Logic

        if (Boolean.TRUE.equals(cusdto.getMembership_enabled())) {

            if (cusdto.getMembership_id() == null) {
                return ResponseEntity.badRequest().body( new MessageResponse("Membership Id is required when enabling membership"));
            }
            

            Optional<Membership> membership = membershipRepository.findByIdAndIsActive(cusdto.getMembership_id(), 1);
                        
            if(membership==null || !membership.isPresent()) {
            	return ResponseEntity.ok(new MessageResponse("Active Membership not found"));
            }
            
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusDays(membership.get().getValidityDays());

            CustomerMembership cm = customerMembershipRepository.findByCustomerId(customer.getId()).orElse(new CustomerMembership());

            cm.setCustomerId(customer.getId());
            cm.setMembershipId(membership.get().getId());
            cm.setStartDate(startDate);
            cm.setEndDate(endDate);
            cm.setStatus(CustomerMembership.Status.ACTIVE);

            customerMembershipRepository.save(cm);

        } else {

            // Disable membership (if exists)
            customerMembershipRepository.findByCustomerId(customer.getId())
                    .ifPresent(cm -> {
                        cm.setStatus(CustomerMembership.Status.EXPIRED);
                        customerMembershipRepository.save(cm);
                    });
        }
        
        }
        
        catch(Exception e) {
        	System.out.println("MemberShip Exception :"+e.getMessage());
//        	return ResponseEntity.ok(new MessageResponse(e.getMessage()));
        }
        
        //finally save the customer
        customer.setBusiness_unit_id(cusdto.getBusiness_unit_id());
        customer.setName(cusdto.getName());
        customer.setMobile(cusdto.getMobile());
        customer.setEmail(cusdto.getEmail());
        customer.setAddress(cusdto.getAddress());
        

        cusrepo.save(customer);
        
        
        if(isNewCustomer) {
        	return ResponseEntity.ok(new MessageResponse("Customer Created Successfully"));
        }
        else {
        	return ResponseEntity.ok(new MessageResponse("Customer Updated Successfully"));
        }
        
        
    }
}
