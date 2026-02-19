package com.rbilling.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rbilling.model.InvoiceItem;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

}
