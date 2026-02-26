package com.rbilling.DTO;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class DashboardSummaryDTO {

    private Long totalServices;
    private Long totalProducts;
    private Long totalCustomers;
    private Long todayAppointments;

    private Double totalRevenue;
    private Double todayRevenue;
	
    private List<TopServiceDTO> topServices;
    private List<Map<String, Object>> recentPayments;
    private List<ProductDTO> lowStockProducts;
    private List<InventoryAlertDTO> inventoryAlerts;
}	
