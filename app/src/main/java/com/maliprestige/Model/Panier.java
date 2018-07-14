package com.maliprestige.Model;

public class Panier {
    private int id;
    private String token;
    private int produitId;
    private int quantite;
    private float prixQuantite;
    private String nomProduit;
    private String imageProduit;

    @Override
    public String toString() {
        return "{\"id\":\""+id+"\"," +
                "\"produitId\":\""+produitId+"\"," +
                "\"quantite\":\""+quantite+"\"," +
                "\"prixQuantite\":\""+prixQuantite+"\"," +
                "\"nomProduit\":\""+nomProduit+"\"," +
                "\"imageProduit\":\""+imageProduit+"\"," +
                "\"token\":"+token+"}";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getProduitId() {
        return produitId;
    }

    public void setProduitId(int produitId) {
        this.produitId = produitId;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public float getPrixQuantite() {
        return prixQuantite;
    }

    public void setPrixQuantite(float prixQuantite) {
        this.prixQuantite = prixQuantite;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getImageProduit() {
        return imageProduit;
    }

    public void setImageProduit(String imageProduit) {
        this.imageProduit = imageProduit;
    }
}
