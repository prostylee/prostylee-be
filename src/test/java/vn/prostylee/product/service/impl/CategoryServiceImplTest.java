package vn.prostylee.product.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.dto.filter.CategoryFilter;
import vn.prostylee.product.dto.request.AttributeOptionRequest;
import vn.prostylee.product.dto.request.AttributeRequest;
import vn.prostylee.product.dto.request.CategoryRequest;
import vn.prostylee.product.dto.response.CategoryResponse;
import vn.prostylee.product.entity.Attribute;
import vn.prostylee.product.entity.AttributeOption;
import vn.prostylee.product.entity.Category;
import vn.prostylee.product.repository.AttributeOptionRepository;
import vn.prostylee.product.repository.AttributeRepository;
import vn.prostylee.product.repository.CategoryRepository;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AttributeRepository attributeRepository;

    @Mock
    private AttributeOptionRepository attrOptionRepository;

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
    void findAllShouldReturnPageWithEmptyRecordWhenCategoryIsEmpty() {
        CategoryFilter categoryFilter = new CategoryFilter();
        categoryFilter.setName("hello");
        when(this.baseFilterSpecs.page(categoryFilter)).thenReturn(pageable);
        when(this.categoryRepository.findAllActive(pageable)).thenReturn(Page.empty());

        Page<CategoryResponse> categoryPage = categoryService.findAll(categoryFilter);

        assertEquals(0, categoryPage.getSize());
        verify(this.categoryRepository, times(1)).findAllActive(pageable);
    }

    @Test
    void findAllShouldReturnPageWithCorrespondingRecordWhenAlCategoryAreActive() {
        CategoryFilter categoryFilter = new CategoryFilter();
        categoryFilter.setName("hello");
        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder().name("Category 1").order(1).active(true).build());
        categories.add(Category.builder().name("Category 2").order(2).active(true).build());
        Page<Category> categoryPage = new PageImpl<>(categories);
        when(this.baseFilterSpecs.page(categoryFilter)).thenReturn(pageable);
        when(this.categoryRepository.findAllActive(pageable)).thenReturn(categoryPage);
        Page<CategoryResponse> categoryResponses = categoryService.findAll(categoryFilter);
        assertEquals(2, categoryResponses.getSize());
        assertThat(categoryResponses, contains(
                hasProperty("active", is(true)),
                hasProperty("active", is(true))
        ));
        verify(this.categoryRepository, times(1)).findAllActive(pageable);
    }

    @Test
    void findAllShouldOnlyReturnPageWithActiveRecordWhenAlCategoryHaveBothActiveAndDeActiveRecord() {
        CategoryFilter categoryFilter = new CategoryFilter();
        categoryFilter.setName("hello");
        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder().name("Category 1").deletedAt(new Date()).order(1).active(true).build());
        categories.add(Category.builder().name("Category 2").deletedAt(null).order(1).order(2).active(true).build());
        Page<Category> categoryPage = new PageImpl<>(categories);
        when(this.baseFilterSpecs.page(categoryFilter)).thenReturn(pageable);
        when(this.categoryRepository.findAllActive(pageable)).thenReturn(categoryPage);
        Page<CategoryResponse> categoryResponses = categoryService.findAll(categoryFilter);
        assertEquals(2, categoryResponses.getSize());
        assertThat(categoryResponses, contains(
                hasProperty("active", is(true)),
                hasProperty("active", is(true))
        ));
        verify(this.categoryRepository, times(1)).findAllActive(pageable);
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
        Set<AttributeRequest> attributeRequests = new HashSet<>();
        attributeRequests.add(BeanUtil.copyProperties(this.mockAttribute(), AttributeRequest.class));
        request.setAttributes(attributeRequests);
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
        Set<AttributeRequest> attributeRequests = new HashSet<>();
        AttributeRequest attributeRequest = BeanUtil.copyProperties(this.mockAttribute(), AttributeRequest.class);
        attributeRequest.setAttributeOptions(attributeOptionRequests);
        attributeRequests.add(attributeRequest);
        request.setAttributes(attributeRequests);
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
    void updateShouldReturnCategoryRecordOnlyWhenGivenRequestWithoutAttributeAndAttributeOption() {
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
    void updateShouldReturnCategoryAndAttributeRecordOnlyWhenGivenRequestWithAttribute() {
        CategoryRequest request = CategoryRequest.builder().name("Category 1").languageCode("vi").order(1).build();
        Set<AttributeRequest> attributeRequests = new HashSet<>();
        attributeRequests.add(BeanUtil.copyProperties(this.mockAttribute(), AttributeRequest.class));
        request.setAttributes(attributeRequests);
        Category mockCategory = this.mockCategory();
        mockCategory.setAttributes(this.mockAttribute());
        when(this.categoryRepository.findOneActive(1L)).thenReturn(Optional.of(mockCategory));
        when(this.categoryRepository.save(Mockito.any())).thenReturn(mockCategory());
        when(this.attributeRepository.findAllById(Mockito.any())).thenReturn(new ArrayList<>(this.mockAttribute()));
        CategoryResponse response = this.categoryService.update(1L, request);
        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getLanguageCode(), response.getLanguageCode());
        //assertNotNull(response.getAttributes());
        verify(this.categoryRepository, times(1)).save(mockCategory);
    }

    @Test
    void updateShouldReturnAllCategoryAndAttributeRelationshipRecordOnlyWhenGivenRequestWithAttributeAndOption() {
        CategoryRequest request = CategoryRequest.builder().name("Category 1").languageCode("vi").order(1).build();
        Set<AttributeOptionRequest> attributeOptionRequests = new HashSet<>();
        attributeOptionRequests.add(BeanUtil.copyProperties(this.mockAttributeOption(), AttributeOptionRequest.class));
        Set<AttributeRequest> attributeRequests = new HashSet<>();
        AttributeRequest attributeRequest = BeanUtil.copyProperties(this.mockAttribute(), AttributeRequest.class);
        attributeRequest.setAttributeOptions(attributeOptionRequests);
        attributeRequests.add(attributeRequest);
        request.setAttributes(attributeRequests);
        Category mockCategory = this.mockCategory();
        mockCategory.setAttributes(this.mockAttribute());
        mockCategory.setAttributes(this.mockAttribute());
        when(this.categoryRepository.findOneActive(1L)).thenReturn(Optional.of(mockCategory));
        when(this.categoryRepository.save(Mockito.any())).thenReturn(mockCategory());
        when(this.attributeRepository.findAllById(Mockito.any())).thenReturn(new ArrayList<>(this.mockAttribute()));

        CategoryResponse response = this.categoryService.update(1L, request);

        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getLanguageCode(), response.getLanguageCode());
        //assertNotNull(response.getAttributes());
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

    private Set<AttributeOption> mockAttributeOption() {
        Set<AttributeOption> attributeOptions = new HashSet<>();
        attributeOptions.add(AttributeOption.builder().id(1L).name("S").label("S").languageCode("vi").build());
        return attributeOptions;
    }
}
