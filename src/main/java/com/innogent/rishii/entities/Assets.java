package com.innogent.rishii.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "assets")
public class Assets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private Long id;

    @Column(name = "name")
    private String name;
    private String status;
    private Long count;
    private String vendor;
    private Boolean recoverable;
    private String properties;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String serialId;

    private Long groupId;

    private Long addedBy;
    private Long companyId;

    @ManyToOne
    private Categories categories;


}
