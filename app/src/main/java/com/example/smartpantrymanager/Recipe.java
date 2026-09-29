package com.example.smartpantrymanager;

import java.util.ArrayList;

public class Recipe {

    private int recipeId;
    private String name;
    private String steps;
    private String source;
    private ArrayList<RecipeIngredient> ingredients;

    public Recipe() {
        recipeId = -1;
        name = "";
        steps = "";
        source = "";
        ingredients = new ArrayList<>();
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ArrayList<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}