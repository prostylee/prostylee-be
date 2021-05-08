package vn.prostylee.product.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.dto.request.AttributeOptionRequest;
import vn.prostylee.product.dto.request.AttributeRequest;
import vn.prostylee.product.dto.request.CategoryRequest;
import vn.prostylee.product.dto.response.CategoryResponse;
import vn.prostylee.product.entity.Attribute;
import vn.prostylee.product.entity.AttributeOption;
import vn.prostylee.product.entity.Category;
import vn.prostylee.product.repository.AttributeRepository;
import vn.prostylee.product.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AttributeRepository attributeRepository;

    @Mock
    private BaseFilterSpecs<Category> baseFilterSpecs;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByIdShouldThrowExceptionWhenCategoryIsNotFound() {
        try {
            Category category = Category.builder().id(2L).name("Category").active(true).build();
            when(this.categoryRepository.findOneActive(2L)).thenReturn(Optional.of(category));
            this.categoryService.findById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("Category is not found with id [" + 1L + "]", e.getMessage());
        }
    }

    @Test
    void findByIdShouldGivenCorrespondingRecordWhenCategoryAvailableInDatabase() {
        Long id = 2L;
        Category category = Category.builder().id(id).name("Category").active(true).build();
        when(this.categoryRepository.findOneActive(id)).thenReturn(Optional.of(category));
        CategoryResponse response = this.categoryService.findById(id);
        assertNotNull(response);
        assertEquals(Optional.of(id).get(), response.getId());
        verify(this.categoryRepository, times(1)).findOneActive(id);
    }

    @Test
    void saveShouldReturnCategoryRecordOnlyWhenGivenRequestWithoutAttributeAndAttributeOption() {
        CategoryRequest request = CategoryRequest.builder().name("Category 1").languageCode("vi").order(1).build();
        when(this.categoryRepository.saveAndFlush(Mockito.any())).thenReturn((mockCategory()));
        CategoryResponse response = this.categoryService.save(request);
        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getLanguageCode(), response.getLanguageCode());
        assertNull(response.getAttributes());
        verify(this.categoryRepository, times(1)).saveAndFlush(Mockito.any());
    }

    @Test
    void saveShouldReturnCategoryAndAttributeRecordOnlyWhenGivenRequestWithAttribute() {
        CategoryRequest request = CategoryRequest.builder().name("Category 1").languageCode("vi").order(1).build();
        Set<Long> attributeRequests = new HashSet<>();
        attributeRequests.add(mockAttributeId());
        request.setAttributeIds(attributeRequests);
        Category mockCategory = this.mockCategory();
        mockCategory.setAttributes(this.mockAttribute());
        when(this.categoryRepository.saveAndFlush(Mockito.any())).thenReturn(mockCategory);
        CategoryResponse response = this.categoryService.save(request);
        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getLanguageCode(), response.getLanguageCode());
        assertNotNull(response.getAttributes());
        verify(this.categoryRepository, times(1)).saveAndFlush(Mockito.any());
    }

    @Test
    void saveShouldReturnAllCategoryAndAttributeRelationshipRecordOnlyWhenGivenRequestWithAttributeAndOption() {
        CategoryRequest request = CategoryRequest.builder().name("Category 1").languageCode("vi").order(1).build();
        Set<AttributeOptionRequest> attributeOptionRequests = new HashSet<>();
        attributeOptionRequests.add(BeanUtil.copyProperties(this.mockAttributeOption(), AttributeOptionRequest.class));
        Set<Long> attributeRequests = new HashSet<>();
        AttributeRequest attributeRequest = BeanUtil.copyProperties(this.mockAttribute(), AttributeRequest.class);
        // attributeRequest.setAttributeOptions(attributeOptionRequests);
        attributeRequests.add(mockAttributeId());
        request.setAttributeIds(attributeRequests);
        Category mockCategory = this.mockCategory();
        mockCategory.setAttributes(this.mockAttribute());
        when(this.categoryRepository.saveAndFlush(Mockito.any())).thenReturn(mockCategory);
        CategoryResponse response = this.categoryService.save(request);
        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getLanguageCode(), response.getLanguageCode());
        assertNotNull(response.getAttributes());
        verify(this.categoryRepository, times(1)).saveAndFlush(Mockito.any());
    }

    @Test
    void updateShouldThrowExceptionWhenCategoryIsNotFound() {
        try {
            CategoryRequest request = CategoryRequest.builder().name("Category 1").languageCode("vi").order(1).build();
            when(this.categoryRepository.findOneActive(1L)).thenReturn(Optional.of(mockCategory()));
            this.categoryService.update(2L, request);
        } catch (ResourceNotFoundException e) {
            assertEquals("Category is not found with id [" + 2L + "]", e.getMessage());
        } finally {
            verify(this.categoryRepository, Mockito.never()).save(mockCategory());
        }
    }

    @Test
    void updateShouldReturnCategoryRecordOnlyWhenGivenRequest() {
        CategoryRequest request = CategoryRequest.builder().name("Category 1").languageCode("vi").order(1).build();
        Category mockCategory = this.mockCategory();
        when(this.categoryRepository.findOneActive(1L)).thenReturn(Optional.of(mockCategory));
        when(this.categoryRepository.save(Mockito.any())).thenReturn(mockCategory());
        when(this.attributeRepository.findAllById(Mockito.any())).thenReturn(new ArrayList<>(this.mockAttribute()));
        CategoryResponse response = this.categoryService.update(1L, request);
        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getLanguageCode(), response.getLanguageCode());
        assertNull(response.getAttributes());
        verify(this.categoryRepository, times(1)).save(mockCategory);
    }

    @Test
    void deleteCannotDeleteCategoryWhenIdIsNotFound() {
        try {
            Mockito.doThrow(EmptyResultDataAccessException.class).when(this.categoryRepository).softDelete(1L);
            this.categoryService.deleteById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("Category is not found with id [" + 1L + "]", e.getMessage());
        }
    }
    @Test
    void deleteShouldDeleteCategorySuccessful() {
        when(this.categoryRepository.softDelete(1l)).thenReturn(1);
        assertEquals(true, this.categoryService.deleteById(1L));
        verify(this.categoryRepository, times(1)).softDelete(1L);
    }


    private Category mockCategory() {
        return Category.builder().id(1L).name("Category 1").languageCode("vi").order(1).build();
    };

    private Set<Attribute> mockAttribute() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(Attribute.builder().id(1L).key("size 1").label("size").languageCode("vi").build());
        return attributes;
    }

    private Long mockAttributeId() {
        return 1L;
    }

    private Set<AttributeOption> mockAttributeOption() {
        Set<AttributeOption> attributeOptions = new HashSet<>();
        attributeOptions.add(AttributeOption.builder().id(1L).name("S").label("S").languageCode("vi").build());
        return attributeOptions;
    }
}
