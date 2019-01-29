package com.example.miola.mmmproject;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{



    private List<Event> eventList;
    private static OnItemClickListener mListener;
    private FiltreActivity filtreActivity;
    private Context context;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public EventAdapter(List<Event> el, Context cnx){
        this.eventList = el;
        this.context = cnx;
        this.filtreActivity = (FiltreActivity) cnx;
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list, parent, false);
        ViewHolder vh = new ViewHolder(v, filtreActivity);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if(eventList.get(i).getImage_url().isEmpty()){
            Picasso.get().load("/home/amine/AndroidStudioProjects/MMMProject/app/src/main/assets/evenement.jpeg").resize(200, 200).into(viewHolder.image);
        }else {
            Picasso.get().load(eventList.get(i).getImage_url()).resize(200, 200).into(viewHolder.image);
        }
        if(eventList.get(i).getTitre().length() > 25){
            viewHolder.titre.setText(eventList.get(i).getTitre().substring(0, 25)+"...");
        }else{
            viewHolder.titre.setText(eventList.get(i).getTitre());
        }
        viewHolder.ville.setText(eventList.get(i).getVille());

        if(!filtreActivity.is_in_action_mode){
            viewHolder.checkBox.setVisibility(View.GONE);
        }else{
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.checkBox.setChecked(false);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private ImageView image;
        public TextView ville;
        public TextView titre;
        public CheckBox checkBox;
        FiltreActivity filtreActivity;
        CardView cardView;

        public ViewHolder(View itemView, FiltreActivity filtreActivity) {
            super(itemView);
            image = itemView.findViewById(R.id.imageEvent);
            ville = itemView.findViewById(R.id.villeEvent);
            titre = itemView.findViewById(R.id.titleEvent);
            checkBox = itemView.findViewById(R.id.checkList);
            this.filtreActivity = filtreActivity;
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnLongClickListener(filtreActivity);
            checkBox.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION){
                                mListener.OnItemClick(position);
                            }
                    }
                }
            });

        }



        @Override
        public void onClick(View v) {
            filtreActivity.prepareSelection(v, getAdapterPosition());
        }
    }

    public void updateList(List<Event> newList){
        eventList = new ArrayList<>();
        eventList.addAll(newList);
        notifyDataSetChanged();
    }


}