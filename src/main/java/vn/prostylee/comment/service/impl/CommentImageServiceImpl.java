package vn.prostylee.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import vn.prostylee.comment.entity.CommentImage;
import vn.prostylee.comment.repository.CommentImageRepository;
import vn.prostylee.comment.service.CommentImageService;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.executor.ChunkServiceExecutor;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.core.utils.JsonUtils;
import vn.prostylee.notification.dto.response.NotificationResponse;
import vn.prostylee.notification.entity.Notification;

import java.util.List;

@RequiredArgsConstructor
public class CommentImageServiceImpl implements CommentImageService {

    private final CommentImageRepository repo;

    @Override
    public Page<CommentImage> findAll(BaseFilter baseFilter) {
        return null;
    }

    @Override
    public CommentImage findById(Long aLong) {
        return null;
    }

    @Override
    public CommentImage save(CommentImage commentImage) {
        CommentImage entity = BeanUtil.copyProperties(commentImage, CommentImage.class);
        CommentImage savedEntity = repo.save(entity);
        return BeanUtil.copyProperties(savedEntity, CommentImage.class);
    }

    @Override
    public int saveAll(List<CommentImage> commentImages){
        return ChunkServiceExecutor.execute(commentImages, subCommentImages -> {
            subCommentImages.forEach(this::save);
            repo.flush();
            return subCommentImages.size();
        });
    }

    @Override
    public CommentImage update(Long aLong, CommentImage s) {
        return null;
    }

    @Override
    public boolean deleteById(Long aLong) {
        return false;
    }
}
