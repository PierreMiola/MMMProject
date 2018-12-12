package com.example.miola.mmmproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

        // specify an adapter (see also next example)
        //mAdapter = new EventAdapter(myDataset);
        //mRecyclerView.setAdapter(mAdapter);
    }
}
