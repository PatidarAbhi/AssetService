package com.innogent.rishii.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetItemsPayload {

    @NotBlank(message = "Cannot be null")
    private String serialId;

    @NotBlank (message = "Status  is required")
    private String status;
}
