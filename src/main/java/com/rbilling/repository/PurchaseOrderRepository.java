package com.rbilling.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rbilling.model.PurchaseOrders;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrders, Long>{

}
