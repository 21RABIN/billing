package com.rbilling.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rbilling.DTO.ServicesDTO;
import com.rbilling.model.Services;
import com.rbilling.repository.ServicesRepository;
import com.rbilling.responce.MessageResponse;
import com.rbilling.service.ServicesService;

@Service
public class ServicesImpl implements ServicesService {

	@Autowired
	ServicesRepository servrepo;

	public ResponseEntity<?> createUpdateServices(ServicesDTO servdto) {

		if (servdto.getId() == null) {
			
			String imagePath = null;
			if (servdto.getImage() != null && !servdto.getImage().isEmpty()) {

				try {
					String base64Image = servdto.getImage();

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

			System.out.println("imagePath :"+imagePath);
			
			Services service = Services.builder()
                    .businessUnitId(servdto.getBusiness_unit_id())
                    .name(servdto.getName())
                    .base_price(servdto.getBase_price())
                    .gst_percent(servdto.getGst_percent())
                    .sac_code(servdto.getSac_code())
                    .isActive(true)
                    .image(imagePath)
                    .build();

			servrepo.save(service);

			return ResponseEntity.ok(new MessageResponse("Service Created Successfully"));
		}

		else {

			Services service = servrepo.findByIdAndBusinessUnitId(servdto.getId(),servdto.getBusiness_unit_id());

			if (service == null) {
				return ResponseEntity.badRequest().body(new MessageResponse("Services not found or unauthorized"));
			}
			
			
			String imagePath = null;
			if (service.getImage() != null) {

				String image = service.getImage().replace("/var/www/html/", "http://192.168.1.182/");

				System.out.println("image :" + image);
				if (image == null || !image.equals(servdto.getImage())) {

					if (servdto.getImage() != null && !servdto.getImage().isEmpty()) {

						try {
							String base64Image = servdto.getImage();

							if (base64Image.contains(",")) {
								base64Image = base64Image.split(",")[1];
							}

							byte[] imageBytes = Base64.getDecoder().decode(base64Image);

							// Delete old image if exists
							if (service.getImage() != null) {
								Files.deleteIfExists(Paths.get(service.getImage()));
							}

							String uploadDir = "/var/www/html/AARPACKAGE/";
							File directory = new File(uploadDir);
							if (!directory.exists()) {
								directory.mkdirs();
							}

							String fileName = System.currentTimeMillis() + ".png";
							Path path = Paths.get(uploadDir + fileName);

							Files.write(path, imageBytes);

//							service.setImage(uploadDir + fileName);
							imagePath = uploadDir + fileName;
							
							
							
							System.out.println("imagePath :"+imagePath);

						} catch (Exception e) {
							return ResponseEntity.badRequest().body(new MessageResponse("Invalid Image Format"));
						}
					}

				}
			}
			else {
				
				if (servdto.getImage() != null && !servdto.getImage().isEmpty()) {

					try {
						String base64Image = servdto.getImage();

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
			
			
			
			
			
			
			
			
			
			

			if (servdto.getName() != null)
				service.setName(servdto.getName());

			if (servdto.getBase_price() != null)
				service.setBase_price(servdto.getBase_price());

			if (servdto.getGst_percent() != null)
				service.setGst_percent(servdto.getGst_percent());
			
			if (servdto.getSac_code() != null)
				service.setSac_code(servdto.getSac_code());

			if (servdto.getIsActive() != null)
				service.setIsActive(servdto.getIsActive());
			
			if (servdto.getImage() != null)
				service.setImage(imagePath);

			servrepo.save(service);

			return ResponseEntity.ok(new MessageResponse("Services Updated Successfully"));
		}
	}

	@Override
	public ResponseEntity<?> deleteService(Long id) {
		Services service = servrepo.findById(id).orElse(null);
		if (service == null) {
			return ResponseEntity.badRequest().body(new MessageResponse("Service not found"));
		}

		service.setIsActive(false);
		servrepo.save(service);
		return ResponseEntity.ok(new MessageResponse("Service Deleted Successfully"));
	}

}
