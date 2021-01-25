package com.mogal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.mogal.R;
import com.mogal.classes.RecyclerType;
import com.mogal.classes.Section;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<Section> arrayList;

    public RecyclerAdapter(ArrayList<Section> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_adapter_item, parent, false);
        if (viewType == 0)
            return new ViewHolderMovies(view);
        return new ViewHolderComments(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderMovies){
            //loads movies into the recycler view of the current view holder
        }else {
            //loads comments into the recycler view of the current view holder
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    public class ViewHolderMovies extends RecyclerView.ViewHolder {

        public ViewHolderMovies(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ViewHolderComments extends RecyclerView.ViewHolder {

        public ViewHolderComments(@NonNull View itemView) {
            super(itemView);
        }
    }
}
