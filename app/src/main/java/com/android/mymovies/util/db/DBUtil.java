package com.android.mymovies.util.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {
    private static SQLiteDatabase sqLiteDatabase;

    private DBUtil() {
    }

    /**
     * Get table data by @tableName
     * Generic method and can be used for any table
     *
     * @param tableName - table name
     * @return - list of table rows as key, value pairs(col name, col value)
     */
    public static List<Map<String, String>> getTableData(String tableName) {
        openDataBase(false);
        List<Map<String, String>> data = new ArrayList<>();

        String query = "Select * from " + tableName;
        Cursor c = sqLiteDatabase.rawQuery(query, null);

        if (c.getCount() == 0) {
            c.close();
            return data;
        }

        while (c.moveToNext()) {
            HashMap<String, String> value = new HashMap<>();
            for (int i = 0; i < c.getColumnCount(); i++) {
                value.put(c.getColumnName(i), c.getString(i));
            }
            data.add(value);
        }
        c.close();
        closeDataBase();
        return data;
    }

    /**
     * insert row in table
     * Generic method and can be used for any table
     *
     * @param tableName - table name
     * @param map - table row as key, value pairs
     * @return insertion result
     */
    public static int insertTableRow(String tableName, Map<String, String> map) {

        openDataBase(true);
        //insert data
        ContentValues contentValues = new ContentValues();

        for (String key : map.keySet()) {
            contentValues.put(key, map.get(key));
        }

        int result;
        try {
            result = (int) sqLiteDatabase.insert(tableName, null, contentValues);
        } catch (Exception ex) {
            result = -1;
        } finally {
            closeDataBase();
        }

        return result;
    }

    /**
     * delete row from table
     * Generic method and can be used for any table
     *
     * @param tableName - table name
     * @param whereClause - where condition for deletion
     * @param whereArgs - input data for where clause
     * @return - deletion result
     */
    public static int deleteTableRow(String tableName, String whereClause, String[] whereArgs) {
        openDataBase(true);
        try {
            return sqLiteDatabase.delete(tableName, whereClause, whereArgs);
        } catch (Exception e) {
            return 0;
        } finally {
            closeDataBase();
        }
    }


    /**
     * Method to open data base in different modes
     *
     * @param isWriteMode - true if DB has to be opened in write mode, otherwise false
     */
    private static synchronized void openDataBase(boolean isWriteMode) {
        if (isWriteMode) {
            sqLiteDatabase = DBHelper.getInstance().getWritableDatabase();
        } else {
            sqLiteDatabase = DBHelper.getInstance().getReadableDatabase();
        }
    }

    /**
     * Method to close the data base
     */
    private static synchronized void closeDataBase() {
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }
    }
}
