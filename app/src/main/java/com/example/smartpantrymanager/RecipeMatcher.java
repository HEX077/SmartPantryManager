package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeMatcher {

    public static ArrayList<Recipe> findMatchingRecipes(ArrayList<PantryItem> pantryItems, ArrayList<Recipe> recipes) {
        HashMap<String, Double> pantryTotals = buildPantryTotals(pantryItems);
        ArrayList<Recipe> matches = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getIngredients().isEmpty()) {
                continue;
            }
            if (canMakeRecipe(recipe, pantryTotals)) {
                matches.add(recipe);
            }
        }
        return matches;
    }

    public static HashMap<String, Double> buildPantryTotals(ArrayList<PantryItem> pantryItems) {
        HashMap<String, Double> totals = new HashMap<>();
        for (PantryItem item : pantryItems) {
            String key = makeKey(IngredientNormaliser.normaliseName(item.getName()), item.getUnit());
            double amount = IngredientNormaliser.toBaseQuantity(item.getQuantity(), item.getUnit());
            if (totals.containsKey(key)) {
                totals.put(key, totals.get(key) + amount);
            } else {
                totals.put(key, amount);
            }
        }
        return totals;
    }

    public static boolean canMakeRecipe(Recipe recipe, HashMap<String, Double> pantryTotals) {
        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            String key = makeKey(ingredient.getNormalisedName(), ingredient.getUnit());
            double needed = IngredientNormaliser.toBaseQuantity(ingredient.getQuantity(), ingredient.getUnit());
            if (!pantryTotals.containsKey(key)) {
                return false;
            }
            if (pantryTotals.get(key) < needed) {
                return false;
            }
        }
        return true;
    }

    private static String makeKey(String normalisedName, String unit) {
        return normalisedName + "|" + IngredientNormaliser.getBaseUnit(unit);
    }
}