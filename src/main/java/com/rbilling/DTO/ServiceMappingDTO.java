package com.rbilling.DTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ServiceMappingDTO {
	
//Service Object
    private Long service_id;
    private BigDecimal special_price=BigDecimal.ZERO;
    private BigDecimal discount_percent=BigDecimal.ZERO;
    
    
    //
    
    private Long id;
    private Long business_unit_id;
    private String business_name;
    private String name;
    private BigDecimal base_Price=BigDecimal.ZERO;
    private BigDecimal gst_percent=BigDecimal.ZERO;
    private String sac_code;
    private Boolean isActive;

}
