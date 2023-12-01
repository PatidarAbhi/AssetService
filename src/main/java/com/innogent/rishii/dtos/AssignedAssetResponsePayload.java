package com.innogent.rishii.dtos;

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
public class AssignedAssetResponsePayload {

    private Long id;

    private AssetResponsePayload assetId;

    private UserDTO assignedBy;

    private Boolean recovered;

    private Long companyId;

    private UserDTO assignedTo;

    private UserDTO recoveredBy;

    private LocalDateTime assignedAt;

    private  LocalDateTime recoveredAt;


}
