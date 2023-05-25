package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.mapper.PostMapper;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public PostDTO createPost(PostDTO postDto) {
        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Category with id [%s] not found".formatted(postDto.getCategoryId())));


        Post post = postMapper.postDtoToPost(postDto);
        post.setCategory(category);
        Post newPost = postRepository.save(post);
        PostDTO postResponse = postMapper.postToPostDTO(newPost);

        return postResponse;

    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        //create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        List<PostDTO> content = posts
                .getContent()
                .stream().map(postMapper::postToPostDTO)
                .collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDTO getPostById(Long id) {
        return postMapper.postToPostDTO(postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post with id [%s] not found".formatted(id))
        ));
    }

    @Override
    public PostDTO updatePost(Long id, PostDTO postDTO) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post with id [%s] not found".formatted(id))
        );

        Category category = categoryRepository.findById(postDTO.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category with id [%s] not found".formatted(postDTO.getCategoryId()))
        );

        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());

        post.setCategory(category);

        Post updatedPost = postRepository.save(post);

        return postMapper.postToPostDTO(updatedPost);

    }

    @Override
    public void deletePostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post with id [%s] not found".formatted(id))
        );
        postRepository.delete(post);
    }

    @Override
    public List<PostDTO> getPostsByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category with id [%s] not found".formatted(categoryId))
        );

        return postRepository.findById(categoryId)
                .stream().map(postMapper::postToPostDTO)
                .collect(Collectors.toList());
    }
}
