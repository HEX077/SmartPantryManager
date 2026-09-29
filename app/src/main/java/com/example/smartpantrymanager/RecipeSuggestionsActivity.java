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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeSuggestionsActivity extends AppCompatActivity {

    private ArrayList<Recipe> matchingRecipes;

    private View.OnClickListener onRecipeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int recipeId = matchingRecipes.get(position).getRecipeId();
            Intent intent = new Intent(RecipeSuggestionsActivity.this, RecipeDetailActivity.class);
            intent.putExtra("recipeId", recipeId);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_suggestions);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initPantryButton();
        initRecipesButton();
        initSettingsButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        PantryDataSource ds = new PantryDataSource(this);
        try {
            ds.open();
            ArrayList<PantryItem> pantryItems = ds.getItems("name", "ASC");
            ArrayList<Recipe> allRecipes = ds.getAllRecipes();
            ds.close();

            matchingRecipes = RecipeMatcher.findMatchingRecipes(pantryItems, allRecipes);

            TextView textSummary = findViewById(R.id.textRecipeSummary);
            textSummary.setText("You can make " + matchingRecipes.size() + " of "
                    + allRecipes.size() + " recipes with what is in your pantry.");

            TextView textEmpty = findViewById(R.id.textEmptyRecipes);
            if (matchingRecipes.size() > 0) {
                textEmpty.setVisibility(View.GONE);
            } else {
                textEmpty.setVisibility(View.VISIBLE);
            }

            RecyclerView recipeList = findViewById(R.id.rvRecipes);
            recipeList.setLayoutManager(new LinearLayoutManager(this));
            RecipeAdapter recipeAdapter = new RecipeAdapter(matchingRecipes);
            recipeAdapter.setOnItemClickListener(onRecipeClickListener);
            recipeList.setAdapter(recipeAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error loading recipes", Toast.LENGTH_LONG).show();
        }
    }

    private void initPantryButton() {
        ImageButton ibPantry = findViewById(R.id.imageButtonPantry);
        ibPantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeSuggestionsActivity.this, PantryListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initRecipesButton() {
        ImageButton ibRecipes = findViewById(R.id.imageButtonRecipes);
        ibRecipes.setEnabled(false);
    }

    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeSuggestionsActivity.this, PantrySettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}