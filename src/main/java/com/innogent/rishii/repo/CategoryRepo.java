package com.innogent.rishii.repo;

import com.innogent.rishii.dtos.CategoryPayload;
import com.innogent.rishii.dtos.UserDTO;
import com.innogent.rishii.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepo  extends JpaRepository<Categories, Long> {
    List<Categories> findByCompanyId(Long CompanyId);

    Optional<Categories> findByNameAndCompanyId(String name,Long companyId);

    @Query("SELECT NEW com.innogent.rishii.dtos.CategoryPayload(u.id, u.name) " +
            "FROM Categories u WHERE u.id = :categoryId")
    Optional<CategoryPayload> findByCategoryPayloadById(Long categoryId);
}
