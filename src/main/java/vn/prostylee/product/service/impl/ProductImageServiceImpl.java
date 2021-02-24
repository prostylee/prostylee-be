package vn.prostylee.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.comment.entity.Comment;
import vn.prostylee.comment.entity.CommentImage;
import vn.prostylee.media.entity.Attachment;
import vn.prostylee.media.service.AttachmentService;
import vn.prostylee.product.dto.request.ProductImageRequest;
import vn.prostylee.product.dto.response.ProductImageResponse;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductImage;
import vn.prostylee.product.repository.ProductImageRepository;
import vn.prostylee.product.service.ProductImageService;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

    private final AttachmentService attachmentService;
    private final ProductImageRepository productImageRepository;

    @Override
    public ProductImageResponse save(Set<ProductImageRequest> productImageRequests, Product product) {
        Set<ProductImage> responses =
        IntStream.range(0, productImageRequests.size())
            .mapToObj(index -> getProductImage(product, productImageRequests, index)
            ).collect(Collectors.toSet());
        return ProductImageResponse.builder().productImages(responses).build();
    }

    private ProductImage getProductImage(Product product, Set<ProductImageRequest> productImageRequests, int index) {
        Attachment attachment = this.saveAttachment(productImageRequests.iterator().next());
        ProductImage entity = buildProductImage(product, attachment.getId(), index + 1);
        return productImageRepository.save(entity);
    }

    private ProductImage buildProductImage(Product product, Long id, Integer index) {
        return ProductImage.builder().attachmentId(id).product(product)
                .order(index)
                .build();
    }

    private Attachment saveAttachment(ProductImageRequest productImageRequest){
        return attachmentService.saveAttachmentByNameAndPath(productImageRequest.getName(), productImageRequest.getPath());
    }
}
