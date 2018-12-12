package com.example.miola.mmmproject;

public class Event {
    private String image_url;
    private String description;
    private String ville;
    private String titre;
    //ajouter les autres info plus tard

    public Event(String iu, String t){
        this.image_url=iu;
        this.titre=t;
    }

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
}
