package com.example.smartpantrymanager;

public class IngredientNormaliser {

    public static String normaliseName(String name) {
        if (name == null) {
            return "";
        }
        String cleaned = name.toLowerCase().trim().replaceAll("\\s+", " ");
        if (cleaned.isEmpty()) {
            return cleaned;
        }
        String[] words = cleaned.split(" ");
        int last = words.length - 1;
        words[last] = singularise(words[last]);
        return String.join(" ", words);
    }

    public static String singularise(String word) {
        if (word.equals("leaves")) {
            return "leaf";
        }
        if (word.equals("loaves")) {
            return "loaf";
        }
        if (word.equals("halves")) {
            return "half";
        }
        if (word.equals("knives")) {
            return "knife";
        }
        if (word.length() <= 3) {
            return word;
        }
        if (word.endsWith("ies")) {
            return word.substring(0, word.length() - 3) + "y";
        }
        if (word.endsWith("oes")) {
            return word.substring(0, word.length() - 2);
        }
        if (word.endsWith("ches") || word.endsWith("shes") || word.endsWith("xes")) {
            return word.substring(0, word.length() - 2);
        }
        if (word.endsWith("ss") || word.endsWith("us")) {
            return word;
        }
        if (word.endsWith("s")) {
            return word.substring(0, word.length() - 1);
        }
        return word;
    }

    public static String getBaseUnit(String unit) {
        String u = unit.toLowerCase().trim();
        if (u.equals("g") || u.equals("kg")) {
            return "g";
        }
        if (u.equals("ml") || u.equals("l")) {
            return "ml";
        }
        return "count";
    }

    public static double toBaseQuantity(double quantity, String unit) {
        String u = unit.toLowerCase().trim();
        if (u.equals("kg") || u.equals("l")) {
            return quantity * 1000;
        }
        return quantity;
    }
}

