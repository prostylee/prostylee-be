package vn.prostylee.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.order.service.OrderService;
import vn.prostylee.product.converter.ProductConverter;
import vn.prostylee.product.dto.filter.PurchasedProductFilter;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.repository.ProductRepository;
import vn.prostylee.product.service.ProductOrderService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final AuthenticatedProvider authenticatedProvider;
    private final ProductConverter productConverter;

    @Override
    public Page<ProductResponse> getPurchasedProductsByMe(PurchasedProductFilter purchasedProductFilter) {
        return getPurchasedProductsByUserId(authenticatedProvider.getUserIdValue(), purchasedProductFilter);
    }

    @Override
    public Page<ProductResponse> getPurchasedProductsByUserId(Long userId, PurchasedProductFilter purchasedProductFilter) {
        Page<Long> pageProductId = orderService.getPurchasedProductIdsByUserId(userId, purchasedProductFilter);
        if (pageProductId.getTotalElements() == 0) {
            return Page.empty();
        }
        List<ProductResponse> products = productRepository.findProductsByIds(pageProductId.getContent())
                .stream()
                .map(productConverter::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(products, pageProductId.getPageable(), pageProductId.getTotalElements());
    }
}
