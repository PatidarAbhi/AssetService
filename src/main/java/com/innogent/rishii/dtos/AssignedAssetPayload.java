package com.innogent.rishii.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignedAssetPayload {

    @NotNull(message = "Asset id is required")
    private Long assetId;

    @NotNull(message = "Assignee id is required")
    private Long assignedBy;

    private Boolean recovered;

    @NotNull(message = "Company id is required")
    private Long companyId;

    @NotNull(message = "AssignTo is required")
    private List<Long> assignedTo;

    private Long recoveredBy;

}
