package vn.prostylee.product.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.converter.ProductAttributeConverter;
import vn.prostylee.product.converter.ProductPriceConverter;
import vn.prostylee.product.dto.request.ProductAttributeRequest;
import vn.prostylee.product.dto.request.ProductPriceRequest;
import vn.prostylee.product.dto.response.ProductPriceResponse;
import vn.prostylee.product.entity.ProductAttribute;
import vn.prostylee.product.entity.ProductPrice;
import vn.prostylee.product.repository.ProductPriceRepository;
import vn.prostylee.product.service.ProductPriceService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class ProductPriceServiceImplTest {

    @Mock
    private ProductPriceRepository productPriceRepository;

    private ProductPriceService productPriceService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ProductAttributeConverter productAttributeConverter = new ProductAttributeConverter();
        ProductPriceConverter productPriceConverter = new ProductPriceConverter(productAttributeConverter);
        this.productPriceService = new ProductPriceServiceImpl(productPriceRepository, productPriceConverter);
    }

    @Test
    void findByIdShouldThrowExceptionWhenProductPriceIsNotFound() {
        try {
            when(this.productPriceRepository.findById(2L)).thenReturn(Optional.empty());
            this.productPriceService.findById(2L);
        } catch (ResourceNotFoundException e) {
            assertEquals("Product price is not found with id [" + 2L + "]", e.getMessage());
        }
    }

    @Test
    void findByIdShouldGivenCorrespondingRecordWhenProductPriceAvailableInDatabase() {
        Long id = 1L;
        ProductPrice productPrice = mockProductPrice();
        when(this.productPriceRepository.findById(id)).thenReturn(Optional.of(productPrice));
        ProductPriceResponse response = this.productPriceService.findById(id);
        assertNotNull(response);
        assertEquals(Optional.of(id).get(), response.getId());
        verify(this.productPriceRepository, times(1)).findById(id);
    }

    @Test
    void saveShouldReturnProductPriceOnlyWhenGivenRequestWithoutProductAttributes() {
        ProductPriceRequest request = mockProductPriceRequest();
        when(this.productPriceRepository.save(Mockito.any())).thenReturn((mockProductPrice()));
        ProductPriceResponse response = this.productPriceService.save(request);
        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getPrice(), response.getPrice());
        assertNull(response.getProductAttributes());
        verify(this.productPriceRepository, times(1)).save(Mockito.any());
    }


    @Test
    void saveShouldReturnProductPriceOnlyWhenGivenRequestWithProductAttributes() {
        ProductPriceRequest request = this.mockProductPriceRequest();
        Set<ProductAttributeRequest> attributeRequests = new HashSet<>();
        attributeRequests.add(BeanUtil.copyProperties(this.mockProductAttribute(), ProductAttributeRequest.class));
        request.setProductAttributes(attributeRequests);

        ProductPrice mockProductPrice = this.mockProductPrice();
        mockProductPrice.setProductAttributes(this.mockProductAttribute());
        when(this.productPriceRepository.save(Mockito.any())).thenReturn(mockProductPrice);
        ProductPriceResponse response = this.productPriceService.save(request);
        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getPrice(), response.getPrice());
        assertNotNull(response.getProductAttributes());
        verify(this.productPriceRepository, times(1)).save(Mockito.any());
    }

    @Test
    void updateShouldThrowExceptionWhenProductPriceIsNotFound() {
        try {
            ProductPriceRequest request = this.mockProductPriceRequest();
            when(this.productPriceRepository.findById(1L)).thenReturn(Optional.of(mockProductPrice()));
            this.productPriceService.update(2L, request);
        } catch (ResourceNotFoundException e) {
            assertEquals("Product price is not found with id [" + 2L + "]", e.getMessage());
        } finally {
            verify(this.productPriceRepository, Mockito.never()).save(mockProductPrice());
        }
    }

    @Test
    void updateShouldReturnProductPriceWhenGivenRequestWithoutProductAttributes() {
        ProductPriceRequest request = this.mockProductPriceRequest();
        ProductPrice mockProductPrice = this.mockProductPrice();
        when(this.productPriceRepository.findById(1L)).thenReturn(Optional.of(mockProductPrice));
        when(this.productPriceRepository.save(Mockito.any())).thenReturn(mockProductPrice);
        ProductPriceResponse response = this.productPriceService.update(1L, request);
        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getPrice(), response.getPrice());
        assertNull(response.getProductAttributes());
        verify(this.productPriceRepository, times(1)).save(mockProductPrice);
    }

    @Test
    void updateShouldReturnProductPriceWhenGivenRequestWithProductAttributes() {
        ProductPriceRequest request = mockProductPriceRequest();
        Set<ProductAttributeRequest> attributeRequests = new HashSet<>();
        attributeRequests.add(BeanUtil.copyProperties(this.mockProductAttribute(), ProductAttributeRequest.class));
        request.setProductAttributes(attributeRequests);

        ProductPrice mockProductPrice = this.mockProductPrice();
        mockProductPrice.setProductAttributes(this.mockProductAttribute());
        when(this.productPriceRepository.findById(1L)).thenReturn(Optional.of(mockProductPrice));
        when(this.productPriceRepository.save(Mockito.any())).thenReturn(mockProductPrice);

        ProductPriceResponse response = this.productPriceService.update(1L, request);
        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getPrice(), response.getPrice());
        assertNotNull(response.getProductAttributes());
        verify(this.productPriceRepository, times(1)).save(mockProductPrice);
    }

    @Test
    void deleteCannotDeleteProductPriceWhenIdIsNotFound() {
        try {
            Mockito.doThrow(EmptyResultDataAccessException.class).when(this.productPriceRepository).softDelete(1L);
            this.productPriceService.deleteById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("Product price is not found with id [" + 1L + "]", e.getMessage());
        }
    }

    @Test
    void deleteShouldDeleteProductPriceSuccessful() {
        assertEquals(true, this.productPriceService.deleteById(1L));
        verify(this.productPriceRepository, times(1)).deleteById(1L);
    }

    private ProductPriceRequest mockProductPriceRequest() {
        return ProductPriceRequest.builder().name("Blackfriday").id(2L).productId(1L).price(100.0).build();
    }

    private ProductPrice mockProductPrice() {
        return ProductPrice.builder().id(1L).name("Blackfriday").price(100.0).build();
    };

    private Set<ProductAttribute> mockProductAttribute() {
        Set<ProductAttribute> attributes = new HashSet<>();
        attributes.add(ProductAttribute.builder().id(1L).build());
        return attributes;
    }

}
