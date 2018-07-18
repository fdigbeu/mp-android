package com.maliprestige.Model;

public class CommandeProduit {
    private int id;
    private int produitId;
    private String numeroCommande;
    private String nomProduit;
    private int qteProduit;
    private float prixQteProduit;
    private String token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduitId() {
        return produitId;
    }

    public void setProduitId(int produitId) {
        this.produitId = produitId;
    }

    public String getNumeroCommande() {
        return numeroCommande;
    }

    public void setNumeroCommande(String numeroCommande) {
        this.numeroCommande = numeroCommande;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public int getQteProduit() {
        return qteProduit;
    }

    public void setQteProduit(int qteProduit) {
        this.qteProduit = qteProduit;
    }

    public float getPrixQteProduit() {
        return prixQteProduit;
    }

    public void setPrixQteProduit(float prixQteProduit) {
        this.prixQteProduit = prixQteProduit;
    }
}
