package vn.prostylee.comment.service.impl;

import org.springframework.stereotype.Service;
import vn.prostylee.comment.entity.Comment;
import vn.prostylee.comment.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

    @Override
    public boolean create(Comment comment) {
        return false;
    }
}
