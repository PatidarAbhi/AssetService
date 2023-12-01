package com.innogent.rishii.controller;
import com.innogent.rishii.customException.*;
import com.innogent.rishii.dtos.CategoryResponsePayload;
import com.innogent.rishii.entities.Categories;
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
@RequestMapping("/categories")
public class CategoryController {
    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    /**
     * For  Adding category
     *
     * @return ResponseEntity
     */
    @PostMapping("/")
    public ResponseEntity<?> addCategory(@Valid @RequestBody Categories category) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Create Category API Called, UUID {}", debugUuid);
            CategoryResponsePayload savedCategory= categoryService.addCategories(category);
            return new ResponseEntity<>(new CustomResponse<>(true,"Category created successfully",savedCategory), HttpStatus.CREATED);
        }
        catch (DuplicateNameException e) {
            log.error("UUID {}, DuplicateNameException in Add Category API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.NOT_FOUND);
        }
        catch (UserNotFoundException e) {
            log.error("UUID {}, UserNotFoundException in Add Category API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.NOT_FOUND);
        }
        catch (CompanyNotFoundException e) {
            log.error("UUID {}, CompanyNotFoundException in Add Category  API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            log.error("UUID {} Exception In Create Category API Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * For  Getting Assets Category By CompanyId
     *
     * @return ResponseEntity
     */
    @GetMapping("/{companyId}")
    public ResponseEntity<?> getAssetsCategoryByCompanyId(@Valid @PathVariable("companyId") Long companyId) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Get Assets Category By CompanyId API Called, UUID {}", debugUuid);
            List<CategoryResponsePayload> categoriesList= categoryService.getAssetsCategoryByCompanyId(companyId);
            return new ResponseEntity<>(new CustomResponse<>(true,"Category fetched",categoriesList), HttpStatus.OK);
        } catch (Exception e) {
            log.error("UUID {} Exception In Get Assets Category By CompanyId API Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * For  Deleting  category
     *
     * @return ResponseEntity
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@Valid @PathVariable("categoryId") Long categoryId) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Delete Category By CategoryId API Called, UUID {}", debugUuid);
            String status= categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(new CustomResponse<>(true,status,null), HttpStatus.OK);
        }
        catch (CategoryNotFoundException e) {
            log.error("UUID {}  Delete Category By CategoryId API CategoryNotFoundException {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
        catch (DataIntegrityViolationException e) {
            log.error("UUID {}  Delete Category By CategoryId API DataIntegrityViolationException {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            log.error("UUID {} Exception Delete Category By CategoryId API Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false,e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
    }
}
