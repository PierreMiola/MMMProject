package com.example.miola.mmmproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FiltreActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview) RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtre);
        ButterKnife.bind(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        List<Event> eventListTest = new ArrayList<>();
        /*eventListTest.add(new Event("Paris", "test1"));
        eventListTest.add(new Event("New York", "test2"));
        eventListTest.add(new Event("Rennes", "test3"));
        eventListTest.add(new Event("Bruz", "test4"));
        eventListTest.add(new Event("Nantes", "test5"));*/

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(this.getApplicationContext()));
            JSONArray arr = obj.getJSONArray("features");
            Log.i("nb total 1 : ", ""+arr.length());
            for(int i=0;i<arr.length();i++){
                JSONObject jo = arr.getJSONObject(i);
                //Log.i("titre : ", jo.getJSONObject("properties").get("titre_fr").toString());
                eventListTest.add(new Event(jo.getJSONObject("properties").get("apercu").toString(), jo.getJSONObject("properties").get("ville").toString(), jo.getJSONObject("properties").get("titre_fr").toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        EventAdapter ea = new EventAdapter(eventListTest);
        rv.setAdapter(ea);
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
}
