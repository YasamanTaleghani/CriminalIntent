package com.example.criminalintent.repository;

import android.content.Context;

import androidx.room.Room;

import com.example.criminalintent.database.CrimeDAO;
import com.example.criminalintent.database.CrimeDatabase;
import com.example.criminalintent.model.Crime;

import java.util.List;
import java.util.UUID;

public class CrimeDBRepository implements CrimeDAO {

    private static CrimeDBRepository sInstance;
    private CrimeDAO mCrimeDAO;
    private Context mContext;

    public static CrimeDBRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new CrimeDBRepository(context);

        return sInstance;
    }

    private CrimeDBRepository(Context context) {
        mContext = context.getApplicationContext();

        CrimeDatabase crimeDatabase = Room.databaseBuilder(
                mContext, CrimeDatabase.class ,"crime.db" )
                .allowMainThreadQueries()
                .build();

        mCrimeDAO = crimeDatabase.mCrimeDAO();
    }

    @Override
    public List<Crime> getEntities() {
        return mCrimeDAO.getEntities();
    }

    @Override
    public Crime get(UUID crimeId) {
        return mCrimeDAO.get(crimeId);
    }

    @Override
    public void insert(Crime crime) {
        mCrimeDAO.insert(crime);
    }

    @Override
    public void updateCrime(Crime crime) {
        mCrimeDAO.updateCrime(crime);
    }

    @Override
    public void deleteCrime(Crime crime) {
        mCrimeDAO.deleteCrime(crime);
    }

    public int getPosition(UUID crimeId) {
        List<Crime> crimes = getEntities();
        for (int i = 0; i < crimes.size(); i++) {
            if (crimes.get(i).getId().equals(crimeId))
                return i;
        }
        return -1;
    }

}
