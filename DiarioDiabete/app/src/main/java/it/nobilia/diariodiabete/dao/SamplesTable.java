package it.nobilia.diariodiabete.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class SamplesTable {
	
	public static final String ID = "ID";
    public static final String GLYCAEMIA = "GLYCAEMIA";
    public static final String INSULIN = "INSULIN";
    public static final String PERIOD_TXT = "PERIOD_TXT";
    public static final String PERIOD_NUM = "PERIOD_NUM";
    public static final String TIME = "TIME";
    public static final String DAY = "DAY";
    public static final String MONTH = "MONTH";
    public static final String YEAR = "YEAR";
	public static final String[] COLUMNS = new String[]{ID,GLYCAEMIA,INSULIN,PERIOD_TXT,PERIOD_NUM,TIME,DAY,MONTH,YEAR};

    public static final String TABLE = "group_table";

    public static void insertSample(SQLiteDatabase db,Sample sample){
        String[] data = sample.getArray();

        ContentValues v = new ContentValues();
        for(int i=1;i<COLUMNS.length;i++){
            v.put(COLUMNS[i], data[i]);
        }

        db.insert(TABLE, null, v);
    }

    public static ArrayList<Sample> getSample(SQLiteDatabase db) throws SQLException{
        ArrayList<Sample> list = new ArrayList<>();

        Cursor c = db.query(true, TABLE, COLUMNS, null, null, null, null, null, null);

        if((c != null) && (c.getCount() > 0)){
            while(c.moveToNext()){
                list.add(new Sample(c.getInt(0),c.getInt(1),c.getInt(2),c.getString(3),c.getInt(4),c.getString(5),c.getInt(6),c.getInt(7),c.getInt(8)));
            }
        }

        c.close();

        return list;
    }

    public static ArrayList<Sample> getSampleMonth(SQLiteDatabase db) throws SQLException {
        ArrayList<Sample> list = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        String month = Integer.toString(calendar.get(Calendar.MONTH));
        String year = Integer.toString(calendar.get(Calendar.YEAR));

        Cursor c = db.query(true, TABLE, COLUMNS, MONTH + " = ? AND " + YEAR + " = ?", new String[]{month,year}, null, null, DAY, null);

        if ((c != null) && (c.getCount() > 0)) {
            while (c.moveToNext())
                list.add(new Sample(c.getInt(0),c.getInt(1),c.getInt(2),c.getString(3),c.getInt(4),c.getString(5),c.getInt(6),c.getInt(7),c.getInt(8)));
        }

        c.close();

        return list;
    }

    public static ArrayList<Sample> getSampleDay(SQLiteDatabase db) throws SQLException {
        ArrayList<Sample> list = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        String month = Integer.toString(calendar.get(Calendar.MONTH));
        String year = Integer.toString(calendar.get(Calendar.YEAR));

        Cursor c = db.query(true, TABLE, COLUMNS, DAY + " = ? AND " + MONTH + " = ? AND " + YEAR + " = ?", new String[]{day,month,year}, null, null, PERIOD_NUM, null);

        if ((c != null) && (c.getCount() > 0)) {
            while (c.moveToNext())
                list.add(new Sample(c.getInt(0),c.getInt(1),c.getInt(2),c.getString(3),c.getInt(4),c.getString(5),c.getInt(6),c.getInt(7),c.getInt(8)));
        }

        c.close();

        return list;
    }

    public static boolean deleteSample(SQLiteDatabase db,int id){
        return db.delete(TABLE, ID+" = ? ", new String[] {Integer.toString(id)}) > 0;
    }

}
