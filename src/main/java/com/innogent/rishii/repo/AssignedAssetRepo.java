package com.innogent.rishii.repo;

import com.innogent.rishii.entities.Assets;
import com.innogent.rishii.entities.AssignedAssets;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignedAssetRepo extends JpaRepository<AssignedAssets, Long> {

    List<AssignedAssets> findByAssignedToAndRecovered(Long assignedTo,Boolean recovered);

    List<AssignedAssets> findByCompanyIdAndRecovered(Long companyId,Boolean recovered);

    List<AssignedAssets> findByAssetIdAndRecovered(Long assetId,Boolean recovered);


}



