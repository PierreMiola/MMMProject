package com.example.miola.mmmproject;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{

    /*public static final String KEY_image = "image";
    public static final String KEY_description = "description";
    public static final String KEY_ville = "ville";
    public static final String KEY_titre = "titre";*/

    private List<Event> eventList;

    public EventAdapter(List<Event> el){
        this.eventList=el;
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Picasso.get().load(eventList.get(i).getImage_url()).resize(200, 200).into(viewHolder.image);
        if(eventList.get(i).getTitre().length() > 25){
            viewHolder.titre.setText(eventList.get(i).getTitre().substring(0, 25)+"...");
        }else{
            viewHolder.titre.setText(eventList.get(i).getTitre());
        }
        viewHolder.ville.setText(eventList.get(i).getVille());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        //private String description;
        public TextView ville;
        public TextView titre;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageEvent);
            ville = itemView.findViewById(R.id.villeEvent);
            titre = itemView.findViewById(R.id.titleEvent);
        }
    }
}