package com.maliprestige.Model;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class DAOAdresse {

    private DAOConnexion connexion;
    private Context context;
    private String table_name;

    public DAOAdresse(Context context){
        this.context = context;
        table_name = "mp_adresse";
    }

    public void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS "+table_name+" (id INTEGER PRIMARY KEY AUTOINCREMENT, adresseId INTEGER, type VARCHAR, token VARCHAR, destinataire VARCHAR, libelle VARCHAR);";
        connexion = new DAOConnexion(context);
        connexion.getDb().execSQL(sql);
    }

    public void add(Adresse adresse){
        createTable();
        String sql = "INSERT INTO " + table_name + " (adresseId, type, token, destinataire, libelle)" +
                " VALUES ('"+adresse.getAdresseId()+"', '"+adresse.getType().replace("'", "''")+"', " +
                "'"+adresse.getToken().replace("'", "''")+"', " +
                "'"+adresse.getDestinataire().replace("'", "''")+"', " +
                "'"+adresse.getLibelle().replace("'", "''")+"');";
        connexion.getDb().execSQL(sql);
    }

    public ArrayList<Adresse> getAll(String mToken, String mType){
        createTable();
        ArrayList<Adresse> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name + " " +
                "WHERE token LIKE '"+mToken.replace("'", "''")+"' AND type LIKE '"+mType+"'", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            Adresse adresse = new Adresse();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int adresseId = cursor.getInt(cursor.getColumnIndex("adresseId"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String token = cursor.getString(cursor.getColumnIndex("token"));
            String destinataire = cursor.getString(cursor.getColumnIndex("destinataire"));
            String libelle = cursor.getString(cursor.getColumnIndex("libelle"));
            //--
            adresse.setId(id);
            adresse.setAdresseId(adresseId);
            adresse.setType(type);
            adresse.setDestinataire(destinataire);
            adresse.setLibelle(libelle);
            adresse.setToken(token);
            resultat.add(adresse);
            //--
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return resultat;
    }

    public void delete(String mToken, String mType)
    {
        createTable();
        String sql = "DELETE FROM " + table_name + " WHERE token LIKE '"+mToken+"' AND type LIKE '"+mType+"'";
        connexion.getDb().execSQL(sql);
    }
}
