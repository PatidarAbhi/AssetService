package com.innogent.rishii.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAssetStatusPayload {

    @NotEmpty(message = "Status is required")
    private String status;
    @NotNull(message = "Company id is required")
    private Long companyId;
}
