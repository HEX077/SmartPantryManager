package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements ExpiryDatePickerDialog.SaveDateListener {

    private PantryItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initPantryButton();
        initRecipesButton();
        initSettingsButton();
        initToggleButton();
        initChangeDateButton();
        initTextChangedEvents();
        initSaveButton();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            initItem(extras.getInt("itemId"));
            setForEditing(false);
        } else {
            currentItem = new PantryItem();
            showExpiryDate(currentItem.getExpiryDate());
            ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
            editToggle.setChecked(true);
            setForEditing(true);
        }
    }

    private void initItem(int id) {
        PantryDataSource ds = new PantryDataSource(MainActivity.this);
        try {
            ds.open();
            currentItem = ds.getSpecificItem(id);
            ds.close();
        } catch (Exception e) {
            Toast.makeText(this, "Load ingredient failed", Toast.LENGTH_LONG).show();
            currentItem = new PantryItem();
        }

        EditText editName = findViewById(R.id.editName);
        EditText editQuantity = findViewById(R.id.editQuantity);
        Spinner spinnerUnit = findViewById(R.id.spinnerUnit);

        editName.setText(currentItem.getName());
        editQuantity.setText(currentItem.getQuantityAsText());

        String[] units = getResources().getStringArray(R.array.units_array);
        for (int i = 0; i < units.length; i++) {
            if (units[i].equals(currentItem.getUnit())) {
                spinnerUnit.setSelection(i);
            }
        }
        showExpiryDate(currentItem.getExpiryDate());
    }

    private void showExpiryDate(Calendar date) {
        TextView textExpiry = findViewById(R.id.textExpiry);
        textExpiry.setText(DateFormat.format("dd/MM/yyyy", date.getTimeInMillis()));
    }

    private void initToggleButton() {
        final ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setForEditing(editToggle.isChecked());
            }
        });
    }

    private void setForEditing(boolean enabled) {
        EditText editName = findViewById(R.id.editName);
        EditText editQuantity = findViewById(R.id.editQuantity);
        Spinner spinnerUnit = findViewById(R.id.spinnerUnit);
        Button buttonExpiry = findViewById(R.id.buttonExpiry);
        Button buttonSave = findViewById(R.id.buttonSave);

        editName.setEnabled(enabled);
        editQuantity.setEnabled(enabled);
        spinnerUnit.setEnabled(enabled);
        buttonExpiry.setEnabled(enabled);
        buttonSave.setEnabled(enabled);

        if (enabled) {
            editName.requestFocus();
        }
    }

    private void initChangeDateButton() {
        Button buttonExpiry = findViewById(R.id.buttonExpiry);
        buttonExpiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                ExpiryDatePickerDialog datePickerDialog = new ExpiryDatePickerDialog();
                datePickerDialog.show(fm, "DatePick");
            }
        });
    }

    @Override
    public void didFinishDatePickerDialog(Calendar selectedTime) {
        showExpiryDate(selectedTime);
        currentItem.setExpiryDate(selectedTime);
    }

    private void initTextChangedEvents() {
        final EditText editName = findViewById(R.id.editName);
        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currentItem != null) {
                    currentItem.setName(editName.getText().toString());
                }
            }
        });

        final EditText editQuantity = findViewById(R.id.editQuantity);
        editQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currentItem != null) {
                    try {
                        currentItem.setQuantity(Double.parseDouble(editQuantity.getText().toString()));
                    } catch (NumberFormatException e) {
                        currentItem.setQuantity(0);
                    }
                }
            }
        });
    }

    private boolean validateInput() {
        EditText editName = findViewById(R.id.editName);
        EditText editQuantity = findViewById(R.id.editQuantity);
        String name = editName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();

        if (name.isEmpty()) {
            editName.setError("Please enter an ingredient name");
            return false;
        }
        if (quantityText.isEmpty()) {
            editQuantity.setError("Please enter a quantity");
            return false;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            editQuantity.setError("Please enter a valid number");
            return false;
        }
        if (quantity <= 0) {
            editQuantity.setError("Quantity must be more than zero");
            return false;
        }

        currentItem.setName(name);
        currentItem.setQuantity(quantity);
        return true;
    }

    private void initSaveButton() {
        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateInput()) {
                    return;
                }
                Spinner spinnerUnit = findViewById(R.id.spinnerUnit);
                currentItem.setUnit(spinnerUnit.getSelectedItem().toString());
                hideKeyboard();

                boolean wasSuccessful;
                PantryDataSource ds = new PantryDataSource(MainActivity.this);
                try {
                    ds.open();
                    if (currentItem.getItemId() == -1) {
                        wasSuccessful = ds.insertItem(currentItem);
                        if (wasSuccessful) {
                            currentItem.setItemId(ds.getLastItemId());
                        }
                    } else {
                        wasSuccessful = ds.updateItem(currentItem);
                    }
                    ds.close();
                } catch (Exception e) {
                    wasSuccessful = false;
                }

                if (wasSuccessful) {
                    ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
                    editToggle.setChecked(false);
                    setForEditing(false);
                    Toast.makeText(MainActivity.this, "Ingredient saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Save failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void initPantryButton() {
        ImageButton ibPantry = findViewById(R.id.imageButtonPantry);
        ibPantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PantryListActivity.class);
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
                Intent intent = new Intent(MainActivity.this, RecipeSuggestionsActivity.class);
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
                Intent intent = new Intent(MainActivity.this, PantrySettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
