package com.maliprestige.Model;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class DAOClient {
    private DAOConnexion connexion;
    private Context context;
    private String table_name;

    public DAOClient(Context context){
        this.context = context;
        table_name = "mp_client";
    }

    public void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS "+table_name+" (id INTEGER PRIMARY KEY AUTOINCREMENT, civilite VARCHAR, nom VARCHAR, prenom VARCHAR, telPort VARCHAR, telFixe VARCHAR, email VARCHAR, token VARCHAR);";
        connexion = new DAOConnexion(context);
        connexion.getDb().execSQL(sql);
    }

    public void add(Client client){
        createTable();
        String sql = "INSERT INTO " + table_name + " (civilite, nom, prenom, telPort, telFixe, email, token)" +
                " VALUES ('"+client.getCivilite().replace("'", "''")+"', " +
                "'"+client.getNom().replace("'", "''")+"', " +
                "'"+client.getPrenom().replace("'", "''")+"', " +
                "'"+client.getTelPort().replace("'", "''")+"', " +
                "'"+client.getTelFixe().replace("'", "''")+"', " +
                "'"+client.getEmail().replace("'", "''")+"', " +
                "'"+client.getToken().replace("'", "''")+"');";
        connexion.getDb().execSQL(sql);
    }

    public Client getInfo(String mToken){
        createTable();
        ArrayList<Client> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name + " " +
                "WHERE token LIKE '"+mToken.replace("'", "''")+"'", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            Client client = new Client();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String civilite = cursor.getString(cursor.getColumnIndex("civilite"));
            String nom = cursor.getString(cursor.getColumnIndex("nom"));
            String prenom = cursor.getString(cursor.getColumnIndex("prenom"));
            String telPort = cursor.getString(cursor.getColumnIndex("telPort"));
            String telFixe = cursor.getString(cursor.getColumnIndex("telFixe"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String token = cursor.getString(cursor.getColumnIndex("token"));
            //--
            client.setId(id);
            client.setCivilite(civilite);
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setTelPort(telPort);
            client.setTelFixe(telFixe);
            client.setEmail(email);
            client.setToken(token);
            resultat.add(client);
            //--
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return resultat.size()>0 ? resultat.get(0) : null;
    }

    public void deleteBy(String mToken)
    {
        createTable();
        String sql = "DELETE FROM " + table_name + " WHERE token LIKE '"+mToken+"'";
        connexion.getDb().execSQL(sql);
    }
}
