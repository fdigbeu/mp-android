package com.maliprestige.Model;

public class Adresse {
    private int id;
    private int adresseId;
    private String type;
    private String token;
    private String destinataire;
    private String libelle;

    @Override
    public String toString() {
        return "{\"id\":\""+id+"\"," +
                "\"adresseId\":\""+adresseId+"\"," +
                "\"type\":\""+type+"\"," +
                "\"destinataire\":\""+destinataire+"\"," +
                "\"libelle\":\""+libelle+"\"," +
                "\"token\":"+token+"}";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdresseId() {
        return adresseId;
    }

    public void setAdresseId(int adresseId) {
        this.adresseId = adresseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
