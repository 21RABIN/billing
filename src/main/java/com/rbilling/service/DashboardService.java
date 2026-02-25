package com.rbilling.service;

import java.util.Map;

import com.rbilling.DTO.DashboardSummaryDTO;

public interface DashboardService {

	DashboardSummaryDTO getDashboardSummary(Long user_id);

}
