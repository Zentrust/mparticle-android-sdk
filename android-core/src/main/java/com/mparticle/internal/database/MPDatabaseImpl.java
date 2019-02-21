package com.mparticle.internal.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.BaseColumns;

import com.mparticle.MParticle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MPDatabaseImpl implements MPDatabase {
   SQLiteDatabase sqLiteDatabase;

    public  MPDatabaseImpl(SQLiteDatabase database) {
        this.sqLiteDatabase = database;
    }

    @Override
    public long insert(String table, String nullColumnHack, ContentValues contentValues) {
        long row = sqLiteDatabase.insert(table, nullColumnHack, contentValues);
        if (MParticle.InternalListener.hasListener()) {
            if (row >= 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    MParticle.InternalListener.getListener().onMessageStored(row, table, contentValues);
                }
            }
        }
        return row;
    }

    @Override
    public Cursor query(String table, String[] columns, String selection,
                 String[] selectionArgs, String groupBy, String having,
                 String orderBy, String limit) {
        if (MParticle.InternalListener.hasListener()) {
            columns = getColumnsWithId(columns);
        }
        Cursor cursor = sqLiteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        if (MParticle.InternalListener.hasListener()) {
            int columnIndex = cursor.getColumnIndex(BaseColumns._ID);
            if (columnIndex >= 0 && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    MParticle.InternalListener.getListener().compositeObject(table + cursor.getInt(columnIndex), cursor);
                    cursor.moveToNext();
                }
            }
            cursor.moveToFirst();
            cursor.move(-1);
        }
        return cursor;
    }

    @Override
    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy) {
        if (MParticle.InternalListener.hasListener()) {
            columns = getColumnsWithId(columns);
        }
        Cursor cursor = sqLiteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        if (MParticle.InternalListener.hasListener()) {
            int columnIndex = cursor.getColumnIndex(BaseColumns._ID);
            if (columnIndex >= 0 && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    MParticle.InternalListener.getListener().compositeObject(table + cursor.getInt(columnIndex), cursor);
                    cursor.moveToNext();
                }
            }
            cursor.moveToFirst();
            cursor.move(-1);
        }
        return cursor;
    }

    private String[] getColumnsWithId(String[] columns) {
        if (columns == null) {
            return columns;
        }
        boolean found = false;
        for (String column: columns) {
            if (column.equals(BaseColumns._ID)) {
                found = true;
            }
        }
        if (!found) {
            List<String> list = new ArrayList<String>(Arrays.asList(columns));
            list.add(BaseColumns._ID);
            columns = list.toArray(new String[columns.length + 1]);
        }
        return columns;
    }

    @Override
    public int delete(String table, String whereClause, String[] whereArgs) {
        return sqLiteDatabase.delete(table, whereClause, whereArgs);
    }

    @Override
    public void beginTransaction() {
        sqLiteDatabase.beginTransaction();
    }

    @Override
    public void setTransactionSuccessful() {
        sqLiteDatabase.setTransactionSuccessful();
    }

    @Override
    public void endTransaction() {
        sqLiteDatabase.endTransaction();
    }

    @Override
    public int update(String tableName, ContentValues contentValues, String s, String[] strings) {
        return sqLiteDatabase.update(tableName, contentValues, s, strings);
    }

}
