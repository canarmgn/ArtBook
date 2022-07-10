package com.armagan.can.artbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.armagan.can.artbook.databinding.RecyclerRowBinding;

import java.util.ArrayList;
import java.util.Objects;

public class ArtAdapter extends RecyclerView.Adapter {
    public ArtAdapter(ArrayList<artt> arttArrayList) {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class Artadapter extends RecyclerView.Adapter<Artadapter.ArtHolder>{

        ArrayList<artt> artArrayList;

        public Artadapter(ArrayList<artt> arttArrayList) {
            this.artArrayList = arttArrayList;
        }


        @NonNull
        @Override
        public ArtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new ArtHolder(recyclerRowBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ArtHolder holder, int position) {
            holder.binding.recyclerViewTextView.setText(artArrayList.get(position).name);

        }

        @Override
        public int getItemCount() {
            return artArrayList.size();
        }

        public class ArtHolder extends RecyclerView.ViewHolder {
            private RecyclerRowBinding binding;
            private com.armagan.can.artbook.databinding.RecyclerRowBinding itemView;


            public ArtHolder( RecyclerRowBinding binding) {
                super (binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
