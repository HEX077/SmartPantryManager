package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Recipe> recipeData;
    private View.OnClickListener mOnItemClickListener;

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        public TextView textRecipeName;
        public TextView textRecipeSource;
        public TextView textRecipeIngredientCount;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            textRecipeName = itemView.findViewById(R.id.textRecipeName);
            textRecipeSource = itemView.findViewById(R.id.textRecipeSource);
            textRecipeIngredientCount = itemView.findViewById(R.id.textRecipeIngredientCount);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }

        public TextView getNameTextView() {
            return textRecipeName;
        }

        public TextView getSourceTextView() {
            return textRecipeSource;
        }

        public TextView getIngredientCountTextView() {
            return textRecipeIngredientCount;
        }
    }

    public RecipeAdapter(ArrayList<Recipe> arrayList) {
        recipeData = arrayList;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecipeViewHolder rvh = (RecipeViewHolder) holder;
        Recipe recipe = recipeData.get(position);
        rvh.getNameTextView().setText(recipe.getName());
        rvh.getSourceTextView().setText("Source: " + recipe.getSource());
        rvh.getIngredientCountTextView().setText(recipe.getIngredients().size() + " ingredients");
    }

    @Override
    public int getItemCount() {
        return recipeData.size();
    }
}