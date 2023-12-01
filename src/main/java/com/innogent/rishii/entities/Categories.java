package com.innogent.rishii.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "Categories")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "name")
    @NotEmpty (message = "Name is required")
    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @NotNull(message = "Creator id is required")
    private Long createdBy;

    @NotNull (message = "Company id is required")
    private Long companyId;

}
