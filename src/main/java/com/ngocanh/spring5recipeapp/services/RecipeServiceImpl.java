package com.ngocanh.spring5recipeapp.services;

import com.ngocanh.spring5recipeapp.domain.Recipe;
import com.ngocanh.spring5recipeapp.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Set<Recipe> getRecipes() {

        Set<Recipe> recipes = new HashSet<>();

        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);

        return recipes;
    }
}
