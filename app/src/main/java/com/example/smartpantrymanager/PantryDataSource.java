package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class PantryDataSource {

    private SQLiteDatabase database;
    private PantryDBHelper dbHelper;

    public PantryDataSource(Context context) {
        dbHelper = new PantryDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertItem(PantryItem item) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put("name", item.getName());
            initialValues.put("normalised_name", IngredientNormaliser.normaliseName(item.getName()));
            initialValues.put("quantity", item.getQuantity());
            initialValues.put("unit", item.getUnit());
            initialValues.put("expiry_date", String.valueOf(item.getExpiryDate().getTimeInMillis()));
            didSucceed = database.insert("pantry_items", null, initialValues) > 0;
        } catch (Exception e) {
            didSucceed = false;
        }
        return didSucceed;
    }

    public boolean updateItem(PantryItem item) {
        boolean didSucceed = false;
        try {
            long rowId = (long) item.getItemId();
            ContentValues updateValues = new ContentValues();
            updateValues.put("name", item.getName());
            updateValues.put("normalised_name", IngredientNormaliser.normaliseName(item.getName()));
            updateValues.put("quantity", item.getQuantity());
            updateValues.put("unit", item.getUnit());
            updateValues.put("expiry_date", String.valueOf(item.getExpiryDate().getTimeInMillis()));
            didSucceed = database.update("pantry_items", updateValues, "_id=" + rowId, null) > 0;
        } catch (Exception e) {
            didSucceed = false;
        }
        return didSucceed;
    }

    public int getLastItemId() {
        int lastId;
        try {
            String query = "SELECT MAX(_id) FROM pantry_items";
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        } catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

    public ArrayList<PantryItem> getItems(String sortField, String sortOrder) {
        ArrayList<PantryItem> items = new ArrayList<>();
        try {
            String query = "SELECT * FROM pantry_items ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                items.add(cursorToItem(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            items = new ArrayList<>();
        }
        return items;
    }

    public PantryItem getSpecificItem(int itemId) {
        PantryItem item = new PantryItem();
        String query = "SELECT * FROM pantry_items WHERE _id = " + itemId;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            item = cursorToItem(cursor);
        }
        cursor.close();
        return item;
    }

    public boolean deleteItem(int itemId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("pantry_items", "_id=" + itemId, null) > 0;
        } catch (Exception e) {
            didDelete = false;
        }
        return didDelete;
    }

    private PantryItem cursorToItem(Cursor cursor) {
        PantryItem item = new PantryItem();
        item.setItemId(cursor.getInt(0));
        item.setName(cursor.getString(1));
        item.setQuantity(cursor.getDouble(3));
        item.setUnit(cursor.getString(4));
        Calendar expiry = Calendar.getInstance();
        if (cursor.getString(5) != null) {
            expiry.setTimeInMillis(Long.parseLong(cursor.getString(5)));
        }
        item.setExpiryDate(expiry);
        return item;
    }

    public ArrayList<Recipe> getAllRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        try {
            String query = "SELECT * FROM recipes ORDER BY name ASC";
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                recipes.add(cursorToRecipe(cursor));
                cursor.moveToNext();
            }
            cursor.close();
            for (Recipe recipe : recipes) {
                recipe.setIngredients(getRecipeIngredients(recipe.getRecipeId()));
            }
        } catch (Exception e) {
            recipes = new ArrayList<>();
        }
        return recipes;
    }

    public Recipe getSpecificRecipe(int recipeId) {
        Recipe recipe = new Recipe();
        String query = "SELECT * FROM recipes WHERE _id = " + recipeId;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            recipe = cursorToRecipe(cursor);
        }
        cursor.close();
        recipe.setIngredients(getRecipeIngredients(recipeId));
        return recipe;
    }

    public ArrayList<RecipeIngredient> getRecipeIngredients(int recipeId) {
        ArrayList<RecipeIngredient> ingredients = new ArrayList<>();
        String query = "SELECT * FROM recipe_ingredients WHERE recipe_id = " + recipeId + " ORDER BY _id ASC";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RecipeIngredient ingredient = new RecipeIngredient();
            ingredient.setName(cursor.getString(2));
            ingredient.setNormalisedName(cursor.getString(3));
            ingredient.setQuantity(cursor.getDouble(4));
            ingredient.setUnit(cursor.getString(5));
            ingredients.add(ingredient);
            cursor.moveToNext();
        }
        cursor.close();
        return ingredients;
    }

    private Recipe cursorToRecipe(Cursor cursor) {
        Recipe recipe = new Recipe();
        recipe.setRecipeId(cursor.getInt(0));
        recipe.setName(cursor.getString(1));
        recipe.setSteps(cursor.getString(2));
        recipe.setSource(cursor.getString(3));
        return recipe;
    }
}