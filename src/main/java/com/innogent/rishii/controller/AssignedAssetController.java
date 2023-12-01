package com.innogent.rishii.controller;
import com.innogent.rishii.customException.*;
import com.innogent.rishii.dtos.AssignedAssetPayload;
import com.innogent.rishii.dtos.AssignedAssetResponsePayload;
import com.innogent.rishii.dtos.RecoveredByRequestPayload;
import com.innogent.rishii.service.AssignedAssetService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/assignedAssets")
public class AssignedAssetController {
    private static final Logger log = LoggerFactory.getLogger(AssignedAssetController.class);

    @Autowired
    private AssignedAssetService assignedAssetService;


    /**
     * For  Assign Asset
     *
     * @return ResponseEntity
     */
    @PostMapping("/")
    public ResponseEntity<?> assignedAsset( @Valid @RequestBody AssignedAssetPayload assignedAssetPayload) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Assign Asset API Called, UUID {}", assignedAssetPayload);
            List<AssignedAssetResponsePayload> assignedAsset = assignedAssetService.assignedAsset(assignedAssetPayload);
            return new ResponseEntity<>(new CustomResponse<>(true,"Asset assigned successfully",assignedAsset), HttpStatus.CREATED);
        }
        catch (UserNotFoundException e) {
            log.error("UUID {}, UserNotFoundException in Assign Asset API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.NOT_FOUND);
        }
        catch (CountNotMatchException e) {
            log.error("UUID {}, CountNotMatchException in Assign  API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
        catch (CompanyNotFoundException e) {
            log.error("UUID {}, CompanyNotFoundException Assign Asset  API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            log.error("UUID {} Exception In Assign Asset API Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * For  Getting Assigned Asset
     *
     * @return ResponseEntity
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getMyAssignedAsset( @Valid @PathVariable("userId") Long userId) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Get Assign Asset By UserId API Called, UUID {}", debugUuid);
            List<AssignedAssetResponsePayload> assignedAsset = assignedAssetService.getMyAssignedAsset(userId);
            return new ResponseEntity<>(new CustomResponse<>(true,"Asset fetch successfully",assignedAsset), HttpStatus.CREATED);
        }
        catch (UserNotFoundException e) {
            log.error("UUID {}, UserNotFoundException in Get Assign Asset  By UserId API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            log.error("UUID {} Exception In Get Assign Asset  By UserId API Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * For  Getting Assigned Asset
     *
     * @return ResponseEntity
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> getAssignedAssetOfCompany( @Valid @PathVariable("companyId") Long companyId) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Get Assign Asset By Company Id API Called, UUID {}", debugUuid);
            List<AssignedAssetResponsePayload> assignedAsset = assignedAssetService.getAssignedAssetOfCompany(companyId);
            return new ResponseEntity<>(new CustomResponse<>(true,"Asset fetch successfully",assignedAsset), HttpStatus.CREATED);
        }
        catch (CompanyNotFoundException e) {
            log.error("UUID {}, CompanyNotFoundException in Get Assign Asset By Company Id API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            log.error("UUID {} Exception In Get Assign Asset Company Id API Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * For  Recover Assigned Asset
     *
     * @return ResponseEntity
     */
    @GetMapping("/recover/{assignedAssetId}")
    public ResponseEntity<?> recoverAssignedAsset(@Valid @PathVariable("assignedAssetId") Long assignedAssetId,
                                                  @Valid @RequestBody RecoveredByRequestPayload recoveredBy) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Recover Assigned Asset By AssignedAssetId API Called, UUID {}", debugUuid);
            Boolean status = assignedAssetService.recoverAssignedAsset(assignedAssetId,recoveredBy.getRecoveredBy());
            return new ResponseEntity<>(new CustomResponse<>(status,"Asset Recovered successfully",null), HttpStatus.OK);
        }
        catch (NotRecoverableException e) {
            log.error("UUID {}, NotRecoverableException in Recover Assigned Asset By AssignedAssetId API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.NOT_FOUND);
        }
        catch (AssetsNotFoundException e) {
            log.error("UUID {}, AssetsNotFoundException in Recover Assigned Asset By AssignedAssetId API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.NOT_FOUND);
        }
        catch (AssignedAssetNotFoundException e) {
            log.error("UUID {}, AssignedAssetNotFoundException in Recover Assigned Asset By AssignedAssetId API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            log.error("UUID {} Exception In Recover Assigned Asset By AssignedAssetId API Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
    }

}
