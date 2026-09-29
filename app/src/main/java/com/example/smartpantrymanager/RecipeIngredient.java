package com.example.smartpantrymanager;

public class RecipeIngredient {

    private String name;
    private String normalisedName;
    private double quantity;
    private String unit;

    public RecipeIngredient() {
        name = "";
        normalisedName = "";
        quantity = 0;
        unit = "g";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalisedName() {
        return normalisedName;
    }

    public void setNormalisedName(String normalisedName) {
        this.normalisedName = normalisedName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantityAsText() {
        if (quantity == Math.floor(quantity)) {
            return String.valueOf((int) quantity);
        }
        return String.valueOf(quantity);
    }

    public String getDisplayText() {
        if (unit.equals("count")) {
            return name + ": " + getQuantityAsText();
        }
        return name + ": " + getQuantityAsText() + " " + unit;
    }
}