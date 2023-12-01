package com.innogent.rishii.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="assigned_assets")
@NoArgsConstructor
@Getter
@Setter
public class AssignedAssets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean recovered;
    private Long assignedTo;
    private Long assignedBy;
    private Long recoveredBy;
    private  Long companyId;
    private LocalDateTime assignedAt;
    private  long assetId;
    private LocalDateTime  recoveredAt;
}

