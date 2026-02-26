package com.rbilling.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rbilling.DTO.CustomerHistoryCustomerDTO;
import com.rbilling.DTO.CustomerHistoryPaymentRecordDTO;
import com.rbilling.DTO.CustomerHistoryResponseDTO;
import com.rbilling.DTO.CustomerHistoryServiceRecordDTO;
import com.rbilling.DTO.CustomerHistorySummaryDTO;
import com.rbilling.DTO.CustomerDTO;
import com.rbilling.model.BusinessUnit;
import com.rbilling.model.Customer;
import com.rbilling.model.CustomerMembership;
import com.rbilling.model.Membership;
import com.rbilling.repository.BusinessUnitRepository;
import com.rbilling.repository.CustomerMembershipRepository;
import com.rbilling.repository.CustomerRepository;
import com.rbilling.repository.InvoiceItemRepository;
import com.rbilling.repository.MembershipRepository;
import com.rbilling.repository.PaymentRepository;
import com.rbilling.responce.MessageResponse;
import com.rbilling.service.AccessScopeService;
import com.rbilling.service.CustomerService;

@Service
@Transactional // During Data insert time errors find Revort the All data
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository cusrepo;

	@Autowired
	private MembershipRepository membershipRepository;

	@Autowired
	private CustomerMembershipRepository customerMembershipRepository;

	@Autowired
	BusinessUnitRepository bunitrepo;

	@Autowired
	private InvoiceItemRepository invoiceItemRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private AccessScopeService accessScopeService;

	public ResponseEntity<?> createUpdateCustomer(CustomerDTO cusdto) {

		Customer customer;

		boolean isNewCustomer = true; // Use For Message Throw Create or Update msg

		// Create Customer

		if (cusdto.getId() == null) {

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

			customer = new Customer();

		}

		// Update Customer
		else {

			customer = cusrepo.findById(cusdto.getId()).orElse(null);
			isNewCustomer = false;

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
		
		// finally save the customer
				customer.setBusiness_unit_id(cusdto.getBusiness_unit_id());
				customer.setName(cusdto.getName());
				customer.setMobile(cusdto.getMobile());
				customer.setEmail(cusdto.getEmail());
				customer.setAddress(cusdto.getAddress());
				customer.setIsActive(cusdto.getIsActive());

				cusrepo.save(customer);

		try {

			// Membership Logic

			if (cusdto.isMembership_enabled()) {
				
				if (cusdto.getMembership_id() == null) {
					return ResponseEntity.badRequest()
							.body(new MessageResponse("Membership Id is required when enabling membership"));
				}

				Optional<Membership> membership = membershipRepository.findByIdAndIsActive(cusdto.getMembership_id(),
						true);

				if (membership == null || !membership.isPresent()) {
					return ResponseEntity.ok(new MessageResponse("Active Membership not found"));
				}

				LocalDate startDate = LocalDate.now();
				LocalDate endDate = startDate.plusDays(membership.get().getValidityDays());

				CustomerMembership cm = customerMembershipRepository.findByCustomerId(customer.getId())
						.orElse(new CustomerMembership());

				cm.setCustomerId(customer.getId());
				cm.setMembership_id(membership.get().getId());
				cm.setStart_date(startDate);
				cm.setEnd_date(endDate);
//            cm.setStatus(CustomerMembership.Status.ACTIVE);

				System.out.println("cusdto.getMembership_enabled() :" + cusdto.isMembership_enabled());

				if (cusdto.isMembership_enabled()) {
					cm.setStatus(CustomerMembership.Status.ACTIVE);
				} else {
					cm.setStatus(CustomerMembership.Status.EXPIRED);
				}

				customerMembershipRepository.save(cm);
				

			} else {

				// Disable membership (if exists)
				customerMembershipRepository.findByCustomerId(customer.getId()).ifPresent(cm -> {
					cm.setStatus(CustomerMembership.Status.EXPIRED);
					customerMembershipRepository.save(cm);
				});
			}

		}

		catch (Exception e) {
			System.out.println("MemberShip Exception :" + e.getMessage());
//        	return ResponseEntity.ok(new MessageResponse(e.getMessage()));
		}

		

		if (isNewCustomer) {
			return ResponseEntity.ok(new MessageResponse("Customer Created Successfully"));
		} else {
			return ResponseEntity.ok(new MessageResponse("Customer Updated Successfully"));
		}

	}

	@Override
	public ResponseEntity<?> deleteCustomer(Long id) {
		Customer customer = cusrepo.findById(id).orElse(null);
		if (customer == null) {
			return ResponseEntity.badRequest().body(new MessageResponse("Customer not found"));
		}

		customer.setIsActive(false);
		customer.setIsDelete(true);
		cusrepo.save(customer);

		return ResponseEntity.ok(new MessageResponse("Customer Deleted Successfully"));
	}

	@Override
	public ResponseEntity<?> getCustomerHistory(Long customerId, Long userId) {
		if (customerId == null || customerId <= 0) {
			return ResponseEntity.badRequest().body(new MessageResponse("Invalid customer id"));
		}

		Map<String, Object> customerRow = cusrepo.getCustomerProfileById(customerId);
		if (customerRow == null || customerRow.isEmpty()) {
			return ResponseEntity.badRequest().body(new MessageResponse("Customer not found"));
		}

		Long businessUnitId = toLong(customerRow.get("business_unit_id"));
		if (!hasCustomerAccess(userId, businessUnitId)) {
			return ResponseEntity.status(403).body(new MessageResponse("You do not have access to this customer"));
		}

		CustomerHistoryCustomerDTO customer = mapCustomer(customerRow);
		List<Map<String, Object>> serviceRows = invoiceItemRepository.getServiceHistoryByCustomerId(customerId);
		List<Map<String, Object>> paymentRows = paymentRepository.getPaymentHistoryByCustomerId(customerId);

		List<CustomerHistoryServiceRecordDTO> serviceHistory = mapServiceHistory(serviceRows, customer.getName());
		List<CustomerHistoryPaymentRecordDTO> paymentHistory = mapPaymentHistory(paymentRows, customer.getName());
		List<CustomerHistoryServiceRecordDTO> lastVisits = serviceHistory.size() > 4
				? new ArrayList<>(serviceHistory.subList(0, 4))
				: new ArrayList<>(serviceHistory);

		CustomerHistoryResponseDTO response = new CustomerHistoryResponseDTO();
		response.setCustomer(customer);
		response.setServiceHistory(serviceHistory);
		response.setPaymentHistory(paymentHistory);
		response.setLastVisits(lastVisits);
		response.setSummary(buildSummary(serviceHistory, paymentHistory));

		return ResponseEntity.ok(response);
	}

	private boolean hasCustomerAccess(Long userId, Long businessUnitId) {
		if (userId == null) {
			return false;
		}
		if (accessScopeService.isAdmin(userId)) {
			return true;
		}
		if (businessUnitId == null) {
			return false;
		}
		List<Long> accessibleUnitIds = accessScopeService.getAccessibleBusinessUnitIds(userId);
		return accessibleUnitIds != null && accessibleUnitIds.contains(businessUnitId);
	}

	private CustomerHistoryCustomerDTO mapCustomer(Map<String, Object> row) {
		CustomerHistoryCustomerDTO customer = new CustomerHistoryCustomerDTO();
		Boolean membershipEnabled = toBoolean(row.get("membership_enabled"));

		customer.setId(toLong(row.get("id")));
		customer.setName(toStringValue(row.get("name")));
		customer.setMobile(toStringValue(row.get("mobile")));
		customer.setEmail(toStringValue(row.get("email")));
		customer.setBusiness_name(toStringValue(row.get("business_name")));
		customer.setBusiness_unit_name(toStringValue(row.get("business_unit_name")));
		customer.setMembership_enabled(membershipEnabled);
		customer.setMembershipEnabled(membershipEnabled);
		customer.setMembership_name(toStringValue(row.get("membership_name")));

		return customer;
	}

	private List<CustomerHistoryServiceRecordDTO> mapServiceHistory(List<Map<String, Object>> rows,
			String defaultCustomerName) {
		if (rows == null || rows.isEmpty()) {
			return Collections.emptyList();
		}

		List<CustomerHistoryServiceRecordDTO> result = new ArrayList<>();
		for (Map<String, Object> row : rows) {
			CustomerHistoryServiceRecordDTO item = new CustomerHistoryServiceRecordDTO();

			Long recordId = toLong(row.get("service_record_id"));
			Integer quantity = toInteger(row.get("quantity"));

			item.setId(recordId == null ? null : String.format("SV-%04d", recordId.longValue()));
			item.setCustomerId(toLong(row.get("customer_id")));
			item.setCustomer(firstNonBlank(toStringValue(row.get("customer_name")), defaultCustomerName, "Customer"));
			item.setService(firstNonBlank(toStringValue(row.get("service_name")), "Service"));
			item.setStaff(firstNonBlank(toStringValue(row.get("staff_name")), "N/A"));
			item.setDuration(toDuration(quantity));
			item.setAmount(toBigDecimal(row.get("amount")));
			item.setStatus(mapServiceStatus(toStringValue(row.get("invoice_status"))));
			item.setDate(toDateString(toLocalDate(row.get("invoice_date"))));
			item.setTime("-");

			result.add(item);
		}
		return result;
	}

	private List<CustomerHistoryPaymentRecordDTO> mapPaymentHistory(List<Map<String, Object>> rows,
			String defaultCustomerName) {
		if (rows == null || rows.isEmpty()) {
			return Collections.emptyList();
		}

		List<CustomerHistoryPaymentRecordDTO> result = new ArrayList<>();
		for (Map<String, Object> row : rows) {
			CustomerHistoryPaymentRecordDTO item = new CustomerHistoryPaymentRecordDTO();
			Long paymentId = toLong(row.get("payment_id"));

			item.setInvoice(firstNonBlank(toStringValue(row.get("invoice_no")),
					paymentId == null ? null : String.format("INV-%04d", paymentId.longValue()), "N/A"));
			item.setCustomerId(toLong(row.get("customer_id")));
			item.setCustomer(firstNonBlank(toStringValue(row.get("customer_name")), defaultCustomerName, "Customer"));
			item.setMethod(normalizeMethod(toStringValue(row.get("mode"))));
			item.setAmount(toBigDecimal(row.get("amount")));
			item.setStatus(mapPaymentStatus(toStringValue(row.get("invoice_status"))));
			item.setDate(toDateString(toLocalDate(row.get("payment_date"))));

			result.add(item);
		}
		return result;
	}

	private CustomerHistorySummaryDTO buildSummary(List<CustomerHistoryServiceRecordDTO> serviceHistory,
			List<CustomerHistoryPaymentRecordDTO> paymentHistory) {
		int totalVisits = serviceHistory == null ? 0 : serviceHistory.size();
		CustomerHistoryServiceRecordDTO lastVisit = totalVisits > 0 ? serviceHistory.get(0) : null;

		BigDecimal totalSpend = BigDecimal.ZERO;
		if (serviceHistory != null) {
			for (CustomerHistoryServiceRecordDTO item : serviceHistory) {
				totalSpend = totalSpend.add(toBigDecimal(item == null ? null : item.getAmount()));
			}
		}

		BigDecimal pending = BigDecimal.ZERO;
		if (paymentHistory != null) {
			for (CustomerHistoryPaymentRecordDTO item : paymentHistory) {
				if (item == null || "Paid".equalsIgnoreCase(item.getStatus())) {
					continue;
				}
				pending = pending.add(toBigDecimal(item.getAmount()));
			}
		}

		CustomerHistorySummaryDTO summary = new CustomerHistorySummaryDTO();
		summary.setTotalVisits((long) totalVisits);
		summary.setLastVisit(lastVisit == null ? "-" : firstNonBlank(lastVisit.getDate(), "-"));
		summary.setTotalSpend(totalSpend);
		summary.setOutstanding(pending);
		return summary;
	}

	private String mapServiceStatus(String status) {
		String normalized = status == null ? "" : status.trim().toUpperCase(Locale.ROOT);
		if (normalized.contains("CANCEL")) {
			return "Cancelled";
		}
		if (normalized.contains("NO_SHOW") || normalized.contains("NOSHOW")) {
			return "No Show";
		}
		if (normalized.contains("PARTIAL") || normalized.contains("PENDING") || normalized.contains("DUE")) {
			return "Waiting";
		}
		if (normalized.contains("CONFIRM") || normalized.contains("ESTIMATE")) {
			return "Confirmed";
		}
		if (normalized.contains("PAID")) {
			return "Completed";
		}
		return "Completed";
	}

	private String mapPaymentStatus(String status) {
		String normalized = status == null ? "" : status.trim().toUpperCase(Locale.ROOT);
		if (normalized.contains("REFUND")) {
			return "Refunded";
		}
		if (normalized.contains("PAID")) {
			return "Paid";
		}
		return "Pending";
	}

	private String toDuration(Integer quantity) {
		if (quantity == null || quantity <= 0) {
			return "1 session";
		}
		return quantity.intValue() == 1 ? "1 session" : quantity + " sessions";
	}

	private String normalizeMethod(String mode) {
		if (mode == null || mode.trim().isEmpty()) {
			return "N/A";
		}
		return mode.trim().toUpperCase(Locale.ROOT);
	}

	private String toDateString(LocalDate date) {
		return date == null ? "-" : date.toString();
	}

	private String firstNonBlank(String... values) {
		if (values == null) {
			return null;
		}
		for (String value : values) {
			if (value != null && !value.trim().isEmpty()) {
				return value;
			}
		}
		return null;
	}

	private Long toLong(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		try {
			return Long.parseLong(value.toString());
		} catch (Exception ex) {
			return null;
		}
	}

	private Integer toInteger(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		try {
			return Integer.parseInt(value.toString());
		} catch (Exception ex) {
			return null;
		}
	}

	private Boolean toBoolean(Object value) {
		if (value == null) {
			return Boolean.FALSE;
		}
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		if (value instanceof Number) {
			return ((Number) value).intValue() != 0;
		}
		String text = value.toString().trim();
		return "true".equalsIgnoreCase(text) || "1".equals(text);
	}

	private BigDecimal toBigDecimal(Object value) {
		if (value == null) {
			return BigDecimal.ZERO;
		}
		if (value instanceof BigDecimal) {
			return (BigDecimal) value;
		}
		if (value instanceof Number) {
			return BigDecimal.valueOf(((Number) value).doubleValue());
		}
		try {
			return new BigDecimal(value.toString());
		} catch (Exception ex) {
			return BigDecimal.ZERO;
		}
	}

	private String toStringValue(Object value) {
		return value == null ? null : value.toString();
	}

	private LocalDate toLocalDate(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof LocalDate) {
			return (LocalDate) value;
		}
		if (value instanceof java.sql.Date) {
			return ((java.sql.Date) value).toLocalDate();
		}
		String text = value.toString();
		try {
			if (text.length() >= 10) {
				return LocalDate.parse(text.substring(0, 10));
			}
			return LocalDate.parse(text);
		} catch (Exception ex) {
			return null;
		}
	}
}
