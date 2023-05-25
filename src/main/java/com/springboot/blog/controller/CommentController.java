package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable(value = "postId") Long postId,
                                                    @Valid @RequestBody CommentDTO commentDTO){
        CommentDTO createdComment = commentService.createComment(postId, commentDTO);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDTO> getCommentsByPostId(@PathVariable(value = "postId") Long postId){
        return commentService.getCommentsByPostById(postId);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable(value = "postId") Long postId,
                                                     @PathVariable(value = "commentId")Long commentId){

        CommentDTO commentById = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(commentById,HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable(value = "postId") Long postId,
                                                    @PathVariable(value = "commentId")Long commentId,
                                                    @Valid @RequestBody CommentDTO commentDTO){


        CommentDTO updateComment = commentService.updateComment(postId, commentId, commentDTO);
        return new ResponseEntity<>(updateComment,HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId,
                                                @PathVariable(value = "commentId")Long commentId){
        commentService.deleteComment(postId,commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
}