package com.example.miola.mmmproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

public class MesParcoursActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listParcours;
    private Firebase mRef;
    private String surnom;
    private ArrayList<String> mParcours = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_parcours);

        toolbar = findViewById(R.id.toolbar);
        listParcours = findViewById(R.id.listParcours);

        mRef = new Firebase("https://mmmproject-97561.firebaseio.com/Parcours");

        toolbar.setTitle("Evènements");
        setSupportActionBar(toolbar);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mParcours);
        listParcours.setAdapter(arrayAdapter);


        Intent intent = getIntent();
        surnom = (String) intent.getSerializableExtra("EMAIL");

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String keyEvent = dataSnapshot.getKey();

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    if(keyEvent.equals(surnom)){
                        for(int i=0; i< childSnapshot.getChildrenCount(); i++){
                            Float latitude = childSnapshot.child(String.valueOf(i)).child("latitude").getValue(Float.class);
                            Float longitude = childSnapshot.child(String.valueOf(i)).child("longitude").getValue(Float.class);
                            String key = childSnapshot.child("latitude").getKey();
                            String key1 = childSnapshot.child("longitude").getKey();
                            mParcours.add(key + " : " + latitude + ", " + key1 + " : " + longitude);
                            arrayAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
}
