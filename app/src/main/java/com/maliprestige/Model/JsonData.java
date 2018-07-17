package com.maliprestige.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonData {
    private String jsonString;
    private Adresse adresse;

    public JsonData(String jsonString) {
        this.jsonString = jsonString;
    }

    public Client getClientFromJson() {
        try {
            if (jsonString != null && !jsonString.isEmpty()) {
                Client client = new Client();
                JSONObject jsonObject = new JSONObject(jsonString);
                String civilite = jsonObject.getString("civilite");
                String nom = jsonObject.getString("nom");
                String prenom = jsonObject.getString("prenom");
                String telPort = jsonObject.getString("telPort");
                String telFixe = jsonObject.getString("telFixe");
                String email = jsonObject.getString("email");
                String token = jsonObject.getString("token");
                client.setCivilite(civilite);
                client.setNom(nom);
                client.setPrenom(prenom);
                client.setTelPort(telPort);
                client.setTelFixe(telFixe);
                client.setEmail(email);
                client.setToken(token);
                return client;
            }
        }
        catch (JSONException ex){
            return null;
        }
        return null;
    }

    public Adresse getAdresseFacturationFromJson(String token) {
        try {
            if(jsonString != null && !jsonString.isEmpty()) {
                JSONObject jsonObject = new JSONObject(jsonString);
                Adresse adresse = new Adresse();
                String libelleAdresse = jsonObject.getString("libelleAdresse");
                adresse.setType("facturation");
                int adresseId = jsonObject.getInt("adresseId");
                adresse.setAdresseId(adresseId);
                String destinataire = libelleAdresse.split(":")[0].trim();
                adresse.setDestinataire(destinataire);
                String libelle = libelleAdresse.replace(destinataire + " : ", "").trim();
                adresse.setLibelle(libelle);
                adresse.setToken(token);
                return adresse;
            }
            return null;
        }
        catch (JSONException ex){
            return null;
        }
    }

    public ArrayList<Adresse> getAdressesFacturationsFromJson(String token) {
        try {
            if(jsonString != null && !jsonString.isEmpty()) {
                ArrayList<Adresse> adresses = new ArrayList<>();
                JSONArray resultats = new JSONArray(jsonString);
                for (int i = 0; i < resultats.length(); i++) {
                    JSONObject jsonObject = resultats.getJSONObject(i);
                    Adresse adresse = new Adresse();
                    String libelleAdresse = jsonObject.getString("libelleAdresse");
                    adresse.setType("facturation");
                    int adresseId = jsonObject.getInt("id");
                    adresse.setAdresseId(adresseId);
                    String destinataire = libelleAdresse.split(":")[0].trim();
                    adresse.setDestinataire(destinataire);
                    String libelle = libelleAdresse.replace(destinataire + " : ", "").trim();
                    adresse.setLibelle(libelle);
                    adresse.setToken(token);
                    adresses.add(adresse);
                }
                return adresses;
            }
            return null;
        }
        catch (JSONException ex){
            return null;
        }
    }

    public Adresse getAdresseLivraisonFromJson(String token) {
        try {
            if(jsonString != null && !jsonString.isEmpty()) {
                JSONObject jsonObject = new JSONObject(jsonString);
                Adresse adresse = new Adresse();
                String libelleAdresse = jsonObject.getString("libelleAdresse");
                adresse.setType("livraison");
                int adresseId = jsonObject.getInt("adresseId");
                adresse.setAdresseId(adresseId);
                String destinataire = libelleAdresse.split(":")[0].trim();
                adresse.setDestinataire(destinataire);
                String libelle = libelleAdresse.replace(destinataire + " : ", "").trim();
                adresse.setLibelle(libelle);
                adresse.setToken(token);
                return adresse;
            }
            return null;
        }
        catch (JSONException ex){
            return null;
        }
    }

    public ArrayList<Adresse> getAdressesLivraisonFromJson(String token) {
        try {
            if(jsonString != null && !jsonString.isEmpty()) {
                ArrayList<Adresse> adresses = new ArrayList<>();
                JSONArray resultats = new JSONArray(jsonString);
                for (int i = 0; i < resultats.length(); i++) {
                    JSONObject jsonObject = resultats.getJSONObject(i);
                    Adresse adresse = new Adresse();
                    String libelleAdresse = jsonObject.getString("libelleAdresse");
                    adresse.setType("livraison");
                    int adresseId = jsonObject.getInt("id");
                    adresse.setAdresseId(adresseId);
                    String destinataire = libelleAdresse.split(":")[0].trim();
                    adresse.setDestinataire(destinataire);
                    String libelle = libelleAdresse.replace(destinataire + " :", "").trim();
                    adresse.setLibelle(libelle);
                    adresse.setToken(token);
                    adresses.add(adresse);
                }
                return adresses;
            }
            return null;
        }
        catch (JSONException ex){
            return null;
        }
    }

    // Retrieve all search data (Product object)
    public ArrayList<Search> getSearchDataFromJson() {
        try {
            if(jsonString != null && !jsonString.isEmpty()) {
                ArrayList<Search> searches = new ArrayList<>();
                JSONArray resultats = new JSONArray(jsonString);
                for (int i = 0; i < resultats.length(); i++) {
                    JSONObject jsonObject = resultats.getJSONObject(i);
                    Search search = new Search();
                    search.setProduitId(jsonObject.getInt("produitId"));
                    search.setNomProduit(jsonObject.getString("nomProduit"));
                    search.setImage1(jsonObject.getString("image1"));
                    search.setImage2(jsonObject.getString("image2"));
                    search.setImage3(jsonObject.getString("image3"));
                    searches.add(search);
                }
                return searches;
            }
            return null;
        }
        catch (JSONException ex){
            return null;
        }
    }

    // Retrieve all search name (Product name)
    public ArrayList<String> getAutoCompleteSearchDataFromJson() {
        try {
            if(jsonString != null && !jsonString.isEmpty()) {
                ArrayList<String> searches = new ArrayList<>();
                JSONArray resultats = new JSONArray(jsonString);
                for (int i = 0; i < resultats.length(); i++) {
                    JSONObject jsonObject = resultats.getJSONObject(i);
                    searches.add(jsonObject.getString("nomProduit"));
                }
                return searches;
            }
            return null;
        }
        catch (JSONException ex){
            return null;
        }
    }
}
