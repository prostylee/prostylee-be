package vn.prostylee.comment.repository;

import vn.prostylee.core.constant.TargetType;

public interface CommentDynamoRepository {

    Long count(Long targetId, TargetType targetType);
}
