package com.maliprestige.Model;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class DAOCommande {
    private DAOConnexion connexion;
    private Context context;
    private String table_name;

    public DAOCommande(Context context){
        this.context = context;
        table_name = "mp_commande";
    }

    public void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS "+table_name+" (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "commandeId INTEGER, " +
                "numeroCommande VARCHAR, " +
                "montantCommande VARCHAR, " +
                "dateValidation VARCHAR, " +
                "dateAnnulation VARCHAR, " +
                "dateRetour VARCHAR, " +
                "dateFournisseur VARCHAR, " +
                "dateExpedition VARCHAR, " +
                "dateReception VARCHAR, " +
                "dateEnPreparation VARCHAR, " +
                "numeroColis VARCHAR, " +
                "dateAchat VARCHAR, " +
                "statutLibelle VARCHAR, " +
                "statutDetail VARCHAR, " +
                "libelleDateLivraison VARCHAR, " +
                "libelleAdresseLivraison VARCHAR, " +
                "libelleAdresseFacturation VARCHAR, " +
                "numeroFacture VARCHAR, " +
                "modePaiement VARCHAR, " +
                "montantRegle VARCHAR, " +
                "isFactureAcquittee VARCHAR, " +
                "dateAcquittement VARCHAR, " +
                "token VARCHAR);";
        connexion = new DAOConnexion(context);
        connexion.getDb().execSQL(sql);
    }

    public void add(Commande commande){
        createTable();
        String sql = "INSERT INTO " + table_name + " (commandeId, numeroCommande, montantCommande, dateValidation, dateAnnulation, dateRetour, " +
                "dateFournisseur, dateExpedition, dateReception, dateEnPreparation, numeroColis, dateAchat, statutLibelle, statutDetail, libelleDateLivraison, libelleAdresseLivraison, " +
                "libelleAdresseFacturation, numeroFacture, modePaiement, montantRegle, isFactureAcquittee, dateAcquittement, token)" +
                " VALUES ('"+commande.getCommandeId()+"', '"+commande.getNumeroCommande().replace("'", "''")+"', " +
                "'"+commande.getMontantCommande()+"', " +
                "'"+commande.getDateValidation()+"', " +
                "'"+commande.getDateAnnulation()+"', " +
                "'"+commande.getDateRetour()+"', " +
                "'"+commande.getDateFournisseur()+"', " +
                "'"+commande.getDateExpedition()+"', " +
                "'"+commande.getDateReception()+"', " +
                "'"+commande.getDateEnPreparation()+"', " +
                "'"+commande.getNumeroColis()+"', " +
                "'"+commande.getDateAchat()+"', " +
                "'"+commande.getStatutLibelle().replace("'", "''")+"', " +
                "'"+commande.getStatutDetail().replace("'", "''")+"', " +
                "'"+commande.getLibelleDateLivraison()+"', " +
                "'"+commande.getLibelleAdresseLivraison().replace("'", "''")+"', " +
                "'"+commande.getLibelleAdresseFacturation().replace("'", "''")+"', " +
                "'"+commande.getNumeroFacture()+"', " +
                "'"+commande.getModePaiement()+"', " +
                "'"+commande.getMontantRegle()+"', " +
                "'"+commande.isFactureAcquittee()+"', " +
                "'"+commande.getDateAcquittement()+"', " +
                "'"+commande.getToken()+"');";
        connexion.getDb().execSQL(sql);
    }

    public ArrayList<Commande> getAllBy(String mToken){
        createTable();
        ArrayList<Commande> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name +" WHERE token LIKE '"+mToken+"' ORDER BY id ASC", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            Commande commande = new Commande();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int commandeId = cursor.getInt(cursor.getColumnIndex("commandeId"));
            String numeroCommande = cursor.getString(cursor.getColumnIndex("numeroCommande"));
            float montantCommande = cursor.getFloat(cursor.getColumnIndex("montantCommande"));
            String dateValidation = cursor.getString(cursor.getColumnIndex("dateValidation"));
            String dateAnnulation = cursor.getString(cursor.getColumnIndex("dateAnnulation"));
            String dateRetour = cursor.getString(cursor.getColumnIndex("dateRetour"));
            String dateFournisseur = cursor.getString(cursor.getColumnIndex("dateFournisseur"));
            String dateExpedition = cursor.getString(cursor.getColumnIndex("dateExpedition"));
            String dateReception = cursor.getString(cursor.getColumnIndex("dateReception"));
            String dateEnPreparation = cursor.getString(cursor.getColumnIndex("dateEnPreparation"));
            String numeroColis = cursor.getString(cursor.getColumnIndex("numeroColis"));
            String dateAchat = cursor.getString(cursor.getColumnIndex("dateAchat"));
            String statutLibelle = cursor.getString(cursor.getColumnIndex("statutLibelle"));
            String statutDetail = cursor.getString(cursor.getColumnIndex("statutDetail"));
            String libelleDateLivraison = cursor.getString(cursor.getColumnIndex("libelleDateLivraison"));
            String libelleAdresseLivraison = cursor.getString(cursor.getColumnIndex("libelleAdresseLivraison"));
            String libelleAdresseFacturation = cursor.getString(cursor.getColumnIndex("libelleAdresseFacturation"));
            String numeroFacture = cursor.getString(cursor.getColumnIndex("numeroFacture"));
            String modePaiement = cursor.getString(cursor.getColumnIndex("modePaiement"));
            float montantRegle = cursor.getFloat(cursor.getColumnIndex("montantRegle"));
            String isFactureAcquittee = cursor.getString(cursor.getColumnIndex("isFactureAcquittee"));
            String dateAcquittement = cursor.getString(cursor.getColumnIndex("dateAcquittement"));
            String token = cursor.getString(cursor.getColumnIndex("token"));
            //--
            commande.setId(id);
            commande.setCommandeId(commandeId);
            commande.setNumeroCommande(numeroCommande);
            commande.setMontantCommande(montantCommande);
            commande.setDateValidation(dateValidation);
            commande.setDateAnnulation(dateAnnulation);
            commande.setDateRetour(dateRetour);
            commande.setDateFournisseur(dateFournisseur);
            commande.setDateExpedition(dateExpedition);
            commande.setDateReception(dateReception);
            commande.setDateEnPreparation(dateEnPreparation);
            commande.setNumeroColis(numeroColis);
            commande.setDateAchat(dateAchat);
            commande.setStatutLibelle(statutLibelle);
            commande.setStatutDetail(statutDetail);
            commande.setLibelleDateLivraison(libelleDateLivraison);
            commande.setLibelleAdresseLivraison(libelleAdresseLivraison);
            commande.setLibelleAdresseFacturation(libelleAdresseFacturation);
            commande.setNumeroFacture(numeroFacture);
            commande.setModePaiement(modePaiement);
            commande.setMontantRegle(montantRegle);
            commande.setFactureAcquittee(Boolean.parseBoolean(isFactureAcquittee));
            commande.setDateAcquittement(dateAcquittement);
            commande.setToken(token);
            //--
            DAOCommandeProduit daoCommandeProduit = new DAOCommandeProduit(context);
            ArrayList<CommandeProduit> commandeProduits = daoCommandeProduit.getAllBy(token, numeroCommande);
            commande.setCommandeProduits(commandeProduits);
            //--
            resultat.add(commande);
            //--
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return resultat;
    }

    // Delete by
    public void deleteBy(String mToken)
    {
        createTable();
        String sql = "DELETE FROM "+table_name+" WHERE token LIKE '"+mToken+"'";
        connexion.getDb().execSQL(sql);
    }
}
