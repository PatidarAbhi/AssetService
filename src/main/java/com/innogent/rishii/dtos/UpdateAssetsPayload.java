package com.innogent.rishii.dtos;

import com.innogent.rishii.entities.Categories;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAssetsPayload {
    private String name;
    private String vendor;
    private Long count;
    private String properties;
    private Long groupId;
    private Long companyId;
    private Categories categories;
}
