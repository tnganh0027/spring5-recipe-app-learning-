package com.ngocanh.spring5recipeapp.repositories;

import com.ngocanh.spring5recipeapp.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
