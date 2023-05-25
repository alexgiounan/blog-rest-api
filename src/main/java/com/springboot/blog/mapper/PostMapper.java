package com.springboot.blog.mapper;

import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.entity.Post;
import org.mapstruct.Mapper;

@Mapper
public interface PostMapper {

    Post postDtoToPost(PostDTO postDTO);

    PostDTO postToPostDTO(Post post);
}
