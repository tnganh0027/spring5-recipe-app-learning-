package com.ngocanh.spring5recipeapp.services;

import com.ngocanh.spring5recipeapp.commands.IngredientCommand;
import com.ngocanh.spring5recipeapp.converters.IngredientCommandToIngredient;
import com.ngocanh.spring5recipeapp.converters.IngredientToIngredientCommand;
import com.ngocanh.spring5recipeapp.domain.Ingredient;
import com.ngocanh.spring5recipeapp.domain.Recipe;
import com.ngocanh.spring5recipeapp.repositories.RecipeRepository;
import com.ngocanh.spring5recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final RecipeRepository recipeRepository;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, RecipeRepository recipeRepository, IngredientCommandToIngredient ingredientCommandToIngredient, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeRepository = recipeRepository;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if(!optionalRecipe.isPresent()){
            log.error("recipe is not found. ID: " + recipeId);
        }

        Recipe recipe = optionalRecipe.get();

        Optional<IngredientCommand> ingredientCommand = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

        if(!ingredientCommand.isPresent()){
            log.error("Ingredient id not found: " + ingredientId);
        }

        return ingredientCommand.get();
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if(!recipeOptional.isPresent()){

            log.error("Recipe not found. ID: " + command.getRecipeId());
            return new IngredientCommand();

        }else {

            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if(ingredientOptional.isPresent()){

                Ingredient ingredientFound = ingredientOptional.get();

                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUom(unitOfMeasureRepository.findById(command.getUom().getId())
                .orElseThrow(() -> new RuntimeException("UOM not Found")));

            }else{
                Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                ingredient.setRecipe(recipe);
                recipe.addIngredient(ingredient);
            }

            Recipe saveRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredient = saveRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                    .findFirst();

            if (!savedIngredient.isPresent()){

                savedIngredient = saveRecipe.getIngredients().stream()
                        .filter( recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredient -> recipeIngredient.getUom().getId().equals(command.getUom().getId()))
                        .findFirst();
            }

            return ingredientToIngredientCommand.convert(savedIngredient.get());
        }
    }
}
