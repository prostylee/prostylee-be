package vn.prostylee.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.media.entity.Attachment;
import vn.prostylee.media.service.AttachmentService;
import vn.prostylee.product.dto.request.ProductImageRequest;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductImage;
import vn.prostylee.product.service.ProductImageService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

    private final AttachmentService attachmentService;

    @Override
    public Set<ProductImage> handleProductImages(List<ProductImageRequest> productImageRequests, Product product) {
        return IntStream.range(0, productImageRequests.size())
            .mapToObj(index -> handleProductImage(product, productImageRequests, index)
        ).collect(Collectors.toSet());
    }

    private ProductImage handleProductImage(Product product, List<ProductImageRequest> productImageRequests, int index) {
        Attachment attachment = this.saveAttachment(productImageRequests.get(index));
        return buildProductImage(product, attachment.getId(), index + 1);
    }

    private ProductImage buildProductImage(Product product, Long id, Integer orderIndex) {
        return ProductImage.builder().attachmentId(id).product(product)
                .order(orderIndex)
                .build();
    }

    private Attachment saveAttachment(ProductImageRequest productImageRequest){
        return attachmentService.saveAttachmentByNameAndPath(productImageRequest.getName(), productImageRequest.getPath());
    }

}
