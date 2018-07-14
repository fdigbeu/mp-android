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
}
