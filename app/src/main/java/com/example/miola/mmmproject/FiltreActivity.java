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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

public class FiltreActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, EventAdapter.OnItemClickListener {

    @BindView(R.id.recyclerview) RecyclerView rv;
    List<Event> eventListTest;

    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtre);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        eventListTest = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(this.getApplicationContext()));
            JSONArray arr = obj.getJSONArray("features");
            Log.i("nb total 1 : ", ""+arr.length());
            for(int i=0;i<arr.length();i++){
                JSONObject jo = arr.getJSONObject(i);
                //Log.i("titre : ", jo.getJSONObject("properties").get("titre_fr").toString());
                    eventListTest.add(new Event(
                            jo.getJSONObject("properties").optString("image"),
                            jo.getJSONObject("properties").get("titre_fr").toString(),
                            jo.getJSONObject("properties").get("description_fr").toString(),
                            jo.getJSONObject("properties").optString("ville"),
                            jo.getJSONObject("properties").get("region").toString(),
                            jo.getJSONObject("properties").optString("mots_cles_fr"),
                            jo.getJSONObject("properties").get("dates").toString(),
                            jo.getJSONObject("properties").optString("telephone_du_lieu").toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        eventAdapter = new EventAdapter(eventListTest);
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
            case R.id.parcours:
                startActivity(new Intent(this,NoteActivity.class));
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


            for(Event event : eventListTest){
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
                        || event.getMots_cles().toLowerCase().contains(userInput)){
                    newList.add(event);
                }


            }
        eventAdapter.updateList(newList);
        return true;
    }

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(this, EvenementActivity.class);
        Event event = eventListTest.get(position);
        intent.putExtra("MyClass",event);
        startActivity(intent);
    }
}
