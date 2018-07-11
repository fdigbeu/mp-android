package com.maliprestige.Model;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class DAOSlide {
    private DAOConnexion connexion;
    private Context context;
    private String table_name;

    public DAOSlide(Context context){
        this.context = context;
        table_name = "mp_slide";
    }

    public void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS "+table_name+" (id INTEGER PRIMARY KEY AUTOINCREMENT, titre VARCHAR, url VARCHAR);";
        connexion = new DAOConnexion(context);
        connexion.getDb().execSQL(sql);
    }

    public void add(Slide slide){
        createTable();
        String sql = "INSERT INTO " + table_name + " (titre, url)" +
                " VALUES ('"+slide.getTitle().replace("'", "''")+"', " +
                "'"+slide.getUrl().replace("'", "''")+"');";
        connexion.getDb().execSQL(sql);
    }

    public ArrayList<Slide> getAll(){
        createTable();
        ArrayList<Slide> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name +" ORDER BY id ASC", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            Slide slide = new Slide();
            String titre = cursor.getString(cursor.getColumnIndex("titre"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            //--
            slide.setTitle(titre);
            slide.setUrl(url);
            //--
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return resultat;
    }

    public void deleteAll()
    {
        createTable();
        String sql = "DELETE FROM " + table_name;
        connexion.getDb().execSQL(sql);
    }
}
