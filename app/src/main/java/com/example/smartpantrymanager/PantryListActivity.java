package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PantryListActivity extends AppCompatActivity {

    private ArrayList<PantryItem> pantryItems;
    private PantryAdapter pantryAdapter;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int itemId = pantryItems.get(position).getItemId();
            Intent intent = new Intent(PantryListActivity.this, MainActivity.class);
            intent.putExtra("itemId", itemId);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantry_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initPantryButton();
        initRecipesButton();
        initSettingsButton();
        initAddItemButton();
        initDeleteSwitch();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("PantryPreferences", Context.MODE_PRIVATE);
        String sortBy = prefs.getString("sortfield", "name");
        String sortOrder = prefs.getString("sortorder", "ASC");

        PantryDataSource ds = new PantryDataSource(this);
        try {
            ds.open();
            pantryItems = ds.getItems(sortBy, sortOrder);
            ds.close();

            TextView textEmpty = findViewById(R.id.textEmptyPantry);
            if (pantryItems.size() > 0) {
                textEmpty.setVisibility(View.GONE);
            } else {
                textEmpty.setVisibility(View.VISIBLE);
            }

            RecyclerView pantryList = findViewById(R.id.rvPantry);
            pantryList.setLayoutManager(new LinearLayoutManager(this));
            pantryAdapter = new PantryAdapter(pantryItems, this);
            pantryAdapter.setOnItemClickListener(onItemClickListener);
            SwitchCompat switchDelete = findViewById(R.id.switchDelete);
            pantryAdapter.setDelete(switchDelete.isChecked());
            pantryList.setAdapter(pantryAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving pantry items", Toast.LENGTH_LONG).show();
        }
    }

    private void initAddItemButton() {
        Button buttonAdd = findViewById(R.id.buttonAddItem);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PantryListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDeleteSwitch() {
        SwitchCompat switchDelete = findViewById(R.id.switchDelete);
        switchDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (pantryAdapter != null) {
                    pantryAdapter.setDelete(isChecked);
                    pantryAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initPantryButton() {
        ImageButton ibPantry = findViewById(R.id.imageButtonPantry);
        ibPantry.setEnabled(false);
    }

    private void initRecipesButton() {
        ImageButton ibRecipes = findViewById(R.id.imageButtonRecipes);
        ibRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PantryListActivity.this, RecipeSuggestionsActivity.class);
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
                Intent intent = new Intent(PantryListActivity.this, PantrySettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}