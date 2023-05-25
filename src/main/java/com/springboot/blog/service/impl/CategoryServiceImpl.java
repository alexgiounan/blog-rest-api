package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.mapper.CategoryMapper;
import com.springboot.blog.payload.CategoryDTO;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;
    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        return categoryMapper.categoryToCategoryDto(categoryRepository
                .save(categoryMapper.categoryDtoToCategory(categoryDTO)));
    }

    @Override
    public CategoryDTO getCategoryById(Long categoryId) {
       return categoryMapper.categoryToCategoryDto(categoryRepository.findById(categoryId).orElseThrow(
               () -> new ResourceNotFoundException("Category with id [%s] not found ".formatted(categoryId))));
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream().map(categoryMapper::categoryToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category with id [%s] not found ".formatted(categoryId)));

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setId(categoryId);

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.categoryToCategoryDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category with id [%s] not found ".formatted(categoryId)));

        categoryRepository.delete(category);
    }
}
