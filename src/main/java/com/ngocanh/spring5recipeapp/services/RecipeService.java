package com.ngocanh.spring5recipeapp.services;

import com.ngocanh.spring5recipeapp.domain.Recipe;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();
}