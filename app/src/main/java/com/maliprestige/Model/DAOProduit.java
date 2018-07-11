package com.maliprestige.Model;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class DAOProduit {
    private DAOConnexion connexion;
    private Context context;
    private String table_name;

    public DAOProduit(Context context){
        this.context = context;
        table_name = "mp_produit";
    }

    public void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS "+table_name+" (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "produitId INTEGER, " +
                "nom VARCHAR, " +
                "descrition VARCHAR, " +
                "prixUnitaire VARCHAR, " +
                "prixUnitaireTtc VARCHAR, " +
                "prixUnitaireGros VARCHAR, " +
                "prixExpedition VARCHAR, " +
                "image1 VARCHAR, " +
                "image2 VARCHAR, " +
                "image3 VARCHAR, " +
                "date VARCHAR, " +
                "quantite VARCHAR, " +
                "isNouveaute VARCHAR, " +
                "isReduction VARCHAR, " +
                "prixReduction VARCHAR, " +
                "prixReductionTtc VARCHAR, " +
                "dateReduction VARCHAR, " +
                "delaiJourMin VARCHAR, " +
                "delaiJourMax VARCHAR, " +
                "dateFinReduction VARCHAR, " +
                "isTailleStandard VARCHAR, " +
                "modeEmploi VARCHAR, " +
                "typeCoupeId VARCHAR, " +
                "typeCoupeLibelle VARCHAR, " +
                "typeProduitId VARCHAR, " +
                "typeProduitLibelle VARCHAR, " +
                "matiereId VARCHAR, " +
                "matiereLibelle VARCHAR, " +
                "typeMancheId VARCHAR, " +
                "typeMancheLibelle VARCHAR);";
        connexion = new DAOConnexion(context);
        connexion.getDb().execSQL(sql);
    }

    public void add(Produit produit){
        createTable();
        String sql = "INSERT INTO " + table_name + " (produitId, nom, descrition, prixUnitaire, prixUnitaireTtc, prixUnitaireGros, " +
                "prixExpedition, image1, image2, image3, date, quantite, isNouveaute, isReduction, prixReduction, prixReductionTtc, " +
                "dateReduction, delaiJourMin, delaiJourMax, dateFinReduction, isTailleStandard, modeEmploi, typeCoupeId, typeCoupeLibelle, " +
                "typeProduitId, typeProduitLibelle, matiereId, matiereLibelle, typeMancheId, typeMancheLibelle)" +
                " VALUES ('"+produit.getProduitId()+"', '"+produit.getNom().replace("'", "''")+"', " +
                "'"+produit.getDescrition().replace("'", "''")+"', " +
                "'"+produit.getPrixUnitaire()+"', " +
                "'"+produit.getPrixUnitaireTtc()+"', " +
                "'"+produit.getPrixUnitaireGros()+"', " +
                "'"+produit.getPrixExpedition()+"', " +
                "'"+produit.getImage1().replace("'", "''")+"', " +
                "'"+produit.getImage2().replace("'", "''")+"', " +
                "'"+produit.getImage3().replace("'", "''")+"', " +
                "'"+produit.getDate().replace("'", "''")+"', " +
                "'"+produit.getQuantite()+"', " +
                "'"+produit.isNouveaute()+"', " +
                "'"+produit.isReduction()+"', " +
                "'"+produit.getPrixReduction()+"', " +
                "'"+produit.getPrixReductionTtc()+"', " +
                "'"+produit.getDateReduction()+"', " +
                "'"+produit.getDelaiJourMin()+"', " +
                "'"+produit.getDelaiJourMax()+"', " +
                "'"+produit.getDateFinReduction()+"', " +
                "'"+produit.isTailleStandard()+"', " +
                "'"+produit.getModeEmploi().replace("'", "''")+"', " +
                "'"+produit.getTypeCoupeId()+"', " +
                "'"+produit.getTypeCoupeLibelle().replace("'", "''")+"', " +
                "'"+produit.getTypeProduitId()+"', " +
                "'"+produit.getTypeProduitLibelle().replace("'", "''")+"', " +
                "'"+produit.getMatiereId()+"', " +
                "'"+produit.getMatiereLibelle().replace("'", "''")+"', " +
                "'"+produit.getTypeMancheId()+"', " +
                "'"+produit.getTypeMancheLibelle().replace("'", "''")+"');";
        connexion.getDb().execSQL(sql);
    }

    public ArrayList<Produit> getAllBy(String mTypeCoupeLibelle){
        createTable();
        ArrayList<Produit> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name +" WHERE LOWER(typeCoupeLibelle) LIKE '"+mTypeCoupeLibelle+"%' ORDER BY id ASC", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            Produit produit = new Produit();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int produitId = cursor.getInt(cursor.getColumnIndex("produitId"));
            String nom = cursor.getString(cursor.getColumnIndex("nom"));
            String descrition = cursor.getString(cursor.getColumnIndex("descrition"));
            String prixUnitaire = cursor.getString(cursor.getColumnIndex("prixUnitaire"));
            String prixUnitaireTtc = cursor.getString(cursor.getColumnIndex("prixUnitaireTtc"));
            String prixUnitaireGros = cursor.getString(cursor.getColumnIndex("prixUnitaireGros"));
            String prixExpedition = cursor.getString(cursor.getColumnIndex("prixExpedition"));
            String image1 = cursor.getString(cursor.getColumnIndex("image1"));
            String image2 = cursor.getString(cursor.getColumnIndex("image2"));
            String image3 = cursor.getString(cursor.getColumnIndex("image3"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String quantite = cursor.getString(cursor.getColumnIndex("quantite"));
            String isNouveaute = cursor.getString(cursor.getColumnIndex("isNouveaute"));
            String isReduction = cursor.getString(cursor.getColumnIndex("isReduction"));
            String prixReduction = cursor.getString(cursor.getColumnIndex("prixReduction"));
            String prixReductionTtc = cursor.getString(cursor.getColumnIndex("prixReductionTtc"));
            String dateReduction = cursor.getString(cursor.getColumnIndex("dateReduction"));
            String delaiJourMin = cursor.getString(cursor.getColumnIndex("delaiJourMin"));
            String delaiJourMax = cursor.getString(cursor.getColumnIndex("delaiJourMax"));
            String dateFinReduction = cursor.getString(cursor.getColumnIndex("dateFinReduction"));
            String isTailleStandard = cursor.getString(cursor.getColumnIndex("isTailleStandard"));
            String modeEmploi = cursor.getString(cursor.getColumnIndex("modeEmploi"));
            String typeCoupeId = cursor.getString(cursor.getColumnIndex("typeCoupeId"));
            String typeCoupeLibelle = cursor.getString(cursor.getColumnIndex("typeCoupeLibelle"));
            String typeProduitId = cursor.getString(cursor.getColumnIndex("typeProduitId"));
            String typeProduitLibelle = cursor.getString(cursor.getColumnIndex("typeProduitLibelle"));
            String matiereId = cursor.getString(cursor.getColumnIndex("matiereId"));
            String matiereLibelle = cursor.getString(cursor.getColumnIndex("matiereLibelle"));
            String typeMancheId = cursor.getString(cursor.getColumnIndex("typeMancheId"));
            String typeMancheLibelle = cursor.getString(cursor.getColumnIndex("typeMancheLibelle"));
            //--
            produit.setId(id);
            produit.setProduitId(produitId);
            produit.setNom(nom);
            produit.setDescrition(descrition);
            produit.setPrixUnitaire(Float.parseFloat(prixUnitaire));
            produit.setPrixUnitaireTtc(Float.parseFloat(prixUnitaireTtc));
            produit.setPrixUnitaireGros(Float.parseFloat(prixUnitaireGros));
            produit.setPrixExpedition(Float.parseFloat(prixExpedition));
            produit.setImage1(image1);
            produit.setImage2(image2);
            produit.setImage3(image3);
            produit.setDate(date);
            produit.setQuantite(Integer.parseInt(quantite));
            produit.setNouveaute(Boolean.parseBoolean(isNouveaute));
            produit.setReduction(Boolean.parseBoolean(isReduction));
            produit.setPrixReduction(Float.parseFloat(prixReduction));
            produit.setPrixReductionTtc(Float.parseFloat(prixReductionTtc));
            produit.setDateReduction(dateReduction);
            produit.setDelaiJourMin(Integer.parseInt(delaiJourMin));
            produit.setDelaiJourMax(Integer.parseInt(delaiJourMax));
            produit.setDateFinReduction(dateFinReduction);
            produit.setTailleStandard(Boolean.parseBoolean(isTailleStandard));
            produit.setModeEmploi(modeEmploi);
            produit.setTypeCoupeId(Integer.parseInt(typeCoupeId));
            produit.setTypeCoupeLibelle(typeCoupeLibelle);
            produit.setTypeProduitId(Integer.parseInt(typeProduitId));
            produit.setTypeProduitLibelle(typeProduitLibelle);
            produit.setMatiereId(Integer.parseInt(matiereId));
            produit.setMatiereLibelle(matiereLibelle);
            produit.setTypeMancheId(Integer.parseInt(typeMancheId));
            produit.setTypeMancheLibelle(typeMancheLibelle);
            resultat.add(produit);
            //--
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return resultat;
    }

    public ArrayList<Produit> getAll(){
        createTable();
        ArrayList<Produit> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name +" ORDER BY id ASC", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            Produit produit = new Produit();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int produitId = cursor.getInt(cursor.getColumnIndex("produitId"));
            String nom = cursor.getString(cursor.getColumnIndex("nom"));
            String descrition = cursor.getString(cursor.getColumnIndex("descrition"));
            String prixUnitaire = cursor.getString(cursor.getColumnIndex("prixUnitaire"));
            String prixUnitaireTtc = cursor.getString(cursor.getColumnIndex("prixUnitaireTtc"));
            String prixUnitaireGros = cursor.getString(cursor.getColumnIndex("prixUnitaireGros"));
            String prixExpedition = cursor.getString(cursor.getColumnIndex("prixExpedition"));
            String image1 = cursor.getString(cursor.getColumnIndex("image1"));
            String image2 = cursor.getString(cursor.getColumnIndex("image2"));
            String image3 = cursor.getString(cursor.getColumnIndex("image3"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String quantite = cursor.getString(cursor.getColumnIndex("quantite"));
            String isNouveaute = cursor.getString(cursor.getColumnIndex("isNouveaute"));
            String isReduction = cursor.getString(cursor.getColumnIndex("isReduction"));
            String prixReduction = cursor.getString(cursor.getColumnIndex("prixReduction"));
            String prixReductionTtc = cursor.getString(cursor.getColumnIndex("prixReductionTtc"));
            String dateReduction = cursor.getString(cursor.getColumnIndex("dateReduction"));
            String delaiJourMin = cursor.getString(cursor.getColumnIndex("delaiJourMin"));
            String delaiJourMax = cursor.getString(cursor.getColumnIndex("delaiJourMax"));
            String dateFinReduction = cursor.getString(cursor.getColumnIndex("dateFinReduction"));
            String isTailleStandard = cursor.getString(cursor.getColumnIndex("isTailleStandard"));
            String modeEmploi = cursor.getString(cursor.getColumnIndex("modeEmploi"));
            String typeCoupeId = cursor.getString(cursor.getColumnIndex("typeCoupeId"));
            String typeCoupeLibelle = cursor.getString(cursor.getColumnIndex("typeCoupeLibelle"));
            String typeProduitId = cursor.getString(cursor.getColumnIndex("typeProduitId"));
            String typeProduitLibelle = cursor.getString(cursor.getColumnIndex("typeProduitLibelle"));
            String matiereId = cursor.getString(cursor.getColumnIndex("matiereId"));
            String matiereLibelle = cursor.getString(cursor.getColumnIndex("matiereLibelle"));
            String typeMancheId = cursor.getString(cursor.getColumnIndex("typeMancheId"));
            String typeMancheLibelle = cursor.getString(cursor.getColumnIndex("typeMancheLibelle"));
            //--
            produit.setId(id);
            produit.setProduitId(produitId);
            produit.setNom(nom);
            produit.setDescrition(descrition);
            produit.setPrixUnitaire(Float.parseFloat(prixUnitaire));
            produit.setPrixUnitaireTtc(Float.parseFloat(prixUnitaireTtc));
            produit.setPrixUnitaireGros(Float.parseFloat(prixUnitaireGros));
            produit.setPrixExpedition(Float.parseFloat(prixExpedition));
            produit.setImage1(image1);
            produit.setImage2(image2);
            produit.setImage3(image3);
            produit.setDate(date);
            produit.setQuantite(Integer.parseInt(quantite));
            produit.setNouveaute(Boolean.parseBoolean(isNouveaute));
            produit.setReduction(Boolean.parseBoolean(isReduction));
            produit.setPrixReduction(Float.parseFloat(prixReduction));
            produit.setPrixReductionTtc(Float.parseFloat(prixReductionTtc));
            produit.setDateReduction(dateReduction);
            produit.setDelaiJourMin(Integer.parseInt(delaiJourMin));
            produit.setDelaiJourMax(Integer.parseInt(delaiJourMax));
            produit.setDateFinReduction(dateFinReduction);
            produit.setTailleStandard(Boolean.parseBoolean(isTailleStandard));
            produit.setModeEmploi(modeEmploi);
            produit.setTypeCoupeId(Integer.parseInt(typeCoupeId));
            produit.setTypeCoupeLibelle(typeCoupeLibelle);
            produit.setTypeProduitId(Integer.parseInt(typeProduitId));
            produit.setTypeProduitLibelle(typeProduitLibelle);
            produit.setMatiereId(Integer.parseInt(matiereId));
            produit.setMatiereLibelle(matiereLibelle);
            produit.setTypeMancheId(Integer.parseInt(typeMancheId));
            produit.setTypeMancheLibelle(typeMancheLibelle);
            resultat.add(produit);
            //--
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return resultat;
    }

    // Delete all
    public void deleteAll()
    {
        createTable();
        String sql = "DELETE FROM " + table_name;
        connexion.getDb().execSQL(sql);
    }

    // Delete by
    public void deleteBy(String mTypeCoupeLibelle)
    {
        createTable();
        String sql = "DELETE FROM "+table_name+" WHERE LOWER(typeCoupeLibelle) LIKE '"+mTypeCoupeLibelle+"%'";
        connexion.getDb().execSQL(sql);
    }
}
