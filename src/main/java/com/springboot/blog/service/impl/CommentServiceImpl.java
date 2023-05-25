package com.springboot.blog.service.impl;

import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.mapper.CommentMapper;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    @Override
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {
       Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post with id [%s] not found".formatted(postId)));

        Comment comment = commentMapper.commentDtoToComment(commentDTO);

        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);

        return commentMapper.commentToCommentDto(newComment);
    }

    @Override
    public List<CommentDTO> getCommentsByPostById(Long postId) {
       return commentRepository.findByPostId(postId)
               .stream().map(commentMapper::commentToCommentDto)
               .collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post with id [%s] not found".formatted(postId)));

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment with id [%s] not found".formatted(commentId)));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to this post");
        }

        return commentMapper.commentToCommentDto(comment);
    }

    @Override
    public CommentDTO updateComment(Long postId, Long commentId, CommentDTO commentDTO) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post with id [%s] not found".formatted(postId)));

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment with id [%s] not found".formatted(commentId)));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to this post");
        }

        comment.setName(commentDTO.getName());
        comment.setEmail(commentDTO.getEmail());
        comment.setBody(commentDTO.getBody());

        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.commentToCommentDto(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post with id [%s] not found".formatted(postId)));

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment with id [%s] not found".formatted(commentId)));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to this post");
        }

        commentRepository.delete(comment);
    }
}
