package com.example.criminalintent.repository;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.criminalintent.model.Crime;

import static com.example.criminalintent.database.CrimeDBSchema.CrimeTable.Cols;

import java.util.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        UUID uuid = UUID.fromString(getString(getColumnIndex(Cols.UUID)));
        String title = getString(getColumnIndex(Cols.TITLE));
        Date date = new Date(getLong(getColumnIndex(Cols.DATE)));
        boolean solved = getInt(getColumnIndex(Cols.SOLVED)) == 0 ? false : true;
        String suspect = getString(getColumnIndex(Cols.SUSPECT));
        String phoneNumber = getString(getColumnIndex(Cols.PHONENUMBER));
        return new Crime(uuid, title, date, solved, suspect,phoneNumber);
    }
}
