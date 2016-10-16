package com.peipeiyang.todolist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangp on 5/26/2016.
 */
public final  class DBHelper {
    private static final String LOGTAG = "DBHelper";

    private static final String DATABASE_NAME    = "Note1.db";
    private static final int    DATABASE_VERSION = 1;
    private static final String TABLE_NAME       = "Notedata1";

    // Column Names
    public static final String KEY_ID           = "id";
    public static final String KEY_TITLE     	= "title";
    public static final String KEY_DESCRIPTION  = "description";
    public static final String KEY_DATE         = "duedate";
    public static final String KEY_CATEGORY     = "category";
    public static final String KEY_ADDINFOR     = "additionalinfo";

    // Column indexes
    public static final int COLUMN_ID         	= 0;
    public static final int COLUMN_TITLE  	  	= 1;
    public static final int COLUMN_DESCRIPTION	= 2;
    public static final int COLUMN_DATE      	= 3;
    public static final int COLUMN_CATEGORY     = 4;
    public static final int COLUMN_ADDINFOR     = 5;

    public static final String workmk = "Work";
    public static final String studymk = "Study";
    public static final String lifemk = "Daily Life";


    private Context context;
    private SQLiteDatabase db;
    private SQLiteStatement insertStmt;

    private static final String INSERT =
            "INSERT INTO " + TABLE_NAME + "(" +
                    KEY_TITLE + ", " +
                    KEY_DESCRIPTION + ", " +
                    KEY_DATE + "," +
                    KEY_CATEGORY + "," +
                   KEY_ADDINFOR + ") values (?, ?, ?, ?, ?)";


    public DBHelper(Context context) throws Exception
    {
        this.context = context;
        try {
            OpenHelper openHelper = new OpenHelper(this.context);
// Open a database for reading and writing
            db = openHelper.getWritableDatabase();
//  compile a sqlite insert statement into re-usable statement object.
            insertStmt = db.compileStatement(INSERT);

        } catch (Exception e) {
            Log.e(LOGTAG, " DBHelper constructor:  could not get database " + e);
            throw (e);
        }
    }
    public long insert (Notedetail noteInfo)
    {
        // bind values to the pre-compiled SQL statement "inserStmt"
        insertStmt.bindString(COLUMN_TITLE, noteInfo.getTitle());
        insertStmt.bindString(COLUMN_DESCRIPTION, noteInfo.getDescription());
        insertStmt.bindString(COLUMN_DATE, noteInfo.getDuedate());
        insertStmt.bindString(COLUMN_CATEGORY,noteInfo.getCategory());
        insertStmt.bindString(COLUMN_ADDINFOR,noteInfo.getAdditionalinfo());

        long value =-1;
        try {
//  Execute the sqlite statement.
            value = insertStmt.executeInsert();
        } catch (Exception e) {
            Log.e(LOGTAG, " executeInsert problem: " + e);
        }
        Log.d(LOGTAG, "value=" + value);
        return value;
    }

    public void deleteAll()
    {
        db.delete(TABLE_NAME, null, null);
    }

    // delete a row in the database
    public boolean deleteRecord(long rowId)
    {
        return db.delete(TABLE_NAME, KEY_ID + "=" + rowId, null) > 0;
    }

    public List<Notedetail> selectAll()
    {
        List<Notedetail> list = new ArrayList<Notedetail>();
        Cursor cursor = db.query(TABLE_NAME,
                new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE,KEY_CATEGORY,KEY_ADDINFOR},
                null, null, null, null, null);


        if (cursor.moveToFirst())
        {
            do
            {
                Notedetail noteInfo = new Notedetail();
                noteInfo.setTitle(cursor.getString(COLUMN_TITLE));
                noteInfo.setDescription(cursor.getString(COLUMN_DESCRIPTION));
                noteInfo.setDuedate(cursor.getString(COLUMN_DATE));
                noteInfo.setCategory(cursor.getString(COLUMN_CATEGORY));
                noteInfo.setAdditionalinfo(cursor.getString(COLUMN_ADDINFOR));
                noteInfo.setId(cursor.getLong(COLUMN_ID));
                list.add(noteInfo);
            }
            while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }
        return list;
    }


    public List<Notedetail> selectPart(String type)
    {
        Cursor cursor1 = null;
        List<Notedetail> list = new ArrayList<Notedetail>();
        if(type.equals(workmk)) {
            String whereClause = " category = \"Work\" " ;
             cursor1 = db.query(TABLE_NAME,
                    new String[]{KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE, KEY_CATEGORY, KEY_ADDINFOR},
                    whereClause, null, null, null, null);
        }
        else if(type.equals(studymk)){
            String whereClause = " category = \"Study\" " ;

            cursor1 = db.query(TABLE_NAME,
                    new String[]{KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE, KEY_CATEGORY, KEY_ADDINFOR},
                   whereClause, null, null, null, null);
        }
        else if(type.equals(lifemk)){
            String whereClause = " category = \"DailyLife\" " ;
            cursor1 = db.query(TABLE_NAME,
                    new String[]{KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE, KEY_CATEGORY, KEY_ADDINFOR},
                    whereClause, null, null, null, null);
        }

        if (cursor1.moveToFirst())
        {
            do
            {
                Notedetail noteInfo = new Notedetail();
                noteInfo.setTitle(cursor1.getString(COLUMN_TITLE));
                noteInfo.setDescription(cursor1.getString(COLUMN_DESCRIPTION));
                noteInfo.setDuedate(cursor1.getString(COLUMN_DATE));
                noteInfo.setCategory(cursor1.getString(COLUMN_CATEGORY));
                noteInfo.setAdditionalinfo(cursor1.getString(COLUMN_ADDINFOR));
                noteInfo.setId(cursor1.getLong(COLUMN_ID));
                list.add(noteInfo);
            }
            while (cursor1.moveToNext());
        }
        if (cursor1 != null && !cursor1.isClosed())
        {
            cursor1.close();
        }
        return list;
    }
    // Helper class for DB creation/update
    // SQLiteOpenHelper provides getReadableDatabase() and getWriteableDatabase() methods
    // to get access to an SQLiteDatabase object; either in read or write mode.

    private static class OpenHelper extends SQLiteOpenHelper {
        private static final String LOGTAG = "OpenHelper";

        private static final String CREATE_TABLE =
                "CREATE TABLE " +
                        TABLE_NAME +
                        "(" + KEY_ID + " INTEGER PRIMARY KEY, " +
                        KEY_TITLE + " TEXT, " +
                        KEY_DESCRIPTION + " TEXT, " +
                        KEY_DATE + " TEXT," +
                        KEY_CATEGORY + " TEXT," +
                        KEY_ADDINFOR + " TEXT);";

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Creates the tables.
         * This function is only run once or after every Clear Data
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOGTAG, " onCreate");
            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                Log.e(LOGTAG, " onCreate:  Could not create SQL database: " + e);
            }
        }

        /**
         * called, if the database version is increased in your application code.
         * This method updating an existing database schema or dropping the existing database
         * and recreating it via the onCreate() method.
         */

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(LOGTAG, "Upgrading database, this will drop tables and recreate.");
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(db);
            } catch (Exception e) {
                Log.e(LOGTAG, " onUpgrade:  Could not update SQL database: " + e);
            }

            // Technique to add a column rather than recreate the tables.
            //   String upgradeQuery_ADD_AREA =
            //       "ALTER TABLE "+ TABLE_NAME + " ADD COLUMN " + KEY_AREA + " TEXT ";
            //   if(oldVersion <2 ){
            //    	db.execSQL(upgradeQuery_ADD_AREA);

        }


    }

}
