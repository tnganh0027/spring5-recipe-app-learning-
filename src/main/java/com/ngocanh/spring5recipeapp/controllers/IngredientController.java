package com.ngocanh.spring5recipeapp.controllers;

import com.ngocanh.spring5recipeapp.commands.IngredientCommand;
import com.ngocanh.spring5recipeapp.domain.Ingredient;
import com.ngocanh.spring5recipeapp.services.IngredientService;
import com.ngocanh.spring5recipeapp.services.RecipeService;
import com.ngocanh.spring5recipeapp.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model){
        log.debug("Getting ingredients list for recipe id: " + recipeId );

        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));

        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/show")
    public String showRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model){

        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));

        return "/recipe/ingredient/show";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/update")
    public String udpateRecipeIngredient(@PathVariable String recipeId, @PathVariable String id,  Model model){

        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));

        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredientform";

    }

    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command){

        IngredientCommand saveCommand = ingredientService.saveIngredientCommand(command);

        log.debug("save recipe id: " + saveCommand.getRecipeId());
        log.debug("saved ingredient id: " + saveCommand.getId());

        return "redirect:/recipe/"+saveCommand.getRecipeId()+"/ingredient/"+saveCommand.getId()+"/show";

    }

}
