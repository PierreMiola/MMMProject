package com.example.miola.mmmproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EvenementActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    private Event event;
    private Firebase mRef;
    private ArrayList<String> mNote = new ArrayList<>();
    private String email;
    private String[] splitArray = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evenement);
        //Connexion vers la table "Evenement" dans firebase
        mRef = new Firebase("https://mmmproject-97561.firebaseio.com/Evenement");



        Intent intent = getIntent();
        event = (Event) intent.getSerializableExtra("MyClass");
        email = (String) intent.getSerializableExtra("EMAIL");

        splitArray = email.split("@");
        email = splitArray[0];

        ImageView imageView = findViewById(R.id.imageEvent);
        TextView tvTitre = findViewById(R.id.titre);
        TextView tvDescription= findViewById(R.id.description);
        TextView tvVille = findViewById(R.id.ville);
        TextView tvRegion = findViewById(R.id.region);
        TextView tvDate = findViewById(R.id.date);
        TextView tvTelephone = findViewById(R.id.motsCles);
        ImageView imageCall = findViewById(R.id.image_call);
        TextView tvMotscles = findViewById(R.id.motsCles);
        TextView tvThematique = findViewById(R.id.thematique);
        Button share = findViewById(R.id.share);
        RatingBar rbStar = findViewById(R.id.ratingBar);
        Button save = findViewById(R.id.save);
        ListView notesList = findViewById(R.id.listNotes);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mNote);
        notesList.setAdapter(arrayAdapter);


        Picasso.get().load(event.getImage_url()).fit().centerInside().into(imageView);
        tvTitre.setText("Titre : " + event.getTitre());
        tvDescription.setText("Description : " + event.getDescription());
        tvVille.setText("Ville : " + event.getVille());
        tvRegion.setText("Region : " + event.getRegion());
        tvMotscles.setText("Mots clés : " + event.getMots_cles());
        tvDate.setText("Dates : " + event.getDate());
        tvTelephone.setText("Téléphone : " + event.getTelephone());
        tvThematique.setText("Thématique : " + event.getThematique());

        imageCall.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        // Méthode pour le partage des évènements par email ou sur les réseaux sociaux
        share.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, event.getTitre());
                intent.putExtra(Intent.EXTRA_TEXT, event.getDescription());
                startActivity(Intent.createChooser(intent, "Share using"));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase mRefChild = mRef.child(String.valueOf(event.getId())).child(email);
                mRefChild.setValue(rbStar.getRating());
            }
        });

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String keyEvent = dataSnapshot.getKey();

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    if(keyEvent.equals(String.valueOf(event.getId()))){
                        Float value = childSnapshot.getValue(Float.class);
                        String key = childSnapshot.getKey();
                        mNote.add(key + " : " + value);
                    }
                }
                arrayAdapter.notifyDataSetChanged();
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

    // Permettre de passer un appel
    public void makePhoneCall(){
        String number = event.getTelephone();
        if(number.trim().length() > 0){
            if(ContextCompat.checkSelfPermission(EvenementActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(EvenementActivity.this,
                        new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            }else{
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }else{
            Toast.makeText(EvenementActivity.this, "Numéro de téléphone vide", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL){
            if(grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                makePhoneCall();
            }else{
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_LONG).show();
            }
        }
    }
}
