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
public class AssetPayload {

    @NotEmpty(message = "Name is required")
    private String name;

    private Long count;

    @NotEmpty (message = "Vendor is required")
    private String vendor;

    @NotNull (message = "Recoverable is required")
    private Boolean recoverable;

    private String properties;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @NotNull (message = "Added by is required")
    private Long addedBy;

    @NotNull (message = "Company id is required")
    private Long companyId;

    private Long groupId;

    private List<AssetItemsPayload> assetItems;

    @NotNull(message = "Category id is required")
    private Categories categories;

}
