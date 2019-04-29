package com.example.springhack;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class DataAdapterForRecyclerViewTopComands extends RecyclerView.Adapter<DataAdapterForRecyclerViewTopComands.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<TopComandForRecyclerView> news;
    View view;

    DataAdapterForRecyclerViewTopComands(Context context, ArrayList<TopComandForRecyclerView> phones) {
        this.news = phones;
        this.inflater = LayoutInflater.from(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    public DataAdapterForRecyclerViewTopComands.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         view = inflater.inflate(R.layout.recycler_view_top_comands, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataAdapterForRecyclerViewTopComands.ViewHolder viewHolder, final int position) {
        final TopComandForRecyclerView new1 = news.get(position);

        viewHolder.id.setText(new1.getID());
        viewHolder.NameComand.setText(new1.getcomandName());
        viewHolder.XP.setText(new1.getXP());

        // обработчик нажатия
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {


            }
        });
    }
    public void restoreItem(TopComandForRecyclerView task, int position) {
        news.add(position, task);
        notifyItemInserted(position);
    }
    public void removeItem(int position) {
        news.remove(position);
        notifyItemRemoved(position);
    }
    public List<TopComandForRecyclerView> getData() {
        return news;
    }
    @Override
    public int getItemCount() {
        return news.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView id;
        final TextView NameComand;
        final TextView XP;
        LinearLayout linearLayout;
        ViewHolder(View view){
            super(view);

            id = view.findViewById(R.id.ID);
            NameComand = view.findViewById(R.id.NameComand);
            XP = view.findViewById(R.id.XP);
            linearLayout = view.findViewById((R.id.linLayout));
        }
    }
}

