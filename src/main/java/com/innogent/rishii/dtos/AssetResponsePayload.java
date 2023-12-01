package com.innogent.rishii.dtos;

import com.innogent.rishii.entities.Categories;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AssetResponsePayload {

    private String name;

    private Long count;

    private String vendor;

    private Boolean recoverable;

    private String properties;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private UserDTO addedBy;

    private Long companyId;

    private List<AssetItemsPayload> assetItems;

    private CategoryPayload category;

}
