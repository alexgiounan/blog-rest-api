package com.springboot.blog.mapper;

import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.entity.Comment;
import org.mapstruct.Mapper;

@Mapper
public interface CommentMapper {

    Comment commentDtoToComment(CommentDTO commentDTO);

    CommentDTO commentToCommentDto(Comment comment);
}
