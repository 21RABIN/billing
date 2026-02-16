package com.rbilling.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rbilling.DTO.ProductBatchDTO;
import com.rbilling.DTO.ProductDTO;
import com.rbilling.model.Product;
import com.rbilling.model.ProductBatch;
import com.rbilling.repository.ProductBatchRepository;
import com.rbilling.repository.ProductRepository;
import com.rbilling.responce.MessageResponse;
import com.rbilling.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository prodrepo;
	
	@Autowired
	ProductBatchRepository prodbatchrepo;

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
                    .selling_price(proddto.getSelling_price())
                    .gstPercent(proddto.getGst_percent())
                    .isActive(true)
                    .build();
			prodrepo.save(product);
			
			//Product Batch Added
			ProductBatchDTO batchDto = proddto.getBatch();
			
			 ProductBatch prodbatch = ProductBatch.builder()
	                    .product_id(product.getId()) // link to product
	                    .batch_no(batchDto.getBatch_no())
	                    .expiry_date(batchDto.getExpiry_date())
	                    .purchase_price(batchDto.getPurchase_price())
	                    .stock_qty(batchDto.getStock_qty())
	                    .supplier_id(batchDto.getSupplier_id())
	                    .isActive(batchDto.getIsActive() != null ? batchDto.getIsActive() : true)
	                    .build();

			 prodbatchrepo.save(prodbatch);
			
			

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
			
			  ProductBatchDTO batchDto = proddto.getBatch();

			    if (product != null) {

			        ProductBatch batch;

			        if (product.getId() != null) {
			            // Update existing batch
			        	
			        	Long prodbatchid=prodbatchrepo.getprodbatchid(product.getId());
			        	
			            batch = prodbatchrepo.findById(prodbatchid).orElse(null);
			            
			            
			            
			            
			            System.out.println("batch :"+batch);
			            if (batch == null) {
			                return ResponseEntity.badRequest()
			                        .body(new MessageResponse("Batch Not Found"));
			            }
			            
			            // Common fields (for both create & update)
				        if (batchDto.getBatch_no() != null) batch.setBatch_no(batchDto.getBatch_no());
				        if (batchDto.getExpiry_date() != null) batch.setExpiry_date(batchDto.getExpiry_date());
				        if (batchDto.getManufacture_date() != null) batch.setManufacture_date(batchDto.getManufacture_date());
				        if (batchDto.getPurchase_price() != null) batch.setPurchase_price(batchDto.getPurchase_price());
				        if (batchDto.getStock_qty() != null) batch.setStock_qty(batchDto.getStock_qty());
				        if (batchDto.getSupplier_id() != null) batch.setSupplier_id(batchDto.getSupplier_id());
				        if (batchDto.getIsActive() != null) batch.setIsActive(batchDto.getIsActive());

				        prodbatchrepo.save(batch);
			            
			            

			        } 

			      
			    }
			
			
			

			return ResponseEntity.ok(new MessageResponse("Product Updated Successfully"));
		}
	}
}
