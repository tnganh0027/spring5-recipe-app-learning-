package com.ngocanh.spring5recipeapp.services;

import com.ngocanh.spring5recipeapp.commands.IngredientCommand;

public interface IngredientService {

    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

    IngredientCommand saveIngredientCommand(IngredientCommand command);

    void deleteIngredient(Long recipeId, Long idToDelete);

}
