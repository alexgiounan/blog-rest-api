package com.springboot.blog.mapper;

import com.springboot.blog.entity.Category;
import com.springboot.blog.payload.CategoryDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {

    Category categoryDtoToCategory(CategoryDTO categoryDTO);

    CategoryDTO categoryToCategoryDto(Category category);
}
