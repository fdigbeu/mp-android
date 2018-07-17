package com.maliprestige.Model;

public class Search {
    private int produitId;
    private String nomProduit;
    private String image1;
    private String image2;
    private String image3;

    @Override
    public String toString() {
        return "{\"produitId\":\""+produitId+"\"," +
                "\"image1\":\""+image1+"\"," +
                "\"image2\":\""+image2+"\"," +
                "\"image3\":"+image3+"}";
    }

    public int getProduitId() {
        return produitId;
    }

    public void setProduitId(int produitId) {
        this.produitId = produitId;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }
}
