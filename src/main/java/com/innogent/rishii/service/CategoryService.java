package com.innogent.rishii.service;
import com.innogent.rishii.customException.*;
import com.innogent.rishii.dtos.AssetItemsPayload;
import com.innogent.rishii.dtos.AssetResponsePayload;
import com.innogent.rishii.dtos.CategoryResponsePayload;
import com.innogent.rishii.dtos.UserDTO;
import com.innogent.rishii.entities.Assets;
import com.innogent.rishii.entities.Categories;
import com.innogent.rishii.entities.Company;
import com.innogent.rishii.repo.CategoryRepo;
import com.innogent.rishii.repo.CompanyRepo;
import com.innogent.rishii.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CompanyRepo companyRepo;


    /**
     * For  Adding category
     *
     * @return Categories
     */
    public CategoryResponsePayload addCategories(Categories categories) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Add Category Service Called , UUID {}", debugUuid);
            categories.setCreatedAt(LocalDateTime.now());
            categories.setUpdatedAt(LocalDateTime.now());
            Optional<UserDTO> userDTO= userRepo.findUserDtoById(categories.getCreatedBy());
            Optional<Categories> optionalCategories=categoryRepo.findByNameAndCompanyId(categories.getName(),categories.getCompanyId());
            if(optionalCategories.isPresent())
            {
                log.error("Add Category Service Throwing DuplicateNameException name Id{}" ,categories.getName());
                throw new DuplicateNameException("Category with name "+categories.getName()+" is already present in company");
            }
            if(userDTO.isEmpty())
            {
                log.error("Add Category Service Throwing UserNotFoundException User Id{}" ,categories.getCreatedBy());
                throw new UserNotFoundException("User Not Found");
            }
            Optional<Company> company=companyRepo.findById(categories.getCompanyId());
            if(company.isEmpty())
            {
                log.error("Add Category Service Throwing CompanyNotFoundException CompanyId{}" ,categories.getCompanyId());
                throw new CompanyNotFoundException("Company Not Found");
            }
            log.info("Add Category Service Called , category {}", categories);
            Categories savedCategory= categoryRepo.save(categories);
            CategoryResponsePayload categoryResponsePayload=new CategoryResponsePayload();
            categoryResponsePayload.setId(savedCategory.getId());
            categoryResponsePayload.setName(savedCategory.getName());
            categoryResponsePayload.setCompanyId(savedCategory.getCompanyId());
            categoryResponsePayload.setCreatedBy(userDTO.get());
            return categoryResponsePayload;
        } catch (Exception e) {
            log.error("Exception In Add Category Service Exception {}", e.getMessage());
            throw e;
        }
    }

    /**
     * For  Getting Assets Category By CompanyId
     *
     * @return List<Categories>
     */
    public List<CategoryResponsePayload> getAssetsCategoryByCompanyId(Long companyId) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Get Assets Category By CompanyId Service Called, UUID {}", debugUuid);
            return getResponse(categoryRepo.findByCompanyId(companyId));
        } catch (Exception e) {
            log.error("UUID {} Exception In Get Assets Category By CompanyId Service Exception {}", debugUuid, e.getMessage());
            throw e;
        }
    }

    /**
     * For  Deleting  category
     *
     * @return String
     */
    public String deleteCategory(Long categoryId) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Delete Category By CategoryId Service Called, UUID {}", debugUuid);
            Optional<Categories> categories=categoryRepo.findById(categoryId);
            if(categories.isEmpty())
            {
                log.error("Delete Category By CategoryId Service throw CategoryNotFoundException {}", debugUuid);
                throw new CategoryNotFoundException("Category Not Fond");
            }
             categoryRepo.deleteById(categoryId);
             return  "Category Deleted Successfully";
        }
        catch (DataIntegrityViolationException e) {
            log.error("UUID {}  Delete Category By CategoryId Service DataIntegrityViolationException {}", debugUuid, e.getMessage());
            throw new DataIntegrityException("Cannot delete category");
        }
        catch (Exception e) {
            log.error("UUID {} Exception In Delete  Category By CategoryId Service Exception {}", debugUuid, e.getMessage());
            throw e;
        }
    }
    private List<CategoryResponsePayload> getResponse(List<Categories> categoriesList) {
        List<CategoryResponsePayload> categoryResponsePayloads = new ArrayList<>();
        for (Categories category : categoriesList) {
            CategoryResponsePayload categoryResponsePayload = new CategoryResponsePayload();
            categoryResponsePayload.setId(category.getId());
            categoryResponsePayload.setName(category.getName());
            Optional<UserDTO> userDTO= userRepo.findUserDtoById(category.getCreatedBy());
            categoryResponsePayload.setCreatedBy(userDTO.get());
            categoryResponsePayload.setCompanyId(category.getCompanyId());
            categoryResponsePayloads.add(categoryResponsePayload);
        }
        return categoryResponsePayloads;
    }
}
