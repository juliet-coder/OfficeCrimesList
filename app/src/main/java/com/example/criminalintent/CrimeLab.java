package com.example.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.criminalintent.database.CrimeBaseHelper;
import com.example.criminalintent.database.CrimeCursorWrapper;
import com.example.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab crimeLab;
    private List<Crime> crimes;
    private Context context;
    private SQLiteDatabase dataBase;

    public static CrimeLab get(Context context){
        if(crimeLab == null){
            crimeLab = new CrimeLab(context);
        }
        return crimeLab;
    }
    private CrimeLab(Context context){
        this.context = context.getApplicationContext();
        dataBase = new CrimeBaseHelper(context).getWritableDatabase();


        crimes = new ArrayList<>();
        /*for (int i=0; i<100; i++){
            Crime crime = new Crime();
            crime.setTitle("Преступление #"+(i+1));
            crime.setSolved(i%2==0);
            crimes.add(crime);
        }*/
    }
    public List<Crime> getCrimes(){
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null,null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return crimes;
    }
    public Crime getCrime(UUID id){
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + "=?", new String[]{id.toString()}
        );
        try {
            if(cursor.getCount() == 0) return null;
            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }

    }

    public void addCrime(Crime c){
        ContentValues values = getContentValues(c);
        dataBase.insert(CrimeTable.NAME,null, values);
    }

    public void updateCrime(Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        dataBase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE,crime.getTitle());
        values.put(CrimeTable.Cols.DATE,crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED,crime.isSolved()?1:0);
        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = dataBase.query(CrimeTable.NAME, null, whereClause,whereArgs,null,null,null);
        return new CrimeCursorWrapper(cursor);
    }
}