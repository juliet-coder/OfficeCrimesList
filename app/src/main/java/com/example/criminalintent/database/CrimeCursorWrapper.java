package com.example.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.criminalintent.Crime;


import java.sql.Date;
import java.util.UUID;

import static com.example.criminalintent.database.CrimeDbSchema.*;
import static java.lang.Long.parseLong;

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Crime getCrime(){
        String uuidSctring = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        String date = getString(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));

        Crime crime = new Crime(UUID.fromString(uuidSctring));
        crime.setTitle(title);
        crime.setDate(new Date(parseLong(date)));
        crime.setSolved(isSolved != 0);
        return crime;
    }
}
