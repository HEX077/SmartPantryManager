package com.example.smartpantrymanager;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PantryDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smartpantry.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_PANTRY =
            "create table pantry_items (_id integer primary key autoincrement, "
                    + "name text not null, normalised_name text not null, "
                    + "quantity real not null, unit text not null, expiry_date text);";

    private static final String CREATE_TABLE_RECIPES =
            "create table recipes (_id integer primary key autoincrement, "
                    + "name text not null, steps text not null, source text not null);";

    private static final String CREATE_TABLE_RECIPE_INGREDIENTS =
            "create table recipe_ingredients (_id integer primary key autoincrement, "
                    + "recipe_id integer not null, name text not null, "
                    + "normalised_name text not null, quantity real not null, unit text not null);";

    public PantryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PANTRY);
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_RECIPE_INGREDIENTS);
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(PantryDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS pantry_items");
        onCreate(db);
    }

    private long addRecipe(SQLiteDatabase db, String name, String steps, String source) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("steps", steps);
        values.put("source", source);
        return db.insert("recipes", null, values);
    }

    private void addIngredient(SQLiteDatabase db, long recipeId, String name, double quantity, String unit) {
        ContentValues values = new ContentValues();
        values.put("recipe_id", recipeId);
        values.put("name", name);
        values.put("normalised_name", name.toLowerCase().trim());
        values.put("quantity", quantity);
        values.put("unit", unit);
        db.insert("recipe_ingredients", null, values);
    }

    private void seedRecipes(SQLiteDatabase db) {
        long id;

        id = addRecipe(db, "Creamy Scrambled Eggs",
                "1. Whisk the eggs with the salt and pepper.\n2. Heat a non-stick pan on high heat and melt the butter.\n3. Pour in the eggs and keep pushing them from the edges to the centre with a spatula so they do not burn.\n4. Add the chives, smoked salmon, ham or feta and cook until done the way you like.\n5. Serve straight away with warm toast.",
                "Woolworths");
        addIngredient(db, id, "egg", 6, "count");
        addIngredient(db, id, "flaky sea salt", 10, "g");
        addIngredient(db, id, "black pepper", 1, "g");
        addIngredient(db, id, "butter", 15, "g");
        addIngredient(db, id, "chives", 5, "g");
        addIngredient(db, id, "smoked salmon", 100, "g");
        addIngredient(db, id, "gypsy ham", 100, "g");
        addIngredient(db, id, "feta", 200, "g");

        id = addRecipe(db, "Vanilla French Toast",
                "1. Whisk the eggs, milk, sugar, vanilla and cinnamon in a bowl.\n2. Melt the butter in a pan on medium-high heat.\n3. Dip the bread slices in the egg mix.\n4. Fry both sides until brown.\n5. Serve with maple syrup.",
                "Food Network");
        addIngredient(db, id, "egg", 2, "count");
        addIngredient(db, id, "milk", 125, "ml");
        addIngredient(db, id, "sugar", 25, "g");
        addIngredient(db, id, "vanilla essence", 15, "ml");
        addIngredient(db, id, "ground cinnamon", 1, "g");
        addIngredient(db, id, "butter", 15, "g");
        addIngredient(db, id, "bread", 6, "count");

        id = addRecipe(db, "Pancakes",
                "1. Heat the oven to 95 degrees to keep the pancakes warm.\n2. Whisk the flour, sugar, baking powder and salt in a big bowl. Add a pinch of nutmeg if you like.\n3. Beat the eggs, then whisk in half the milk and the vanilla.\n4. Melt the butter in the rest of the milk on low heat and whisk it into the egg mix.\n5. Stir the wet mix into the dry mix until a thick, slightly lumpy batter forms. Let it stand for 10 minutes.\n6. Brush a pan with oil on medium heat. Pour in the batter and cook for about 2 minutes until bubbles appear, then flip and cook 1 more minute.\n7. Serve with butter and maple syrup.",
                "Food Network Kitchen");
        addIngredient(db, id, "flour", 190, "g");
        addIngredient(db, id, "sugar", 40, "g");
        addIngredient(db, id, "baking powder", 12, "g");
        addIngredient(db, id, "salt", 3, "g");
        addIngredient(db, id, "egg", 2, "count");
        addIngredient(db, id, "milk", 310, "ml");
        addIngredient(db, id, "vanilla essence", 1, "ml");
        addIngredient(db, id, "butter", 55, "g");
        addIngredient(db, id, "vegetable oil", 15, "ml");

        id = addRecipe(db, "Mashed Potatoes",
                "1. Put the peeled, cut potatoes and salt in a pot, cover with cold water and bring to the boil.\n2. Turn down the heat and simmer for about 10 minutes until soft.\n3. Drain, put back in the pot and shake over medium heat for 1 minute to dry them.\n4. Heat the milk and butter in the pot until the butter melts.\n5. Mash the potatoes into the milk and butter, then season with salt and pepper. Add nutmeg if you like.",
                "Food Network Kitchen");
        addIngredient(db, id, "potato", 4, "count");
        addIngredient(db, id, "salt", 5, "g");
        addIngredient(db, id, "milk", 180, "ml");
        addIngredient(db, id, "butter", 55, "g");
        addIngredient(db, id, "black pepper", 1, "g");

        id = addRecipe(db, "Chicken Curry",
                "1. Dust the chicken pieces with turmeric.\n2. Heat the oil in a big pot and fry the cardamom, cinnamon and cloves until they smell fragrant.\n3. Add the butter and onion and fry until see-through.\n4. Add the tomato, tomato paste and garlic and cook on low heat into a thick sauce.\n5. When the oil rises to the top, add the chicken, ginger, coriander, cumin, chilli powder and turmeric.\n6. Add 375 ml boiling water and cook for 20 minutes until the chicken is done.\n7. Add the potatoes and cook for another 20 minutes until soft.\n8. Serve with roti.",
                "Woolworths TASTE (Farida Omar)");
        addIngredient(db, id, "chicken", 1500, "g");
        addIngredient(db, id, "turmeric", 3, "g");
        addIngredient(db, id, "sunflower oil", 45, "ml");
        addIngredient(db, id, "cardamom pod", 3, "count");
        addIngredient(db, id, "cinnamon stick", 1, "count");
        addIngredient(db, id, "whole clove", 4, "count");
        addIngredient(db, id, "onion", 2, "count");
        addIngredient(db, id, "butter", 15, "g");
        addIngredient(db, id, "tomato", 2, "count");
        addIngredient(db, id, "tomato paste", 15, "g");
        addIngredient(db, id, "garlic", 3, "count");
        addIngredient(db, id, "fresh ginger", 10, "g");
        addIngredient(db, id, "ground coriander", 6, "g");
        addIngredient(db, id, "ground cumin", 4, "g");
        addIngredient(db, id, "chilli powder", 4, "g");
        addIngredient(db, id, "potato", 9, "count");
        addIngredient(db, id, "roti", 4, "count");

        id = addRecipe(db, "Fish and Chips",
                "1. Slice the potatoes very thin and soak them in warm water for 30 minutes.\n2. Whisk the flour, baking powder and salt, then whisk in the beer to make a smooth batter.\n3. Pat the fish dry and cut it into strips.\n4. Heat the oil to 180 degrees. Drain the potatoes and dry them well.\n5. Heat the oven to 95 degrees. Fry the chips in small batches for 2 to 3 minutes until golden, drain, salt and keep warm in the oven.\n6. Season the fish, dip it in the batter and fry for 3 to 4 minutes, turning once, until golden. Drain and keep warm.\n7. Serve with malt vinegar or tartar sauce.",
                "Food Network Kitchen");
        addIngredient(db, id, "potato", 4, "count");
        addIngredient(db, id, "flour", 375, "g");
        addIngredient(db, id, "baking powder", 12, "g");
        addIngredient(db, id, "salt", 12, "g");
        addIngredient(db, id, "beer", 710, "ml");
        addIngredient(db, id, "white fish", 900, "g");
        addIngredient(db, id, "vegetable oil", 1000, "ml");
        addIngredient(db, id, "black pepper", 1, "g");
        addIngredient(db, id, "malt vinegar", 30, "ml");

        id = addRecipe(db, "Smooth and Creamy Pap",
                "1. Boil 6 cups of water in a big pot.\n2. Whisk in the maize meal.\n3. Cook for 25 to 30 minutes.\n4. Stir in the butter and serve warm.",
                "Woolworths TASTE (Hannah Lewry)");
        addIngredient(db, id, "maize meal", 300, "g");
        addIngredient(db, id, "butter", 50, "g");

        id = addRecipe(db, "Grilled Cheese Sandwich",
                "1. Heat a pan on medium-low heat.\n2. Butter one side of each bread slice.\n3. Put one slice butter-side down in the pan, cover it with the cheese and put the other slice on top, butter-side up.\n4. Cook for 3 to 4 minutes until golden underneath.\n5. Flip, cover with a small metal bowl and cook for 3 to 4 minutes until the cheese melts.\n6. Cut in half and serve straight away.",
                "Food Network (Jeff Mauro)");
        addIngredient(db, id, "bread", 2, "count");
        addIngredient(db, id, "butter", 20, "g");
        addIngredient(db, id, "american cheese", 80, "g");

        id = addRecipe(db, "Tomato Soup",
                "1. Heat the oven grill.\n2. Put the halved tomatoes cut-side down on a baking tray and grill for 10 to 12 minutes until the skins burst.\n3. When cool enough, peel off the skins.\n4. Melt the butter in a big pot on medium heat and cook the onion for 3 to 5 minutes until soft.\n5. Add the tomatoes and simmer for about 20 minutes.\n6. Add the salt, pepper, sugar, basil and oregano and serve.",
                "Food Network (Martha Foose)");
        addIngredient(db, id, "tomato", 9, "count");
        addIngredient(db, id, "butter", 45, "g");
        addIngredient(db, id, "onion", 1, "count");
        addIngredient(db, id, "salt", 1, "g");
        addIngredient(db, id, "black pepper", 1, "g");
        addIngredient(db, id, "sugar", 2, "g");
        addIngredient(db, id, "fresh basil", 5, "g");
        addIngredient(db, id, "fresh oregano", 2, "g");

        id = addRecipe(db, "Macaroni and Cheese",
                "1. Boil the macaroni in salted water as the packet says. Keep 1 cup of the water, then drain.\n2. Melt the butter in the same pot on medium heat and whisk in the flour, mustard powder and paprika for 1 minute.\n3. Slowly whisk in the milk and cook for 7 to 8 minutes until it thickens.\n4. Whisk in all the cheeses until melted, then take off the heat.\n5. Add the macaroni and mix, adding some pasta water if it is too thick. Season with salt.",
                "Food Network Kitchen");
        addIngredient(db, id, "salt", 5, "g");
        addIngredient(db, id, "macaroni", 225, "g");
        addIngredient(db, id, "butter", 30, "g");
        addIngredient(db, id, "flour", 15, "g");
        addIngredient(db, id, "mustard powder", 3, "g");
        addIngredient(db, id, "paprika", 1, "g");
        addIngredient(db, id, "milk", 375, "ml");
        addIngredient(db, id, "cheddar", 115, "g");
        addIngredient(db, id, "american cheese", 85, "g");
        addIngredient(db, id, "cream cheese", 55, "g");

        id = addRecipe(db, "Spaghetti Bolognese",
                "1. Heat the olive oil in a pot on medium heat and fry the onion for 5 minutes until see-through. Add garlic if you like and cook 5 more minutes until brown.\n2. Add the mince and cook for 5 minutes, stirring often.\n3. Add the pasta sauce, cover and simmer for 20 minutes, stirring every 10 minutes. Season.\n4. Cook the spaghetti in salted water as the packet says, drain and drizzle with olive oil.\n5. Serve the mince on the spaghetti with grated cheese on top. Add fresh basil if you like.",
                "Woolworths");
        addIngredient(db, id, "olive oil", 30, "ml");
        addIngredient(db, id, "onion", 1, "count");
        addIngredient(db, id, "beef mince", 1000, "g");
        addIngredient(db, id, "tomato and basil pasta sauce", 600, "g");
        addIngredient(db, id, "spaghetti", 500, "g");
        addIngredient(db, id, "salt", 5, "g");
        addIngredient(db, id, "grated hard cheese", 60, "g");

        id = addRecipe(db, "Beef Burgers",
                "1. Mix the mince, red onion, egg, parsley, salt and pepper in a bowl.\n2. Shape into thick patties and rub with olive oil.\n3. Fry in a pan on medium-high heat until cooked through.\n4. Serve in the buns with roasted red peppers, fried pineapple, onion marmalade, rocket and potato chips.",
                "Woolworths TASTE (Abigail Donnelly)");
        addIngredient(db, id, "beef mince", 500, "g");
        addIngredient(db, id, "red onion", 1, "count");
        addIngredient(db, id, "egg", 1, "count");
        addIngredient(db, id, "fresh parsley", 10, "g");
        addIngredient(db, id, "salt", 2, "g");
        addIngredient(db, id, "black pepper", 1, "g");
        addIngredient(db, id, "olive oil", 15, "ml");
        addIngredient(db, id, "red pepper", 2, "count");
        addIngredient(db, id, "pineapple", 0.5, "count");
        addIngredient(db, id, "onion marmalade", 60, "g");
        addIngredient(db, id, "rocket", 40, "g");
        addIngredient(db, id, "potato chips", 100, "g");
        addIngredient(db, id, "burger bun", 4, "count");

        id = addRecipe(db, "Cottage Pie",
                "1. Heat the oven to 180 degrees.\n2. Heat the oil in a big pan and brown the mince until the liquid is gone.\n3. Add the onion, celery, carrot and garlic, cook for a few minutes, then sprinkle over the flour.\n4. Add the Worcestershire sauce, stock, bay leaves, thyme and tomato paste. Bring to the boil, cover and simmer for 30 minutes.\n5. Boil the potatoes until soft, then mash with the cheese, butter and nutmeg. Season with salt and white pepper.\n6. Put the mince in an ovenproof dish, spread the mash on top, sprinkle with nutmeg and roughen the top with a fork.\n7. Bake for 20 minutes until golden and bubbling.",
                "Woolworths TASTE (Abigail Donnelly)");
        addIngredient(db, id, "cooking oil", 30, "ml");
        addIngredient(db, id, "beef mince", 1000, "g");
        addIngredient(db, id, "onion", 1, "count");
        addIngredient(db, id, "celery", 3, "count");
        addIngredient(db, id, "carrot", 2, "count");
        addIngredient(db, id, "garlic", 4, "count");
        addIngredient(db, id, "flour", 15, "g");
        addIngredient(db, id, "worcestershire sauce", 60, "ml");
        addIngredient(db, id, "beef stock", 500, "ml");
        addIngredient(db, id, "bay leaf", 2, "count");
        addIngredient(db, id, "dried thyme", 1, "g");
        addIngredient(db, id, "tomato paste", 15, "g");
        addIngredient(db, id, "potato", 6, "count");
        addIngredient(db, id, "cheddar", 100, "g");
        addIngredient(db, id, "butter", 45, "g");
        addIngredient(db, id, "nutmeg", 2, "g");
        addIngredient(db, id, "salt", 2, "g");
        addIngredient(db, id, "white pepper", 1, "g");

        id = addRecipe(db, "Boerewors Rolls with Pineapple Salsa",
                "1. Mix the pineapple, red onion, cucumber, coriander, chilli and lime juice to make the salsa.\n2. Braai the wors slowly on medium coals until cooked so the skin does not split.\n3. Cut the rolls open lengthways, butter them and toast on the grid until the butter melts.\n4. Fill each roll with wors and top with lots of salsa.",
                "Woolworths TASTE (Mogau Seshoene)");
        addIngredient(db, id, "hot dog roll", 6, "count");
        addIngredient(db, id, "boerewors", 800, "g");
        addIngredient(db, id, "pineapple", 1, "count");
        addIngredient(db, id, "red onion", 1, "count");
        addIngredient(db, id, "cucumber", 0.5, "count");
        addIngredient(db, id, "fresh coriander", 10, "g");
        addIngredient(db, id, "red chilli", 1, "count");
        addIngredient(db, id, "lime", 1, "count");
        addIngredient(db, id, "butter", 30, "g");

        id = addRecipe(db, "Chicken Mayo Sandwich",
                "1. Mix the onion, chopped gherkins, mayonnaise, mustard, dill, lemon juice and zest in a bowl.\n2. Add the spiced chicken, mix well and season with salt and pepper.\n3. Lay out 4 slices of bread, spread each with 1 tablespoon salad cream and add lettuce.\n4. Spread a quarter of the chicken mix on each, then add a layer of smoked chicken.\n5. Close with the other 4 slices, cut and serve with whole gherkins and pretzels.",
                "Woolworths TASTE (Marcelle van Rooyen)");
        addIngredient(db, id, "red onion", 0.5, "count");
        addIngredient(db, id, "dill gherkin", 140, "g");
        addIngredient(db, id, "mayonnaise", 120, "ml");
        addIngredient(db, id, "wholegrain mustard", 10, "ml");
        addIngredient(db, id, "fresh dill", 2, "g");
        addIngredient(db, id, "lemon", 1, "count");
        addIngredient(db, id, "spiced chicken breast", 140, "g");
        addIngredient(db, id, "smoked chicken breast", 140, "g");
        addIngredient(db, id, "salt", 1, "g");
        addIngredient(db, id, "black pepper", 1, "g");
        addIngredient(db, id, "brown bread", 8, "count");
        addIngredient(db, id, "salad cream", 60, "ml");
        addIngredient(db, id, "lettuce", 80, "g");
        addIngredient(db, id, "pretzel knots", 22, "g");
    }
}
