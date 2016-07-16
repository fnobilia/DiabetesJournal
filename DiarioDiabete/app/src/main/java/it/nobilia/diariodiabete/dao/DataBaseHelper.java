package it.nobilia.diariodiabete.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{

    public static final String NAME_DB = "diabetes_db";
    public static final int VERSION_DB = 1;

    public static final String CREATE_TABLE_GROUP =
            "CREATE TABLE IF NOT EXISTS "+ SamplesTable.TABLE+" ( " +
                    SamplesTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , "+
                    SamplesTable.GLYCAEMIA + " INTEGER NOT NULL, "+
                    SamplesTable.INSULIN + " INTEGER DEFAULT 0, "+
                    SamplesTable.PERIOD_TXT + " TEXT, "+
                    SamplesTable.PERIOD_NUM + " INTEGER, "+
                    SamplesTable.TIME+" TEXT NOT NULL, "+
                    SamplesTable.DAY + " INTEGER, "+
                    SamplesTable.MONTH + " INTEGER, "+
                    SamplesTable.YEAR+" INTEGER "+" ) ; ";

    public static final String DROP_TABLE_GROUP = "DROP TABLE IF EXISTS "+ SamplesTable.TABLE+" ;";

    public DataBaseHelper(Context context){
        super(context, NAME_DB, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_GROUP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_GROUP);
        onCreate(db);
    }

}
