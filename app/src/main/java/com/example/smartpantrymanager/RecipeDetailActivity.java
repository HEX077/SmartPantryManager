package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initPantryButton();
        initRecipesButton();
        initSettingsButton();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            initRecipe(extras.getInt("recipeId"));
        }
    }

    private void initRecipe(int recipeId) {
        PantryDataSource ds = new PantryDataSource(this);
        Recipe recipe;
        try {
            ds.open();
            recipe = ds.getSpecificRecipe(recipeId);
            ds.close();
        } catch (Exception e) {
            Toast.makeText(this, "Load recipe failed", Toast.LENGTH_LONG).show();
            return;
        }

        TextView textName = findViewById(R.id.textDetailName);
        TextView textSource = findViewById(R.id.textDetailSource);
        TextView textIngredients = findViewById(R.id.textDetailIngredients);
        TextView textSteps = findViewById(R.id.textDetailSteps);

        textName.setText(recipe.getName());
        textSource.setText("Source: " + recipe.getSource());

        StringBuilder ingredientText = new StringBuilder();
        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            ingredientText.append("- ").append(ingredient.getDisplayText()).append("\n");
        }
        textIngredients.setText(ingredientText.toString().trim());
        textSteps.setText(recipe.getSteps());
    }

    private void initPantryButton() {
        ImageButton ibPantry = findViewById(R.id.imageButtonPantry);
        ibPantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeDetailActivity.this, PantryListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initRecipesButton() {
        ImageButton ibRecipes = findViewById(R.id.imageButtonRecipes);
        ibRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeDetailActivity.this, RecipeSuggestionsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeDetailActivity.this, PantrySettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}