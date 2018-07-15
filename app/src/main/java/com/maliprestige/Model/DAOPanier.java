package com.maliprestige.Model;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class DAOPanier {
    private DAOConnexion connexion;
    private Context context;
    private String table_name;

    public DAOPanier(Context context){
        this.context = context;
        table_name = "mp_panier";
    }

    public void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS "+table_name+" (id INTEGER PRIMARY KEY AUTOINCREMENT, token VARCHAR, produitId VARCHAR, quantite VARCHAR, nomProduit VARCHAR, imageProduit VARCHAR, delaiJourMin INTEGER, delaiJourMax INTEGER, prixQuantite VARCHAR);";
        connexion = new DAOConnexion(context);
        connexion.getDb().execSQL(sql);
    }

    public void add(Panier panier){
        createTable();
        String sql = "INSERT INTO " + table_name + " (token, produitId, quantite, nomProduit, imageProduit, delaiJourMin, delaiJourMax, prixQuantite)" +
                " VALUES ('"+panier.getToken().replace("'", "''")+"', " +
                "'"+panier.getProduitId()+"', " +
                "'"+panier.getQuantite()+"', " +
                "'"+panier.getNomProduit().replace("'", "''")+"', " +
                "'"+panier.getImageProduit().replace("'", "''")+"', " +
                "'"+panier.getDelaiJourMin()+"', " +
                "'"+panier.getDelaiJourMax()+"', " +
                "'"+panier.getPrixQuantite()+"');";
        connexion.getDb().execSQL(sql);
    }

    public void modify(String mToken, int mProduitId, int mQuantite, float mPrixQuantite)
    {
        createTable();
        String sql = "UPDATE " + table_name + " SET quantite = '"+mQuantite+"', prixQuantite = '"+mPrixQuantite+"'  WHERE token LIKE '"+mToken+"' AND produitId = '"+mProduitId+"'";
        connexion.getDb().execSQL(sql);
    }

    public void modifyByToken(String mOldToken, String mNewToken)
    {
        createTable();
        String sql = "UPDATE " + table_name + " SET token = '"+mNewToken+"'  WHERE token LIKE '"+mOldToken+"'";
        connexion.getDb().execSQL(sql);
    }

    public Panier getInfoBy(String mToken, int mProduitId){
        createTable();
        ArrayList<Panier> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name + " " +
                "WHERE token LIKE '"+mToken.replace("'", "''")+"' AND produitId = '"+mProduitId+"'", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            Panier panier = new Panier();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String token = cursor.getString(cursor.getColumnIndex("token"));
            int produitId = cursor.getInt(cursor.getColumnIndex("produitId"));
            int quantite = cursor.getInt(cursor.getColumnIndex("quantite"));
            String nomProduit = cursor.getString(cursor.getColumnIndex("nomProduit"));
            String imageProduit = cursor.getString(cursor.getColumnIndex("imageProduit"));
            int delaiJourMin = cursor.getInt(cursor.getColumnIndex("delaiJourMin"));
            int delaiJourMax = cursor.getInt(cursor.getColumnIndex("delaiJourMax"));
            String prixQuantite = cursor.getString(cursor.getColumnIndex("prixQuantite"));
            //--
            panier.setId(id);
            panier.setToken(token);
            panier.setProduitId(produitId);
            panier.setQuantite(quantite);
            panier.setNomProduit(nomProduit);
            panier.setImageProduit(imageProduit);
            panier.setDelaiJourMin(delaiJourMin);
            panier.setDelaiJourMax(delaiJourMax);
            panier.setPrixQuantite(Float.parseFloat(prixQuantite));
            resultat.add(panier);
            //--
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return resultat.size()>0 ? resultat.get(0) : null;
    }

    public ArrayList<Panier> getAllBy(String mToken){
        createTable();
        ArrayList<Panier> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name + " " +
                "WHERE token LIKE '"+mToken.replace("'", "''")+"' ORDER BY id DESC", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            Panier panier = new Panier();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String token = cursor.getString(cursor.getColumnIndex("token"));
            int produitId = cursor.getInt(cursor.getColumnIndex("produitId"));
            int quantite = cursor.getInt(cursor.getColumnIndex("quantite"));
            String nomProduit = cursor.getString(cursor.getColumnIndex("nomProduit"));
            String imageProduit = cursor.getString(cursor.getColumnIndex("imageProduit"));
            int delaiJourMin = cursor.getInt(cursor.getColumnIndex("delaiJourMin"));
            int delaiJourMax = cursor.getInt(cursor.getColumnIndex("delaiJourMax"));
            String prixQuantite = cursor.getString(cursor.getColumnIndex("prixQuantite"));
            //--
            panier.setId(id);
            panier.setToken(token);
            panier.setProduitId(produitId);
            panier.setQuantite(quantite);
            panier.setNomProduit(nomProduit);
            panier.setImageProduit(imageProduit);
            panier.setDelaiJourMin(delaiJourMin);
            panier.setDelaiJourMax(delaiJourMax);
            panier.setPrixQuantite(Float.parseFloat(prixQuantite));
            resultat.add(panier);
            //--
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return resultat;
    }

    public void deleteBy(String mToken)
    {
        createTable();
        String sql = "DELETE FROM " + table_name + " WHERE token LIKE '"+mToken+"'";
        connexion.getDb().execSQL(sql);
    }

    public void deleteBy(String mToken, int produitId)
    {
        createTable();
        String sql = "DELETE FROM " + table_name + " WHERE token LIKE '"+mToken+"' AND produitId = '"+produitId+"'";
        connexion.getDb().execSQL(sql);
    }
}
