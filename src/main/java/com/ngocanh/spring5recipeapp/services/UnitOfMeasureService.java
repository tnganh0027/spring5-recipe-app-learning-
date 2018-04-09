package com.ngocanh.spring5recipeapp.services;

import com.ngocanh.spring5recipeapp.commands.UnitOfMeasureCommand;
import com.ngocanh.spring5recipeapp.domain.UnitOfMeasure;

import java.util.Set;

public interface UnitOfMeasureService {

    Set<UnitOfMeasureCommand> listAllUoms();
}
