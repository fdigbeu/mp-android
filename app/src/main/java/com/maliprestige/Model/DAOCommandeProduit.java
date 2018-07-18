package com.maliprestige.Model;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class DAOCommandeProduit {

    private DAOConnexion connexion;
    private Context context;
    private String table_name;

    public DAOCommandeProduit(Context context){
        this.context = context;
        table_name = "mp_commande_produit";
    }

    public void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS "+table_name+" (id INTEGER PRIMARY KEY AUTOINCREMENT, produitId INTEGER, nomProduit VARCHAR, qteProduit VARCHAR, prixQteProduit VARCHAR, numeroCommande VARCHAR, token VARCHAR);";
        connexion = new DAOConnexion(context);
        connexion.getDb().execSQL(sql);
    }

    public void add(CommandeProduit commandeProduit){
        createTable();
        String sql = "INSERT INTO " + table_name + " (produitId, nomProduit, qteProduit, prixQteProduit, numeroCommande, token)" +
                " VALUES ('"+commandeProduit.getProduitId()+"', " +
                "'"+commandeProduit.getNomProduit().replace("'", "''")+"', " +
                "'"+commandeProduit.getQteProduit()+"', " +
                "'"+commandeProduit.getPrixQteProduit()+"', " +
                "'"+commandeProduit.getNumeroCommande()+"', " +
                "'"+commandeProduit.getToken().replace("'", "''")+"');";
        connexion.getDb().execSQL(sql);
    }

    public ArrayList<CommandeProduit> getAllBy(String mToken){
        createTable();
        ArrayList<CommandeProduit> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name +" WHERE tokenLOWER(typeCoupeLibelle) LIKE '"+mToken+"' ORDER BY id ASC", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            CommandeProduit commandeProduit = new CommandeProduit();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int produitId = cursor.getInt(cursor.getColumnIndex("produitId"));
            String nomProduit = cursor.getString(cursor.getColumnIndex("nomProduit"));
            String qteProduit = cursor.getString(cursor.getColumnIndex("qteProduit"));
            String prixQteProduit = cursor.getString(cursor.getColumnIndex("prixQteProduit"));
            String numeroCommande = cursor.getString(cursor.getColumnIndex("numeroCommande"));
            String token = cursor.getString(cursor.getColumnIndex("token"));
            //--
            commandeProduit.setId(id);
            commandeProduit.setProduitId(produitId);
            commandeProduit.setNomProduit(nomProduit);
            commandeProduit.setQteProduit(Integer.parseInt(qteProduit));
            commandeProduit.setPrixQteProduit(Float.parseFloat(prixQteProduit));
            commandeProduit.setNumeroCommande(numeroCommande);
            commandeProduit.setToken(token);
            resultat.add(commandeProduit);
            //--
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return resultat;
    }

    public ArrayList<CommandeProduit> getAllBy(String mToken, String mNumeroCommande){
        createTable();
        ArrayList<CommandeProduit> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name +" WHERE tokenLOWER(typeCoupeLibelle) LIKE '"+mToken+"' AND numeroCommande LIKE '"+mNumeroCommande+"' ORDER BY id ASC", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            CommandeProduit commandeProduit = new CommandeProduit();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int produitId = cursor.getInt(cursor.getColumnIndex("produitId"));
            String nomProduit = cursor.getString(cursor.getColumnIndex("nomProduit"));
            String qteProduit = cursor.getString(cursor.getColumnIndex("qteProduit"));
            String prixQteProduit = cursor.getString(cursor.getColumnIndex("prixQteProduit"));
            String numeroCommande = cursor.getString(cursor.getColumnIndex("numeroCommande"));
            String token = cursor.getString(cursor.getColumnIndex("token"));
            //--
            commandeProduit.setId(id);
            commandeProduit.setProduitId(produitId);
            commandeProduit.setNomProduit(nomProduit);
            commandeProduit.setQteProduit(Integer.parseInt(qteProduit));
            commandeProduit.setPrixQteProduit(Float.parseFloat(prixQteProduit));
            commandeProduit.setNumeroCommande(numeroCommande);
            commandeProduit.setToken(token);
            resultat.add(commandeProduit);
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

    public void deleteBy(String mToken, String mNumeroCommande)
    {
        createTable();
        String sql = "DELETE FROM " + table_name + " WHERE token LIKE '"+mToken+"' AND numeroCommande LIKE '"+mNumeroCommande+"'";
        connexion.getDb().execSQL(sql);
    }
}
