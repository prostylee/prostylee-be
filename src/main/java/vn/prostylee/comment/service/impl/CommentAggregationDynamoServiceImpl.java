package vn.prostylee.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.comment.repository.impl.CommentDynamoRepositoryImpl;
import vn.prostylee.comment.service.CommentAggregationService;
import vn.prostylee.core.constant.TargetType;

@RequiredArgsConstructor
@Service
public class CommentAggregationDynamoServiceImpl implements CommentAggregationService {

    private final CommentDynamoRepositoryImpl commentDynamoRepository;

    @Override
    public Long count(Long targetId, TargetType targetType) {
        return commentDynamoRepository.count(targetId, targetType);
    }
}
