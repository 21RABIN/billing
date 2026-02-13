package com.rbilling.service;

import org.springframework.http.ResponseEntity;

import com.rbilling.DTO.ProductDTO;

public interface ProductService {



	ResponseEntity<?> createUpdateProduct(ProductDTO proddto);

}
