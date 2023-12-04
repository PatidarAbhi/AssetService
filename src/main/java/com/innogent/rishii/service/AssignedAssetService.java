package com.innogent.rishii.service;
import com.innogent.rishii.customException.*;
import com.innogent.rishii.dtos.*;
import com.innogent.rishii.entities.Assets;
import com.innogent.rishii.entities.AssignedAssets;
import com.innogent.rishii.entities.Company;
import com.innogent.rishii.repo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AssignedAssetService {

    private static final Logger log = LoggerFactory.getLogger(AssignedAssetService.class);

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private AssignedAssetRepo assignedAssetRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private AssetService assetService;


    /**
     * For  Assign Asset
     *
     * @return List<AssignedAssetResponsePayload>
     */
    public List<AssignedAssetResponsePayload> assignedAsset(AssignedAssetPayload assignedAssetPayload) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Assigned asset Service Called , UUID {}", debugUuid);
            Optional<Assets> assets = assetRepo.findByIdAndCompanyId(assignedAssetPayload.getAssetId(),
                    assignedAssetPayload.getCompanyId());
            List<AssignedAssets> assignedAssetsList = new ArrayList<>();
            if (assets.isEmpty()) {
                log.error("Assigned Asset Service Throwing AssetsNotFoundException");
                throw new AssetsNotFoundException("Asset not Found");
            }
            Optional<Company> company = companyRepo.findById(assignedAssetPayload.getCompanyId());
            if (company.isEmpty()) {
                log.error("Assigned Asset Service Throwing CompanyNotFoundException CompanyId{}", assignedAssetPayload.getCompanyId());
                throw new CompanyNotFoundException("Company Not Found");
            }
            if (assets.get().getCount() == 0) {
                log.error("Assigned Asset Service Throwing AssetsAlreadyAssigned");
                throw new AssetsNotFoundException("Asset already assigned");
            }
            if (assets.get().getCount() < assignedAssetPayload.getAssignedTo().size()) {
                log.error("Assigned Asset Service Throwing Insufficient Assets");
                throw new AssetsNotFoundException("Insufficient assets available for assignment. Please check the available asset count and try again.");
            }
            if (!assets.get().getRecoverable()) {
                for (long userId : assignedAssetPayload.getAssignedTo()) {
                    if (userRepo.findUserDtoById(userId).isEmpty()) {
                        log.error("Assigned Asset Service Throwing UserNotFoundException");
                        throw new UserNotFoundException("User Not Found With Id " + userId);
                    }
                    AssignedAssets asset = new AssignedAssets();
                    asset.setAssignedAt(LocalDateTime.now());
                    asset.setAssignedBy(assignedAssetPayload.getAssignedBy());
                    asset.setCompanyId(assignedAssetPayload.getCompanyId());
                    asset.setRecovered(false);
                    asset.setAssetId(assignedAssetPayload.getAssetId());
                    asset.setAssignedTo(userId);
                    AssignedAssets savedAssignedAsset = assignedAssetRepo.save(asset);
                    assets.get().setCount(assets.get().getCount() - 1);
                    assets.get().setStatus("Assigned");
                    assetRepo.save(assets.get());
                    assignedAssetsList.add(savedAssignedAsset);
                }
            } else {
                if (userRepo.findUserDtoById(assignedAssetPayload.getAssignedTo().get(0)).isEmpty()) {
                    log.error("Assigned Asset Service Throwing UserNotFoundException");
                    throw new UserNotFoundException("User Not Found With Id " + assignedAssetPayload.getAssignedTo().get(0));
                }
                AssignedAssets asset = new AssignedAssets();
                asset.setAssignedAt(LocalDateTime.now());
                asset.setAssignedBy(assignedAssetPayload.getAssignedBy());
                asset.setCompanyId(assignedAssetPayload.getCompanyId());
                asset.setRecovered(false);
                asset.setAssetId(assignedAssetPayload.getAssetId());
                asset.setAssignedTo(assignedAssetPayload.getAssignedTo().get(0));
                assets.get().setCount(assets.get().getCount() - 1);
                assets.get().setStatus("Assigned");
                assetRepo.save(assets.get());
                AssignedAssets savedAssignedAsset = assignedAssetRepo.save(asset);
                assignedAssetsList.add(savedAssignedAsset);
            }
            log.info("Assigned Asset Service Returning AssignedAssetResponsePayloads");
            return getAssignedAssetResponseList(assignedAssetsList);
        } catch (Exception e) {
            log.error("Exception In Assigned Asset Service Exception {}", e.getMessage());
            throw e;
        }
    }


    private List<AssignedAssetResponsePayload> getAssignedAssetResponseList(List<AssignedAssets> assignedAssetsList) {
        List<AssignedAssetResponsePayload> responsePayloads = new ArrayList<>();
        for (AssignedAssets assignedAsset : assignedAssetsList) {
            AssignedAssetResponsePayload responsePayload = new AssignedAssetResponsePayload();
            responsePayload.setId(assignedAsset.getId());
            responsePayload.setAssignedBy(userRepo.findUserDtoById(assignedAsset.getAssignedBy()).get());
            responsePayload.setRecovered(assignedAsset.getRecovered());
            responsePayload.setCompanyId(assignedAsset.getCompanyId());
            responsePayload.setRecoveredBy(userRepo.findUserDtoById(assignedAsset.getRecoveredBy()).orElse(null));
            responsePayload.setAssignedAt(assignedAsset.getAssignedAt());
            responsePayload.setAssignedTo(userRepo.findUserDtoById(assignedAsset.getAssignedTo()).orElse(null));
            responsePayload.setRecoveredAt(assignedAsset.getRecoveredAt());
            AssetResponsePayload assetResponsePayload = assetService.getResponse(Collections.singletonList(assetRepo.findById(assignedAsset.getAssetId()).orElseThrow(() -> new AssetsNotFoundException("Asset not Found")))).get(0);
            responsePayload.setAssetId(assetResponsePayload);
            responsePayloads.add(responsePayload);
        }

        log.info("Assigned Asset Service Returning AssignedAssetResponsePayloads {}", responsePayloads);
        return responsePayloads;
    }


    /**
     * For  Getting Assigned Asset
     *
     * @return List<AssignedAssetResponsePayload>
     */
    public List<AssignedAssetResponsePayload> getMyAssignedAsset(Long userId) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Get Assigned Asset By UserID Service Called , UUID {}", debugUuid);
            Optional<UserDTO> userDto = userRepo.findUserDtoById(userId);
            if (userDto.isEmpty()) {
                log.info("Get Assigned Asset By UserID Service Throw UserNotFoundException , UserId {}", userId);
                throw new UserNotFoundException("User Not Found..");
            }
            return getAssignedAssetResponseList(assignedAssetRepo.findByAssignedToAndRecovered(userId, false));
        } catch (Exception e) {
            log.error("Exception In Get Assigned Asset By UserID Service Exception {}", e.getMessage());
            throw e;
        }
    }


    /**
     * For  Getting Assigned Asset
     *
     * @return List<AssignedAssetResponsePayload>
     */
    public List<AssignedAssetResponsePayload> getAssignedAssetOfCompany(Long companyId) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Get Assigned Asset By CompanyId Service Called , UUID {}", debugUuid);
            Optional<Company> company = companyRepo.findById(companyId);
            if (company.isEmpty()) {
                log.info("Get Assigned Asset By CompanyId Service Throw CompanyNotFoundException , CompanyId {}", companyId);
                throw new CompanyNotFoundException("Company Not Found..");
            }
            List<AssignedAssetResponsePayload> assignedAssetResponsePayloads = getAssignedAssetResponseList
                    (assignedAssetRepo.findByCompanyIdAndRecovered(companyId, false));
            if (assignedAssetResponsePayloads.isEmpty()) {
                throw new AssetsNotFoundException("Assets Not Found");
            }
            return assignedAssetResponsePayloads;
        } catch (Exception e) {
            log.error("Exception In Get Assigned Asset By CompanyId Service Exception {}", e.getMessage());
            throw e;
        }
    }

    /**
     * For  Recover Assigned Asset
     *
     * @return Boolean
     */
    public Boolean recoverAssignedAsset(Long assignedAssetId, Long recoveredBy) {

        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Recover Assigned Asset By AssignedAssetId Service Called , UUID {}", debugUuid);
            Optional<AssignedAssets> assignedAsset = assignedAssetRepo.findById(assignedAssetId);
            if (assignedAsset.isEmpty()) {
                log.info("Recover Assigned Asset By AssignedAssetIdService Throw AssignedAssetNotFoundException , assignedAssetId {}",
                        assignedAssetId);
                throw new AssignedAssetNotFoundException("Assigned Asset Not Found..");
            }
            if (assignedAsset.get().getRecovered()) {
                log.info("Recover Assigned Asset By AssignedAssetIdService Throw NotRecoverableException , assignedAssetId {}",
                        assignedAssetId);
                throw new NotRecoverableException("Asset Already Recovered..");
            }
            Optional<Assets> asset = assetRepo.findById(assignedAsset.get().getAssetId());
            if (asset.isEmpty()) {
                log.info("Recover Assigned Asset By AssignedAssetIdService Throw AssetsNotFoundException , assignedAssetId {}",
                        assignedAssetId);
                throw new AssetsNotFoundException("Asset Not Found..");
            }
            Assets assets = asset.get();
            if (!asset.get().getRecoverable()) {
                log.info("Recover Assigned Asset By AssignedAssetIdService Throw AssetsNotFoundException , Recoverable {}",
                        asset.get().getRecoverable());
                throw new NotRecoverableException("Can Not Recovered A Non Recoverable Asset");
            }
            assets.setCount(assets.getCount() + 1);
            assets.setStatus("Recovered");
            assetRepo.save(assets);

            AssignedAssets assignedAssets = assignedAsset.get();
            assignedAssets.setRecovered(true);
            assignedAssets.setRecoveredAt(LocalDateTime.now());
            log.info("Recovered by {} ", recoveredBy);
            assignedAssets.setRecoveredBy(recoveredBy);
            assignedAssetRepo.save(assignedAssets);
            return true;
        } catch (Exception e) {
            log.error("Exception In Get Assigned Asset By CompanyId Service Exception {}", e.getMessage());
            throw e;
        }
    }
}