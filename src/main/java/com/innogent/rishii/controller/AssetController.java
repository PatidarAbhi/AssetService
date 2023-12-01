package com.innogent.rishii.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innogent.rishii.customException.*;
import com.innogent.rishii.dtos.AssetPayload;
import com.innogent.rishii.dtos.AssetResponsePayload;
import com.innogent.rishii.dtos.CategoryResponsePayload;
import com.innogent.rishii.dtos.UpdateAssetsPayload;
import com.innogent.rishii.entities.Assets;
import com.innogent.rishii.entities.AssignedAssets;
import com.innogent.rishii.entities.Categories;
import com.innogent.rishii.service.AssetService;
import com.innogent.rishii.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/assets")
public class AssetController {
    private static final Logger log = LoggerFactory.getLogger(AssetController.class);

    @Autowired
    private AssetService assetService;

    /**
     * For  Adding Asset
     *
     * @return ResponseEntity
     */
    @PostMapping("/")
    public ResponseEntity<?> addAsset( @Valid @RequestBody AssetPayload assetPayload) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Create Asset API Called, UUID {}", assetPayload);
             List<AssetResponsePayload> savedAssets =assetService.createAsset(assetPayload);
            return new ResponseEntity<>(new CustomResponse<>(true,"Asset created successfully",savedAssets), HttpStatus.CREATED);
        }
        catch (UserNotFoundException e) {
            log.error("UUID {}, UserNotFoundException in Add Assert API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.NOT_FOUND);
        }
        catch (CountNotMatchException e) {
            log.error("UUID {}, CountNotMatchException in Add Asset API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
        catch (CompanyNotFoundException e) {
            log.error("UUID {}, CompanyNotFoundException in Add Asset  API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.NOT_FOUND);
        }
        catch (AssetsItemsNotFoundException e) {
            log.error("UUID {}, AssetsItemsNotFoundException in Add Asset  API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            log.error("UUID {} Exception In Create Asset API Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * For  Getting Assets  By CompanyId
     *
     * @return ResponseEntity
     */
    @GetMapping("/{companyId}")
    public ResponseEntity<?> getAssetsByCompanyId(@Valid @PathVariable("companyId") Long companyId) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Get Assets  By CompanyId API Called, UUID {}", debugUuid);
            List<AssetResponsePayload> categoriesList= assetService.getAssetsByCompanyId(companyId);
            return new ResponseEntity<>(new CustomResponse<>(true,"Assets fetched",categoriesList), HttpStatus.OK);
        }
        catch (AssetsNotFoundException e) {
            log.error("UUID {}  Get Assets By CompanyId API AssetsNotFoundException {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            log.error("UUID {} Exception In Get Assets By CompanyId API Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * For  Updating Asset
     *
     * @return ResponseEntity
     */
    @PutMapping("/{groupId}/company/{companyId}")
    public ResponseEntity<?> updateAsset(
            @PathVariable("groupId") Long groupId,
            @PathVariable("companyId") Long companyId,
            @Valid @RequestBody UpdateAssetsPayload assetPayload) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            assetPayload.setGroupId(groupId);
            assetPayload.setCompanyId(companyId);
            log.info("Update Asset API Called, UUID {}", assetPayload);
            Boolean updateStatus =assetService.updateAsset(assetPayload);
            return new ResponseEntity<>(new CustomResponse<>(updateStatus," Assets Updated successfully",null), HttpStatus.OK);
        }
        catch (UserNotFoundException e) {
            log.error("UUID {}, UserNotFoundException in Update Assert API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.NOT_FOUND);
        }
        catch (CategoryNotFoundException e) {
            log.error("UUID {}, CategoryNotFoundException in Update Assert API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
        catch (CompanyNotFoundException e) {
            log.error("UUID {}, CompanyNotFoundException in Update Assert  API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.NOT_FOUND);
        }
        catch (AssetsNotFoundException e) {
            log.error("UUID {}, AssetsNotFoundException in Update Assert  API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            log.error("UUID {} Exception In Update Assert API Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * For  Deleting  Assets
     *
     * @return ResponseEntity
     */
    @DeleteMapping("/{assetId}")
    public ResponseEntity<?> deleteAssets(@Valid @PathVariable("assetId") Long assetId
                                          ) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Delete Asset API Called, UUID {}", debugUuid);
            String status=  assetService.deleteAssets(assetId);
            return new ResponseEntity<>(new CustomResponse<>(true,status,null), HttpStatus.OK);
        }
        catch (AssetsNotFoundException e) {
            log.error("UUID {}  Delete Asset AssetsNotFoundException {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }

        catch (DataIntegrityViolationException e) {
            log.error("UUID {}  Delete Asset API DataIntegrityViolationException {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            log.error("UUID {} Exception Delete Asset API Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
    }
}
