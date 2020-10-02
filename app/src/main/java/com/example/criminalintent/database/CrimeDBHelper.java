package com.example.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.example.criminalintent.database.UserDBSchema.userTable.UserCols;
import static com.example.criminalintent.database.CrimeDBSchema.CrimeTable.Cols;

import androidx.annotation.Nullable;

public class CrimeDBHelper extends SQLiteOpenHelper {

    public CrimeDBHelper(@Nullable Context context) {
        super(context, UserDBSchema.NAME, null, UserDBSchema.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("CREATE TABLE " + UserDBSchema.userTable.NAME + " (");
        sbQuery.append(UserCols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        sbQuery.append(UserCols.UUID + " TEXT NOT NULL,");
        sbQuery.append(UserCols.USERNAME + " TEXT NOT NULL,");
        sbQuery.append(UserCols.PASSWORD + " TEXT NOT NULL");
        sbQuery.append(");");

        sqLiteDatabase.execSQL(sbQuery.toString());

        StringBuilder sbQuery2 = new StringBuilder();
        sbQuery2.append("CREATE TABLE " + CrimeDBSchema.CrimeTable.NAME + " (");
        sbQuery2.append(Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        sbQuery2.append(Cols.UUID + " TEXT NOT NULL,");
        sbQuery2.append(Cols.TITLE + " TEXT,");
        sbQuery2.append(Cols.DATE + " TEXT,");
        sbQuery2.append(Cols.SUSPECT + " TEXT,");
        sbQuery2.append(Cols.PHONENUMBER + " TEXT,");
        sbQuery2.append(Cols.SOLVED + " INTEGER");
        sbQuery2.append(");");

        sqLiteDatabase.execSQL(sbQuery2.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
