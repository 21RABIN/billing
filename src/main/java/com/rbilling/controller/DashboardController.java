package com.rbilling.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rbilling.DTO.DashboardSummaryDTO;
import com.rbilling.service.DashboardService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor 
@CrossOrigin(origins = "*", maxAge = 3600)  
@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary(@RequestParam Long user_id) {
        return ResponseEntity.ok(dashboardService.getDashboardSummary(user_id));
    }
}
	