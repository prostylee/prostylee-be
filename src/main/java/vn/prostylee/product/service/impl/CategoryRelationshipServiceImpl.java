package vn.prostylee.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.product.repository.CategoryRelationshipRepository;
import vn.prostylee.product.service.CategoryRelationshipService;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class CategoryRelationshipServiceImpl implements CategoryRelationshipService {

    private final CategoryRelationshipRepository categoryRelationshipRepository;

    @Override
    public Set<Long> getCategoryIdsRelationshipWith(Long categoryId) {
        Set<Long> categoryIds = new HashSet<>();
        categoryIds.addAll(categoryRelationshipRepository.getCategoryId1sRelationshipWith(categoryId));
        categoryIds.addAll(categoryRelationshipRepository.getCategoryId2sRelationshipWith(categoryId));
        return categoryIds;
    }
}
