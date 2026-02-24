package com.rbilling.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessResponseDTO {
	
	private String bname;
	private int bid;
	private String parentname;
	private int parentid;

}
