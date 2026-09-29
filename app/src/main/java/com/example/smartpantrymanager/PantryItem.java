package com.example.smartpantrymanager;

package com.example.smartpantrymanager;

import java.util.Calendar;

public class PantryItem {

    private int itemId;
    private String name;
    private double quantity;
    private String unit;
    private Calendar expiryDate;

    public PantryItem() {
        itemId = -1;
        name = "";
        quantity = 0;
        unit = "g";
        expiryDate = Calendar.getInstance();
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Calendar getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Calendar expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getQuantityAsText() {
        if (quantity == Math.floor(quantity)) {
            return String.valueOf((int) quantity);
        }
        return String.valueOf(quantity);
    }
}

