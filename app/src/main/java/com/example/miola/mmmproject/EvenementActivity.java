package com.example.miola.mmmproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class EvenementActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evenement);

        ImageView imageView = findViewById(R.id.imageEvent);
        TextView tvTitre = findViewById(R.id.titre);
        TextView tvDescription= findViewById(R.id.description);
        TextView tvVille = findViewById(R.id.ville);
        TextView tvRegion = findViewById(R.id.region);
        TextView tvDate = findViewById(R.id.date);
        TextView tvTelephone = findViewById(R.id.motsCles);
        ImageView imageCall = findViewById(R.id.image_call);
        TextView tvMotscles = findViewById(R.id.motsCles);



        Intent intent = getIntent();
        event = (Event) intent.getSerializableExtra("MyClass");

        Picasso.get().load(event.getImage_url()).fit().centerInside().into(imageView);
        tvTitre.setText("Titre : " + event.getTitre());
        tvDescription.setText("Description " + event.getDescription());
        tvVille.setText("Ville : " + event.getVille());
        tvRegion.setText("Region : " + event.getRegion());
        tvMotscles.setText("Mots clés : " + event.getMots_cles());
        tvDate.setText("Dates : " + event.getDate());
        tvTelephone.setText("Téléphone : " + event.getTelephone());

        imageCall.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

    }

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
