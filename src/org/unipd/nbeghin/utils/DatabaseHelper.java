package org.unipd.nbeghin.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nicola Beghin on 10/06/13.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_TABLE="samples";
    private static final String DATABASE_NAME="accelbench.db";
    private static final int SCHEMA=4;

    public static String getDatabaseTable() {
        return DATABASE_TABLE;
    }

    static final String TITLE="title";
    static final String VALUE="value";
    static final String TABLE="constants";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS \""+DATABASE_TABLE+"\" (\"x\" REAL,\"y\" REAL,\"z\" REAL,\"action\" TEXT, \"delay\" INTEGER, \"timestamp\" INTEGER, \"trunk\" INTEGER, \"step\" INTEGER, \"mode\" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS samples");
        onCreate(db);
    }

}
