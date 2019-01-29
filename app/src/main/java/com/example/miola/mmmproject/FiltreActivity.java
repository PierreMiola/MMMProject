package com.example.miola.mmmproject;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LogPrinter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FiltreActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, EventAdapter.OnItemClickListener, View.OnLongClickListener {

    @BindView(R.id.recyclerview) RecyclerView rv;
    private List<Event> eventList;
    public boolean is_in_action_mode = false;
    private EventAdapter eventAdapter;
    private Toolbar toolbar;
    private ArrayList<Event> selectionList = new ArrayList<>();
    private int counter = 0;
    private Firebase mRef;
    private String surnom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtre);
        ButterKnife.bind(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Evènements");

        mRef = new Firebase("https://mmmproject-97561.firebaseio.com/Parcours");

        setSupportActionBar(toolbar);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        surnom = (String) intent.getSerializableExtra("EMAIL");

        eventList = new ArrayList<>();
        int id = 1;
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(this.getApplicationContext()));
            JSONArray arr = obj.getJSONArray("features");
            Log.i("nb total 1 : ", ""+arr.length());
            for(int i=0;i<arr.length();i++){
                JSONObject jo = arr.getJSONObject(i);
                //Log.i("titre : ", jo.getJSONObject("properties").get("titre_fr").toString());

                eventList.add(new Event(
                        id,
                        jo.getJSONObject("properties").optString("image"),
                        jo.getJSONObject("properties").get("titre_fr").toString(),
                        jo.getJSONObject("properties").get("description_fr").toString(),
                        jo.getJSONObject("properties").optString("ville"),
                        jo.getJSONObject("properties").optString("region"),
                        jo.getJSONObject("properties").optString("mots_cles_fr"),
                        jo.getJSONObject("properties").get("dates").toString(),
                        jo.getJSONObject("properties").optString("telephone_du_lieu"),
                        jo.getJSONObject("properties").optString("thematiques"),
                        jo.getJSONObject("geometry").getJSONArray("coordinates").get(0).toString(),
                        jo.getJSONObject("geometry").getJSONArray("coordinates").get(1).toString()
                ));
                id++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        eventAdapter = new EventAdapter(eventList, FiltreActivity.this);
        rv.setAdapter(eventAdapter);
        eventAdapter.setOnItemClickListener(FiltreActivity.this);


    }


    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            //InputStream is = context.getAssets().open("fr-esr-fete-de-la-science-18.geojson");
            InputStream is = context.getAssets().open("fr-esr-fete-de-la-science-18.geojson");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter, menu);
        inflater.inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.addParcours:
                String parcours = mRef.push().getKey();
                for(int i=0;i<selectionList.size();i++) {
                    Firebase mRefChild = mRef.child(surnom);
                    mRefChild.child(parcours).child(String.valueOf(i)).child("titre").setValue(selectionList.get(i).getTitre());
                    mRefChild.child(parcours).child(String.valueOf(i)).child("ville").setValue(selectionList.get(i).getVille());
                    mRefChild.child(parcours).child(String.valueOf(i)).child("latitude").setValue(selectionList.get(i).getLatitude());
                    mRefChild.child(parcours).child(String.valueOf(i)).child("longitude").setValue(selectionList.get(i).getLongitude());
                }
                clearActionMode();
                eventAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Parcours ajouté", Toast.LENGTH_LONG);
                break;
            case R.id.home:
                clearActionMode();
                eventAdapter.notifyDataSetChanged();
                break;
            case R.id.mesParcours:
                Intent intent = new Intent(FiltreActivity.this, MesParcoursActivity.class);
                intent.putExtra("EMAIL", surnom);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String userInput = s.toLowerCase();
        List<Event> newList = new ArrayList<>();

        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;


            for(Event event : eventList){
                try {
                    date = originalFormat.parse(userInput);
                    targetFormat.format(date);
                    if(event.getDate().contains(targetFormat.format(date))){
                        newList.add(event);
                    }
                } catch (ParseException ex) {
                    // Handle Exception.
                }

                if(event.getTitre().toLowerCase().contains(userInput) || event.getVille().toLowerCase().contains(userInput)
                        || event.getRegion().toLowerCase().contains(userInput) || event.getDate().contains(userInput)
                        || event.getMots_cles().toLowerCase().contains(userInput)
                        || event.getThematique().contains(userInput)){
                    newList.add(event);
                }


            }
        eventAdapter.updateList(newList);
        return true;
    }

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(this, EvenementActivity.class);
        Event event = eventList.get(position);
        intent.putExtra("MyClass",event);
        intent.putExtra("EMAIL", surnom);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {

        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.parcours);
        toolbar.setTitle("0 item selected");
        is_in_action_mode = true;
        eventAdapter.notifyDataSetChanged();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    public void prepareSelection(View view, int position){
        if(((CheckBox) view).isChecked()){
            selectionList.add(eventList.get(position));
            counter = counter + 1;
            updateCounter(counter);

        }else{
            selectionList.remove(eventList.get(position));
            counter = counter - 1;
            updateCounter(counter);
        }
    }

    public void updateCounter(int counter){
        if(counter <= 0){
            toolbar.setTitle("0 item selected");
        }else{
            toolbar.setTitle(counter + " items selected");
        }
    }

    public void clearActionMode(){
        is_in_action_mode = false;
        toolbar.setTitle("Evènements");
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu);
        toolbar.inflateMenu(R.menu.filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        counter = 0;
        selectionList.clear();
    }

    @Override
    public void onBackPressed() {
        if(is_in_action_mode){
            clearActionMode();
            eventAdapter.notifyDataSetChanged();
        }else{
            super.onBackPressed();
        }
    }

}
