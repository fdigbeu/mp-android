package com.maliprestige.Model;

public class Slide {
    private String titre;
    private String url;

    @Override
    public String toString() {
        return "{\"titre\":\""+titre+"\"," +
                "\"url\":"+url+"}";
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
