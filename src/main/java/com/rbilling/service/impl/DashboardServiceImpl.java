package com.rbilling.service.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rbilling.DTO.DashboardSummaryDTO;
import com.rbilling.DTO.InventoryAlertDTO;
import com.rbilling.DTO.ProductDTO;
import com.rbilling.DTO.TopServiceDTO;
import com.rbilling.model.BusinessUnit;
import com.rbilling.model.Employee;
import com.rbilling.repository.BusinessUnitRepository;
import com.rbilling.repository.CustomerRepository;
import com.rbilling.repository.EmployeeRepository;
import com.rbilling.repository.InvoiceRepository;
import com.rbilling.repository.PaymentRepository;
import com.rbilling.repository.ProductRepository;
import com.rbilling.repository.RoleRepository;
import com.rbilling.repository.ServicesRepository;
import com.rbilling.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {
	private static final int TOP_SERVICES_LIMIT = 8;
	private static final int RECENT_PAYMENTS_LIMIT = 5;
	private static final int LOW_STOCK_LIMIT = 5;
	private static final int INVENTORY_ALERT_LIMIT = 8;
	private static final int LOW_STOCK_THRESHOLD = 10;
	private static final int EXPIRY_ALERT_DAYS = 30;

	@Autowired
	ServicesRepository servicesRepo;
	@Autowired
	ProductRepository productRepo;
	@Autowired
	PaymentRepository paymentRepo;
	@Autowired
	CustomerRepository customerRepo;
	@Autowired
	InvoiceRepository invoiceRepo;
	@Autowired
	RoleRepository roleRepo;
	@Autowired
	EmployeeRepository employeeRepo;
	@Autowired
	BusinessUnitRepository businessUnitRepo;

	@Override
	public DashboardSummaryDTO getDashboardSummary(Long user_id) {
		DashboardSummaryDTO dashboardSummaryDTO = new DashboardSummaryDTO();

		dashboardSummaryDTO.setTopServices(new ArrayList<>());
		dashboardSummaryDTO.setRecentPayments(new ArrayList<>());
		dashboardSummaryDTO.setLowStockProducts(new ArrayList<>());
		dashboardSummaryDTO.setInventoryAlerts(new ArrayList<>());

		if (user_id == null) {
			setZeroSummary(dashboardSummaryDTO);
			return dashboardSummaryDTO;
		}

		String role = roleRepo.getRoleuserid(user_id);
		if (role == null || role.trim().isEmpty()) {
			setZeroSummary(dashboardSummaryDTO);
			return dashboardSummaryDTO;
		}

		role = role.trim().toUpperCase(Locale.ROOT);

		if ("ROLE_ADMIN".equals(role)) {
			List<Long> adminUnitIds = getAllBusinessUnitIds();
			dashboardSummaryDTO.setTotalServices(longOrZero(servicesRepo.countAllServices()));
			dashboardSummaryDTO.setTotalProducts(longOrZero(productRepo.countAllProducts()));
			dashboardSummaryDTO.setTotalCustomers(longOrZero(customerRepo.countActiveCustomers()));
			dashboardSummaryDTO.setTodayAppointments(longOrZero(invoiceRepo.countTodayInvoices()));
			dashboardSummaryDTO.setTotalRevenue(doubleOrZero(paymentRepo.getTotalReceivedByUser(null)));
			dashboardSummaryDTO.setTodayRevenue(doubleOrZero(paymentRepo.getTodayReceivedByUser(null)));
			dashboardSummaryDTO.setTopServices(mapTopServices(servicesRepo.getTopServicesAll()));
			dashboardSummaryDTO.setRecentPayments(
					mapRecentPayments(adminUnitIds.isEmpty() ? new ArrayList<>() : paymentRepo.getPaymentsByUnits(adminUnitIds)));
			List<Map<String, Object>> lowStockRows = productRepo.getLowStockProductsAll(LOW_STOCK_THRESHOLD);
			dashboardSummaryDTO.setLowStockProducts(mapLowStockProducts(lowStockRows));
			dashboardSummaryDTO.setInventoryAlerts(mapInventoryAlerts(lowStockRows));
			return dashboardSummaryDTO;
		}

		List<Long> scopedUnitIds = resolveScopedUnitIds(user_id, role);

		if ("ROLE_EMPLOYEE".equals(role)) {
			if (!scopedUnitIds.isEmpty()) {
				dashboardSummaryDTO.setTotalServices(longOrZero(servicesRepo.countServicesByBusinessUnitIds(scopedUnitIds)));
				dashboardSummaryDTO.setTotalProducts(longOrZero(productRepo.countProductsByBusinessUnitIds(scopedUnitIds)));
				dashboardSummaryDTO
						.setTotalCustomers(longOrZero(customerRepo.countActiveCustomersByBusinessUnitIds(scopedUnitIds)));
			} else {
				dashboardSummaryDTO.setTotalServices(0L);
				dashboardSummaryDTO.setTotalProducts(0L);
				dashboardSummaryDTO.setTotalCustomers(0L);
			}

			dashboardSummaryDTO.setTodayAppointments(longOrZero(invoiceRepo.countTodayInvoicesByUser(user_id)));
			dashboardSummaryDTO.setTotalRevenue(doubleOrZero(paymentRepo.getTotalReceivedByUser(user_id)));
			dashboardSummaryDTO.setTodayRevenue(doubleOrZero(paymentRepo.getTodayReceivedByUser(user_id)));
			dashboardSummaryDTO.setTopServices(mapTopServices(servicesRepo.getTopServicesByUser(user_id)));
			dashboardSummaryDTO.setRecentPayments(
					mapRecentPayments(scopedUnitIds.isEmpty() ? new ArrayList<>() : paymentRepo.getPaymentsByUnits(scopedUnitIds)));
			List<Map<String, Object>> lowStockRows = scopedUnitIds.isEmpty() ? new ArrayList<>()
					: productRepo.getLowStockProductsByBusinessUnitIds(scopedUnitIds, LOW_STOCK_THRESHOLD);
			dashboardSummaryDTO.setLowStockProducts(mapLowStockProducts(lowStockRows));
			dashboardSummaryDTO.setInventoryAlerts(mapInventoryAlerts(lowStockRows));
			return dashboardSummaryDTO;
		}

		if (scopedUnitIds.isEmpty()) {
			setZeroSummary(dashboardSummaryDTO);
			return dashboardSummaryDTO;
		}

		dashboardSummaryDTO.setTotalServices(longOrZero(servicesRepo.countServicesByBusinessUnitIds(scopedUnitIds)));
		dashboardSummaryDTO.setTotalProducts(longOrZero(productRepo.countProductsByBusinessUnitIds(scopedUnitIds)));
		dashboardSummaryDTO.setTotalCustomers(longOrZero(customerRepo.countActiveCustomersByBusinessUnitIds(scopedUnitIds)));
		dashboardSummaryDTO.setTodayAppointments(longOrZero(invoiceRepo.countTodayInvoicesByBusinessUnitIds(scopedUnitIds)));
		dashboardSummaryDTO.setTotalRevenue(doubleOrZero(paymentRepo.getTotalReceivedByUnits(scopedUnitIds)));
		dashboardSummaryDTO.setTodayRevenue(doubleOrZero(paymentRepo.getTodayReceivedByUnits(scopedUnitIds)));
		dashboardSummaryDTO.setTopServices(mapTopServices(servicesRepo.getTopServicesByBusinessUnitIds(scopedUnitIds)));
		dashboardSummaryDTO.setRecentPayments(mapRecentPayments(paymentRepo.getPaymentsByUnits(scopedUnitIds)));
		List<Map<String, Object>> lowStockRows = productRepo.getLowStockProductsByBusinessUnitIds(scopedUnitIds,
				LOW_STOCK_THRESHOLD);
		dashboardSummaryDTO.setLowStockProducts(mapLowStockProducts(lowStockRows));
		dashboardSummaryDTO.setInventoryAlerts(mapInventoryAlerts(lowStockRows));

		return dashboardSummaryDTO;
	}

	private List<Long> resolveScopedUnitIds(Long userId, String role) {
		if ("ROLE_EMPLOYEE".equals(role)) {
			Employee employee = employeeRepo.findByUserId(userId);
			if (employee == null || employee.getBusiness_unit_id() == null) {
				return Collections.emptyList();
			}
			List<Long> ids = new ArrayList<>();
			ids.add(employee.getBusiness_unit_id());
			return ids;
		}

		if ("ROLE_MANAGER".equals(role)) {
			Employee manager = employeeRepo.findByUserId(userId);
			if (manager == null || manager.getBusiness_unit_id() == null) {
				return Collections.emptyList();
			}
			List<Long> ids = businessUnitRepo.getAllChildUnitIds(manager.getBusiness_unit_id());
			return ids == null ? Collections.emptyList() : ids;
		}

		if ("ROLE_FRANCHESE".equals(role) || "ROLE_MAIN".equals(role)) {
			BusinessUnit businessUnit = businessUnitRepo.findByUserId(userId);
			if (businessUnit == null || businessUnit.getId() == null) {
				return Collections.emptyList();
			}
			List<Long> ids = businessUnitRepo.getAllChildUnitIds(businessUnit.getId());
			return ids == null ? Collections.emptyList() : ids;
		}

		return Collections.emptyList();
	}

	private List<Long> getAllBusinessUnitIds() {
		List<Long> ids = new ArrayList<>();
		List<BusinessUnit> allUnits = businessUnitRepo.findAll();
		if (allUnits == null || allUnits.isEmpty()) {
			return ids;
		}

		for (BusinessUnit unit : allUnits) {
			if (unit != null && unit.getId() != null) {
				ids.add(unit.getId());
			}
		}
		return ids;
	}

	private void setZeroSummary(DashboardSummaryDTO summary) {
		summary.setTotalServices(0L);
		summary.setTotalProducts(0L);
		summary.setTotalCustomers(0L);
		summary.setTodayAppointments(0L);
		summary.setTotalRevenue(0D);
		summary.setTodayRevenue(0D);
	}

	private Long longOrZero(Long value) {
		return value == null ? 0L : value;
	}

	private Double doubleOrZero(BigDecimal value) {
		return value == null ? 0D : value.doubleValue();
	}

	private List<TopServiceDTO> mapTopServices(List<Map<String, Object>> rows) {
		List<TopServiceDTO> result = new ArrayList<>();
		if (rows == null || rows.isEmpty()) {
			return result;
		}

		long totalQty = 0L;
		for (Map<String, Object> row : rows) {
			totalQty += longOrZero(toLong(row.get("total_qty")));
		}

		int count = 0;
		for (Map<String, Object> row : rows) {
			if (count >= TOP_SERVICES_LIMIT) {
				break;
			}
			TopServiceDTO dto = new TopServiceDTO();
			Long qty = longOrZero(toLong(row.get("total_qty")));
			BigDecimal revenue = toBigDecimal(row.get("total_revenue"));
			dto.setId(toLong(row.get("id")));
			dto.setName(toStringValue(row.get("name")));
			dto.setCount(qty);
			dto.setRevenue(formatRupee(revenue));
			dto.setPercent(toPercent(qty, totalQty));
			result.add(dto);
			count++;
		}
		return result;
	}

	private List<Map<String, Object>> mapRecentPayments(List<Map<String, Object>> rows) {
		List<Map<String, Object>> result = new ArrayList<>();
		if (rows == null || rows.isEmpty()) {
			return result;
		}

		int count = 0;
		for (Map<String, Object> row : rows) {
			if (count >= RECENT_PAYMENTS_LIMIT) {
				break;
			}
			Map<String, Object> item = new LinkedHashMap<>();
			item.put("payment_id", row.get("payment_id"));
			item.put("amount", row.get("amount"));
			item.put("payment_date", row.get("payment_date"));
			item.put("mode", row.get("mode"));
			item.put("reference_no", row.get("reference_no"));
			item.put("invoice_no", row.get("invoice_no"));
			item.put("invoice_date", row.get("invoice_date"));
			item.put("tax_amount", row.get("tax_amount"));
			item.put("invoice_net_amount", row.get("invoice_net_amount"));
			item.put("customer_id", row.get("customer_id"));
			item.put("status", row.get("status"));
			item.put("employee_name", row.get("employee_name"));
			item.put("empid", row.get("empid"));
			item.put("customer_name", row.get("customer_name"));
			result.add(item);
			count++;
		}
		return result;
	}

	private List<ProductDTO> mapLowStockProducts(List<Map<String, Object>> rows) {
		List<ProductDTO> result = new ArrayList<>();
		if (rows == null || rows.isEmpty()) {
			return result;
		}

		int count = 0;
		for (Map<String, Object> row : rows) {
			if (count >= LOW_STOCK_LIMIT) {
				break;
			}
			ProductDTO dto = new ProductDTO();
			dto.setId(toLong(row.get("id")));
			dto.setBusiness_unit_id(toLong(row.get("business_unit_id")));
			dto.setName(toStringValue(row.get("name")));
			dto.setSku(toStringValue(row.get("sku")));
			dto.setHsn_code(toStringValue(row.get("hsn_code")));
			dto.setPrice(toDouble(row.get("price")));
			dto.setSelling_price(toBigDecimal(row.get("selling_price")));
			dto.setDiscount_percent(toBigDecimal(row.get("discount_percent")));
			dto.setGst_percent(toBigDecimal(row.get("gst_percent")));
			dto.setTrack_batch(toBoolean(row.get("track_batch")));
			dto.setIsActive(toBoolean(row.get("is_active")));
			dto.setImage(toStringValue(row.get("image_url")));
			result.add(dto);
			count++;
		}
		return result;
	}

	private List<InventoryAlertDTO> mapInventoryAlerts(List<Map<String, Object>> rows) {
		List<InventoryAlertDTO> result = new ArrayList<>();
		if (rows == null || rows.isEmpty()) {
			return result;
		}

		LocalDate expiryCutoff = LocalDate.now().plusDays(EXPIRY_ALERT_DAYS);

		int count = 0;
		for (Map<String, Object> row : rows) {
			if (count >= INVENTORY_ALERT_LIMIT) {
				break;
			}
			InventoryAlertDTO dto = new InventoryAlertDTO();
			Integer stock = toInteger(row.get("stock_qty"));
			LocalDate nearestExpiry = toLocalDate(row.get("nearest_expiry_date"));

			dto.setId(toLong(row.get("id")));
			dto.setItem(toStringValue(row.get("name")));
			dto.setBusiness_unit_id(toLong(row.get("business_unit_id")));
			dto.setStock(stock == null ? 0 : stock);
			dto.setStatus(isExpiring(nearestExpiry, expiryCutoff) ? "Expiring" : "Low");

			result.add(dto);
			count++;
		}
		return result;
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

	private Double toDouble(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		try {
			return Double.parseDouble(value.toString());
		} catch (Exception ex) {
			return null;
		}
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

	private Boolean toBoolean(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		if (value instanceof Number) {
			return ((Number) value).intValue() != 0;
		}
		return Boolean.parseBoolean(value.toString());
	}

	private String toStringValue(Object value) {
		return value == null ? null : value.toString();
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

	private boolean isExpiring(LocalDate expiryDate, LocalDate cutoffDate) {
		if (expiryDate == null) {
			return false;
		}
		return !expiryDate.isAfter(cutoffDate);
	}

	private Integer toPercent(Long part, Long total) {
		if (part == null || total == null || total <= 0L) {
			return 0;
		}
		double percent = (part.doubleValue() * 100.0d) / total.doubleValue();
		return (int) Math.round(percent);
	}

	private String formatRupee(BigDecimal amount) {
		BigDecimal safe = amount == null ? BigDecimal.ZERO : amount;
		NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("en", "IN"));
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(0);
		return "\u20b9" + formatter.format(safe);
	}

}
