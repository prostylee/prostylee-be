package vn.prostylee.comment.service;

import vn.prostylee.core.constant.TargetType;

public interface CommentAggregationService {

    Long count(Long targetId, TargetType targetType);
}
