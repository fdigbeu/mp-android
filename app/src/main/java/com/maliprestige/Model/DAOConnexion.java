package com.maliprestige.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DAOConnexion {

    private Context context;
    private SQLiteDatabase db;
    private final String database = "db_mali_prestige";

    public DAOConnexion(Context context){
        this.context = context;
        db = context.openOrCreateDatabase(database, context.MODE_PRIVATE, null);
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
