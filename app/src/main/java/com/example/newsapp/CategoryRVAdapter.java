package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.Viewholder> {

    private ArrayList<CategoryRVModal> categoryRVModals;
    private Context context;

    public CategoryRVAdapter(ArrayList<CategoryRVModal> categoryRVModals, Context context, CategoryClickInterface categoryClickInterface) {
        this.categoryRVModals = categoryRVModals;
        this.context = context;
        this.categoryClickInterface = categoryClickInterface;
    }

    private CategoryClickInterface categoryClickInterface;


    public CategoryRVAdapter(ArrayList<CategoryRVModal> categoryRVModals, Context context) {
        this.categoryRVModals = categoryRVModals;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryRVAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_full,parent,false);

        return new CategoryRVAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRVAdapter.Viewholder holder, int position) {
        CategoryRVModal categoryRVModal = categoryRVModals.get(position);
        holder.categoryTV.setText(categoryRVModal.getCategory());
        Picasso.get().load(categoryRVModal.getCategoryImageUrl()).into(holder.categoryIV);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This mfs don't want to cooperate well
                categoryClickInterface.onCategoryClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryRVModals.size();
    }

    public interface CategoryClickInterface{
        void onCategoryClick(int position);
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        private TextView categoryTV;
        private ImageView categoryIV;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            categoryTV = itemView.findViewById(R.id.idTVCategory);
            categoryIV = itemView.findViewById(R.id.idIVCategory);
        }
    }
}
