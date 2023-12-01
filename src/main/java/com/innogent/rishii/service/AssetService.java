package com.innogent.rishii.service;

import com.innogent.rishii.customException.*;
import com.innogent.rishii.dtos.*;
import com.innogent.rishii.entities.Assets;
import com.innogent.rishii.entities.AssignedAssets;
import com.innogent.rishii.entities.Categories;
import com.innogent.rishii.entities.Company;
import com.innogent.rishii.repo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AssetService {

    private static final Logger log = LoggerFactory.getLogger(AssetService.class);
    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private AssignedAssetRepo assignedAssetRepo;


    /**
     * For  Adding Asset
     *
     * @return List<AssetResponsePayload>
     */
    public List<AssetResponsePayload> createAsset(AssetPayload assetPayload) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Add asset Service Called , UUID {}", debugUuid);
            assetPayload.setCreatedAt(LocalDateTime.now());
            assetPayload.setUpdatedAt(LocalDateTime.now());
            Optional<UserDTO> userDTO = userRepo.findUserDtoById(assetPayload.getAddedBy());
            if (userDTO.isEmpty()) {
                log.error("Add Assert Service Throwing UserNotFoundException User Id{}", assetPayload.getAddedBy());
                throw new UserNotFoundException("User Not Found");
            }
            Optional<Company> company = companyRepo.findById(assetPayload.getCompanyId());
            if (company.isEmpty()) {
                log.error("Add Assert Service Throwing CompanyNotFoundException CompanyId{}", assetPayload.getCompanyId());
                throw new CompanyNotFoundException("Company Not Found");
            }
            if (assetPayload.getRecoverable() && assetPayload.getAssetItems() == null) {
                log.error("Add Assert Service Throwing AssetsItemsNotFoundException ");
                throw new AssetsItemsNotFoundException("Assets items Not Found");
            }
            if(assetPayload.getRecoverable()&&assetPayload.getAssetItems().size()!=assetPayload.getCount())
            {
                log.error("Add Assert Service Throwing CountNotMatchException ");
                throw new CountNotMatchException("Please enter valid count");
            }
            Long lastGroupId = getLastCreatedGroupId(assetPayload.getCompanyId());
            Long newGroupId = lastGroupId != null ? lastGroupId + 1 : 1;
            log.info("Add asset Service Called , assert {}", assetPayload);
            List<Assets> assetsList = new ArrayList<>();
            if(assetPayload.getRecoverable()) {
                assetPayload.setCount(1L);
            }
            if (assetPayload.getRecoverable() && !assetPayload.getAssetItems().isEmpty()) {
                for (AssetItemsPayload itemsPayload : assetPayload.getAssetItems()) {
                    Assets asset = getAssets(assetPayload, itemsPayload, newGroupId);
                    assetsList.add(asset);
                    log.info("Add asset Service Called, asset {}", asset);
                }
            } else {
                Assets asset = getAssets(assetPayload, null, newGroupId);
                assetsList.add(asset);
                log.info("Add asset Service Called, asset {}", asset);
            }
            return getResponse(assetRepo.saveAll(assetsList));
        } catch (Exception e) {
            log.error("Exception In Add Asset Service Exception {}", e.getMessage());
            throw e;
        }
    }

    private static Assets getAssets(AssetPayload assetPayload, AssetItemsPayload itemsPayload, Long newGroupId) {
        Assets asset = new Assets();
        asset.setName(assetPayload.getName());
        asset.setCount(assetPayload.getCount());
        asset.setVendor(assetPayload.getVendor());
        asset.setRecoverable(assetPayload.getRecoverable());
        asset.setProperties(assetPayload.getProperties());
        asset.setCreatedAt(assetPayload.getCreatedAt());
        asset.setUpdatedAt(assetPayload.getUpdatedAt());
        asset.setAddedBy(assetPayload.getAddedBy());
        asset.setCompanyId(assetPayload.getCompanyId());
        asset.setCategories(assetPayload.getCategories());
        asset.setGroupId(newGroupId);
        if (itemsPayload != null) {
            asset.setSerialId(itemsPayload.getSerialId());
            asset.setStatus(itemsPayload.getStatus());
        }
        return asset;
    }


    public List<AssetResponsePayload> getResponse(List<Assets> assetList) {
        List<AssetResponsePayload> assetPayloads = new ArrayList<>();
        for (Assets asset : assetList) {
            AssetResponsePayload assetPayload = new AssetResponsePayload();
            assetPayload.setName(asset.getName());
            assetPayload.setCount(asset.getCount());
            assetPayload.setVendor(asset.getVendor());
            assetPayload.setRecoverable(asset.getRecoverable());
            assetPayload.setProperties(asset.getProperties());
            assetPayload.setCreatedAt(asset.getCreatedAt());
            assetPayload.setUpdatedAt(asset.getUpdatedAt());
            Optional<UserDTO> userDTO = userRepo.findUserDtoById(asset.getAddedBy());
            assetPayload.setAddedBy(userDTO.get());
            assetPayload.setCompanyId(asset.getCompanyId());
            assetPayload.setCategory(categoryRepo.findByCategoryPayloadById(asset.getCategories().getId()).get());
            AssetItemsPayload itemsPayload = new AssetItemsPayload();
            itemsPayload.setSerialId(asset.getSerialId());
            itemsPayload.setStatus(asset.getStatus());
            assetPayload.setAssetItems(Collections.singletonList(itemsPayload));
            assetPayloads.add(assetPayload);
        }
        return assetPayloads;
    }

    private Long getLastCreatedGroupId(Long companyId) {
        List<Long> ids = assetRepo.findTopGroupIdByCompanyId(companyId);
        if (ids.isEmpty()) {
            return null;
        } else {
            return ids.get(0);
        }
    }

    /**
     * For  Getting Assets  By CompanyId
     *
     * @return List<AssetResponsePayload>
     */
    public List<AssetResponsePayload> getAssetsByCompanyId(Long companyId) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            Optional<List<Assets>> optionalAssets = assetRepo.findByCompanyId(companyId);
            if (optionalAssets.get().isEmpty()) {
                throw new AssetsNotFoundException("Assets Not Found");
            }
            return getResponse(optionalAssets.get());
        } catch (Exception e) {
            log.error("UUID {} Exception In Get Assets By CompanyId Service Exception {}", debugUuid, e.getMessage());
            throw e;
        }
    }


    /**
     * For  Updating Asset
     *
     * @return Boolean
     */
    public Boolean updateAsset(UpdateAssetsPayload assetPayload) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Update asset Service Called , UUID {}", debugUuid);
            Optional<List<Assets>> optionalAssets = assetRepo.findByGroupIdAndCompanyId(assetPayload.getGroupId(), assetPayload.getCompanyId());
            if (optionalAssets.get().isEmpty()) {
                log.error("Update Asset Throwing AssetsNotFoundException {}",debugUuid);
                throw new AssetsNotFoundException("Assets not found");
            } else {
                Assets existingAssets = optionalAssets.get().get(0);
                if (existingAssets.getRecoverable()) {
                    assetPayload.setCount(existingAssets.getCount());
                }
            }
            Optional<Categories> categories=categoryRepo.findById(assetPayload.getCategories().getId());
            if(categories.isEmpty())
            {
                log.error("Update Asset Throwing CategoryNotFoundException {}",debugUuid);
                throw new CategoryNotFoundException("Category not found");
            }
            updateAssetByGroupIdAndCompanyId(assetPayload);
            return true;
        }
        catch (Exception e) {
            log.error("Exception In Update Asset Service Exception {}", e.getMessage());
            throw e;
        }
    }


    private void updateAssetByGroupIdAndCompanyId(UpdateAssetsPayload assetPayload) {
        assetRepo.updateAssets(assetPayload.getName(), assetPayload.getVendor(),
                assetPayload.getProperties(), assetPayload.getCount(), assetPayload.getCategories().getId()
                , assetPayload.getGroupId(), assetPayload.getCompanyId());
    }



    /**
     * For  Deleting  Assets
     *
     * @return String
     */
    public String deleteAssets(Long assetId) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Delete asset Service Called , UUID {}", debugUuid);
            Optional<Assets> optionalAssets = assetRepo.findById(assetId);
            if(optionalAssets.isEmpty())
            {
                log.error("UUID {}  Delete Assets Service DataIntegrityViolationException", debugUuid);
                throw new AssetsNotFoundException("Asset Not Found With Id "+assetId);
            }
           List<AssignedAssets> assignedAssetsList = assignedAssetRepo.findByAssetIdAndRecovered(assetId,false);
            if(assignedAssetsList.isEmpty())
            {
                assetRepo.deleteById(assetId);
                return  "Asset deleted Successfully";
            }
            else {
                log.error("UUID {}  Delete Assets Service DataIntegrityViolationException", debugUuid);
                throw new DataIntegrityException("Cannot Delete Assigned Assets..");
            }

        }
        catch (DataIntegrityViolationException e) {
            log.error("UUID {}  Delete Assets Service DataIntegrityViolationException {}", debugUuid, e.getMessage());
            throw new DataIntegrityException("Cannot delete assets");
        }
        catch (Exception e) {
            log.error("Exception In Delete Category Service Exception {}", e.getMessage());
            throw e;
        }
    }
}