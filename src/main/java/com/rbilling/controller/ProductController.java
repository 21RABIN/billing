package com.rbilling.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rbilling.DTO.ProductDTO;
import com.rbilling.repository.ProductRepository;
import com.rbilling.service.AccessScopeService;
import com.rbilling.service.ProductService;

@CrossOrigin(origins = "*", maxAge = 3600) // Allowing all origins is risky. Consider restricting to trusted domains in
											// production.
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

	@Autowired
	ProductService prodservice;

	@Autowired
	ProductRepository prodrepo;

	@Autowired
	AccessScopeService accessScopeService;

	@PostMapping("/create") // Both Api Create and Update
//    @PreAuthorize("hasRole('ADMIN','FRANCHESE')")  //ADMIN AND FRANCHESE ROLE ONLY ACESS THIS API
	public ResponseEntity<?> createUpdateProduct(@RequestBody ProductDTO proddto) {
		
		return prodservice.createUpdateProduct(proddto);
	}

	@GetMapping("/all")
	public ResponseEntity<List<Map<String, Object>>> getAllProducts(@RequestParam Long user_id) {
		List<Map<String, Object>> products = prodrepo.getAllProducts(0L);
		products = accessScopeService.filterRowsByBusinessUnits(products, "business_unit_id", user_id);

		List<Map<String, Object>> enrichedProducts = products.stream().map(p -> {
			Map<String, Object> mutable = new HashMap<>(p);
			mutable.put("type", "PRODUCT");
			return mutable;
		}).collect(Collectors.toList());
		return ResponseEntity.ok(enrichedProducts);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
		return prodservice.deleteProduct(id);
	}

}
