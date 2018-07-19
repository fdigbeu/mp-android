package com.maliprestige.Model;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class DAOSearch {
    private DAOConnexion connexion;
    private Context context;
    private String table_name;

    public DAOSearch(Context context){
        this.context = context;
        table_name = "mp_search";
    }

    public void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS "+table_name+" (id INTEGER PRIMARY KEY AUTOINCREMENT, produitId VARCHAR, nomProduit VARCHAR, image1 VARCHAR, image2 VARCHAR, image3 VARCHAR);";
        connexion = new DAOConnexion(context);
        connexion.getDb().execSQL(sql);
    }

    public void add(Search search){
        createTable();
        String sql = "INSERT INTO " + table_name + " (produitId, nomProduit, image1, image2, image3)" +
                " VALUES ('"+search.getProduitId()+"', " +
                "'"+search.getNomProduit().replace("'", "''")+"', " +
                "'"+search.getImage1().replace("'", "''")+"', " +
                "'"+search.getImage2().replace("'", "''")+"', " +
                "'"+search.getImage3().replace("'", "''")+"');";
        connexion.getDb().execSQL(sql);
    }

    public ArrayList<Search> getAll(){
        createTable();
        ArrayList<Search> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name + " ORDER BY id ASC", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            Search search = new Search();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int produitId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("produitId")));
            String nomProduit = cursor.getString(cursor.getColumnIndex("nomProduit"));
            String image1 = cursor.getString(cursor.getColumnIndex("image1"));
            String image2 = cursor.getString(cursor.getColumnIndex("image2"));
            String image3 = cursor.getString(cursor.getColumnIndex("image3"));
            //--
            search.setProduitId(produitId);
            search.setNomProduit(nomProduit);
            search.setImage1(image1);
            search.setImage2(image2);
            search.setImage3(image3);
            resultat.add(search);
            //--
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return resultat;
    }

    public Search getInfo(int mProduitId){
        createTable();
        ArrayList<Search> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name + " " +
                "WHERE produitId = '"+mProduitId+"'", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            Search search = new Search();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int produitId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("produitId")));
            String nomProduit = cursor.getString(cursor.getColumnIndex("nomProduit"));
            String image1 = cursor.getString(cursor.getColumnIndex("image1"));
            String image2 = cursor.getString(cursor.getColumnIndex("image2"));
            String image3 = cursor.getString(cursor.getColumnIndex("image3"));
            //--
            search.setProduitId(produitId);
            search.setNomProduit(nomProduit);
            search.setImage1(image1);
            search.setImage2(image2);
            search.setImage3(image3);
            resultat.add(search);
            //--
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return resultat.size()>0 ? resultat.get(0) : null;
    }

    public Search getInfoByNom(String mProduitNom){
        createTable();
        ArrayList<Search> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name + " " +
                "WHERE nomProduit LIKE '"+mProduitNom+"'", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            Search search = new Search();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int produitId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("produitId")));
            String nomProduit = cursor.getString(cursor.getColumnIndex("nomProduit"));
            String image1 = cursor.getString(cursor.getColumnIndex("image1"));
            String image2 = cursor.getString(cursor.getColumnIndex("image2"));
            String image3 = cursor.getString(cursor.getColumnIndex("image3"));
            //--
            search.setProduitId(produitId);
            search.setNomProduit(nomProduit);
            search.setImage1(image1);
            search.setImage2(image2);
            search.setImage3(image3);
            resultat.add(search);
            //--
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return resultat.size()>0 ? resultat.get(0) : null;
    }

    public void deleteAll()
    {
        createTable();
        String sql = "DELETE FROM " + table_name;
        connexion.getDb().execSQL(sql);
    }
}
