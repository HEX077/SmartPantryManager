package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PantrySettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantry_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initPantryButton();
        initRecipesButton();
        initSettingsButton();
        initSettings();
        initSortByClick();
        initSortOrderClick();
    }

    private void initSettings() {
        SharedPreferences prefs = getSharedPreferences("PantryPreferences", Context.MODE_PRIVATE);
        String sortBy = prefs.getString("sortfield", "name");
        String sortOrder = prefs.getString("sortorder", "ASC");

        RadioButton rbName = findViewById(R.id.radioName);
        RadioButton rbQuantity = findViewById(R.id.radioQuantity);
        RadioButton rbExpiry = findViewById(R.id.radioExpiry);
        if (sortBy.equalsIgnoreCase("name")) {
            rbName.setChecked(true);
        } else if (sortBy.equalsIgnoreCase("quantity")) {
            rbQuantity.setChecked(true);
        } else {
            rbExpiry.setChecked(true);
        }

        RadioButton rbAscending = findViewById(R.id.radioAscending);
        RadioButton rbDescending = findViewById(R.id.radioDescending);
        if (sortOrder.equalsIgnoreCase("ASC")) {
            rbAscending.setChecked(true);
        } else {
            rbDescending.setChecked(true);
        }
    }

    private void initSortByClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbName = findViewById(R.id.radioName);
                RadioButton rbQuantity = findViewById(R.id.radioQuantity);
                SharedPreferences.Editor editor = getSharedPreferences("PantryPreferences", Context.MODE_PRIVATE).edit();
                if (rbName.isChecked()) {
                    editor.putString("sortfield", "name");
                } else if (rbQuantity.isChecked()) {
                    editor.putString("sortfield", "quantity");
                } else {
                    editor.putString("sortfield", "expiry_date");
                }
                editor.apply();
            }
        });
    }

    private void initSortOrderClick() {
        RadioGroup rgSortOrder = findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbAscending = findViewById(R.id.radioAscending);
                SharedPreferences.Editor editor = getSharedPreferences("PantryPreferences", Context.MODE_PRIVATE).edit();
                if (rbAscending.isChecked()) {
                    editor.putString("sortorder", "ASC");
                } else {
                    editor.putString("sortorder", "DESC");
                }
                editor.apply();
            }
        });
    }

    private void initPantryButton() {
        ImageButton ibPantry = findViewById(R.id.imageButtonPantry);
        ibPantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PantrySettingsActivity.this, PantryListActivity.class);
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
                Intent intent = new Intent(PantrySettingsActivity.this, RecipeSuggestionsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setEnabled(false);
    }
}