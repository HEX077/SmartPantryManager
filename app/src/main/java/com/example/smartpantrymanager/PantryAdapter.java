package com.example.smartpantrymanager;

import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PantryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PantryItem> pantryData;
    private View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;

    public class PantryViewHolder extends RecyclerView.ViewHolder {

        public TextView textItemName;
        public TextView textItemQuantity;
        public TextView textItemExpiry;
        public Button deleteButton;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            textItemName = itemView.findViewById(R.id.textItemName);
            textItemQuantity = itemView.findViewById(R.id.textItemQuantity);
            textItemExpiry = itemView.findViewById(R.id.textItemExpiry);
            deleteButton = itemView.findViewById(R.id.buttonDeleteItem);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }

        public TextView getNameTextView() {
            return textItemName;
        }

        public TextView getQuantityTextView() {
            return textItemQuantity;
        }

        public TextView getExpiryTextView() {
            return textItemExpiry;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }
    }

    public PantryAdapter(ArrayList<PantryItem> arrayList, Context context) {
        pantryData = arrayList;
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new PantryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final PantryViewHolder pvh = (PantryViewHolder) holder;
        PantryItem item = pantryData.get(position);
        pvh.getNameTextView().setText(item.getName());
        pvh.getQuantityTextView().setText(item.getQuantityAsText() + " " + item.getUnit());
        pvh.getExpiryTextView().setText("Expires: "
                + DateFormat.format("dd/MM/yyyy", item.getExpiryDate().getTimeInMillis()));

        if (isDeleting) {
            pvh.getDeleteButton().setVisibility(View.VISIBLE);
            pvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmDelete(pvh.getAdapterPosition());
                }
            });
        } else {
            pvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return pantryData.size();
    }

    public void setDelete(boolean b) {
        isDeleting = b;
    }

    private void confirmDelete(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parentContext);
        builder.setTitle("Delete ingredient");
        builder.setMessage("Are you sure you want to delete "
                + pantryData.get(position).getName() + "?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItem(position);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteItem(int position) {
        PantryItem item = pantryData.get(position);
        PantryDataSource ds = new PantryDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteItem(item.getItemId());
            ds.close();
            if (didDelete) {
                pantryData.remove(position);
                notifyDataSetChanged();
                Toast.makeText(parentContext, "Ingredient deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(parentContext, "Delete failed", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(parentContext, "Delete failed", Toast.LENGTH_LONG).show();
        }
    }
}

