package e1800958.vamk.assign12;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Vector;

public class DBAdapter {


    private static String[] tableColumnNames, tableColumnTypes;
    private static  String TAG;
    private static String tableName;
    private static final int DATABASE_VERSION = 1;
    private static String TABLE_CREATE_QUERY;
    private static String TABLE_DELETE_QUERY;
    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqlLiteDb;
    private String dbPath;
    private static String dbName;

    public DBAdapter(Context context, String dbPath, String dbName, String tableName) {
        this.context = context;
        this.dbPath = dbPath;
        DBAdapter.dbName = dbName;
        DBAdapter.tableName = tableName;
        tableColumnNames=context.getResources().getStringArray(R.array.table_column_names);
        tableColumnTypes=context.getResources().getStringArray(R.array.table_column_types);
        TAG = context.getString(R.string.db_class_tag);
        TABLE_CREATE_QUERY = "CREATE TABLE " + tableName + " (" + tableColumnNames[0] + "  " + tableColumnTypes[0] +  " " +
                "primary key, " + tableColumnNames[1] +"  " + tableColumnTypes[1] + " not null, " + tableColumnNames[2] + "  " + tableColumnTypes[2]  + " not null, "
                +  tableColumnNames[3] + "  " + tableColumnTypes[3]  + " not null, "
                +  tableColumnNames[4] + "  " + tableColumnTypes[4]  + " not null, "
                +  tableColumnNames[5] + "  " + tableColumnTypes[5]  + " not null);";
        TABLE_DELETE_QUERY = "DROP TABLE IF EXISTS " + tableName;
        dbHelper = new DatabaseHelper(context);
    }
    public DBAdapter(){}
    //Here we define the DatabaseHelper class

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            //super(context, DATABASE_NAME, null, DATABASE_VERSION);
            super(context, dbName, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(TABLE_CREATE_QUERY);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, new DBAdapter().context.getString(R.string.upgrading_db_version) + " " + oldVersion + " to "
                    + newVersion + ". " + new DBAdapter().context.getString(R.string.warning_data_delete));
            //Here we remove the table
            db.execSQL(TABLE_DELETE_QUERY);
            //Here we create the table again
            onCreate(db);
        }
    }


    public DBAdapter openDBConnection() {
        //sqlLiteDb=dbHelper.getWritableDatabase();
        sqlLiteDb = SQLiteDatabase.openDatabase(dbPath + dbName, null, SQLiteDatabase.OPEN_READWRITE);
        return this;
    }
    //Here we close connection to the database
    public void closeDBConnection() {
        dbHelper.close();
    }

    public boolean addMeeting(Meeting m) {
        long result;
        ContentValues initialValues = new ContentValues();
        initialValues.put(tableColumnNames[0], m.getTitle());
        initialValues.put(tableColumnNames[1], m.getPlace());
        initialValues.put(tableColumnNames[2], m.getParticipantText());
        initialValues.put(tableColumnNames[3], m.getImage());
        initialValues.put(tableColumnNames[4], m.getDate());
        initialValues.put(tableColumnNames[5], m.getTime());

        openDBConnection();
        result = sqlLiteDb.insert(tableName, null, initialValues);
        closeDBConnection();


        if(result==-1) return false;
        return true;
    }

    public boolean deleteMeeting(String title) {
        openDBConnection();
        //boolean updatedRows=sqlLiteDb.delete(TABLE_NAME, KEY_TITLE + "=" + "'"+title+"'", null);
        boolean result = (sqlLiteDb.delete(tableName, tableColumnNames[0] + "=" +  "'"+title+"'", null) > 0);
        closeDBConnection();
        return result;
    }

    private ArrayList<Meeting> getRowData(Cursor cursor){
        Object[] dataRow;
        ArrayList<Meeting> meetings = new ArrayList<Meeting>();
        byte[] imgByte;
        ImageView imageView;
        //Here we go through each row of the result set and copy data of each row to an Object array
        if(cursor.moveToFirst()) {
            do {
                Meeting m = new Meeting();
                m.setTitle(cursor.getString(0));
                m.setPlace(cursor.getString(1));
                m.setParticipantsText(cursor.getString(2));
                cursor.getBlob(3);
                m.setImage(cursor.getBlob(3));
                m.setDate(cursor.getString(4));
                m.setTime(cursor.getString(5));
                meetings.add(m);
            } while(cursor.moveToNext());
        }
        return meetings;
    }

    public ArrayList<Meeting> getAllMeetings() {
        openDBConnection();
        Cursor cursor = sqlLiteDb.query(tableName, new String[]{tableColumnNames[0], tableColumnNames[1], tableColumnNames[2], tableColumnNames[3], tableColumnNames[4], tableColumnNames[5]}, null, null, null, null, null);
        ArrayList<Meeting> dataRows= getRowData(cursor);
        closeDBConnection();
        return dataRows;
    }

    public ArrayList<Meeting>getMeeting(String par, String date, String time) {
        openDBConnection();
        Cursor cursor = sqlLiteDb.query(true, tableName, new String[]{tableColumnNames[0], tableColumnNames[1], tableColumnNames[2], tableColumnNames[3], tableColumnNames[4], tableColumnNames[5]},
                tableColumnNames[2] + " LIKE " + "'%"+par+"%' AND " + tableColumnNames[4] +  " LIKE " + "'%"+date+"%' AND "+ tableColumnNames[5] + " LIKE " + "'%"+time+"%'", null, null, null, null, null);
        ArrayList<Meeting> dataRows= getRowData(cursor);

        closeDBConnection();
        return dataRows;
    }


    public boolean updateMeeting(Meeting m) {
        boolean compressionResult;
        boolean updateResult;
        ContentValues values = new ContentValues();
        values.put(tableColumnNames[1], m.getPlace());
        values.put(tableColumnNames[2], m.getParticipantText());
        values.put(tableColumnNames[3],m.getImage());
        values.put(tableColumnNames[4], m.getDate());
        values.put(tableColumnNames[5], m.getTime());
        openDBConnection();
        updateResult = (sqlLiteDb.update(tableName, values, tableColumnNames[0] + "=" + "'"+m.getTitle()+"'", null) > 0);
        closeDBConnection();
        //Here we return false if image.compress() fails.
        return updateResult;
    }


}