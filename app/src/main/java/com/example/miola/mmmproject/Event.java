package com.example.miola.mmmproject;

import java.io.Serializable;

public class Event implements Serializable {

    private int id;
    private String image_url;
    private String titre;
    private String description;
    private String ville;
    private String region;
    private String mots_cles;
    private String date;
    private String telephone;
    private String thematique;
    private String latitude;
    private String longitude;
    private String organisateur;
    private String tauxRemplissage;

    public Event(int id, String image, String titre, String description, String ville, String region, String mots_cles, String date,
                 String telephone, String thematique, String latitude, String longitude){
        this.id = id;
        this.image_url = image;
        this.titre = titre;
        this.description = description;
        this.ville = ville;
        this.region = region;
        this.mots_cles = mots_cles;
        this.date = date;
        this.telephone = telephone;
        this.thematique = thematique;
        this.latitude = latitude;
        this.longitude = longitude;
        this.organisateur = null;
        this.tauxRemplissage = null;
    }

    public int getId() { return id;}

    public void setId(int id) { this.id = id; }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegion() { return region; }

    public void setRegion(String region) { this.region = region; }

    public String getMots_cles() { return mots_cles; }

    public void setMots_cles(String mots_cles) { this.mots_cles = mots_cles; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getTelephone() { return telephone; }

    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getThematique() { return thematique; }

    public void setThematique(String thematique) { this.thematique = thematique; }

    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getOrganisateur() { return organisateur; }

    public void setOrganisateur(String organisateur) { this.organisateur = organisateur; }

    public String getTauxRemplissage() { return tauxRemplissage; }

    public void setTauxRemplissage(String tauxRemplissage) { this.tauxRemplissage = tauxRemplissage; }
}
