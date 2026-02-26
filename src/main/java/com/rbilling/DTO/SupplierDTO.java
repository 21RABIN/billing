package com.rbilling.DTO;

import lombok.Data;

@Data
public class SupplierDTO {

    private Long id;   // null = create

    private Long business_unit_id;
    private String supplier_name;
    private String contact_person;
    private String phone;
    private String email;
    private String address;
    private String gst_number;
    private Boolean isActive;
}
