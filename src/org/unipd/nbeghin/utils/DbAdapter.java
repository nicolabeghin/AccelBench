package org.unipd.nbeghin.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by Nicola Beghin on 10/06/13.
 */
public class DbAdapter {

        @SuppressWarnings("unused")
        private static final String LOG_TAG = DbAdapter.class.getSimpleName();

        private Context context;
        private SQLiteDatabase database;
        private DatabaseHelper dbHelper;
        private int trunk=1;

        // Database fields
        private static String DATABASE_TABLE      = "contact";
        public static final String KEY_x = "x";
        public static final String KEY_y = "y";
        public static final String KEY_z = "z";
        public static final String KEY_timestamp = "timestamp";
        public static final String KEY_action = "action";
        public static final String KEY_delay = "delay";
        public static final String KEY_trunk = "trunk";
        public static final String KEY_mode = "mode";
        public static final String KEY_mindiff = "mindiff";
        
        public DbAdapter(Context context) {
            this.context = context;
        }

        public DbAdapter open() throws SQLException {
            dbHelper = new DatabaseHelper(context);
            database = dbHelper.getWritableDatabase();
            DATABASE_TABLE = DatabaseHelper.getDatabaseTable();
            this.getNewTrunkId();
            return this;
        }

        public void close() {
            database.close();
        }

        public String getDbPath() {
            return database.getPath();
        }

        private ContentValues createContentValues(float x, float y, float z, long timestamp, String action, int delay, int trunk, String mode, float minDiff) {
            ContentValues values = new ContentValues();
            values.put(KEY_x, x);
            values.put(KEY_y, y);
            values.put(KEY_z, z);
            values.put(KEY_timestamp, timestamp);
            values.put(KEY_action, action);
            values.put(KEY_delay, delay);
            values.put(KEY_trunk, trunk);
            values.put(KEY_mode, mode);
            values.put(KEY_mindiff, minDiff);
            return values;
        }

        public void getNewTrunkId() {
            this.trunk=1;
            if (this.getCount()>0) this.trunk=(int)database.compileStatement("SELECT MAX(trunk)+1 FROM samples").simpleQueryForLong();
        }

        public long getCount() {
            return database.compileStatement("SELECT COUNT(*) FROM samples").simpleQueryForLong();
        }

        //create a contact
        public long saveSample(float x, float y, float z, long timestamp, String action, int sensorDelay, String mode, float minDiff) {
            return database.insertOrThrow(DATABASE_TABLE, null, createContentValues(x,y,z,timestamp, action, sensorDelay, trunk, mode, minDiff));
        }

        public void cleanDb() {
            database.execSQL("DELETE FROM "+DATABASE_TABLE);
            database.execSQL("VACUUM");
        }
/*
        //update a contact
        public boolean updateContact( long contactID, String name, String surname, String sex, String birth_date ) {
            ContentValues updateValues = createContentValues(name, surname, sex, birth_date);
            return database.update(DATABASE_TABLE, updateValues, KEY_ CONTACTID + "=" + contactID, null) > 0;
        }

        //delete a contact
        public boolean deleteContact(long contactID) {
            return database.delete(DATABASE_TABLE, KEY_ CONTACTID + "=" + contactID, null) > 0;
        }

        //fetch all contacts
        public Cursor fetchAllContacts() {
            return database.query(DATABASE_TABLE, new String[] { KEY_CONTACTID, KEY_NAME, KEY_SURNAME, KEY_SEX, KEY_BIRTH_DATE}, null, null, null, null, null);
        }

        //fetch contacts filter by a string
        public Cursor fetchContactsByFilter(String filter) {
            Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
                    KEY_CONTACTID, KEY_NAME, KEY_SURNAME, KEY_SEX, KEY_BIRTH_DATE },
                    KEY_NAME + " like '%"+ filter + "%'", null, null, null, null, null);

            return mCursor;
        }*/
}
