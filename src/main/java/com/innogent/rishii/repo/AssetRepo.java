package com.innogent.rishii.repo;

import com.innogent.rishii.entities.Assets;
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
public interface AssetRepo extends JpaRepository<Assets, Long> {

    @Query("SELECT a.groupId FROM Assets a WHERE a.companyId = ?1 ORDER BY a.groupId DESC")
    List<Long> findTopGroupIdByCompanyId( Long companyId);

    Optional<List<Assets>> findByCompanyId(@Param("companyId") Long companyId);

    Optional<List<Assets>> findByGroupIdAndCompanyId(@Param("groupId") Long groupId,
                                                   @Param("companyId") Long companyId);
    @Modifying
    @Transactional
    @Query(value = "UPDATE assets SET name = :name, vendor = :vendor, properties = :properties, " +
            "count = :count, categories_category_id = :categoryId, updated_at = NOW() " +
            "WHERE group_id = :groupId AND company_id = :companyId", nativeQuery = true)
     void updateAssets(@Param("name") String name, @Param("vendor") String vendor,
                      @Param("properties") String properties, @Param("count") Long count,
                      @Param("categoryId") Long categoryId, @Param("groupId") Long groupId,
                      @Param("companyId") Long companyId);

    @Transactional
    public void deleteByGroupIdAndCompanyId(Long groupId,Long companyId);

    Optional<Assets> findByIdAndCompanyId(Long assetId,
                                         Long companyId);



}



