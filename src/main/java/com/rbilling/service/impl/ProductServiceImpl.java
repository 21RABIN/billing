package com.rbilling.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

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

		// CREATE 
		if (proddto.getId() == null) {

			if (prodrepo.existsByName(proddto.getName())) {

				System.out.println("..");

				return ResponseEntity.badRequest().body(new MessageResponse("Product Name Alredy Exist"));
			}

			String imagePath = null;
			if (proddto.getImage() != null && !proddto.getImage().isEmpty()) {

				try {
					String base64Image = proddto.getImage();

					// Remove data:image/png;base64, part if present
					if (base64Image.contains(",")) {
						base64Image = base64Image.split(",")[1];
					}

					byte[] imageBytes = Base64.getDecoder().decode(base64Image);

					String uploadDir = "/var/www/html/AARPACKAGE/";
					File directory = new File(uploadDir);
					if (!directory.exists()) {
						directory.mkdirs();
					}

					String fileName = System.currentTimeMillis() + ".png";
					Path path = Paths.get(uploadDir + fileName);

					Files.write(path, imageBytes);

					System.out.println("Saving file to: " + path.toAbsolutePath());

					imagePath = uploadDir + fileName;

				} catch (Exception e) {
					return ResponseEntity.badRequest().body(new MessageResponse("Invalid Image Format"));
				}
			}

			Product product = Product.builder().businessUnitId(proddto.getBusiness_unit_id()).name(proddto.getName())
					.sku(proddto.getSku()).price(proddto.getPrice()).selling_price(proddto.getSelling_price())
					.discount_percent(proddto.getDiscount_percent()).gst_percent(proddto.getGst_percent())
					.isActive(true).image(imagePath).build();
			prodrepo.save(product);

			if (Boolean.TRUE.equals(proddto.getTrack_batch())) {
				// Product Batch Added
				ProductBatchDTO batchDto = proddto.getBatch();

				ProductBatch prodbatch = ProductBatch.builder().product_id(product.getId()) // link to product
						.batch_no(batchDto.getBatch_no()).expiry_date(batchDto.getExpiry_date())
						.purchase_price(batchDto.getPurchase_price()).stock_qty(batchDto.getStock_qty())
						.supplier_id(batchDto.getSupplier_id())
						.isActive(batchDto.getIsActive() != null ? batchDto.getIsActive() : true).build();

				prodbatchrepo.save(prodbatch);
			}

			return ResponseEntity.ok(new MessageResponse("Product Created Successfully"));
		}

		// ================= UPDATE =================
		else {

			Product product = prodrepo.findByIdAndBusinessUnitId(proddto.getId(), proddto.getBusiness_unit_id());

			if (product == null) {
				return ResponseEntity.badRequest().body(new MessageResponse("Product not found or unauthorized"));
			}
			System.out.println("product.getImage() :" + product.getImage());
			
			String imagePath = null;

			if (product.getImage() != null) {

				String image = product.getImage().replace("/var/www/html/", "http://192.168.1.182/");

				System.out.println("image :" + image);
				if (image == null || !image.equals(proddto.getImage())) {

					if (proddto.getImage() != null && !proddto.getImage().isEmpty()) {

						try {
							String base64Image = proddto.getImage();

							if (base64Image.contains(",")) {
								base64Image = base64Image.split(",")[1];
							}

							byte[] imageBytes = Base64.getDecoder().decode(base64Image);

							// Delete old image if exists
							if (product.getImage() != null) {
								Files.deleteIfExists(Paths.get(product.getImage()));
							}

							String uploadDir = "/var/www/html/AARPACKAGE/";
							File directory = new File(uploadDir);
							if (!directory.exists()) {
								directory.mkdirs();
							}

							String fileName = System.currentTimeMillis() + ".png";
							Path path = Paths.get(uploadDir + fileName);

							Files.write(path, imageBytes);

							product.setImage(uploadDir + fileName);
							
							imagePath = uploadDir + fileName;


						} catch (Exception e) {
							return ResponseEntity.badRequest().body(new MessageResponse("Invalid Image Format"));
						}
					}

				}
			}
			
			else {
				
				
				if (proddto.getImage() != null && !proddto.getImage().isEmpty()) {

					try {
						String base64Image = proddto.getImage();

						// Remove data:image/png;base64, part if present
						if (base64Image.contains(",")) {
							base64Image = base64Image.split(",")[1];
						}

						byte[] imageBytes = Base64.getDecoder().decode(base64Image);

						String uploadDir = "/var/www/html/AARPACKAGE/";
						File directory = new File(uploadDir);
						if (!directory.exists()) {
							directory.mkdirs();
						}

						String fileName = System.currentTimeMillis() + ".png";
						Path path = Paths.get(uploadDir + fileName);

						Files.write(path, imageBytes);

						System.out.println("Saving file to: " + path.toAbsolutePath());

						imagePath = uploadDir + fileName;

					} catch (Exception e) {
						return ResponseEntity.badRequest().body(new MessageResponse("Invalid Image Format"));
					}
				}
				
			}

			if (proddto.getName() != null)
				product.setName(proddto.getName());

			if (proddto.getSku() != null)
				product.setSku(proddto.getSku());

			if (proddto.getPrice() != null)
				product.setPrice(proddto.getPrice());

			if (proddto.getGst_percent() != null)
				product.setGst_percent(proddto.getGst_percent());

			if (proddto.getIsActive() != null)
				product.setIsActive(proddto.getIsActive());

			if (proddto.getDiscount_percent() != null)
				product.setDiscount_percent(proddto.getDiscount_percent());

			if (proddto.getSelling_price() != null)
				product.setSelling_price(proddto.getSelling_price());
			
			if (proddto.getImage() != null)
				product.setImage(imagePath);

			prodrepo.save(product);

			if (Boolean.TRUE.equals(proddto.getTrack_batch())) {

				ProductBatchDTO batchDto = proddto.getBatch();

				if (batchDto == null) {
					return ResponseEntity.badRequest().body(new MessageResponse("Batch details required"));
				}

				if (product != null) {

//					ProductBatch batch;

					if (product.getId() != null) {
						// Update existing batch

						Long prodbatchid = prodbatchrepo.getprodbatchid(product.getId());

						ProductBatch batch = null;

						if (prodbatchid != null) {
							batch = prodbatchrepo.findById(prodbatchid).orElse(null);
						}

						System.out.println("batch :" + batch);
//						if (batch == null) {
//							return ResponseEntity.badRequest().body(new MessageResponse("Batch Not Found"));
//						}

						if (batch != null) {

							// Common fields (for both create & update)
							if (batchDto.getBatch_no() != null)
								batch.setBatch_no(batchDto.getBatch_no());
							if (batchDto.getExpiry_date() != null)
								batch.setExpiry_date(batchDto.getExpiry_date());
							if (batchDto.getManufacture_date() != null)
								batch.setManufacture_date(batchDto.getManufacture_date());
							if (batchDto.getPurchase_price() != null)
								batch.setPurchase_price(batchDto.getPurchase_price());
							if (batchDto.getStock_qty() != null)
								batch.setStock_qty(batchDto.getStock_qty());
							if (batchDto.getSupplier_id() != null)
								batch.setSupplier_id(batchDto.getSupplier_id());
							if (batchDto.getIsActive() != null)
								batch.setIsActive(batchDto.getIsActive());

							prodbatchrepo.save(batch);

						} else {

							batch = ProductBatch.builder().product_id(product.getId()).batch_no(batchDto.getBatch_no())
									.expiry_date(batchDto.getExpiry_date())
									.manufacture_date(batchDto.getManufacture_date())
									.purchase_price(batchDto.getPurchase_price()).stock_qty(batchDto.getStock_qty())
									.supplier_id(batchDto.getSupplier_id())
									.isActive(batchDto.getIsActive() != null ? batchDto.getIsActive() : true).build();

							prodbatchrepo.save(batch);
						}

					}
				}

			}

			return ResponseEntity.ok(new MessageResponse("Product Updated Successfully"));
		}
	}
}
