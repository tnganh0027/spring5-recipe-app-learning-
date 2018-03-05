package com.ngocanh.spring5recipeapp.repositories;

import com.ngocanh.spring5recipeapp.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
