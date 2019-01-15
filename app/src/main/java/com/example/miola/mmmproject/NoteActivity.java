package com.example.miola.mmmproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

    private Button mSave;
    private RatingBar mNoteBar;
    private ListView mlistNote;
    private Firebase mRef;
    private ArrayList<Float> mNote = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mRef = new Firebase("https://mmmproject-97561.firebaseio.com/Evenement");

        mSave = (Button) findViewById(R.id.save);
        mNoteBar = findViewById(R.id.ratingBar);
        mlistNote = findViewById(R.id.listNotes);
        final ArrayAdapter<Float> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mNote);


        mlistNote.setAdapter(arrayAdapter);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase mRefChild = mRef.child("note");
                mRefChild.setValue(mNoteBar.getRating());

            }
        });

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Float value = dataSnapshot.getValue(Float.class);

                mNote.add(value);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                //Note newModel = dataSnapshot.getValue(NoteActivity.this.mModelClass);
                //int index = mKeys.indexOf(key);

                //mModels.set(index, newModel);

                //notifyDataSetChanged();
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
