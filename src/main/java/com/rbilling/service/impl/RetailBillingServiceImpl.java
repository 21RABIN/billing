package com.rbilling.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rbilling.DTO.RetailInvoiceRequestDTO;
import com.rbilling.model.Customer;
import com.rbilling.model.Employee;
import com.rbilling.model.Invoice;
import com.rbilling.model.InvoiceItem;
import com.rbilling.model.Payment;
import com.rbilling.model.Product;
import com.rbilling.model.ProductBatch;
import com.rbilling.model.Services;
import com.rbilling.model.User;
import com.rbilling.repository.BusinessUnitRepository;
import com.rbilling.repository.CustomerRepository;
import com.rbilling.repository.EmployeeRepository;
import com.rbilling.repository.InvoiceItemRepository;
import com.rbilling.repository.InvoiceRepository;
import com.rbilling.repository.PaymentRepository;
import com.rbilling.repository.ProductBatchRepository;
import com.rbilling.repository.ProductRepository;
import com.rbilling.repository.PurchaseOrderRepository;
import com.rbilling.repository.RoleRepository;
import com.rbilling.repository.ServicesRepository;
import com.rbilling.repository.UserRepository;
import com.rbilling.responce.MessageResponse;
import com.rbilling.service.RetailBillingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RetailBillingServiceImpl implements RetailBillingService {

	@Autowired
	ProductRepository productRepo;
	@Autowired
	ProductBatchRepository batchRepo;
	@Autowired
	InvoiceRepository invoiceRepo;
	@Autowired
	InvoiceItemRepository itemRepo;
	@Autowired
	PaymentRepository paymentRepo;
	@Autowired
	CustomerRepository cusrepo;
	@Autowired
	ServicesRepository servrepo;
	@Autowired
	PurchaseOrderRepository porepo;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	EmployeeRepository emprepo;
	
	@Autowired
	UserRepository userrepo;
	
	@Autowired
	BusinessUnitRepository bunitrepo;
	
	@Autowired
	InvoiceRepository invoicerepo;

	public ResponseEntity<?> createRetailInvoice(RetailInvoiceRequestDTO request) {

		BigDecimal totalTax = BigDecimal.ZERO;
		BigDecimal grossAmount = BigDecimal.ZERO;

		Optional<Customer> cus = cusrepo.findById(request.getCustomer_id());
		if (!cus.isPresent()) {
			return ResponseEntity.badRequest().body(new MessageResponse("Customer Not Found"));
		}
		// Create Invoice
		Invoice invoice = new Invoice();
		invoice.setBusiness_unit_id(cus.get().getBusiness_unit_id());
		invoice.setCustomer_id(request.getCustomer_id());
		invoice.setInvoice_date(LocalDate.now());
		invoice.setStatus(request.getStatus());
		invoice.setBilled_by(request.getBilled_by());

		invoice = invoiceRepo.save(invoice);
		
//		PurchaseOrders po = new PurchaseOrders();
//		
//		po.setSupplier_id(invoice.getBusiness_unit_id());
//		po.setOrder_date(invoice.getInvoice_date());
//		po.setStatus(invoice.getStatus());
//		po.setTotalAmount(invoice.getTotal_amount());
//		po.setBusiness_unit_id(invoice.getBusiness_unit_id());
//		
//		porepo.save(po);
//		

		// Loop Products
		for (RetailInvoiceRequestDTO.ProductItemDTO item : request.getProductitems()) {
			
		    BigDecimal price = BigDecimal.ZERO;
		    BigDecimal gstPercent = BigDecimal.ZERO;
		    String description = "";
		    Long itemId = null;		  

			if(item.getItem_type().equalsIgnoreCase("PRODUCT")) {
			Product product = productRepo.findById(item.getProduct_id())
					.orElseThrow(() -> new RuntimeException("Product not found"));
			
			price = product.getSelling_price() != null ? product.getSelling_price() : BigDecimal.ZERO;
	        gstPercent = product.getGst_percent() != null ? product.getGst_percent() : BigDecimal.ZERO;
	        description = product.getName();
	        itemId = product.getId();
			
			// Batch Handling
			if (Boolean.TRUE.equals(product.getTrack_batch())) {

				if (item.getBatch_id() == null) {
					throw new RuntimeException("Batch required for this product");
				}

				ProductBatch batch = batchRepo.findById(item.getBatch_id())
						.orElseThrow(() -> new RuntimeException("Batch not found"));

				if (batch.getStock_qty() < item.getQuantity()) {
					throw new RuntimeException("Insufficient stock");
				}

				batch.setStock_qty(batch.getStock_qty() - item.getQuantity());
				batchRepo.save(batch);
			}
			}
			
			else if(item.getItem_type().equalsIgnoreCase("SERVICE")) {
				  Services serv = servrepo.findById(item.getProduct_id())
			                .orElseThrow(() -> new RuntimeException("Service not found"));

			        price = serv.getBase_price() != null ? serv.getBase_price() : BigDecimal.ZERO;
			        gstPercent = serv.getGst_percent() != null ? serv.getGst_percent() : BigDecimal.ZERO;
			        description = serv.getName();
			        itemId = serv.getId();
			}
			  else {
			        throw new RuntimeException("Invalid item type");
			    }
			

//			BigDecimal price = product.getSelling_price() != null ? product.getSelling_price() : BigDecimal.ZERO;
//
//			BigDecimal gstPercent = product.getGst_percent() != null ? product.getGst_percent() : BigDecimal.ZERO;

			BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());

			BigDecimal lineAmount = price.multiply(quantity);

			BigDecimal tax = lineAmount.multiply(gstPercent).divide(BigDecimal.valueOf(100));

			BigDecimal total = lineAmount.add(tax);

			
			// Save Invoice Item
			InvoiceItem invoiceItem = new InvoiceItem();
			invoiceItem.setInvoice_id(invoice.getId());
			invoiceItem.setItem_type(item.getItem_type());
			invoiceItem.setItem_id(itemId);
			invoiceItem.setDescription(description);
			invoiceItem.setQuantity(item.getQuantity());
			invoiceItem.setGst_percent(gstPercent);
			invoiceItem.setTax_amount(tax);
			invoiceItem.setTotal_amount(total);
			invoiceItem.setNet_amount(request.getPayment().getTotal_amount());
			invoiceItem.setGross_amount(lineAmount);
			invoiceItem.setPerformed_by(request.getBilled_by());
			itemRepo.save(invoiceItem);

			grossAmount = grossAmount.add(lineAmount);
			totalTax = totalTax.add(tax);
		}

		// Final Invoice Calculation
		BigDecimal netAmount = grossAmount.add(totalTax);

		invoice.setGross_amount(grossAmount);
		invoice.setTax_amount(request.getPayment().getTax_amount());// totalTax
		invoice.setTotal_amount(request.getPayment().getTotal_amount());// netAmount
		invoice.setNet_amount(request.getPayment().getTotal_amount());// netAmount
//        invoice.setRound_off(BigDecimal.ZERO);

		// Payment Handling
		if (request.getPayment() != null) {

			Payment payment = new Payment();

			payment.setInvoice_id(invoice.getId());
			payment.setAmount(request.getPayment().getPayment_amount());
			payment.setMode(request.getPayment().getMode());

			payment.setPayment_date(LocalDate.now());

			String refNo = ReferenceGenerator.generateReferenceNo();
			payment.setReference_no(refNo);

			paymentRepo.save(payment);

			if (request.getPayment().getPayment_amount().compareTo(request.getPayment().getTotal_amount()) >= 0) {
				invoice.setStatus("PAID");
			} else {
				invoice.setStatus("PARTIAL");
			}
		}

		invoiceRepo.save(invoice);

		return ResponseEntity.ok(new MessageResponse("Retail Invoice Created Successfully"));

	}
	
	public List<Map<String, Object>> getRoleBasedPayments(Long userId) {

        String role = roleRepository.getRoleuserid(userId);

        User user = userrepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get employee record to fetch business_unit
        Employee emp = null;

        try {
            emp = emprepo.findByUserId(userId);
        } catch (Exception ex) {
            emp = null;
        }

        if (role != null && role.contains("ROLE_ADMIN")) {
            return paymentRepo.getAllPayments();
        }

        // ROLE_EMPLOYEE
    
        if (role.contains("ROLE_EMPLOYEE")) {
        	System.out.println("");
            return paymentRepo.getPaymentsByUser(userId);
        }

       
        // ROLE_MANAGER / FRANCHISE / MAIN
       
        if (emp != null) {

            Long unitId = emp.getBusiness_unit_id();

            List<Long> allUnits =
            		bunitrepo.getAllChildUnitIds(unitId);

            return paymentRepo.getPaymentsByUnits(allUnits);
        }

        return new ArrayList<>();
    }
	
	
	
	 public List<Map<String, Object>> getPaymentData(Long userId) {
		 
		  String role = roleRepository.getRoleuserid(userId);
		  
		 Long filterUserId = "ADMIN".equalsIgnoreCase(role) ? null : userId;


	        BigDecimal totalReceived =
	                Optional.ofNullable(paymentRepo.getTotalReceivedByUser(filterUserId))
	                        .orElse(BigDecimal.ZERO);

	        BigDecimal todayReceived =
	                Optional.ofNullable(paymentRepo.getTodayReceivedByUser(filterUserId))
	                        .orElse(BigDecimal.ZERO);

	        BigDecimal totalInvoiceAmount =
	                Optional.ofNullable(invoicerepo.getTotalInvoiceAmount(filterUserId))
	                        .orElse(BigDecimal.ZERO);

	        // Pending = Invoice - Payment
	        BigDecimal pendingAmount = totalInvoiceAmount.subtract(totalReceived);

	        // Refund logic
	        BigDecimal refundAmount = BigDecimal.ZERO;
	        if (totalReceived.compareTo(totalInvoiceAmount) > 0) {
	            refundAmount = totalReceived.subtract(totalInvoiceAmount);
	        }

	        List<Map<String, Object>> responseList = new ArrayList<>();

	        Map<String, Object> map = new LinkedHashMap<>();
	        map.put("total_received", totalReceived);
	        map.put("pending_amount", pendingAmount);
	        map.put("refund_amount", refundAmount);
	        map.put("today_received", todayReceived);

	        responseList.add(map);

	        return responseList;
	    }

}
