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
			
			if(prodrepo.existsByName(proddto.getName())) {
				
				return ResponseEntity.badRequest().body(new MessageResponse("Product Name Alredy Exist"));
			}
			

			Product product = Product.builder()
                    .businessUnitId(proddto.getBusiness_unit_id())
                    .name(proddto.getName())
                    .sku(proddto.getSku())
                    .price(proddto.getPrice())
                    .gstPercent(proddto.getGst_percent())
                    .isActive(true)
                    .build();
			prodrepo.save(product);

			return ResponseEntity.ok( new MessageResponse("Product Created Successfully"));
		}

		// ================= UPDATE =================
		else {

			Product product = prodrepo.findByIdAndBusinessUnitId(proddto.getId(), proddto.getBusiness_unit_id());

			if (product == null) {
				return ResponseEntity.badRequest().body(new MessageResponse("Product not found or unauthorized"));
			}

			if (proddto.getName() != null)
				product.setName(proddto.getName());

			if (proddto.getSku() != null)
				product.setSku(proddto.getSku());

			if (proddto.getPrice() != null)
				product.setPrice(proddto.getPrice());

			if (proddto.getGst_percent() != null)
				product.setGstPercent(proddto.getGst_percent());

			if (proddto.getIsActive() != null)
				product.setIsActive(proddto.getIsActive());

			prodrepo.save(product);

			return ResponseEntity.ok(new MessageResponse("Product Updated Successfully"));
		}
	}
}
