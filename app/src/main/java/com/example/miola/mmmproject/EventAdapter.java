package com.example.miola.mmmproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{

    /*private String image_url;
    private String description;
    private String ville;
    private String titre;*/

    public static final String KEY_image = "image";
    public static final String KEY_description = "description";
    public static final String KEY_ville = "ville";
    public static final String KEY_titre = "titre";

    private List<Event> eventList;
    private Context context;

    public EventAdapter(List<Event> el, Context c){
        this.eventList=el;
        this.context=c;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

    }
}