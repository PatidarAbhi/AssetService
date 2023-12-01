package com.innogent.rishii.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssetItemsPayload {

    @NotNull(message = "Serial id is required")
    private String serialId;

    @NotNull (message = "Status  is required")
    private String status;
}
