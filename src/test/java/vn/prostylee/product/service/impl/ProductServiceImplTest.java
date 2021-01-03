package vn.prostylee.product.service.impl;

import org.junit.Assert;
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
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.request.ProductRequest;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BaseFilterSpecs<Product> baseFilterSpecs;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findAllShouldReturnPageWithEmptyRecordWhenProductIsEmpty() {
        ProductFilter productFilter = new ProductFilter();
        Mockito.when(this.baseFilterSpecs.page(productFilter)).thenReturn(pageable);
        Mockito.when(this.productRepository.findAllActive(pageable)).thenReturn(Page.empty());
        Page<ProductResponse> productResponsePage = productService.findAll(productFilter);
        Assert.assertEquals(0, productResponsePage.getSize());
        Mockito.verify(this.productRepository, Mockito.times(1)).findAllActive(pageable);
    }

    @Test
    public void findAllShouldReturnPageWithCorrespondingRecordWhenAProductAreActive() {
        ProductFilter productFilter = new ProductFilter();
        List<Product> products = new ArrayList<>();
        products.add(Product.builder().name("Product 1").build());
        products.add(Product.builder().name("Product 2").build());
        Page<Product> productResponsePage = new PageImpl<>(products);
        Mockito.when(this.baseFilterSpecs.page(productFilter)).thenReturn(pageable);
        Mockito.when(this.productRepository.findAllActive(pageable)).thenReturn(productResponsePage);
        Page<ProductResponse> categoryResponses = productService.findAll(productFilter);
        Assert.assertEquals(2, categoryResponses.getSize());
        assertThat(categoryResponses, contains(
                hasProperty("name", is("Product 1")),
                hasProperty("name", is("Product 2"))
        ));
        Mockito.verify(this.productRepository, Mockito.times(1)).findAllActive(pageable);
    }

    @Test
    public void findAllShouldOnlyReturnPageWithActiveRecordWhenAlCategoryHaveBothActiveAndDeActiveRecord() {
        ProductFilter productFilter = new ProductFilter();
        List<Product> products = new ArrayList<>();
        products.add(Product.builder().name("Product 1").build());
        products.add(Product.builder().name("Product 2").build());
        Page<Product> productResponsePage = new PageImpl<>(products);
        Mockito.when(this.baseFilterSpecs.page(productFilter)).thenReturn(pageable);
        Mockito.when(this.productRepository.findAllActive(pageable)).thenReturn(productResponsePage);
        Page<ProductResponse> categoryResponses = productService.findAll(productFilter);
        Assert.assertEquals(2, categoryResponses.getSize());
        assertThat(categoryResponses, contains(
                hasProperty("name", is("Product 1")),
                hasProperty("name", is("Product 2"))
        ));
        Mockito.verify(this.productRepository, Mockito.times(1)).findAllActive(pageable);
    }

    @Test
    public void findByIdShouldThrowExceptionWhenProductIsNotFound() {
        try {
            Product product = Product.builder().name("Product 1").build();
            Mockito.when(this.productRepository.findOneActive(2L)).thenReturn(Optional.of(product));
            this.productService.findById(1L);
        } catch (ResourceNotFoundException e) {
            Assert.assertEquals("Product is not found with id [" + 1L + "]", e.getMessage());
        }
    }

    @Test
    public void findByIdShouldGivenCorrespondingRecordWhenProductAvailableInDatabase() {
        Long id = 2L;
        Product product = Product.builder().id(id).name("Product 1").build();
        Mockito.when(this.productRepository.findOneActive(id)).thenReturn(Optional.of(product));
        ProductResponse response = this.productService.findById(id);
        Assert.assertNotNull(response);
        Assert.assertEquals(Optional.of(id).get(), response.getId());
        Mockito.verify(this.productRepository, Mockito.times(1)).findOneActive(id);
    }

    @Test
    public void saveShouldReturnCategoryRecordOnlyWhenGivenRequestWithoutAttributeAndAttributeOption() {
        ProductRequest request = ProductRequest.builder().name("Category 1").build();
        Mockito.when(this.productRepository.saveAndFlush(Mockito.any()  )).thenReturn((mockProduct()));
        ProductResponse response = this.productService.save(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(request.getName(), response.getName());
        Mockito.verify(this.productRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
    }


    @Test
    public void updateShouldThrowExceptionWhenProductIsNotFound() {
        try {
            ProductRequest request = ProductRequest.builder().name("Category 1").build();
            Mockito.when(this.productRepository.findOneActive(1L)).thenReturn(Optional.of(mockProduct()));
            this.productService.update(2L, request);
        } catch (ResourceNotFoundException e) {
            Assert.assertEquals("Product is not found with id [" + 2L + "]", e.getMessage());
        } finally {
            Mockito.verify(this.productRepository, Mockito.never()).save(mockProduct());
        }
    }

    @Test
    public void deleteCannotDeleteProductWhenIdIsNotFound() {
        try {
            Mockito.doThrow(EmptyResultDataAccessException.class).when(this.productRepository).softDelete(1L);
            this.productService.deleteById(1L);
        } catch (ResourceNotFoundException e) {
            Assert.assertEquals("Product is not found with id [" + 1L + "]", e.getMessage());
        }
    }
    @Test
    public void deleteShouldDeleteProductSuccessful() {
        Mockito.when(this.productRepository.softDelete(1l)).thenReturn(1);
        Assert.assertEquals(true, this.productService.deleteById(1L));
        Mockito.verify(this.productRepository, Mockito.times(1)).softDelete(1L);
    }


    private Product mockProduct() {
        return Product.builder().id(1L).name("Category 1").build();
    };
}
