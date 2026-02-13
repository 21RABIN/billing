package com.rbilling.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rbilling.DTO.ProductDTO;
import com.rbilling.model.Product;
import com.rbilling.repository.ProductRepository;
import com.rbilling.responce.MessageResponse;
import com.rbilling.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository prodrepo;

	public ResponseEntity<?> createUpdateProduct(ProductDTO proddto) {

		// ================= CREATE =================
		if (proddto.getId() == null) {

			Product product = Product.builder()
                    .businessUnitId(proddto.getBusinessUnitId())
                    .name(proddto.getName())
                    .sku(proddto.getSku())
                    .price(proddto.getPrice())
                    .gstPercent(proddto.getGstPercent())
                    .isActive(true)
                    .build();
			prodrepo.save(product);

			return ResponseEntity.ok( new MessageResponse("Product Created Successfully"));
		}

		// ================= UPDATE =================
		else {

			Product product = prodrepo.findByIdAndBusinessUnitId(proddto.getId(), proddto.getBusinessUnitId());

			if (product == null) {
				return ResponseEntity.badRequest().body(new MessageResponse("Product not found or unauthorized"));
			}

			if (proddto.getName() != null)
				product.setName(proddto.getName());

			if (proddto.getSku() != null)
				product.setSku(proddto.getSku());

			if (proddto.getPrice() != null)
				product.setPrice(proddto.getPrice());

			if (proddto.getGstPercent() != null)
				product.setGstPercent(proddto.getGstPercent());

			if (proddto.getIsActive() != null)
				product.setIsActive(proddto.getIsActive());

			prodrepo.save(product);

			return ResponseEntity.ok(new MessageResponse("Product Updated Successfully"));
		}
	}
}
