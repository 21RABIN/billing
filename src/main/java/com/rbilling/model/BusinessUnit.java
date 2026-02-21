package com.rbilling.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "business_units")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private BusinessType type;

  
    private Long parent_id;

    @Column(name = "is_active")
    private Boolean isActive = true;

    
    private LocalDateTime created_at;

    private String address;

    private String mobile;


    private String gst_in;

    @PrePersist
    public void onCreate() {
        this.created_at = LocalDateTime.now();
        if (this.isActive == null) {
            this.isActive = true;
        }
    }
}
